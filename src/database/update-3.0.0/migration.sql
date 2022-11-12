SELECT 'Start upgrade of betoffice 2.6.0 to 3.0.0 MySQL/MariaDB schema.' as INFO;
SELECT version();

select concat(bo_name, ' ', bo_year) from bo_season;
--select * from bo_season;
--select * from bo_community bc;
delete from bo_community;
delete from bo_community_user;
--select * from bo_community_user bcu;
--select * from bo_user u where u.bo_nickname = 'Frosch'; /* 6 */
--select * from bo_user_season bus;
--select * from bo_gametipp bg;

select
	bc.*, bu.*
FROM
	bo_community bc
	join bo_community_user bcu on (bcu.bo_community_ref = bc.id)
    join bo_user bu on (bu.id = bcu.bo_user_ref)
WHERE
	bc.bo_season_ref = 30
;
	

insert into bo_community
(
	id,
	bo_shortname,
	bo_name,
	bo_year,
	bo_user_ref,
	bo_season_ref
)
select
	s.id,
	concat('TDKB', ' ', s.bo_year),
	s.bo_name,
	s.bo_year,
	6,
	s.id
from
	bo_season s
;

insert into bo_community_user 
(
	id,
	bo_community_ref,
	bo_user_ref
)
select
	bus.id,
	bus.bo_season_ref,
	bus.bo_user_ref
from 
	bo_user_season bus
;

select * from bo_community_user cu, bo_user u where cu.bo_community_ref = 32 and cu.bo_user_ref = u.id;
select * from bo_community_user cu, bo_user u where cu.bo_community_ref = 33 and cu.bo_user_ref = u.id;



