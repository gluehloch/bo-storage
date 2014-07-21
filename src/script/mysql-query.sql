select
  gl.id as GL_ID, gl.*, s.bo_name, s.bo_year, gt.bo_name
from
  bo_gamelist gl, bo_season s, bo_group g, bo_grouptype gt
where
      s.id               = 1
  and gl.bo_date         = '09.06.2006'
  and gl.bo_season_ref   = s.id
  and s.id               = g.bo_season_ref
  and g.bo_grouptype_ref = gt.id;
  
select * from bo_season;


select
  (select bo_name from bo_team where id = g.bo_hometeam_ref) as home,
  g.bo_homegoals,
  (select bo_name from bo_team where id = g.bo_guestteam_ref) as guest,
  g.bo_guestgoals
from
  bo_game g;
