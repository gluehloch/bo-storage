select
    gl.id
from
    bo_gamelist gl
where
    gl.bo_season_ref =
    (
        select
            gl3.bo_season_ref
        from
            bo_gamelist gl3
        where
            gl3.id = 623
   )
   and gl.bo_index =
   (
       select
           gl4.bo_index + 1
       from
           bo_gamelist gl4
       where
           gl4.id = 623
   )
;
