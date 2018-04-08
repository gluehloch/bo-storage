SELECT 'Start upgrade of betoffice 2.5.0 to 2.6.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_game ADD bo_ko tinyint(1) NOT NULL DEFAULT 0;

UPDATE bo_player SET bo_vorname = TRIM(bo_vorname);
UPDATE bo_player SET bo_name = TRIM(bo_name);

UPDATE bo_player SET bo_name = 'Kevin Volland' WHERE id = 667;
UPDATE bo_player SET bo_name = 'Kai Havertz' WHERE id = 685;
UPDATE bo_player SET bo_name = 'Karim Bellarabi' WHERE id = 696;
