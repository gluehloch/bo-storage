select 'Start upgrade of betoffice 2.0.0 to 2.1.0 MySQL schema.' as INFO;
select version();

drop table if exists bo_session;
create table bo_session (
    id bigint not null auto_increment,
    bo_nickname VARCHAR(255) not null comment 'Nickname',
    bo_token VARCHAR(2048) not null comment 'Session Token',
    bo_login datetime not null,
    bo_logout datetime,
    bo_remoteaddress VARCHAR(100),
    bo_browser VARCHAR(200),
    bo_failedlogins bigint,
    bo_user_ref bigint,
    primary key (id)
) ENGINE=InnoDB;

alter table bo_session
    add index fk_session_user (bo_user_ref),
    add constraint fk_session_user
    foreign key (bo_user_ref)
    references bo_user (id);
