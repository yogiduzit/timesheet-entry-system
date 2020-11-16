/**
 *
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.access.CredentialsManager;
import com.corejsf.access.EmployeeManager;
import com.corejsf.messages.MessageProvider;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

/**
 * @author yogeshverma
 *
 */
@ConversationScoped
@Named("employeeController")
public class EmployeeController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5825295337476934595L;

    @Inject
    private MessageProvider msgProvider;

    @Inject
    /**
     * Injected conversation
     */
    private Conversation conversation;

    @Inject
    private EmployeeManager empManager;

    @Inject
    private CredentialsManager credManager;

    /**
     * Represents an editable timesheet
     */
    private EditableEmployee editEmployee;

    List<EditableEmployee> empList;

    public List<EditableEmployee> getList() {
        if (empList == null) {
            refreshList();
        }
        return empList;
    }

    public void refreshList() {
        final FacesContext context = FacesContext.getCurrentInstance();
        Employee[] employees;
        try {
            employees = empManager.getAll();
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
            return;
        }
        empList = new ArrayList<EditableEmployee>();
        for (int i = 0; i < employees.length; i++) {
            empList.add(new EditableEmployee(employees[i]));
        }
    }

    public String deleteRow(EditableEmployee emp) {
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            empManager.remove(emp.getEmployee());
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
        }

        refreshList();
        return null;
    }

    /**
     *
     * Checks if an admin user is logged in and directs the user to the list of
     * employees page
     *
     * @return the route to list of employees
     */
    public String prepareList() {
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!empManager.isAdminLogin()) {
                return null;
            }
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
        }

        refreshList();
        return "/employee/list";
    }

    /**
     *
     * Checks if an admin user is logged in and directs the user to the create
     * employee page
     *
     * @return the route to create employees page
     */
    public String prepareCreate() {
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!empManager.isAdminLogin()) {
                return null;
            }
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
        }
        if (conversation.isTransient()) {
            conversation.begin();
        }
        editEmployee = new EditableEmployee(true);
        return "/employee/create";
    }

    /**
     *
     * Checks if an admin user is logged in and directs the user to the edit
     * employees page
     *
     * @return the route to edit employees
     */
    public String prepareEdit(String username) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (conversation.isTransient()) {
            conversation.begin();
        }
        Employee employee;
        try {
            employee = empManager.find(username);
            if (employee == null) {
                throw new Exception(msgProvider.getValue("error.find", new Object[] { "Employee" }));
            }
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
            return null;
        }

        editEmployee = new EditableEmployee(employee, true);
        return "/employee/edit";
    }

    /**
     *
     * Checks if an admin user is logged in and directs the user to the view
     * employees page
     *
     * @return the route to view employees
     */
    public String prepareView(String username) {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (conversation.isTransient()) {
            conversation.begin();
        }
        Employee employee;
        try {
            employee = empManager.find(username);
            if (employee == null) {
                throw new Exception(msgProvider.getValue("error.find", new Object[] { "Employee" }));
            }
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
            return null;
        }
        editEmployee = new EditableEmployee(employee, false);
        return "/employee/view";
    }

    /**
     * Checks if the employee is unique and stores it in the employees list
     *
     * @return route to list employees
     */
    public String onCreate() {
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            empManager.persist(editEmployee.getEmployee());

            final Credentials credentials = new Credentials(editEmployee.getEmployee().getUsername(),
                    editEmployee.getCredentials().getPassword());
            credentials.setEmpNumber(editEmployee.getEmployee().getEmpNumber());
            credManager.insert(credentials);
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
            return null;
        }

        conversation.end();
        return prepareList();
    }

    /**
     * Edits an employee
     *
     * @return the route to list employees
     */
    public String onEdit() {
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            empManager.merge(editEmployee.getEmployee());

            final Credentials credentials = new Credentials(editEmployee.getEmployee().getUsername(),
                    editEmployee.getCredentials().getPassword());
            credentials.setEmpNumber(editEmployee.getEmployee().getEmpNumber());
            credManager.insert(credentials);
        } catch (final Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage()));
            return null;
        }
        conversation.end();
        return prepareList();
    }

    /**
     * Get the editable employee
     *
     * @return
     */
    public EditableEmployee getEditEmployee() {
        return editEmployee;
    }

    /**
     * Set the editable employee
     *
     * @param editEmployee
     */
    public void setEditEmployee(EditableEmployee editEmployee) {
        this.editEmployee = editEmployee;
    }

}
