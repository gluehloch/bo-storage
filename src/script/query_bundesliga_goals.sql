select team_id, team_name, SUM(pos_goals - neg_goals), SUM(pos_goals), SUM(neg_goals)
from
(
	(
		select
		  t.id as team_id, t.bo_name as team_name, SUM(m.bo_homegoals) as pos_goals, SUM(m.bo_guestgoals) as neg_goals
		from
		  bo_season s,
		  bo_team t,
		  bo_group g,
		  bo_grouptype grouptype,
		  bo_team_group tg,
		  bo_gamelist r,
		  bo_game m
		where
			  s.bo_name = 'Fussball Bundesliga'
		  and s.bo_year = '2008/2009'
		  and grouptype.bo_name = '1. Liga'
		  and g.bo_season_ref = s.id
		  and tg.bo_group_ref = g.id
		  and tg.bo_team_ref = t.id
		  and r.bo_season_ref = s.id
		  and m.bo_group_ref = g.id
		  and m.bo_gamelist_ref = r.id
		  and m.bo_isplayed = 1
		  and m.bo_hometeam_ref = t.id  
		group by t.id
	)
	UNION ALL
	(
		select
		   t.id as team_id, t.bo_name as team_name, SUM(m.bo_guestgoals) as pos_goals, SUM(m.bo_homegoals) as neg_goals
		from
		  bo_season s,
		  bo_team t,
		  bo_group g,
		  bo_grouptype grouptype,
		  bo_team_group tg,
		  bo_gamelist r,
		  bo_game m
		where
			  s.bo_name = 'Fussball Bundesliga'
		  and s.bo_year = '2008/2009'
		  and grouptype.bo_name = '1. Liga'
		  and g.bo_season_ref = s.id
		  and tg.bo_group_ref = g.id
		  and tg.bo_team_ref = t.id
		  and r.bo_season_ref = s.id
		  and m.bo_group_ref = g.id
		  and m.bo_gamelist_ref = r.id
		  and m.bo_isplayed = 1
		  and m.bo_guestteam_ref = t.id
		group by t.id
	)
) tabelle
group by team_id
order by SUM(pos_goals - neg_goals) desc;
