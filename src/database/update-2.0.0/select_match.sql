SELECT
    l.bo_name,
    m.bo_datetime,
    home_team.bo_name,
    guest_team.bo_name,
    m.bo_homegoals,
    m.bo_guestgoals,
    m.bo_halftimehomegoals,
    m.bo_halftimeguestgoals,
    g.bo_minute,
    p.bo_name,
    m.bo_openligaid,
    home_team.bo_openligaid,
    guest_team.bo_openligaid,
    g.bo_openligaid,
    p.bo_openligaid,
    l.bo_openligaid
FROM
    bo_game m,
    bo_team home_team,
    bo_team guest_team,
    bo_goal g,
    bo_player p,
    bo_location l
WHERE
    m.id = 4723
    AND m.bo_hometeam_ref = home_team.id
    AND m.bo_guestteam_ref = guest_team.id
    AND g.bo_game_ref = m.id
    AND g.bo_player_ref = p.id
    AND l.id = m.bo_location_ref
;
