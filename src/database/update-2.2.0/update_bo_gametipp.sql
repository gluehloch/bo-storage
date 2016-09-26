select 'Start upgrade of betoffice 2.1.0 to 2.2.0 MySQL/MariaDB schema.' as INFO;
select version();

ALTER TABLE bo_gametipp ADD bo_create DATETIME;
ALTER TABLE bo_gametipp ADD bo_update DATETIME;
ALTER TABLE bo_gametipp ADD bo_token VARCHAR(2048);
