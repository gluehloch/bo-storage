SELECT  
    u.bo_nickname,
    ht.bo_name,
    gt.bo_name,
    t.bo_homegoals,
    t.bo_guestgoals,
    g.bo_datetime,
    u.bo_nickname
FROM
    bo_season s,
    bo_gamelist gl,
    bo_game g,
    bo_team ht,
    bo_team gt,
    bo_user u,
    bo_gametipp t
WHERE
    s.id = 19 
    AND gl.bo_season_ref = s.id
    AND gl.bo_index = 0
    AND g.bo_gamelist_ref = gl.id
    AND u.bo_nickname = 'Frosch'
    AND t.bo_user_ref = u.id
    AND t.bo_game_ref = g.id
    AND ht.id = g.bo_hometeam_ref
    AND gt.id = g.bo_guestteam_ref
ORDER BY
    g.bo_datetime
