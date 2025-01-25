SELECT 'Start upgrade of betoffice 3.0.1 to 3.0.2 MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_user ADD bo_notification INT(2) NOT NULL DEFAULT 0;
