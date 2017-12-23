SELECT 'Start upgrade of betoffice 2.3.0 to 2.5.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_team ADD bo_shortname VARCHAR(30);
ALTER TABLE bo_team ADD bo_xshortname VARCHAR(3);
