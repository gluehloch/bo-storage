select 'Start upgrade of betoffice 1.1.4 to 2.0.0 MySQL schema.' as INFO;
select version();

drop table if exists bo_player;
create table bo_player (
    id bigint not null auto_increment,
    bo_name VARCHAR(100),
    bo_vorname VARCHAR(100),
    bo_openligaid bigint comment 'Openligadb player/goalgetter ID',
    primary key(id)
) ENGINE=InnoDB;

drop table if exists bo_goal;
create table bo_goal (
    id bigint not null auto_increment,
    bo_index integer not null,
    bo_homegoals integer not null,
    bo_guestgoals integer not null,
    bo_game_ref bigint not null ,
    bo_player_ref bigint not null ,
    bo_minute integer not null,
    bo_goaltype integer comment '0 Regulaer, 1 Elfmeter, 2 Eigentor, 3 Verlaengerung',
    bo_comment VARCHAR(255) comment 'Kommentar',
    bo_openligaid bigint,
    primary key (id)
) ENGINE=InnoDB;

alter table bo_goal
    add index fk_goal_game (bo_game_ref),
    add constraint fk_goal_game
    foreign key (bo_game_ref)
    references bo_game (id);

alter table bo_goal
    add index fk_goal_player (bo_player_ref),
    add constraint fk_goal_player
    foreign key (bo_player_ref)
    references bo_player (id);

drop table if exists bo_location;
create table bo_location (
    id bigint not null auto_increment,
    bo_name VARCHAR(100),
    bo_city VARCHAR(100),
    bo_geodat VARCHAR(100),
    bo_openligaid bigint comment 'Openligdb location',
    primary key(id)
) ENGINE=InnoDB;

-- Removal of bo_season#bo_current_ref
ALTER TABLE bo_season DROP FOREIGN KEY fk_season_gamelist;
ALTER TABLE bo_season DROP bo_current_ref;
ALTER TABLE bo_season ADD bo_openligaleagueshortcut VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league shortcut';
ALTER TABLE bo_season ADD bo_openligaleagueseason VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league season';

-- Reference to openligadb team id
ALTER TABLE bo_gamelist ADD bo_openligaid BIGINT NULL DEFAULT NULL COMMENT 'Openligadb group ID' , ADD UNIQUE (bo_openligaid);

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

ALTER TABLE bo_game
    ADD INDEX fk_game_location (bo_location_ref),
    ADD CONSTRAINT fk_game_location
    FOREIGN KEY (bo_location_ref)
    REFERENCES bo_location (id);
