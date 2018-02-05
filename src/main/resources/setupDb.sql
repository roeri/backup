create table backups (
    jobName varchar(255),
    startTime datetime,
    endTime datetime,
    duration bigint,
    files int,
    folders int,
    size bigint,
    transferredSize bigint,
    transferSpeed int,
    filesCreated int,
    foldersCreated int,
    filesDeleted int,
    foldersDeleted int,
    sourcePath varchar(255),
    targetPath varchar(255)
);