package com.corejsf.model.employee;

/**
 * This is an Admin class which is a sub-class
 * of Employee class
 * 
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */
public class Admin extends Employee {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3893651203522967194L;

	public Admin(Employee e) {
    	super(e.getEmpNumber(), e.getFullName(), e.getUsername());
    }

}
