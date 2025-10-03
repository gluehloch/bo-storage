select
    gl2.id next_round_id
from
    bo_gamelist gl2
where
    gl2.bo_season_ref =
    (
        select
            gl3.bo_season_ref
        from
            bo_gamelist gl3
        where
            gl3.id = :roundId
   )
   and gl2.bo_index =
   (
       select
           gl4.bo_index + 1
       from
           bo_gamelist gl4
       where
           gl4.id = :roundId
   )
;