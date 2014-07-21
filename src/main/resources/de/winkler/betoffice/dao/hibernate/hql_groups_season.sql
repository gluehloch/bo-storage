from
  Group g left join fetch g.groupType gt
  left join fetch g.season
where
  g.season.id = :seasonId
order by
  gt.name
