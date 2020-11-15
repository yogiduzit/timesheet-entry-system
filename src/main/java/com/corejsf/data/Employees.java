package com.corejsf.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.model.employee.Employee;

/**
 * This is the class called employees that stores all employees info into a
 * list.
 *
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */
@Named("employees")
@ApplicationScoped
public class Employees {

    /**
     * List for the employees.
     */
    private final List<Employee> employees;

    /**
     * Getting employees into a list.
     *
     * @return employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Constructor for the employees class.
     */
    public Employees() {
        employees = new ArrayList<>();
        employees.add(new Employee(1, "Bruce Link", "bdlink"));
    }

}
