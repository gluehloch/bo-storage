SELECT
    u.bo_nickname, gt.bo_create, gt.bo_update, gt.bo_token, count(*)
FROM
    bo_user u
    JOIN bo_gametipp gt ON (gt.bo_user_ref = u.id)
WHERE
    u.bo_nickname = 'Frosch'
    AND gt.bo_create IS NOT NULL
GROUP BY
    gt.bo_create 
ORDER BY
    gt.bo_create  DESC
;