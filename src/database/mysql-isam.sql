
    alter table bo_game 
        drop 
        foreign key FK2F7A0A4F72EF1E4;

    alter table bo_game 
        drop 
        foreign key FK2F7A0A4A5A1DD3B;

    alter table bo_game 
        drop 
        foreign key FK2F7A0A4343772B9;

    alter table bo_game 
        drop 
        foreign key FK2F7A0A4ABA2A68B;

    alter table bo_gamelist 
        drop 
        foreign key FK87615F42A5A1DD3B;

    alter table bo_gamelist 
        drop 
        foreign key FK87615F42F5473151;

    alter table bo_gametipp 
        drop 
        foreign key FK876501D9322960E1;

    alter table bo_gametipp 
        drop 
        foreign key FK876501D9D891B5AF;

    alter table bo_group 
        drop 
        foreign key FK5C04380D257B374F;

    alter table bo_group 
        drop 
        foreign key FK5C04380DF5473151;

    alter table bo_season 
        drop 
        foreign key FK383F5B155EB87050;

    alter table bo_team_group 
        drop 
        foreign key FK9FAB294FA5A1DD3B;

    alter table bo_team_group 
        drop 
        foreign key FK9FAB294FDE967505;

    alter table bo_teamalias
       drop
	   foreign key FK1E0B10A1DE967505;

    alter table bo_user_season 
        drop 
        foreign key FKB2D710E5322960E1;

    alter table bo_user_season 
        drop 
        foreign key FKB2D710E5F5473151;

    drop table if exists bo_game;

    drop table if exists bo_gamelist;

    drop table if exists bo_gametipp;

    drop table if exists bo_group;

    drop table if exists bo_grouptype;

    drop table if exists bo_season;

    drop table if exists bo_team;

    drop table if exists bo_team_group;

    drop table if exists bo_teamalias;

    drop table if exists bo_user;

    drop table if exists bo_user_season;

    create table bo_game (
        id bigint not null auto_increment,
        bo_date varchar(255),
        bo_group_ref bigint,
        bo_hometeam_ref bigint,
        bo_guestteam_ref bigint,
        bo_homegoals integer,
        bo_guestgoals integer,
        bo_isplayed bit,
        bo_gamelist_ref bigint,
        bo_index integer,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_gamelist (
        id bigint not null auto_increment,
        bo_index integer,
        bo_date varchar(255),
        bo_season_ref bigint,
        bo_group_ref bigint,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_gametipp (
        id bigint not null auto_increment,
        bo_homegoals integer,
        bo_guestgoals integer,
        bo_user_ref bigint,
        bo_game_ref bigint,
        bo_status integer,
        bo_tipps_index integer,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_group (
        id bigint not null auto_increment,
        bo_season_ref bigint not null,
        bo_grouptype_ref bigint not null,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_grouptype (
        id bigint not null auto_increment,
        bo_name varchar(255) not null unique,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_season (
        id bigint not null auto_increment,
        bo_exportdirectory varchar(255),
        bo_exporttemplate varchar(255),
        bo_year varchar(255),
        bo_name varchar(255),
        bo_current_ref bigint,
        bo_mode integer not null,
        bo_teamtype integer not null,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_team (
        id bigint not null auto_increment,
        bo_name varchar(255) not null unique,
        bo_longname varchar(255),
        bo_logo varchar(255),
        bo_teamtype integer not null,
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_team_group (
        bo_group_ref bigint not null,
        bo_team_ref bigint not null,
        primary key (bo_group_ref, bo_team_ref)
    ) ENGINE = MYISAM;

    create table bo_teamalias (
        id bigint not null auto_increment,
        bo_aliasname varchar(255) not null unique,
        bo_team_ref bigint,
        primary key (id)
    ) ENGINE = MYISAM;
  
    create table bo_user (
        id bigint not null auto_increment,
        bo_name varchar(255),
        bo_surname varchar(255),
        bo_nickname varchar(255) not null unique,
        bo_email varchar(255),
        bo_phone varchar(255),
        bo_password varchar(255),
        bo_automat bit,
        bo_excluded bit,
        bo_title varchar(255),
        primary key (id)
    ) ENGINE = MYISAM;

    create table bo_user_season (
        id bigint not null auto_increment,
        bo_season_ref bigint not null,
        bo_user_ref bigint not null,
        bo_wager integer,
        primary key (id)
    ) ENGINE = MYISAM;

    alter table bo_game 
        add index FK2F7A0A4F72EF1E4 (bo_hometeam_ref), 
        add constraint FK2F7A0A4F72EF1E4 
        foreign key (bo_hometeam_ref) 
        references bo_team (id);

    alter table bo_game 
        add index FK2F7A0A4A5A1DD3B (bo_group_ref), 
        add constraint FK2F7A0A4A5A1DD3B 
        foreign key (bo_group_ref) 
        references bo_group (id);

    alter table bo_game 
        add index FK2F7A0A4343772B9 (bo_guestteam_ref), 
        add constraint FK2F7A0A4343772B9 
        foreign key (bo_guestteam_ref) 
        references bo_team (id);

    alter table bo_game 
        add index FK2F7A0A4ABA2A68B (bo_gamelist_ref), 
        add constraint FK2F7A0A4ABA2A68B 
        foreign key (bo_gamelist_ref) 
        references bo_gamelist (id);

    alter table bo_gamelist 
        add index FK87615F42A5A1DD3B (bo_group_ref), 
        add constraint FK87615F42A5A1DD3B 
        foreign key (bo_group_ref) 
        references bo_group (id);

    alter table bo_gamelist 
        add index FK87615F42F5473151 (bo_season_ref), 
        add constraint FK87615F42F5473151 
        foreign key (bo_season_ref) 
        references bo_season (id);

    alter table bo_gametipp 
        add index FK876501D9322960E1 (bo_user_ref), 
        add constraint FK876501D9322960E1 
        foreign key (bo_user_ref) 
        references bo_user (id);

    alter table bo_gametipp 
        add index FK876501D9D891B5AF (bo_game_ref), 
        add constraint FK876501D9D891B5AF 
        foreign key (bo_game_ref) 
        references bo_game (id);

    alter table bo_group 
        add index FK5C04380D257B374F (bo_grouptype_ref), 
        add constraint FK5C04380D257B374F 
        foreign key (bo_grouptype_ref) 
        references bo_grouptype (id);

    alter table bo_group 
        add index FK5C04380DF5473151 (bo_season_ref), 
        add constraint FK5C04380DF5473151 
        foreign key (bo_season_ref) 
        references bo_season (id);

    alter table bo_season 
        add index FK383F5B155EB87050 (bo_current_ref), 
        add constraint FK383F5B155EB87050 
        foreign key (bo_current_ref) 
        references bo_gamelist (id);

    alter table bo_team_group 
        add index FK9FAB294FA5A1DD3B (bo_group_ref), 
        add constraint FK9FAB294FA5A1DD3B 
        foreign key (bo_group_ref) 
        references bo_group (id);

    alter table bo_team_group 
        add index FK9FAB294FDE967505 (bo_team_ref), 
        add constraint FK9FAB294FDE967505 
        foreign key (bo_team_ref) 
        references bo_team (id);

    alter table bo_teamalias
        add index FK1E0B10A1DE967505 (bo_team_ref), 	
        add constraint FK1E0B10A1DE967505
        foreign key (bo_team_ref)
        references bo_team (id);

    alter table bo_user_season 
        add index FKB2D710E5322960E1 (bo_user_ref), 
        add constraint FKB2D710E5322960E1 
        foreign key (bo_user_ref) 
        references bo_user (id);

    alter table bo_user_season 
        add index FKB2D710E5F5473151 (bo_season_ref), 
        add constraint FKB2D710E5F5473151 
        foreign key (bo_season_ref) 
        references bo_season (id);
