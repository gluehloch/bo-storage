SELECT
    g.id, gt.bo_user_ref, gt.id
FROM
    bo_season s
    JOIN bo_gamelist gl ON (gl.bo_season_ref = s.id)
    JOIN bo_game g ON (g.bo_gamelist_ref = gl.id)
    JOIN bo_gametipp gt ON (gt.bo_game_ref = g.id)
WHERE
    s.id = 30
GROUP BY
    g.id, gt.bo_user_ref HAVING COUNT(*) > 1
ORDER BY
    gt.id

