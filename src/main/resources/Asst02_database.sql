DROP DATABASE IF EXISTS timesheet_entry_system;
CREATE DATABASE timesheet_entry_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON timesheet_entry_system.* TO 'admin'@'localhost';
GRANT ALL ON timesheet_entry_system.* TO 'admin'@'%';

USE timesheet_entry_system;

DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees(
    EmpNo INT(5) AUTO_INCREMENT NOT NULL UNIQUE,
    EmpName VARCHAR(50) NOT NULL,
    EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    CONSTRAINT PKEmployee PRIMARY KEY (EmpNo)
);

DROP TABLE IF EXISTS Admins;
CREATE TABLE Admins(
    EmpNo INT(5) NOT NULL UNIQUE,
    CONSTRAINT PKAdmin PRIMARY KEY (EmpNo), 
    CONSTRAINT FKAdminEmpNo
        FOREIGN KEY (EmpNo)
            REFERENCES Employees(EmpNo)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);

DROP TABLE IF EXISTS Credentials;
CREATE TABLE Credentials(
    EmpNo INT(5) NOT NULL UNIQUE,
    EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    EmpPassword VARCHAR(15) NOT NULL,
    CONSTRAINT FKCredentialEmpNo
        FOREIGN KEY (EmpNo)
            REFERENCES Employees(EmpNo)
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    CONSTRAINT FKCredentialEmpUserName
        FOREIGN KEY (EmpUserName)
            REFERENCES Employees(EmpUserName)
);

INSERT INTO Employees VALUES (1, "Bruce Link", "bdlink");
INSERT INTO Employees VALUES (2, "Yogesh Verma", "yogiduzit");
INSERT INTO Employees VALUES (3, "Sung Na", "sungna");

INSERT INTO Admins VALUES (1);

INSERT INTO Credentials VALUES (1, "bdlink", "bruce");
INSERT INTO Credentials VALUES (2, "yogiduzit", "yogesh");
INSERT INTO Credentials VALUES (3, "sungna", "sung");

DROP TABLE IF EXISTS Timesheets;
CREATE TABLE Timesheets(
    EmpNo INT(5) UNIQUE,
    EndWeek DATE NOT NULL UNIQUE,
    TimesheetID INT(5) NOT NULL UNIQUE,
    CONSTRAINT PKTimesheet PRIMARY KEY (EmpNo, EndWeek)
);

DROP TABLE IF EXISTS TimesheetRows;
CREATE TABLE TimesheetRows(
    TimesheetID INT(5) NOT NULL,
    ProjectID INT(5) NOT NULL,
    WorkPackage VARCHAR(10) NOT NULL,
    Notes VARCHAR(50) NOT NULL,
    HoursForWeek VARCHAR(50) NOT NULL,
    CONSTRAINT PKTimesheetRows PRIMARY KEY (TimesheetID, ProjectID, WorkPackage),
    CONSTRAINT FKTimesheetRowsTimesheetID
        FOREIGN KEY (TimesheetID)
            REFERENCES Timesheets (TimesheetID)
);

INSERT INTO Timesheets VALUES (1, DATE'2020-11-13', 1);
INSERT INTO Timesheets VALUES (2, DATE'2020-11-20', 2);
INSERT INTO Timesheets VALUES (3, DATE'2020-11-27', 3);

INSERT INTO TimesheetRows VALUES (1, 142, "142", "Redo project", "1,0,5,7,0,0,0");
INSERT INTO TimesheetRows VALUES (1, 142, "143", "Excellence", "5,5,5,0,0,0,8");
INSERT INTO TimesheetRows VALUES (2, 100, "150", "Edit some parts", "8,8,8,0,0,0,4");

