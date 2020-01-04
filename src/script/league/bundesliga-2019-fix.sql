update bo_game set bo_index = 0 where id = 6354;
update bo_game set bo_index = 1 where id = 6355;
update bo_game set bo_index = 2 where id = 6356;
update bo_game set bo_index = 3 where id = 6357;
update bo_game set bo_index = 4 where id = 6358;
update bo_game set bo_index = 5 where id = 6359;
update bo_game set bo_index = 6 where id = 6360;
update bo_game set bo_index = 7 where id = 6361;
update bo_game set bo_index = 8 where id = 6362;
update bo_game set bo_index = 9 where id = 6363;


insert into bo_user(id, bo_name, bo_surname, bo_nickname, bo_email, bo_password)
    values (17290, 'Wortmann', 'Martin', 'Martin04', 'm_wortmann@gmx.de', 'S04');

update bo_user set bo_phone = '', bo_automat = 0, bo_excluded = 0, bo_title = '' where id = 17290;    