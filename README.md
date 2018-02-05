Backup
======
This application uses rsync to incrementally back up files and log the results of the operation to a MYSQL database.

Database setup
------
- Create a MYSQL database and user, then use **setupDb.sql** to setup the table for storing backup logs.
- Enter the database information into the applications config file, which should be located at **~/.config/backup/backup.conf**.