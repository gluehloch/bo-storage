select
  t.*
from
  bo_team t, bo_group g, bo_team_group tg
where
      g.id = :group_id
  and tg.bo_group_ref = g.id
  and tg.bo_team_ref = t.id
order by
  t.bo_name
;