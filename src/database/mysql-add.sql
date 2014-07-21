/*
 * $Id: mysql-add.sql 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2009 by Andre Winkler. All rights reserved.
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
 * MySQL schema update script: 1.0.2 to 1.1.0
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
select 'Start upgrade of betoffice 1.0.2 to 1.1.0 MySQL schema.' as INFO;
select version();

/*
 * Change the table encoding to UTF8_GENERAL_CI
 */
ALTER TABLE `bo_game`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_gamedaylist`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_gamelist`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_gametipp`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_group`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_grouptype`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_season`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_team`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_team_group`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_user`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `bo_user_season`  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Remove unused table BO_GAMEDAYLIST
 */
drop table if exists bo_gamedaylist;
alter table bo_gamelist change bo_gamedaylist_ref bo_season_ref bigint(20);

CREATE UNIQUE INDEX IDX_SEASON ON BO_SEASON(bo_name(30), bo_year);

/*
 * New table: BO_TEAMALIAS
 */
    alter table bo_teamalias
    drop
    foreign key FK1E0B10A1DE967505;

    drop table if exists bo_teamalias;

    create table bo_teamalias (
        id bigint not null auto_increment,
        aliasName varchar(255) not null unique,
        bo_team_ref bigint not null,
        primary key (id)
    ) ENGINE = InnoDB;

    alter table bo_teamalias
        add index FK1E0B10A1DE967505 (bo_team_ref),     
        add constraint FK1E0B10A1DE967505
        foreign key (bo_team_ref)
        references bo_team (id);
