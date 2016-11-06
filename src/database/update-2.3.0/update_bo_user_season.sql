SELECT 'Start upgrade of betoffice 2.2.0 to 2.3.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_user_season ADD bo_roletype INTEGER;
ALTER TABLE bo_user ADD bo_admin BIT;

UPDATE bo_user SET bo_admin = 0;
COMMIT;
