Backup
======
This application uses rsync to incrementally back up files and log the results of the operation to a MYSQL database.

Config file
------
- The config file for the application should be stored at **~/.config/backup/backup.conf**.
- You should copy the example config file **./build/main/resources/backup_example.conf** and modify it.

Database setup
------
- Create a MYSQL database and user, then use **setupDb.sql** to setup the table for storing backup logs.
- Enter the database information and credentials into the config file.

Running the application
------
You can build the application by running:
> ./gradlew build

To start the application you simply run the script located at:
> ./build/main/resources/run.sh