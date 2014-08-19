-- Removal of bo_season#bo_current_ref
ALTER TABLE bo_season DROP FOREIGN KEY fk_season_gamelist;
ALTER TABLE bo_season DROP bo_current_ref;
