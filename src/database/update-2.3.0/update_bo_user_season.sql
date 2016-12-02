SELECT 'Start upgrade of betoffice 2.2.0 to 2.3.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_user_season DROP bo_roletype;
ALTER TABLE bo_user_season ADD bo_roletype INTEGER DEFAULT 0;

ALTER TABLE bo_user DROP bo_admin;
ALTER TABLE bo_user ADD bo_admin INTEGER DEFAULT 0;

UPDATE bo_user SET bo_admin = 0;
COMMIT;
