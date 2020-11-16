package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.AuthenticationFailedException;
import javax.sql.DataSource;

import com.corejsf.messages.MessageProvider;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

@Named("employeeManager")
@ConversationScoped
public class EmployeeManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String TAG = "Employee";

    /**
     * Datasource for the project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    @Inject
    /**
     * Provides access to the credentials table in the datasource
     */
    private CredentialsManager credentialsManager;

    @Inject
    /**
     * Provides access to messages in the message bundle
     */
    private MessageProvider msgProvider;

    @Inject
    /**
     * Provides access to the admin table in the datasource
     */
    private AdminManager adminManager;

    /**
     * Finds an employee by their username
     *
     * @param username, unique identifier for the Employee
     * @return Employee POJO
     * @throws SQLException
     */
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.find", new Object[] { TAG }));
        }
    }

    /**
     * Finds an employee by their unique number
     *
     * @param empNo, the unique id of the employee
     * @return Employee POJO
     * @throws SQLException
     */
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
            throw new SQLDataException(msgProvider.getValue("error.find", new Object[] { TAG }));
        }
    }

    /**
     * Inserts an Employee record into the employees table
     *
     * @param employee, POJO representing an employee record
     * @throws SQLException
     */
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
        } catch (final SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.create", new Object[] { TAG }));
        }
    }

    /**
     * Updates an existing employee record in the employees table
     *
     * @param employee, POJO representing the employee record
     * @throws SQLException
     */
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
        } catch (final SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (final SQLException ex) {
            throw new SQLDataException(msgProvider.getValue("error.edit", new Object[] { TAG }));
        }
    }

    /**
     * Removes an employee record from the employees table in the datasource
     *
     * @param employee, Employee POJO
     * @throws SQLException
     */
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
        } catch (final SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (final SQLException ex) {
            throw new SQLDataException(msgProvider.getValue("error.delete", new Object[] { TAG }));
        }
    }

    /**
     * Gets the list of all employee records in the table
     *
     * @return list of all employees
     * @throws SQLException
     */
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
            throw new SQLDataException(msgProvider.getValue("error.getAll", new Object[] { TAG }));
        }

        final Employee[] subarray = new Employee[employees.size()];
        return employees.toArray(subarray);
    }

    /**
     * Verifies if the credentials of an employee match
     *
     * @param employee    the employee to be authenticated
     * @param credentials credentials of the authenticating employee
     * @return true, if credentials match
     * @return false, otherwise
     * @throws AuthenticationFailedException, if credentials don't match
     * @throws SQLException
     */
    public boolean verifyUser(Employee employee, Credentials credentials)
            throws AuthenticationFailedException, SQLException {
        if (employee == null || credentials == null) {
            return false;
        }
        final Credentials found = credentialsManager.find(employee.getEmpNumber());
        if (found == null) {
            throw new AuthenticationFailedException(msgProvider.getValue("error.authentication.unknownEmployee"));
        }
        if (!credentials.equals(found)) {
            throw new AuthenticationFailedException(msgProvider.getValue("error.authentication.wrongCredentials"));
        }
        return true;
    }

    /**
     * Gets the current employee
     *
     * @return Employee POJO
     * @throws SQLException
     */
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

    /**
     * Checks if the current employee is an admin
     *
     * @return true, if admin is logged in
     * @return false, otherwise
     * @throws SQLException
     */
    public boolean isAdminLogin() throws SQLException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Boolean adminLogin = (Boolean) context.getExternalContext().getSessionMap().get("admin");
        if (adminLogin != null) {
            return adminLogin;
        }
        final Employee currEmployee = getCurrentEmployee();
        if (currEmployee == null) {
            return false;
        }
        final Employee admin = adminManager.find();
        if (currEmployee.getUsername().equals(admin.getUsername())) {
            context.getExternalContext().getSessionMap().put("admin", true);
            return true;
        }
        return false;
    }
}
