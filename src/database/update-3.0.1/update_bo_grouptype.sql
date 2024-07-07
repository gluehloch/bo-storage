SELECT 'Start upgrade of betoffice 3.0.0 to 3.0.1 MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_grouptype ADD bo_type INT(2) NOT NULL DEFAULT 0;
