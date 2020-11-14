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
import com.corejsf.access.EmployeeManagers;
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
    /**
     * Provides access to employees
     */
    private EmployeeManager empManager;

    @Inject
    /**
     * Provides access to credentials
     */
    private CredentialsManager credentialsManager;

    @Inject
    /**
     * Injected conversation
     */
    private Conversation conversation;
    
    @Inject EmployeeManagers empManagers;

    /**
     * Represents an editable timesheet
     */
    private EditableEmployee editEmployee;
    
    List<EditableEmployee> list;
    
    public List<EditableEmployee> getList() {
        if (list == null) {
            refreshList();
        }
        return list;
    }
    
    public void refreshList() {
        Employee[] employees = empManagers.getAll();
        list = new ArrayList<EditableEmployee>();
        for (int i = 0; i < employees.length; i++) {
            list.add(new EditableEmployee(employees[i]));
        }
    }
    
    public void setList(List<EditableEmployee> es) {
        list = es;
    }
    
    public String deleteRow(EditableEmployee e) {
        empManagers.remove(e.getEmployee());
        list.remove(e);
        return null;
    }
    
    public String save() {
        for (EditableEmployee e : list) {
            if (e.getEditable()) {
                empManagers.merge(e.getEmployee());
                e.setEditable(false);
            }
        }
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
        if (!empManager.isAdminLogin()) {
            return null;
        }
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
        if (!empManager.isAdminLogin()) {
            return null;
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
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = empManager.getEmployee(username);
        if (employee == null) {
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
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final Employee employee = empManager.getEmployee(username);
        if (employee == null) {
            return null;
        }
        editEmployee = new EditableEmployee(employee, false);
        return "/employee/view";
    }

    public String prepareDelete(String username) {
        final Employee employee = empManager.getEmployee(username);
        if (employee == null) {
            return null;
        }
        empManager.deleteEmployee(employee);
        return null;
    }

    /**
     * Checks if the employee is unique and stores it in the employees list
     *
     * @return route to list employees
     */
    public String onCreate() {
        try {
            empManager.addEmployee(editEmployee.getEmployee());
        } catch (final IllegalArgumentException ex) {
            final FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(ex.getLocalizedMessage()));
            return null;
        }

        editEmployee.getCredentials().setEmpNumber(editEmployee.getEmployee().getEmpNumber());
        editEmployee.getCredentials().setUsername(editEmployee.getEmployee().getUsername());

        credentialsManager.add(editEmployee.getCredentials());
        editEmployee = null;
        conversation.end();
        return "/employee/list";
    }

    /**
     * Edits an employee
     *
     * @return the route to list employees
     */
    public String onEdit() {
        editEmployee.getCredentials().setEmpNumber(editEmployee.getEmployee().getEmpNumber());
        editEmployee.getCredentials().setUsername(editEmployee.getEmployee().getUsername());
        editEmployee = null;
        conversation.end();
        return "/employee/list";
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
