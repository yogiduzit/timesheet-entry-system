package com.corejsf.access;

import java.util.List;
import java.util.Map;

import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

/**
 * This is an interface  for EmployeeList
 * 
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
public interface EmployeeList {
    /**
     * Shows the list of employees
     * 
     * @return ArrayList of users
     */
    List<Employee> getEmployees();
    
    /**
     * Gets the employee's name
     * 
     * @param username
     * @return name
     */
    Employee getEmployee(String username);
    
    /**
     * Returns a map of valid password for username
     * 
     * @return map with username and password
     */
    Map<String, String> getLoginCombos();
    
    /**
     * Getters for current employee
     * 
     * @return current employee
     */
    Employee getCurrentEmployee();
    
    /**
     * Gets the administrator 
     * 
     * @return admin
     */
    Employee getAdministrator();
    
    /**
     * Verifies the username and password
     * 
     * @param credentials
     * @return true if valid, false if not valid
     */
    boolean verifyUser(Employee employee, Credentials credentials);
    
    /**
     * Deletes the user
     * 
     * @param userToDelete
     */
    void deleteEmployee(Employee userToDelete);
    
    /**
     * Adds a new Employee
     * 
     * @param newEmployee
     */
    void addEmployee(Employee newEmployee);

}
