/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.data.CredentialsList;
import com.corejsf.model.employee.Credentials;

/**
 * This is the class called CredentialsManager
 *
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
@Named("credentialsManager")
@ConversationScoped
public class CredentialsManager implements Serializable {

    /**
     * Variable for implementing Serialiable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    /**
     * Injecting the CredentialsList
     */
    @Inject
    private CredentialsList credentialsList;

    /**
     * Method to get the credentials by employee number
     *
     * @param empNumber
     * @return credentials
     */
    public Credentials getCredentials(int empNumber) {
        for (final Credentials credentials : credentialsList.getAllCredentials()) {
            if (credentials.getEmpNumber() == empNumber) {
                return credentials;
            }
        }
        return null;
    }

    public void add(Credentials credentials) {
        credentialsList.getAllCredentials().add(credentials);
    }

}
