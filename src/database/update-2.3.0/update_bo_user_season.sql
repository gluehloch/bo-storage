select 'Start upgrade of betoffice 2.2.0 to 2.3.0 MySQL/MariaDB schema.' as INFO;
select version();

ALTER TABLE bo_user_season ADD bo_roletype INTEGER;
