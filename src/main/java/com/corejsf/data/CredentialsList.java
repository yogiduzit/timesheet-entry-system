/**
 *
 */
package com.corejsf.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.model.employee.Credentials;

@Named("credentialsList")
@ApplicationScoped
/**
 * This is the class for the CredentialsList.
 *
 * @author yogeshverma
 * @version 1.0
 *
 */
public class CredentialsList {
    private final List<Credentials> allCredentials;

    /**
     * Constructor for the CredentialsList.
     */
    public CredentialsList() {
        allCredentials = new ArrayList<Credentials>();
        allCredentials.add(new Credentials("bdlink", "password"));
        allCredentials.get(0).setEmpNumber(1);
    }

    /**
     * Getting all the Credentials into a list.
     *
     * @return allCredentials into a list
     */
    public List<Credentials> getAllCredentials() {
        return allCredentials;
    }

}
