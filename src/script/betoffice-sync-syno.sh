###!/bin/bash

BACKUP_DIR=/var/services/homes/winkler/betoffice/vserver
MYSQL_CLIENT=/usr/syno/mysql/bin/mysql
KEY_FILE=/var/services/homes/winkler/ssh/83.169.40.139.betoffice.id_rsa

rsync --delete -avzbe "ssh -i $KEY_FILE" betoffice@tippdiekistebier.de:~/projects/database/vserver/ $BACKUP_DIR --backup-dir=/var/services/homes/winkler/old

NEWEST_BACKUP=$(ls -t $BACKUP_DIR/* | head -1)
IMPORT_FILE=${NEWEST_BACKUP%.*}

echo "Start gunzip of $NEWEST_BACKUP"
gunzip  $NEWEST_BACKUP

echo "Import to mysql ${NEWEST_BACKUP%.*}"
$($MYSQL_CLIENT -u betofficesu --password=betoffice -D betoffice -h 127.0.0.1 < $IMPORT_FILE)

exit 0
