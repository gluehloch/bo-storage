#!/bin/bash
rsync --delete -avzbe ssh betoffice@tippdiekistebier.de:~/projects/database/vserver ~/tippdiekistebier --backup-dir=~/old

NEWEST_BACKUP=$(ls -t ~/tippdiekistebier/vserver/* | head -1)
IMPORT_FILE=${NEWEST_BACKUP%.*}

echo "Start gunzip of $NEWEST_BACKUP"
gunzip  $NEWEST_BACKUP

echo "Import to mysql ${NEWEST_BACKUP%.*}"
/bin/mysql -u betofficesu --password=betoffice -D betoffice -h 127.0.0.1 < $IMPORT_FILE

exit 0
