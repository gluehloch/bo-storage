SELECT
  COUNT(*) AS 'full_points', u.*
FROM
  bo_gamelist gl
  JOIN bo_game m ON (m.bo_gamelist_ref = gl.id)
  JOIN bo_gametipp t ON (t.bo_game_ref = m.id)
  JOIN bo_season s ON (gl.bo_season_ref = s.id)
  JOIN bo_user_season us ON (us.bo_season_ref = s.id AND us.bo_user_ref = u.id)
  JOIN bo_user u ON (t.bo_user_ref = u.id)
WHERE
      s.id = :season_id
  AND m.bo_isplayed = 1
  AND t.bo_status <> 0
  AND t.bo_homegoals = m.bo_homegoals
  AND t.bo_guestgoals = m.bo_guestgoals
GROUP BY
  u.bo_nickname
ORDER BY
  COUNT(*) DESC;