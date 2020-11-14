package com.corejsf.model.employee;

import java.time.LocalDate;

/**
 * This class represents one employee.
 * 
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 */
public class Employee {
    /**
     * Represents the employee number
     */
    private int empNumber;
    /**
     * Represents the first name of the employee
     */
    private String fullName;
    /**
     * Represents the last name of the eployee
     */
    private String username;
    
    /**
     * no parameter constructor
     */
    public Employee() {
    }
    
    /**
     * Three parameter constructor. Creates the initial employees
     * who have access as well as the admin
     * 
     * @param empNum
     * @param empName
     * @param id
     */
    public Employee(final int empNum, final String empName, final String id) {
        empNumber = empNum;
        fullName = empName;
        username = id;
    }
    
    /**
     * Getter for employee number
     * @return empNumber
     */
    public int getEmpNumber() {
        return empNumber;
    }
    /**
     * Setter for employee number
     * @param empNumber
     */
    public void setEmpNumber(final int empNum) {
        empNumber = empNum;
    }
    /**
     * Getter for employee full name
     * @return fullName
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Setter for employee full name
     * @param fullName
     */
    public void setFullName(final String empName) {
        fullName = empName;
    }
    /**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Setter for username
     * @param username
     */
    public void setUsername(final String id) {
        username = id;
    }
    
    

   
    
    

}
