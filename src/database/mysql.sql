/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2019 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

/*
 * MySQL schema script
 *
 * @author by Andre Winkler
 */
select 'Start installation of betoffice 2.6.0 MySQL schema.' as INFO;
select version();


drop table if exists bo_gametipp;
drop table if exists bo_goal;
drop table if exists bo_game;
drop table if exists bo_gamelist;

drop table if exists bo_player;

drop table if exists bo_team_group;
drop table if exists bo_group;
drop table if exists bo_grouptype;

drop table if exists bo_community;
drop table if exists bo_community_user;
drop table if exists bo_user_season;
drop table if exists bo_season;

drop table if exists bo_teamalias;
drop table if exists bo_team;

drop table if exists bo_location;
drop table if exists bo_session;
drop table if exists bo_user;


create table bo_session (
    id bigint not null auto_increment,
    bo_nickname VARCHAR(255) not null comment 'Nickname',
    bo_token VARCHAR(2048) not null comment 'Session Token',
    bo_login datetime not null,
    bo_logout datetime,
    bo_remoteaddress VARCHAR(100),
    bo_browser VARCHAR(200),
    bo_failedlogins bigint,
    bo_user_ref bigint,
    primary key (id)
) ENGINE=InnoDB;
  
create table bo_game (
    id bigint not NULL auto_increment,
    bo_date VARCHAR(255),
    bo_group_ref bigint,
    bo_hometeam_ref bigint,
    bo_guestteam_ref bigint,
    bo_homegoals integer,
    bo_guestgoals integer,
    bo_isplayed bit,
    bo_gamelist_ref bigint,
    bo_index integer,
    bo_datetime datetime,
    bo_openligaid bigint,
    bo_halftimehomegoals integer,
    bo_halftimeguestgoals integer,
    bo_overtimehomegoals integer,
    bo_overtimeguestgoals integer,
    bo_penaltyhomegoals integer,
    bo_penaltyguestgoals integer,
    bo_location_ref bigint,
    bo_ko tinyint(1) NOT NULL DEFAULT 0,
    primary key (id)
) ENGINE=InnoDB;

create table bo_player (
    id bigint not NULL auto_increment,
    bo_name VARCHAR(100),
    bo_vorname VARCHAR(100),
    bo_openligaid bigint comment 'Openligadb player/goalgetter ID',
    primary key(id)
) ENGINE=InnoDB;

create table bo_goal (
    id bigint not NULL auto_increment,
    bo_index integer,
    bo_homegoals integer not null,
    bo_guestgoals integer not null,
    bo_game_ref bigint not NULL ,
    bo_player_ref bigint not NULL ,
    bo_minute integer,
    bo_goaltype integer comment '0 Regulaer, 1 Elfmeter, 2 Eigentor, 3 Verlaengerung',
    bo_comment VARCHAR(255) comment 'Kommentar',
    bo_openligaid bigint,
    primary key (id)
) ENGINE=InnoDB;

create table bo_location (
    id bigint not NULL auto_increment,
    bo_name VARCHAR(100),
    bo_city VARCHAR(100),
    bo_geodat VARCHAR(100),
    bo_openligaid bigint comment 'Openligdb location',
    primary key(id)
) ENGINE=InnoDB;

create table bo_gamelist (
    id bigint not NULL auto_increment,
    bo_index integer,
    bo_date VARCHAR(255),
    bo_season_ref bigint,
    bo_group_ref bigint,
    bo_datetime datetime,
    bo_openligaid bigint,
    primary key (id)
) ENGINE=InnoDB;

create table bo_gametipp (
    id bigint not NULL auto_increment,
    bo_homegoals integer,
    bo_guestgoals integer,
    bo_user_ref bigint,
    bo_game_ref bigint,
    bo_status integer,
    bo_tipps_index integer,
    bo_create DATETIME DEFAULT CURRENT_TIMESTAMP,
    bo_update DATETIME ON UPDATE CURRENT_TIMESTAMP,
    bo_token VARCHAR(2048),
    primary key (id)
) ENGINE=InnoDB;

create table bo_group (
    id bigint not NULL auto_increment,
    bo_season_ref bigint not null,
    bo_grouptype_ref bigint not null,
    primary key (id)
) ENGINE=InnoDB;

create table bo_grouptype (
    id bigint not NULL auto_increment,
    bo_name VARCHAR(255) not NULL unique,
    bo_type INT(2) NOT NULL DEFAULT 0,
    primary key (id)
) ENGINE=InnoDB;

create table bo_season (
    id bigint not NULL auto_increment,
    bo_exportdirectory VARCHAR(255),
    bo_exporttemplate VARCHAR(255),
    bo_year VARCHAR(255),
    bo_name VARCHAR(255),
    bo_mode integer not null,
    bo_teamtype integer not null,
    bo_openligaleagueshortcut VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league shortcut',
    bo_openligaleagueseason VARCHAR(20) NULL DEFAULT NULL COMMENT 'Openligadb league season',
    primary key (id)
) ENGINE=InnoDB;

create table bo_team (
    id bigint not NULL auto_increment,
    bo_name VARCHAR(255) not NULL unique,
    bo_longname VARCHAR(255),
    bo_shortname VARCHAR(30),
    bo_xshortname VARCHAR(3),
    bo_logo VARCHAR(255),
    bo_teamtype integer not null,
    bo_location_ref BIGINT NULL DEFAULT NULL,
    bo_openligaid bigint,
    primary key (id)
) ENGINE=InnoDB;

create table bo_team_group (
    bo_group_ref bigint not null,
    bo_team_ref bigint not null,
    primary key (bo_group_ref, bo_team_ref)
) ENGINE=InnoDB;

create table bo_teamalias (
    id bigint not NULL auto_increment,
    bo_aliasName VARCHAR(255) not NULL unique,
    bo_team_ref bigint not null,
    primary key (id)
) ENGINE = InnoDB;

create table bo_user (
    id bigint not NULL auto_increment,
    bo_name VARCHAR(255),
    bo_surname VARCHAR(255),
    bo_nickname VARCHAR(255) not NULL unique,
    bo_email VARCHAR(255),
    bo_phone VARCHAR(255),
    bo_password VARCHAR(255),
    bo_automat bit,
    bo_excluded bit,
    bo_title VARCHAR(255),
    bo_admin INTEGER DEFAULT 0,
    bo_notification INT(2) NOT NULL DEFAULT 0,
    bo_change_token VARCHAR(2048) comment 'Change Token',
    bo_change_email VARCHAR(255) comment 'Holds the new email addresse',
    bo_change_send  integer comment 'Number of notification mails',
    bo_change_date  DATETIME comment 'Datetime of the requested change',
    primary key (id)
) ENGINE=InnoDB;

create table bo_user_season (
    id bigint not NULL auto_increment,
    bo_season_ref bigint not NULL,
    bo_user_ref bigint not NULL,
    bo_wager INTEGER,
    bo_roletype INTEGER DEFAULT 0,
    primary key (id)
) ENGINE=InnoDB;

alter table bo_game
    add index fk_game_home_team (bo_hometeam_ref),
    add constraint fk_game_home_team
    foreign key (bo_hometeam_ref)
    references bo_team (id);

alter table bo_game
    add index fk_game_guest_team (bo_guestteam_ref),
    add constraint fk_game_guest_team
    foreign key (bo_guestteam_ref)
    references bo_team (id);

alter table bo_game
    add index fk_game_group (bo_group_ref),
    add constraint fk_game_group
    foreign key (bo_group_ref)
    references bo_group (id);

alter table bo_game
    add index fk_game_gamelist (bo_gamelist_ref),
    add constraint fk_game_gamelist
    foreign key (bo_gamelist_ref)
    references bo_gamelist (id);

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

alter table bo_game
    add index fk_game_location (bo_location_ref),
    add constraint fk_game_location
    foreign key (bo_location_ref)
    references bo_location (id);

alter table bo_gamelist
    add index fk_gamelist_group (bo_group_ref),
    add constraint fk_gamelist_group
    foreign key (bo_group_ref)
    references bo_group (id);

alter table bo_gamelist
    add index fk_gamelist_season (bo_season_ref),
    add constraint fk_gamelist_season
    foreign key (bo_season_ref)
    references bo_season (id);

alter table bo_gametipp
    add index fk_gametipp_user (bo_user_ref),
    add constraint fk_gametipp_user
    foreign key (bo_user_ref)
    references bo_user (id);

alter table bo_gametipp
    add index fk_gametipp_game (bo_game_ref),
    add constraint fk_gametipp_game
    foreign key (bo_game_ref)
    references bo_game (id);

alter table bo_group
    add index fk_group_grouptype (bo_grouptype_ref),
    add constraint fk_group_grouptype
    foreign key (bo_grouptype_ref)
    references bo_grouptype (id);

alter table bo_group
    add index fk_group_season (bo_season_ref),
    add constraint fk_group_season
    foreign key (bo_season_ref)
    references bo_season (id);

ALTER TABLE bo_team
    ADD INDEX fk_team_location (bo_location_ref),
    ADD CONSTRAINT fk_team_location
    FOREIGN KEY (bo_location_ref)
    REFERENCES bo_location (id);

alter table bo_team_group
    add index fk_team_group_group (bo_group_ref),
    add constraint fk_team_group_group
    foreign key (bo_group_ref)
    references bo_group (id);

alter table bo_team_group
    add index bo_team_group_team (bo_team_ref),
    add constraint fk_team_group_team
    foreign key (bo_team_ref)
    references bo_team (id);

alter table bo_teamalias
    add index fk_teamalias_team (bo_team_ref),
    add constraint fk_teamalias_team
    foreign key (bo_team_ref)
    references bo_team (id);

alter table bo_user_season
    add index fk_user_season_user (bo_user_ref),
    add constraint fk_user_season_user
    foreign key (bo_user_ref)
    references bo_user (id);

alter table bo_user_season
    add index fk_user_season_season (bo_season_ref),
    add constraint fk_user_season_season
    foreign key (bo_season_ref)
    references bo_season (id);

alter table bo_session
    add index fk_session_user (bo_user_ref),
    add constraint fk_session_user
    foreign key (bo_user_ref)
    references bo_user (id);

--
-- Community Edition
--
create table bo_community (
    id BIGINT NOT NULL auto_increment,
    bo_shortname VARCHAR(20) NOT NULL UNIQUE,
    bo_name VARCHAR(255),
    bo_year VARCHAR(255),
    bo_user_ref BIGINT NOT NULL comment 'Community Manager',
    bo_season_ref BIGINT NOT NULL,
    primary key (id)
) ENGINE=InnoDB;

create table bo_community_user (
    id BIGINT NOT NULL auto_increment,
    bo_community_ref BIGINT NOT NULL,
    bo_user_ref BIGINT NOT NULL,
    primary key (id)
) ENGINE=InnoDB;

alter table bo_community_user
    add index idx_community_user_user(bo_user_ref),
    add constraint fk_community_user_user foreign key (bo_user_ref) references bo_user(id);

alter table bo_community_user
    add index idx_community_user_community(bo_community_ref),
    add constraint fk_community_user_community foreign key (bo_community_ref) references bo_community(id);
