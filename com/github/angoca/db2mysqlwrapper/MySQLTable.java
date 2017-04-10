import java.util.Iterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

import COM.ibm.db2.app.UDF;

/**
 * This class allows to create a UDF that access a MySQL database and retrieve the data.
 * <p>
 * The following lines are necessaries to create the UDF in DB2, by
 * associating the Java class file and the function.
 * <p>
 * TODO The return should be dynamic.
 * <code>
CREATE OR REPLACE FUNCTION MYSQL_TABLE(
  SERVER VARCHAR(128),
  PORT INTEGER DEFAULT 3306,
  DATABASE VARCHAR(64),
  USERNAME VARCHAR(32),
  PASSWORD VARCHAR(32),
  QUERY VARCHAR(128) DEFAULT 'SELECT 1 FROM DUAL'
 ) RETURNS TABLE (user varchar(32), date varchar(50), message varchar(140))
     language java
     external name 'MySQLTable.query'
     parameter style db2general
     not deterministic
     fenced
     returns null on null input
     no sql
     external action
     no scratchpad
     final call
     disallow parallel;
    </code>
 *
 * @author Andres Gomez Casanova (AngocA)
 */
public class MySQLTable extends UDF {
    /**
     * MySQL JDBC driver.
     */
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * Connection to MySQL.
     */
    private static Connection conn = null;
    /**
     * Statement.
     */
    Statement stmt = null;
    /**
     * Results of the query.
     */
    ResultSet rs = null;


    /**
     * Establishes a connection to MySQL with the given credentials
     * an performs the query in the remote database.
     *
     * @param server
     *            Name of the server or IP address where the MySQL server resides.
     * @param port
     *            Port number in which MySQL listens.
     * @param database
     *            Name of the database to access.
     * @param username
     *            Username to access the database.
     * @param password
     *            Password for the username.
     * @param query
     *            Query to execute in the MySQL database.
     * @throws Exception
     *             If there is any error while accessing MySQL.
     */
    public void query(String server, int port, String database, String username, String password, String query)
            throws Exception {
        int value = 0;
        switch (getCallType()) {
        case SQLUDF_TF_FIRST:
            // Established the connection to MySQL.
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database +"?" +
                                   "user=" + username + "&password=" + password);
// TODO Valida los datos de entrada, que la tabla exista.
            break;
        case SQLUDF_TF_OPEN:
            // Creates the Statement and executes it.
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            break;
        case SQLUDF_TF_FETCH:
            if (rs.next()) {
// TODO recupera los datos dinánicamente basado en la metadata (Programa de liberty y computec)
                value = rs.getInt("id");
                set(1, value);
            } else {
                // No more rows. Returns SQLSTATE
                setSQLstate("02000");
            }
            break;
        case SQLUDF_TF_CLOSE:
            // Closes all objects
            rs.close();
            stmt.close();
            break;
        case SQLUDF_TF_FINAL:
            // Closes the connection.
            conn.close();
            break;
        default:
            throw new Exception("Unexpected call type of " + getCallType());
        }
// TODO catch the errors and convert them to SQLSTATE if possible.
    }
}
