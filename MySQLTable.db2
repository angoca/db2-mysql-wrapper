CREATE OR REPLACE FUNCTION MYSQL_TABLE(
  SERVER VARCHAR(128),
  PORT INTEGER DEFAULT 3306,
  DATABASE VARCHAR(64),
  USERNAME VARCHAR(32),
  PASSWORD VARCHAR(32),
  QUERY VARCHAR(128) DEFAULT 'SELECT 1 FROM DUAL'
 ) RETURNS GENERIC TABLE
     language java
     external name 'com.github.angoca.db2MysqlWrapper.MySQLTable!query(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)'
     parameter style db2general
     not deterministic
     fenced
     returns null on null input
     no sql
     external action
     no scratchpad
     final call
     disallow parallel;
