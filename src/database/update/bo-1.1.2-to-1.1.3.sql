ALTER TABLE bo_game ENGINE=INNODB;
ALTER TABLE bo_gamelist ENGINE=INNODB;
ALTER TABLE bo_gametipp ENGINE=INNODB;
ALTER TABLE bo_group ENGINE=INNODB;
ALTER TABLE bo_grouptype ENGINE=INNODB;
ALTER TABLE bo_season ENGINE=INNODB;
ALTER TABLE bo_team ENGINE=INNODB;
ALTER TABLE bo_team_group ENGINE=INNODB;
ALTER TABLE bo_teamalias ENGINE=INNODB;
ALTER TABLE bo_user ENGINE=INNODB;
ALTER TABLE bo_user_season ENGINE=INNODB;

ALTER TABLE bo_game ADD CONSTRAINT
    FOREIGN KEY fk_game_gamelist(bo_gamelist_ref)
    REFERENCES bo_gamelist(id);
    
/* Default values for some enum mapped properties. */
ALTER TABLE bo_gametipp
    CHANGE COLUMN bo_status bo_status INT(11) NULL DEFAULT 4;
ALTER TABLE bo_season
    CHANGE COLUMN bo_mode bo_mode INT(11) NULL DEFAULT 0;
ALTER TABLE bo_season
    CHANGE COLUMN bo_teamtype bo_teamtype INT(11) NULL DEFAULT 0;
ALTER TABLE bo_team
    CHANGE COLUMN bo_teamtype bo_teamtype INT(11) NULL DEFAULT 0;

