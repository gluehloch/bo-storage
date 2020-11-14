SELECT
    gt.*
FROM
    bo_season s
    JOIN bo_gamelist gl ON (gl.bo_season_ref = s.id)
    JOIN bo_game g ON (g.bo_gamelist_ref = gl.id)
    JOIN bo_gametipp gt ON (gt.bo_game_ref = g.id)
WHERE
    s.id = 30
    AND gl.bo_index = 3
    AND gt.bo_user_ref = 16
ORDER BY
    bo_game_ref;


DELETE FROM bo_gametipp WHERE id IN (
    57030, 57033, 57039, 57042, 57043, 57037, 57045,
    57040, 57047
);
