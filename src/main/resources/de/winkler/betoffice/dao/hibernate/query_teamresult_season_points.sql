select
  tabelle.id,
  tabelle.bo_name,
  tabelle.bo_longname,
  tabelle.bo_shortname,
  tabelle.bo_xshortname,
  tabelle.bo_logo,
  tabelle.bo_teamtype,
  tabelle.bo_location_ref,
  tabelle.bo_openligaid,
  SUM(win) as win, SUM(remis) as remis, SUM(lost) as lost, (SUM(win) * 3 + SUM(remis)) as points
from
(
    (
        select
          t.id,
          t.bo_name,
          t.bo_longname,
          t.bo_shortname,
          t.bo_xshortname,
          t.bo_logo,
          t.bo_teamtype,
          t.bo_location_ref,
          t.bo_openligaid,
          count(*) as win, 0 as remis, 0 as lost
        from
          bo_season s,
          bo_team t,
          bo_group g,
          bo_grouptype gt,
          bo_team_group tg,
          bo_gamelist r,
          bo_game m
        where
              s.id = :season_id
          and gt.id = :grouptype_id
          and g.bo_grouptype_ref = gt.id
          and g.bo_season_ref = s.id
          and tg.bo_group_ref = g.id
          and tg.bo_team_ref = t.id
          and r.bo_season_ref = s.id
          and m.bo_group_ref = g.id
          and m.bo_gamelist_ref = r.id
          and m.bo_isplayed = 1
          and
          (
              (
                    m.bo_hometeam_ref = t.id
                and m.bo_homegoals > m.bo_guestgoals
              )
              or
              (
                    m.bo_guestteam_ref = t.id
                and m.bo_guestgoals > m.bo_homegoals
              )
          )
        group by t.id
    )
    UNION ALL
    (
        select
          t.id,
          t.bo_name,
          t.bo_longname,
          t.bo_shortname,
          t.bo_xshortname,
          t.bo_logo,
          t.bo_teamtype, 
          t.bo_location_ref,
          t.bo_openligaid,
          0 as win, count(*) as remis, 0 as lost
        from
          bo_season s,
          bo_team t,
          bo_group g,
          bo_grouptype gt,
          bo_team_group tg,
          bo_gamelist r,
          bo_game m
        where
              s.id = :season_id
          and gt.id = :grouptype_id
          and g.bo_grouptype_ref = gt.id
          and g.bo_season_ref = s.id
          and tg.bo_group_ref = g.id
          and tg.bo_team_ref = t.id
          and r.bo_season_ref = s.id
          and m.bo_group_ref = g.id
          and m.bo_gamelist_ref = r.id
          and m.bo_isplayed = 1
          and
          (
              (
                    m.bo_hometeam_ref = t.id
                and m.bo_homegoals = m.bo_guestgoals
              )
              or
              (
                    m.bo_guestteam_ref = t.id
                and m.bo_guestgoals = m.bo_homegoals
              )
          )
        group by t.id
    )
    UNION ALL
    (
        select
          t.id,
          t.bo_name,
          t.bo_longname,
          t.bo_shortname,
          t.bo_xshortname,
          t.bo_logo,
          t.bo_teamtype, 
          t.bo_location_ref,
          t.bo_openligaid,
          0 as win, 0 as remis, count(*) as lost
        from
          bo_season s,
          bo_team t,
          bo_group g,
          bo_grouptype gt,
          bo_team_group tg,
          bo_gamelist r,
          bo_game m
        where
              s.id = :season_id
          and gt.id = :grouptype_id
          and g.bo_grouptype_ref = gt.id
          and g.bo_season_ref = s.id
          and tg.bo_group_ref = g.id
          and tg.bo_team_ref = t.id
          and r.bo_season_ref = s.id
          and m.bo_group_ref = g.id
          and m.bo_gamelist_ref = r.id
          and m.bo_isplayed = 1
          and
          (
              (
                    m.bo_hometeam_ref = t.id
                and m.bo_homegoals < m.bo_guestgoals
              )
              or
              (
                    m.bo_guestteam_ref = t.id
                and m.bo_guestgoals < m.bo_homegoals
              )
          )
        group by t.id
    )
) tabelle
group by id
order by points desc
;
