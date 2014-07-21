select
  count(*) as 'half_points', u.*
from
  bo_gamelist gl,
  bo_game m,
  bo_gametipp t,
  bo_season s,
  bo_user_season us,
  bo_user u
where
      gl.bo_season_ref = :season_id
  and s.id = :season_id
  and us.bo_season_ref = s.id
  and us.bo_user_ref = u.id
  and m.bo_gamelist_ref = gl.id
  and m.bo_isplayed = 1
  and t.bo_game_ref = m.id
  and t.bo_status <> 0 
  and t.bo_user_ref = u.id
  /* Alle Tipps die 1.3 Punkte Tipps sind ausschliessen */
  and not
  (
        t.bo_homegoals  = m.bo_homegoals
    and t.bo_guestgoals = m.bo_guestgoals
  )
  and
  (
  (
    /* Toto 1 */
        m.bo_homegoals > m.bo_guestgoals
    /* User Toto 1*/
    and t.bo_homegoals > t.bo_guestgoals
  )
  or
  (
    /* Toto 0 */
        m.bo_homegoals = m.bo_guestgoals
    /* User Toto 0 */
    and t.bo_homegoals = t.bo_guestgoals
  )
  or
  (
    /* Toto 2 */
        m.bo_homegoals < m.bo_guestgoals
    /* User Toto 2 */
    and t.bo_homegoals < t.bo_guestgoals
  )
  )
group by
  u.bo_nickname
order by
  count(*) desc;