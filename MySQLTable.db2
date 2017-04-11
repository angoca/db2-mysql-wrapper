CREATE OR REPLACE FUNCTION MYSQL_TABLE(
  SERVER VARCHAR(128),
  PORT INTEGER DEFAULT 3306,
  DATABASE VARCHAR(64),
  USERNAME VARCHAR(32),
  PASSWORD VARCHAR(32),
  QUERY VARCHAR(128) DEFAULT 'SELECT 1 FROM DUAL'
 ) RETURNS TABLE (VALS INT)
     language java
     external name 'com.github.angoca.db2MysqlWrapper.MySQLTable!query(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String)'
     parameter style db2general
     not deterministic
     fenced
     returns null on null input
     no sql
     external action
     no scratchpad
     final call
     disallow parallel;
