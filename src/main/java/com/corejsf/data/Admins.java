/**
 *
 */
package com.corejsf.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.model.employee.Admin;
import com.corejsf.model.employee.Employee;

/**
 * This is the class for Administrator.
 *
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
@Named("admins")
@ApplicationScoped
public class Admins {

    /**
     * List for all the admins.
     */
    private final List<Admin> admins;

    /**
     * Constructor of the Admins class.
     */
    public Admins() {
        admins = new ArrayList<>();
        admins.add(new Admin(new Employee(1, "Bruce Link", "bdlink")));
    }

    /**
     * Getting the adminstrators.
     *
     * @return admins
     */
    public List<Admin> getAdmins() {
        return admins;
    }
}
