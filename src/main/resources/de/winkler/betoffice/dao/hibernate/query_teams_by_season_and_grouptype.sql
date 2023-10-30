SELECT
  t.*
FROM
  bo_team t
  JOIN bo_team_group tg ON tg.bo_team_ref = t.id
  JOIN bo_group g       ON tg.bo_group_ref= g.id 
  JOIN bo_grouptype gt  ON g.bo_grouptype_ref = gt.id 
WHERE
  g.bo_season_ref = :season_id
  AND gt.id = :grouptype_id
ORDER BY
  t.bo_name
;
