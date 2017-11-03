/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2017 by Andre Winkler. All rights reserved.
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
select 'Start dropping of betoffice 2.2.5 MySQL schema.' as INFO;
select version();

alter table bo_game drop foreign key fk_game_home_team;
alter table bo_game drop foreign key fk_game_guest_team;
alter table bo_game drop foreign key fk_game_group;
alter table bo_game drop foreign key fk_game_gamelist;
alter table bo_game drop foreign key fk_game_location;

alter table bo_goal drop foreign key fk_goal_game;
alter table bo_goal drop foreign key fk_goal_player;

alter table bo_gamelist drop foreign key fk_gamelist_group;
alter table bo_gamelist drop foreign key fk_gamelist_season;
alter table bo_gametipp drop foreign key fk_gametipp_user;
alter table bo_gametipp drop foreign key fk_gametipp_game;

alter table bo_group drop foreign key fk_group_grouptype;
alter table bo_group drop foreign key fk_group_season;

alter table bo_team drop foreign key fk_team_location;

alter table bo_team_group drop foreign key fk_team_group_group;
alter table bo_team_group drop foreign key fk_team_group_team;

alter table bo_teamalias drop foreign key fk_teamalias_team;
alter table bo_user_season drop foreign key fk_user_season_user;
alter table bo_user_season drop foreign key fk_user_season_season;

alter table bo_session drop foreign key fk_session_user;

drop table if exists bo_gametipp;
drop table if exists bo_goal;
drop table if exists bo_game;
drop table if exists bo_gamelist;

drop table if exists bo_player;

drop table if exists bo_team_group;
drop table if exists bo_group;
drop table if exists bo_grouptype;

drop table if exists bo_user_season;
drop table if exists bo_season;

drop table if exists bo_teamalias;
drop table if exists bo_team;

drop table if exists bo_location;
drop table if exists bo_session;
drop table if exists bo_user;
