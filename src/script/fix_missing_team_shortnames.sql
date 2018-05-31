select * from bo_team where bo_shortname is null;

update bo_team set bo_shortname = bo_name where bo_shortname is null;
