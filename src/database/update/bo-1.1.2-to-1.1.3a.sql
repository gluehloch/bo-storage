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

alter table bo_season 
    add index fk_season_gamelist (bo_current_ref), 
    add constraint fk_season_gamelist 
    foreign key (bo_current_ref) 
    references bo_gamelist (id);

alter table bo_team_group 
    add index fk_team_group_group (bo_group_ref), 
    add constraint fk_team_group_group 
    foreign key (bo_group_ref) 
    references bo_group (id);

alter table bo_team_group 
    add index bo_team_group_team (bo_team_ref), 
    add constraint bo_team_group_team 
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
