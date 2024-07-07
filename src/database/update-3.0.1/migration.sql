SELECT 'Start migration of betoffice 3.0.0 to 3.0.1 MySQL/MariaDB schema.' as INFO;
SELECT version();

 update bo_grouptype set bo_type = 1 where bo_name like 'Gruppe%';

 update bo_grouptype set bo_type = 2 where bo_name = 'Achtelfinale' or bo_name = 'Viertelfinale';

 update bo_grouptype set bo_type = 3 where bo_name = 'Halbfinale';

 update bo_grouptype set bo_type = 4 where bo_name = 'Finale' or bo_name = 'Spiel um Platz 3';

