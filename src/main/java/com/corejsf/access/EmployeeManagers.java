package com.corejsf.access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.data.Admins;
import com.corejsf.data.Employees;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Named("employeeManagers")
@ConversationScoped
public class EmployeeManagers implements Serializable {
    private static final long serialVersionUID =1L;
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;
    
    public Employee find(int num) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees where EmpNo = '" + num + "'");
                    if (result.next()) {
                        return new Employee(result.getInt("EmpNo"),
                                result.getString("EmpName"),
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
        } catch (SQLException ex) {
            System.out.println("Error in find " + num);
            ex.printStackTrace();
            return null;
        }
    }
    
    public void persist(Employee employee) {
        final int empNo = 1;
        final int empName = 2;
        final int empUsername = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Employees " 
                            + "VALUES (?, ?, ?)");
                    stmt.setInt(empNo, employee.getEmpNumber());
                    stmt.setString(empName, employee.getFullName());
                    stmt.setString(empUsername, employee.getUsername());
                    stmt.executeUpdate();
                } finally {
                    if(stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in persist " + employee);
            ex.printStackTrace();
        }
    }
    
    public void merge(Employee employee) {
        final int empName = 1;
        final int empUsername = 2;
        final int empNo = 3;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Employees "
                            + "SET EmpName = ?, EmpUserName = ? "
                            + "WHERE EmpNo = >");
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
        } catch (SQLException ex) {
            System.out.println("Error in merge " + employee);
            ex.printStackTrace();
        }
    }
    
    public void remove(Employee employee) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Employees WHERE EmpNo = ?");
                    stmt.setInt(1, employee.getEmpNumber());
                    stmt.executeUpdate();
                } finally {
                    if(stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in remove " + employee);
            ex.printStackTrace();
        }
    }
    
    public Employee[] getAll() {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees ORDER BY EmpNo");
                    while (result.next()) {
                        employees.add(new Employee(
                                result.getInt("EmpNo"),
                                result.getString("EmpName"),
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
        } catch (SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }
        
        Employee [] subarray = new Employee[employees.size()];
        return employees.toArray(subarray);
    }
}
