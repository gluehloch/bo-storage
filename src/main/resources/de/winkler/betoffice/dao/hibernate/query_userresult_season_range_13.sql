select
  count(*) as 'full_points', u.*
from
  bo_gamelist gl,
  bo_game m,
  bo_gametipp t,
  bo_season s,
  bo_user_season us,
  bo_user u
where
      gl.bo_season_ref = :season_id
  and gl.bo_index >= :start_index
  and gl.bo_index <= :end_index
  and s.id = :season_id
  and us.bo_season_ref = s.id
  and us.bo_user_ref = u.id
  and m.bo_gamelist_ref = gl.id
  and m.bo_isplayed = 1
  and t.bo_game_ref = m.id
  and t.bo_status <> 0
  and t.bo_user_ref = u.id
  and t.bo_homegoals = m.bo_homegoals
  and t.bo_guestgoals = m.bo_guestgoals
group by
  u.bo_nickname
order by
  count(*) desc;