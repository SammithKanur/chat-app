CREATE TABLE USER (
      userName VARCHAR(255) NOT NULL,
      password VARCHAR(255) NOT NULL,
      groups INTEGER,
      followers INTEGER,
      PRIMARY KEY (userName)
);
CREATE TABLE FRIENDS (
     user VARCHAR(255) NOT NULL,
     connection VARCHAR(255) NOT NULL,
     status INTEGER NOT NULL,
     PRIMARY KEY (user, connection)
);
CREATE TABLE GROUPS (
    groupName VARCHAR(255) NOT NULL,
    userName VARCHAR(255) NOT NULL,
    status INTEGER NOT NULL,
    PRIMARY KEY (groupName, userName)
);