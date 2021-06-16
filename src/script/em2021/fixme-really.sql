/*
 TODO Fehler: service.addMatch(round_2021_06_16, '2021-06-17 21:00:00', em2021_gruppe_C, italien, schweiz)
 TODO Korrektur: /Die Mannschafts-IDs austauschen/
*/

select * from bo_team t where t.bo_name = 'Italien';

select * from bo_team t where t.bo_name = 'Schweiz';

update bo_game set bo_hometeam_ref = 924, bo_guestteam_ref = 1168 where id = 6991;
