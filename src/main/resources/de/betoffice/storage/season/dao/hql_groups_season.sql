select
    distinct g
from
    Group g
    left join fetch g.groupType gt
    left join fetch g.season
    left join fetch g.teams
where
    g.season.id = :seasonId
order by
    gt.name
