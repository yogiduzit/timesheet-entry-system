DROP DATABASE IF EXISTS timesheet_entry_system;
CREATE DATABASE timesheet_entry_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON inventory.* TO 'admin'@'localhost';
GRANT ALL ON inventory.* TO 'admin'@'%';

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