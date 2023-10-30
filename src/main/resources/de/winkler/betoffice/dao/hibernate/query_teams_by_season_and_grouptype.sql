select
  t.*
from
  bo_team t,
  bo_group g,
  bo_team_group tg,
  bo_grouptype gt
where
      g.bo_season_ref    = :season_id
  and g.bo_grouptype_ref = gt.id
  and gt.id              = :grouptype_id 
  and tg.bo_group_ref    = g.id
  and tg.bo_team_ref     = t.id
order by
  t.bo_name
;