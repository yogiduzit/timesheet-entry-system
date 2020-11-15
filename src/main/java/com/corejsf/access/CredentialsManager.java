/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.model.employee.Credentials;

/**
 * This is the class called CredentialsManager
 *
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
@Named("credentialsManager")
@ConversationScoped
public class CredentialsManager implements Serializable {

    /**
     * Variable for implementing Serialiable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_entry_system")
    private DataSource dataSource;

    /**
     * Method to get the credentials by employee number
     *
     * @param empNumber
     * @return credentials
     * @throws SQLException
     */
    public Credentials find(int empNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Credentials WHERE EmpNo = ?");
                    stmt.setInt(1, empNumber);
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        final Credentials credentials = new Credentials(result.getString("EmpUserName"),
                                result.getString("EmpPassword"));
                        credentials.setEmpNumber(result.getInt("EmpNo"));
                        return credentials;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ex.getCause());
        }
        return null;
    }

    public void insert(Credentials credentials) throws SQLException {
        final int EmpNo = 1;
        final int EmpUserName = 2;
        final int EmpPassword = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Credentials VALUES(?, ?, ?)");
                    stmt.setInt(EmpNo, credentials.getEmpNumber());
                    stmt.setString(EmpUserName, credentials.getUsername());
                    stmt.setString(EmpPassword, credentials.getPassword());

                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ex.getCause());
        }
    }

    public void merge(Credentials credentials) throws SQLException {
        final int EmpUserName = 1;
        final int EmpPassword = 2;
        final int EmpNo = 3;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Credentials " + "SET EmpUserName=?, EmpPassword=? " + "WHERE EmpNo = ?");
                    stmt.setInt(EmpNo, credentials.getEmpNumber());
                    stmt.setString(EmpUserName, credentials.getUsername());
                    stmt.setString(EmpPassword, credentials.getPassword());

                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ex.getCause());
        }
    }

}
