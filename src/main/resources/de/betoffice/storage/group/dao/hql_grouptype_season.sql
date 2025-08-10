select
  gt
from
  Group as g 
  join g.season as s
  join g.groupType as gt
where
   s.id = :season_id
order by
   gt.name
