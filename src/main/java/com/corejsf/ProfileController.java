/**
 *
 */
package com.corejsf;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.access.CredentialsManager;
import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

@Named("profileController")
@ConversationScoped
/**
 * @author yogeshverma
 *
 */
public class ProfileController implements Serializable {

    @Inject
    /**
     * Conversation for this bean
     */
    private Conversation conversation;

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

    /**
     * employee to be used in forms
     */
    private EditableEmployee editEmployee;

    /**
     * Credentials of the current user
     */
    private Credentials credentials;

    /**
     * Old password for the user
     */
    private String oldPassword;

    /**
     * New password for the user
     */
    private String newPassword;

    /**
     * New password for the user confirmation
     */
    private String confirmNewPassword;

    /**
     * Prepares the page to show user profile
     *
     * @return route to profile page
     */
    public String prepareProfile() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        final FacesContext context = FacesContext.getCurrentInstance();
        Employee employee;
        try {
            employee = empManager.getCurrentEmployee();
            if (employee == null) {
                throw new Exception("Could not find current employee! Please login again");
            }
            credentials = credentialsManager.find(employee.getEmpNumber());
        } catch (final Exception e) {
            context.addMessage(null, new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        editEmployee = new EditableEmployee(employee, true);
        return "/employee/profile";
    }

    /**
     * Saves the user profile and performs validation
     *
     * @return String route to list timesheets page
     */
    public String onSaveProfile() {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (oldPassword == null || newPassword == null || confirmNewPassword == null) {
            context.addMessage(null, new FacesMessage("Please fill in the required fields"));
            return null;
        }

        if (!credentials.getPassword().equals(oldPassword)) {
            context.addMessage(null, new FacesMessage("Old password is incorrect"));
            return null;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            context.addMessage(null, new FacesMessage("New password and confirm new password do not match"));
            return null;
        }

        credentials.setUsername(editEmployee.getEmployee().getUsername());
        credentials.setPassword(newPassword);
        try {
            credentialsManager.merge(credentials);
        } catch (final Exception e) {
            context.addMessage(null, new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        conversation.end();
        return "/timesheet/list";
    }

    /**
     * @return the employee
     */
    public EditableEmployee getEditEmployee() {
        return editEmployee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEditEmployee(EditableEmployee employee) {
        editEmployee = employee;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the confirmNewPassword
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * @param confirmNewPassword the confirmNewPassword to set
     */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
