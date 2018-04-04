SELECT 'Start upgrade of betoffice 2.5.0 to 2.6.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_game ADD bo_ko tinyint(1) NOT NULL DEFAULT 0;
