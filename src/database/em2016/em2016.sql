-- EM 2016
START TRANSACTION;
SELECT 'The new teams ...' AS '';

-- Die neuen Mannschaften
-- Albanien
INSERT INTO bo_team(id, bo_name, bo_longname, bo_logo, bo_teamtype, bo_location_ref, bo_openligaid)
    VALUES(1187, 'Albanien', 'Albanien', 'albanien.gif', 1, NULL, NULL);
-- Wales
INSERT INTO bo_team(id, bo_name, bo_longname, bo_logo, bo_teamtype, bo_location_ref, bo_openligaid)
    VALUES(1188, 'Wales', 'Wales', 'wales.gif', 1, NULL, NULL);
-- Nordirland
INSERT INTO bo_team(id, bo_name, bo_longname, bo_logo, bo_teamtype, bo_location_ref, bo_openligaid)
    VALUES(1189, 'Nordirland', 'Nordirland', 'nordirland.gif', 1, NULL, NULL);
-- Ungarn
INSERT INTO bo_team(id, bo_name, bo_longname, bo_logo, bo_teamtype, bo_location_ref, bo_openligaid)
    VALUES(1190, 'Ungarn', 'Ungarn', 'ungarn.gif', 1, NULL, NULL);
-- Island
INSERT INTO bo_team(id, bo_name, bo_longname, bo_logo, bo_teamtype, bo_location_ref, bo_openligaid)
    VALUES(1191, 'Island', 'Island', 'island.gif', 1, NULL, NULL);


SELECT 'The new EM 2016 season ...' AS '';
-- EM 2016
INSERT INTO bo_season(id,  bo_exportdirectory, bo_exporttemplate, bo_year, bo_name, bo_mode, bo_teamtype, bo_openligaleagueshortcut, bo_openligaleagueseason)
    VALUES(24, 'aktuell', 'de.betoffice.veloexport.WMVelocityExport', '2016', 'EM 2016', 5, 1, NULL, NULL);

-- GRUPPEN: A, B, C, D, E, F
/*
 ID  | NAME
| 59 | Gruppe A
| 60 | Gruppe B
| 61 | Gruppe C
| 62 | Gruppe D
| 63 | Gruppe E
| 64 | Gruppe F
*/

SELECT 'The new groups ...' AS '';

-- Mannschaftsverteilung
-- Gruppe A
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(90, 24, 59);

-- Albanien (1187), Frankreich (27), Rumänien (1169), Schweiz (911)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(90, 1187);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(90, 27);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(90, 1169);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(90, 911);

-- Gruppe B
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(91, 24, 60);
-- England (47), Russland (57), Slowakei (1174), Wales (1188)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(91, 47);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(91, 57);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(91, 1174);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(91, 1188);

-- Gruppe C
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(92, 24, 61);
-- Deutschland (45), Nordirland (1189), Polen (40), Ukraine (1140)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(92, 45);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(92, 1189);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(92, 40);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(92, 1140);

-- Gruppe D
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(93, 24, 62);
-- Kroatien (51), Spanien (33), Tschechien (922), Türkei (36)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(93, 51);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(93, 33);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(93, 922);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(93, 36);

-- Gruppe E
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(94, 24, 63);
-- Belgien (56), Irland (43), Italien (54), Schweden (48)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(94, 56);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(94, 43);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(94, 54);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(94, 48);

-- Gruppe F
INSERT INTO bo_group(id, bo_season_ref, bo_grouptype_ref)
    VALUES(95, 24, 64);
-- Island (1191), Österreich (1168), Portugal (42), Ungarn (1190)
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(95, 1191);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(95, 1168);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(95, 42);
INSERT INTO bo_team_group(bo_group_ref, bo_team_ref)
    VALUES(95, 1190);

SELECT 'The new EM 2016 locations ...' AS '';

-- Locations
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(60, 'Stade de France', 'Saint-Denis', null, 1020);
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(61, 'Stade Bollaert-Delelis', 'Lens Agglo', null, 1037);
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(62, 'Parc de Princes', 'Paris', null, 1038);
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(63, 'Stade Vélodrome', 'Marseille', null, 381);
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(64, 'Stade Pierre Mauroy', 'Lille Métropole', null, 1039);
INSERT INTO bo_location(id, bo_name, bo_city, bo_geodat, bo_openligaid)
    VALUES(65, 'Stade de Lyon', 'Lyon', null, 1040);

SELECT 'The new EM 2016 rounds ...' AS '';

/*
-- Spieltag (688 Start-ID).
-- Fr 	10.06. 21:00 	Paris St. Denis 	A 	Frankreich 	 -  	Rumänien
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(688, 0, '2016-06-10 21:00', 24, 90, '2016-06-10 21:00', NULL);

/*
INSERT INTO bo_game(id, bo_date, bo_group_ref, bo_hometeam_ref, bo_guestteam_ref, bo_homegoals, bo_guestgoals, bo_isplayed, bo_gamelist_ref, bo_index, bo_datetime)
    VALUES(5300, NULL, 90, 688, ...
*/
    
/*
--Sa 	11.06. 15:00 	Lens 	A 	Albanien 	 -  	Schweiz
--Sa 	11.06. 18:00 	Bordeaux 	B 	Wales 	 -  	Slowakei
--Sa 	11.06. 21:00 	Marseille 	B 	England 	 -  	Russland
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(689, 1, '2016-06-11 15:00', 24, 90, '2016-06-11 15:00', NULL);

/*
--So 	12.06. 15:00 	Paris 	D 	Türkei 	 -  	Kroatien
--So 	12.06. 18:00 	Nizza 	C 	Polen 	 -  	Nordirland
--So 	12.06. 21:00 	Lille 	C 	Deutschland 	 -  	Ukraine
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(690, 2, '2016-06-12 15:00', 24, 93, '2016-06-12 15:00', NULL);

/*
--Mo 	13.06. 15:00 	Toulouse 	D 	Spanien 	 -  	Tschechien
--Mo 	13.06. 18:00 	Paris St. Denis 	E 	Irland 	 -  	Schweden
--Mo 	13.06. 21:00 	Lyon 	E 	Belgien 	 -  	Italien
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(691, 3, '2016-06-13 15:00', 24, 93, '2016-06-13 15:00', NULL);

/*
--Di 	14.06. 18:00 	Bordeaux 	F 	Österreich 	 -  	Ungarn
--Di 	14.06. 21:00 	Saint Étienne 	F 	Portugal 	 -  	Island
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(692, 4, '2016-06-14 18:00', 24, 94,  '2016-06-14 18:00', NULL);

/*
--Mi 	15.06. 15:00 	Lille 	B 	Russland 	 -  	Slowakei
--Mi 	15.06. 18:00 	Paris 	A 	Rumänien 	 -  	Schweiz
--Mi 	15.06. 21:00 	Marseille 	A 	Frankreich 	 -  	Albanien
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(693, 5, '2016-06-15 15:00', 24, 94, '2016-06-15 15:00', NULL);

/*
--Do 	16.06. 15:00 	Lens 	B 	England 	 -  	Wales
--Do 	16.06. 18:00 	Lyon 	C 	Ukraine 	 -  	Nordirland
--Do 	16.06. 21:00 	Paris St. Denis 	C 	Deutschland 	 -  	Polen
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(694, 6, '2016-06-16 15:00', 24, 91, '2016-06-16 15:00', NULL);

/*
--Fr 	17.06. 15:00 	Toulouse 	E 	Italien 	 -  	Schweden
--Fr 	17.06. 18:00 	Saint Étienne 	D 	Tschechien 	 -  	Kroatien
--Fr 	17.06. 21:00 	Nizza 	D 	Spanien 	 -  	Türkei
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(695, 7, '2016-06-17 15:00', 24, 94, '2016-06-17 15:00', NULL);

/*
--Sa 	18.06. 15:00 	Bordeaux 	E 	Belgien 	 -  	Irland
--Sa 	18.06. 18:00 	Marseille 	F 	Island 	 -  	Ungarn
--Sa 	18.06. 21:00 	Paris 	F 	Portugal 	 -  	Österreich
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(696, 8, '2016-06-18 15:00', 24, 94, '2016-06-18 15:00', NULL);

/*
--So 	19.06. 21:00 	Lille 	A 	Schweiz 	 -  	Frankreich
--So 	19.06. 21:00  	Lyon 	A 	Rumänien 	 -  	Albanien
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(697, 9, '2016-06-18 15:00', 24, 90, '2016-06-18 15:00', NULL);

/*
--Mo 	20.06. 21:00 	Saint Étienne 	B 	Slowakei 	 -  	England
--Mo 	20.06. 21:00 	Toulouse 	B 	Russland 	 -  	Wales
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(698, 10, '2016-06-19 21:00', 24, 91, '2016-06-19 21:00', NULL);

/*
--Di 	21.06. 18:00 	Marseille 	C 	Ukraine 	 -  	Polen
--Di 	21.06. 18:00 	Paris 	C 	Nordirland 	 -  	Deutschland
--Di 	21.06. 21:00 	Bordeaux 	D 	Kroatien 	 -  	Spanien
--Di 	21.06. 21:00 	Lens 	D 	Tschechien 	 -  	Türkei
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(699, 11, '2016-06-17 18:00', 24, 92, '2016-06-17 18:00', NULL);

/*
--Mi 	22.06. 18:00 	Lyon 	F 	Ungarn 	 -  	Portugal
--Mi 	22.06. 18:00 	Paris St. Denis 	F 	Island 	 -  	Österreich
--Mi 	22.06. 21:00 	Lille 	E 	Italien 	 -  	Irland
--Mi 	22.06. 21:00 	Nizza 	E 	Schweden 	 -  	Belgien
*/
INSERT INTO bo_gamelist(id, bo_index, bo_date, bo_season_ref, bo_group_ref , bo_datetime, bo_openligaid)
    VALUES(700, 12, '2016-06-18 18:00', 24, 95, '2016-06-18 18:00', NULL);

COMMIT;
