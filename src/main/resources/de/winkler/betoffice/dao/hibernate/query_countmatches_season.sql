select
  count(*) as 'count_matches'
from 
  bo_gamelist gl,
  bo_game m
where
      gl.bo_season_ref = :season_id
  and m.bo_gamelist_ref = gl.id
  and m.bo_isplayed = 1;
