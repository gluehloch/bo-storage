SELECT
  COUNT(*) AS 'full_points', u.*
FROM
  bo_gamelist gl
  JOIN bo_game m ON (m.bo_gamelist_ref = gl.id)
  JOIN
  (
    SELECT
      mt.id,
      CASE
        WHEN bo_ko = 0 THEN
          mt.bo_homegoals
        -- Ende nach 90 Minuten
        WHEN bo_ko = 1 AND mt.bo_homegoals <> mt.bo_guestgoals THEN
          mt.bo_homegoals
        -- Ende nach der Verlaengerung
        WHEN bo_ko = 1 AND mt.bo_homegoals = mt.bo_guestgoals AND mt.bo_overtimehomegoals <> mt.bo_overtimeguestgoals THEN
          mt.bo_overtimehomegoals
        ELSE
          mt.bo_penaltyhomegoals
      END as bo_homegoals,
      CASE
        WHEN bo_ko = 0 THEN
          mt.bo_guestgoals
        -- Ende nach 90 Minuten
        WHEN bo_ko = 1 AND mt.bo_homegoals <> mt.bo_guestgoals THEN
          mt.bo_guestgoals
        -- Ende nach der Verlaengerung
        WHEN bo_ko = 1 AND mt.bo_homegoals = mt.bo_guestgoals AND mt.bo_overtimehomegoals <> mt.bo_overtimeguestgoals THEN
          mt.bo_overtimeguestgoals
        ELSE
          mt.bo_penaltyguestgoals
      END as bo_guestgoals
    FROM
      bo_game mt
  ) as gameresult ON (gameresult.id = m.id)
  JOIN bo_gametipp t ON (t.bo_game_ref = m.id)
  JOIN bo_season s ON (gl.bo_season_ref = s.id)
  JOIN bo_community cm ON (cm.bo_season_ref = s.id)
  JOIN bo_community_user cm_user ON (cm_user.bo_community_ref = cm.id)
  JOIN bo_user u ON (cm_user.bo_user_ref = u.id AND cm_user.bo_user_ref = u.id)
WHERE
      s.id = :season_id
  AND m.bo_isplayed = 1
  AND t.bo_status <> 0
  AND t.bo_homegoals = gameresult.bo_homegoals
  AND t.bo_guestgoals = gameresult.bo_guestgoals
GROUP BY
  u.bo_nickname
ORDER BY
  COUNT(*) DESC;