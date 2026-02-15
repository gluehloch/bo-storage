use registertest;

SELECT 'Start installation of registerservice 0.0.1 MySQL schema.' as INFO;
SELECT version();

DROP TABLE IF EXISTS cookie;
DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS useraccount_session;
DROP TABLE IF EXISTS useraccount_application;
DROP TABLE IF EXISTS useraccount_role;
DROP TABLE IF EXISTS role_privilege;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS privilege;
DROP TABLE IF EXISTS application;
DROP TABLE IF EXISTS useraccount;

CREATE TABLE cookie(
    id bigint NOT NULL auto_increment,
    website VARCHAR(50),
    remoteaddress VARCHAR(100) NOT NULL comment 'the user remote address',
    browser VARCHAR(200) NOT NULL comment 'the user browser',
    created datetime NOT NULL comment 'entry creation time',
    acceptcookie bit comment 'user accepts cookies',
    primary key (id)
) ENGINE=InnoDB;

CREATE TABLE registration (
    id bigint NOT NULL auto_increment,
    nickname varchar(20) NOT NULL UNIQUE,
    name varchar(50) NOT NULL,
    firstname varchar(50) NOT NULL,
    password varchar(60) NOT NULL,
    email varchar(50) NOT NULL comment 'email address',
    created datetime NOT NULL comment 'creation time',
    token varchar(60) NOT NULL UNIQUE comment 'token to confirm email address',
    application varchar(50) NOT NULL comment 'name of the appliction',
    confirmed bit comment 'registration completed successful',
    acceptmail bit comment 'user accepts emails',
    acceptcookie bit comment 'user accepts cookies',
    supplement varchar(100) comment 'supplement / untyped data.',
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE useraccount (
    id bigint NOT NULL auto_increment,
    nickname varchar(20) NOT NULL UNIQUE,
    name varchar(50) NOT NULL,
    firstname varchar(50) NOT NULL,
    password varchar(60) NOT NULL,
    email varchar(50) NOT NULL,
    created datetime NOT NULL,
    last_change datetime comment 'Last password change',
    enabled bit comment 'account enabled',
    expired bit comment 'password expired',
    locked bit comment 'account locked',
    credential_expired bit comment 'credential expired?',
    acceptmail bit comment 'user accepts emails',
    acceptcookie bit comment 'user accepts cookies',
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE application (
    id bigint NOT NULL auto_increment,
    name varchar(50) NOT NULL UNIQUE,
    description varchar(500) NOT NULL,
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE useraccount_session (
    id bigint NOT NULL auto_increment,
    useraccount_ref bigint,
    token varchar(2048) NOT NULL comment 'Session Token',
    login datetime comment 'Login date and time',
    logout datetime comment 'Logout date and time',
    remoteaddress varchar(30) comment 'IP address',
    browser varchar(200) comment 'Browser',
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE useraccount_application (
    useraccount_ref bigint NOT NULL,
    application_ref bigint NOT NULL,
    primary key(useraccount_ref, application_ref)
) ENGINE=InnoDB;

CREATE TABLE role (
    id bigint NOT NULL auto_increment,
    name varchar(50) NOT NULL UNIQUE,
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE privilege (
    id bigint NOT NULL auto_increment,
    name varchar(50) NOT NULL UNIQUE,
    primary key(id)
) ENGINE=InnoDB;

CREATE TABLE useraccount_role (
    useraccount_ref bigint NOT NULL comment 'Reference to table useraccount',
    role_ref bigint NOT NULL comment 'Reference to table role'
) ENGINE=InnoDB;

CREATE TABLE role_privilege (
    role_ref bigint NOT NULL comment 'Reference to table role',
    privilege_ref bigint NOT NULL comment 'Reference to table privilege'
) ENGINE=InnoDB;


/* TABLE useraccount_session */
ALTER TABLE useraccount_session
    ADD CONSTRAINT fk_useraccount_session_user
    FOREIGN KEY (useraccount_ref)
    REFERENCES useraccount(id);

/* TABLE useraccount_application */
ALTER TABLE useraccount_application
    ADD CONSTRAINT fk_useracc_app_useracc
    FOREIGN KEY (useraccount_ref)
    REFERENCES useraccount(id);
    
ALTER TABLE useraccount_application
    ADD CONSTRAINT fk_useracc_app_app
    FOREIGN KEY (application_ref)
    REFERENCES application(id);

/* TABLE useraccount_role */
ALTER TABLE useraccount_role
    ADD CONSTRAINT fk_useracc_role_useracc
    FOREIGN KEY (useraccount_ref)
    REFERENCES useraccount(id);

ALTER TABLE useraccount_role
    ADD CONSTRAINT fk_useracc_role_role
    FOREIGN KEY (role_ref)
    REFERENCES role(id);

/* TABLE role_privilege */
ALTER TABLE role_privilege
    ADD CONSTRAINT fk_role_privilege_role
    FOREIGN KEY (role_ref)
    REFERENCES role(id);

ALTER TABLE role_privilege
    ADD CONSTRAINT fk_role_privilege_privilege
    FOREIGN KEY (privilege_ref)
    REFERENCES privilege(id);


