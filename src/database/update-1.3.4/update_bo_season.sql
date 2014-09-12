-- Removal of bo_season#bo_current_ref
ALTER TABLE bo_season DROP FOREIGN KEY fk_season_gamelist;
ALTER TABLE bo_season DROP bo_current_ref;

-- Reference to openligadb team id
ALTER TABLE bo_team ADD bo_openligaid BIGINT NULL DEFAULT NULL COMMENT 'Openligadb ID' , ADD UNIQUE (bo_openligaid);
