SELECT 'Start upgrade of betoffice 3.0.0 to 3.0.1 MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_grouptype ADD bo_type INT(2) NOT NULL DEFAULT 0;

SELECT 'Start upgrade of betoffice 3.0.1 to 3.0.2 MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_user ADD bo_notification INT(2) NOT NULL DEFAULT 0;

SELECT 'Start upgrade of betoffice 3.0.1 to 3.0.2 MariaDB schema.' as INFO;
SELECT version();

ALTER TABLE bo_user ADD bo_change_token VARCHAR(2048) comment 'Change Token';
ALTER TABLE bo_user ADD bo_change_email VARCHAR(255)  comment 'Holds the new email addresse';
ALTER TABLE bo_user ADD bo_change_send  INTEGER       comment 'Number of notification mails';