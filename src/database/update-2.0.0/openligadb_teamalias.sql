-- Season Shortcut
UPDATE bo_season
    SET bo_openligaleagueshortcut = 'bl1', bo_openligaleagueseason = '2013'
    WHERE id = 20;

UPDATE bo_season
    SET bo_openligaleagueshortcut = 'bl1', bo_openligaleagueseason = '2014'
    WHERE id = 22;

-- Bayern München
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('FC Bayern München', 1141);
UPDATE bo_team SET bo_openligaid = 40 WHERE id = 1141;

-- VfL Wolfsburg
UPDATE bo_team SET bo_openligaid = 131 WHERE id = 1147;

-- TSG 1899 Hoffenheim
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('TSG 1899 Hoffenheim', 1170);
UPDATE bo_team SET bo_openligaid = 123 WHERE id = 1170;

-- FC Augsburg
UPDATE bo_team SET bo_openligaid = 95 WHERE id = 1178;

-- Hannover 96
UPDATE bo_team SET bo_openligaid = 55 WHERE id = 1162;

-- Schalke 04
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('FC Schalke 04', 1161);
UPDATE bo_team SET bo_openligaid = 9 WHERE id = 1161;

-- Hertha BSC
UPDATE bo_team SET bo_openligaid = 54 WHERE id = 1157;

-- SV Werder Bremen
UPDATE bo_team SET bo_openligaid = 134 WHERE id = 1145;
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('SV Werder Bremen', 1145);

-- Eintracht Frankfurt
UPDATE bo_team SET bo_openligaid = 91 WHERE id = 1155;

-- SC Freiburg
UPDATE bo_team SET bo_openligaid = 112 WHERE id = 1144;

-- 1. FC Köln
UPDATE bo_team SET bo_openligaid = 65 WHERE id = 1148;

-- Hamburger SV
UPDATE bo_team SET bo_openligaid = 100 WHERE id = 1151;
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('HSV', 1151);

-- Borussia Dortmund
UPDATE bo_team SET bo_openligaid = 7 WHERE id = 1142;
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('BVB', 1142);

-- Bayer 04 Leverkusen
UPDATE bo_team SET bo_openligaid = 6 WHERE id = 1143;

-- SC Paderborn
UPDATE bo_team SET bo_openligaid = 31 WHERE id = 1184;
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('SC Paderborn 07', 1184);

-- 1. FSV Mainz 05
UPDATE bo_team SET bo_openligaid = 81 WHERE id = 1164;
INSERT INTO bo_teamalias(bo_aliasname, bo_team_ref) VALUES('1. FSV Mainz 05', 1164);

-- Borussia Mönchengladbach
UPDATE bo_team SET bo_openligaid = 87 WHERE id = 1160;

-- VfB Stuttgart
UPDATE bo_team SET bo_openligaid = 16 WHERE id = 1153

