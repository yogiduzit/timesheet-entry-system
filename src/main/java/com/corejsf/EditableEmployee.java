/**
 * 
 */
package com.corejsf;

import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

/**
 * @author yogeshverma
 *
 */
public class EditableEmployee {
	
	/**
	 * Determines if employee is editable or not
	 */
	private Boolean editable = true;
	
	/**
	 * The employee stored
	 */
	private Employee employee;
	
	/**
	 * Credentials (username, password) of the stored employee
	 */
	private Credentials credentials;
	
	/**
	 * Constructor to create a new editable employee
	 * @param editable, if the employee is editable
	 */
	public EditableEmployee(boolean editable) {
		this.editable = editable;
		this.employee = new Employee();
		this.credentials = new Credentials();
	}
	
	/**
	 * Constructor for an already existing employee
	 * @param employee, the stored employee
	 * @param editable, if the employee is editable
	 */
	public EditableEmployee(Employee employee, boolean editable) {
		this.editable = editable;
		this.employee = employee;
		this.credentials = new Credentials();
	}
	
	public EditableEmployee(Employee employee) {
	    this.employee = employee;
	}
	
	/**
	 * Getter for editable
	 * @return editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * Setter for editable
	 * @param editable
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * Getter for employee
	 * @return
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * Setter for employee
	 * @param employee
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * Getter for credentials
	 * @return
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * Setter for credentials
	 * @param credentials
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	
}
