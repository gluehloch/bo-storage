select 'Start upgrade of betoffice 1.1.4 to 2.0.0 MySQL schema.' as INFO;
select version();

-- Removal of bo_season#bo_current_ref
ALTER TABLE bo_season DROP FOREIGN KEY fk_season_gamelist;
ALTER TABLE bo_season DROP bo_current_ref;
ALTER TABLE bo_season ADD bo_openligaleagueshortcut VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league shortcut';
ALTER TABLE bo_season ADD bo_openligaleagueseason VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league season';

-- Reference to openligadb team id
ALTER TABLE bo_team ADD bo_openligaid BIGINT NULL DEFAULT NULL COMMENT 'Openligadb team ID' , ADD UNIQUE (bo_openligaid);

ALTER TABLE bo_game ADD bo_halftimehomegoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_halftimeguestgoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_overtimehomegoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_overtimeguestgoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_penaltyhomegoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_penaltyguestgoals INTEGER DEFAULT 0;
ALTER TABLE bo_game ADD bo_location_ref BIGINT NULL DEFAULT NULL;
ALTER TABLE bo_game ADD bo_openligaid BIGINT NULL DEFAULT NULL COMMENT 'Openligadb match ID' , ADD UNIQUE (bo_openligaid);
