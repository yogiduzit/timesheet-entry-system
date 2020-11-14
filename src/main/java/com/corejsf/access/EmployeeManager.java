package com.corejsf.access;

import java.io.Serializable;
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

@Named("employeeManager")
@ConversationScoped
/**
 * This is the class called EmployeeManager that implements an interface called
 * EmployeeList.
 *
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */
public class EmployeeManager implements EmployeeList, Serializable {

    /**
     * Variable for implementing serializable.
     */
    private static final long serialVersionUID = -8890730783815459598L;

    /**
     * Injecting Employees and Admins.
     */
    @Inject
    private Employees dataSource;
    @Inject
    private Admins adminList;

    /**
     * Injecting the CredentialsManager.
     */
    @Inject
    private CredentialsManager credentialsManager;

    /**
     * Getting the employees through the list
     */
    @Override
    public List<Employee> getEmployees() {
        return dataSource.getEmployees();
    }

    /**
     * Overriding method to get the employee.
     */
    @Override
    public Employee getEmployee(String username) {
        final List<Employee> employees = getEmployees();

        for (final Employee e : employees) {
            if (e.getUsername().equals(username)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Overriding method that gets the login combo.
     */
    @Override
    public Map<String, String> getLoginCombos() {

        return null;
    }

    /**
     * Overriding method that gets the current employee.
     */
    @Override
    public Employee getCurrentEmployee() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String username = (String) context.getExternalContext().getSessionMap().get("emp_no");
        return getEmployee(username);
    }

    /**
     * Overriding method that gets the admin.
     */
    @Override
    public Employee getAdministrator() {
        return adminList.getAdmins().get(0);
    }

    /**
     * Boolean method that checks if the admin is logged in.
     *
     * @return
     */
    public Boolean isAdminLogin() {
        final Employee currEmployee = getCurrentEmployee();
        final Employee admin = getAdministrator();
        if (admin == null || currEmployee == null) {
            return false;
        }
        return currEmployee.getEmpNumber() == admin.getEmpNumber();
    }

    /**
     * Overriding method that deletes the employee.
     */
    @Override
    public void deleteEmployee(Employee userToDelete) {
        if (getAdministrator().getUsername().equals(userToDelete.getUsername())) {
            return;
        }
        dataSource.getEmployees().remove(userToDelete);
    }

    /**
     * Overriding method that adds the employee.
     */
    @Override
    public void addEmployee(Employee newEmployee) {
        for (final Employee e : getEmployees()) {
            if (e.getUsername().equals(newEmployee.getUsername())) {
                throw new IllegalArgumentException("A user with the same username already exists");
            }

            if (e.getEmpNumber() == newEmployee.getEmpNumber()) {
                throw new IllegalArgumentException("A user with the same employee number already exists");
            }
        }
        dataSource.getEmployees().add(newEmployee);
    }

    /**
     * Overriding method that verifies the user.
     */
    @Override
    public boolean verifyUser(Employee employee, Credentials credentials) {
        if (employee == null) {
            return false;
        }
        final Credentials foundCredentials = credentialsManager.getCredentials(employee.getEmpNumber());
        return credentials.equals(foundCredentials);
    }

}
