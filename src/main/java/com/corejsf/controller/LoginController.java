/**
 *
 */
package com.corejsf.controller;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.access.EmployeeManager;
import com.corejsf.messages.MessageProvider;
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

    @Inject
    /**
     * Provides messages from the message bundle.
     */
    private MessageProvider msgProvider;

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
        final FacesContext context = FacesContext.getCurrentInstance();
        try {
            final Employee employee = employeeManager.find(username);
            if (employee == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        msgProvider.getValue("error.authentication.unknownEmployee"), null));
                username = null;
                password = null;
                return null;
            } else {
                final Credentials credentials = new Credentials(username, password);
                credentials.setEmpNumber(employee.getEmpNumber());
                if (!employeeManager.verifyUser(employee, credentials)) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            msgProvider.getValue("error.authentication.wrongCredentials"), null));
                    return null;
                }
                context.getExternalContext().getSessionMap().put("emp_no", employee.getUsername());
                conversation.end();
                return "success";
            }
        } catch (final Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), null));
            return null;
        }
    }

    /**
     * Logout method that returns to logout.
     *
     * @return "logout"
     */
    public String logout() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (!conversation.isTransient()) {
            conversation.end();
        }
        context.getExternalContext().invalidateSession();
        context.getExternalContext().getSessionMap().clear();
        return "logout";
    }

    /**
     * Checks if a user is authenticated or not
     *
     * @return true, if user is authenticated
     * @return false, otherwise
     */
    public boolean isAuthenticated() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String username = (String) context.getExternalContext().getSessionMap().get("emp_no");
        if (username == null) {
            return false;
        }
        return true;
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
