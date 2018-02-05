Backup
======
This application uses rsync to incrementally back up files and log the results of the operation to a MYSQL database.

Database setup
------
- Create a MYSQL database and user, then use **setupDb.sql** to setup the table for storing backup logs.
- Enter the database information into the applications config file, which should be located at **~/.config/backup/backup.conf**.

Running the application
------
After setting everything up in your config file (~/.config/backup/backup.conf), you can build the application by running:
> ./gradlew build
To start the application you simply run the script located at:
> ./build/main/resources/run.sh