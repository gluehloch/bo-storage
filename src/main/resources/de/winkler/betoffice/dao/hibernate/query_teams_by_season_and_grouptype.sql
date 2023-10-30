select
  t.id, t.bo_name, t.bo_longname, t.bo_logo, t.bo_teamtype, t.bo_location_ref, t.bo_openligaid, t.bo_shortname, t.bo_xshortname
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