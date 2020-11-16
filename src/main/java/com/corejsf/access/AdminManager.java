package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.messages.MessageProvider;
import com.corejsf.model.employee.Employee;

@Named("adminManager")
@ConversationScoped
public class AdminManager implements Serializable {

    private static String TAG = "Admin";

    private static final long serialVersionUID = 1233413L;

    /**
     * Datasource for the project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    @Inject
    /**
     * Provides access to messages in the message bundle
     */
    private MessageProvider msgProvider;

    /**
     * Finds the admin user for the application
     *
     * @return the admin employee
     * @throws SQLDataException
     */
    public Employee find() throws SQLDataException {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    final ResultSet result = stmt
                            .executeQuery("SELECT * FROM Employees WHERE EmpNo IN (SELECT EmpNo  FROM Admins)");
                    if (result.next()) {
                        return new Employee(result.getInt("EmpNo"), result.getString("EmpName"),
                                result.getString("EmpUserName"));
                    } else {
                        return null;
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
            throw new SQLDataException(msgProvider.getValue("error.find", new Object[] { TAG }));
        }
    }

}
