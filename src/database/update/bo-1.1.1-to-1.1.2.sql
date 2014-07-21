/*
 * $Id: bo-1.1.1-to-1.1.2.sql 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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
 * MySQL schema update script: 1.1.1 to 1.1.2
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
select 'Start upgrade of betoffice 1.1.1 to 1.1.2 MySQL schema.' as INFO;
select version();

alter table bo_team add (bo_teamtype integer not null);
alter table bo_season add (bo_teamtype integer not null);

select 'Start data update.' as INFO;
update bo_season set bo_mode = 4 where id = 2;
update bo_season set bo_mode = 4 where id = 15;
update bo_season set bo_mode = 5 where id = 3;


update bo_team
set bo_teamtype = 1
where exists
(
  select 1
  from bo_season s, bo_group g, bo_team_group tg
  where
    s.bo_mode       <> 0 and
    s.id            = g.bo_season_ref and 
    tg.bo_group_ref = g.id and
    tg.bo_team_ref  = bo_team.id
);

update bo_season set bo_teamtype = 1 where bo_mode <> 0;
commit;
