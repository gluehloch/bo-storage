SELECT 'Start upgrade of betoffice 2.6.0 to 2.7.0 MariaDB schema.' as INFO;
SELECT version();

drop table bo_community_user;
drop table bo_community;

--
-- Community Edition
--
create table bo_community (
    id BIGINT NOT NULL auto_increment,
    bo_shortname VARCHAR(20) NOT NULL UNIQUE,
    bo_name VARCHAR(255),
    bo_year VARCHAR(255),
    bo_user_ref BIGINT NOT NULL comment 'Community Manager',
    bo_season_ref BIGINT NOT NULL,
    primary key (id)
) ENGINE=InnoDB;

create table bo_community_user (
    id BIGINT NOT NULL auto_increment,
    bo_community_ref BIGINT NOT NULL,
    bo_user_ref BIGINT NOT NULL,
    primary key (id)
) ENGINE=InnoDB;

alter table bo_community_user
    add index idx_community_user_user(bo_user_ref),
    add constraint fk_community_user_user foreign key (bo_user_ref) references bo_user(id);

alter table bo_community_user
    add index idx_community_user_community(bo_community_ref),
    add constraint fk_community_user_community foreign key (bo_community_ref) references bo_community(id);
