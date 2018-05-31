select
    gl.bo_datetime datetime,
    gl.id last_round_id
from
    bo_gamelist gl
where gl.bo_datetime =
(
  select max(t.bo_datetime)
  from
  (
    select r.bo_datetime, r.id
    from bo_gamelist r, bo_game m
    where r.bo_season_ref = 27 /*:season_id*/
      and r.id = m.bo_gamelist_ref
      and m.bo_datetime < now() /*:date*/
  ) as t
)