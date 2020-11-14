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

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

/**
 * This class represents the login controller.
 *
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 */
@Named("loginController")
@ConversationScoped
public class LoginController implements Serializable {

    /**
     * Variable for the serializable.
     */
    private static final long serialVersionUID = 6687823809360236313L;

    /**
     * Injecting the employee manager.
     */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Injecting the conversation.
     */
    @Inject
    private Conversation conversation;

    /**
     * String for the username.
     */
    private String username;
    /**
     * String for the password.
     */
    private String password;

    /**
     * Login method that returns a string to let user know if it was successful or
     * not.
     *
     * @return String
     */
    public String login() {
        if (conversation.isTransient()) {
            conversation.begin();
        }

        final Employee employee = employeeManager.getEmployee(username);
        final FacesContext context = FacesContext.getCurrentInstance();

        if (employee == null) {
            context.addMessage(null, new FacesMessage("Unknown login, please try again"));
            username = null;
            password = null;
            return null;
        } else {
            final Credentials credentials = new Credentials(username, password);
            credentials.setEmpNumber(employee.getEmpNumber());
            if (!employeeManager.verifyUser(employee, credentials)) {
                context.addMessage(null, new FacesMessage("Could not authenticate user, please try again"));
                return null;
            }
            context.getExternalContext().getSessionMap().put("emp_no", employee.getUsername());
            conversation.end();
            return "success";
        }
    }

    /**
     * Logout method that returns to logout.
     *
     * @return "logout"
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout";
    }

    /**
     * Getter for the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
