
select u.bo_nickname, gt.bo_create, gt.bo_update,
g.bo_datetime, g.bo_index, gl.bo_datetime
from
    bo_gametipp gt
    join bo_user u on (gt.bo_user_ref = u.id)
    join bo_game g on (gt.bo_game_ref = g.id)
    join bo_gamelist gl on (g.bo_gamelist_ref = gl.id)
where
    gt.bo_create > str_to_date('2019-01-18', '%Y-%m-%d')
group by u.bo_nickname;

select * from bo_gametipp gt where gt.bo_create is null and gt.bo_update is not null;
select * from bo_gametipp gt where gt.bo_create is not null and gt.bo_update is null;
