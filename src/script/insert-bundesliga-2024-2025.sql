insert into bo_community(bo_shortname, bo_name, bo_year, bo_user_ref, bo_season_ref) values ('TDKB 2024/2025', 'Bundesliga', '2024/2025', 6, 37);

insert into bo_community_user(bo_community_ref, bo_user_ref) values
select 37, u2.bo_user_ref from bo_community_user u2 where u2.bo_community_ref = 35;