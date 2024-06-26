CREATE DATABASE `botest` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
CREATE USER 'test'@'%' IDENTIFIED BY 'test';

CREATE USER 'testsu'@'localhost' IDENTIFIED BY 'test';
CREATE USER 'testsu'@'%' IDENTIFIED BY 'test';

REVOKE ALL PRIVILEGES ON * . * FROM 'test'@'localhost';
REVOKE ALL PRIVILEGES ON * . * FROM 'test'@'%';

REVOKE ALL PRIVILEGES ON * . * FROM 'testsu'@'localhost';
REVOKE ALL PRIVILEGES ON * . * FROM 'testsu'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON botest. * TO 'test'@'localhost'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE ON botest. * TO 'test'@'%'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE,
  ALTER, INDEX, DROP, CREATE TEMPORARY TABLES, SHOW VIEW,
  CREATE ROUTINE, ALTER ROUTINE, EXECUTE, CREATE VIEW, EVENT, TRIGGER,
  LOCK TABLES
  ON botest. * TO 'testsu'@'localhost'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE,
  ALTER, INDEX, DROP, CREATE TEMPORARY TABLES, SHOW VIEW,
  CREATE ROUTINE, ALTER ROUTINE, EXECUTE, CREATE VIEW, EVENT, TRIGGER,
  LOCK TABLES
  ON botest. * TO 'testsu'@'%'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;
  