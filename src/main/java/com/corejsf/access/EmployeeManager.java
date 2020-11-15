package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.AuthenticationFailedException;
import javax.sql.DataSource;

import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

@Named("employeeManager")
@ConversationScoped
public class EmployeeManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource(mappedName = "java:jboss/datasources/timesheet_entry_system")
    private DataSource dataSource;

    @Inject
    private CredentialsManager credentialsManager;

    @Inject
    private AdminManager adminManager;

    public Employee find(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM " + "Employees WHERE EmpUserName = ?");
                    stmt.setString(1, username);
                    final ResultSet result = stmt.executeQuery();
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
            throw new SQLException("Could not find employee! Please try again");
        }
    }

    public Employee find(int empNo) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM " + "Employees WHERE EmpNo = ?");
                    stmt.setInt(1, empNo);
                    final ResultSet result = stmt.executeQuery();
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
            throw new SQLException("Could not find Employee! Please try again");
        }
    }

    public void persist(Employee employee) throws SQLException {
        final int empNo = 1;
        final int empName = 2;
        final int empUsername = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Employees " + "VALUES (?, ?, ?)");
                    stmt.setInt(empNo, employee.getEmpNumber());
                    stmt.setString(empName, employee.getFullName());
                    stmt.setString(empUsername, employee.getUsername());
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
            throw new SQLException("Could not create new Employee! Please try again");
        }
    }

    public void merge(Employee employee) throws SQLException {
        final int empName = 1;
        final int empUsername = 2;
        final int empNo = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Employees " + "SET EmpName = ?, EmpUserName = ? " + "WHERE EmpNo = ?");
                    stmt.setString(empName, employee.getFullName());
                    stmt.setString(empUsername, employee.getUsername());
                    stmt.setInt(empNo, employee.getEmpNumber());
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
            throw new SQLException("Could not update employee! Please try again");
        }
    }

    public void remove(Employee employee) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("DELETE FROM Employees WHERE EmpNo = ?");
                    stmt.setInt(1, employee.getEmpNumber());
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
            throw new SQLException("Unable to remove user! Please try again");
        }
    }

    public Employee[] getAll() throws SQLException {
        final ArrayList<Employee> employees = new ArrayList<Employee>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    final ResultSet result = stmt.executeQuery("SELECT * FROM Employees ORDER BY EmpNo");
                    while (result.next()) {
                        employees.add(new Employee(result.getInt("EmpNo"), result.getString("EmpName"),
                                result.getString("EmpUserName")));
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
            throw new SQLException("Could not fetch employees! Please try again");
        }

        final Employee[] subarray = new Employee[employees.size()];
        return employees.toArray(subarray);
    }

    public boolean verifyUser(Employee employee, Credentials credentials)
            throws AuthenticationFailedException, SQLException {
        if (employee == null || credentials == null) {
            return false;
        }
        final Credentials found = credentialsManager.find(employee.getEmpNumber());
        if (found == null) {
            throw new AuthenticationFailedException(
                    "Could not find an employee with the given username / password. Please try again.");
        }
        if (!credentials.equals(found)) {
            throw new AuthenticationFailedException("Unable to authenticate! Please try again");
        }
        return true;
    }

    public Employee getCurrentEmployee() throws SQLException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String username = (String) context.getExternalContext().getSessionMap().get("emp_no");
        if (username == null) {
            return null;
        }
        Employee current;
        try {
            current = find(username);
        } catch (final SQLException e) {
            throw new SQLException(e.getCause());
        }
        return current;
    }

    public boolean isAdminLogin() throws SQLException {
        final Employee currEmployee = getCurrentEmployee();
        if (currEmployee == null) {
            return false;
        }
        final Employee admin = adminManager.find();
        if (currEmployee.getUsername().equals(admin.getUsername())) {
            return true;
        }
        return false;
    }
}
