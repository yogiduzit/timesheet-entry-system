/**
 * 
 */
package com.corejsf;

import com.corejsf.model.timesheet.Timesheet;

/**
 * This is the class that represents editable
 * timesheets.
 * 
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */
public class EditableTimesheet {
	
    /**
     * Represents if edit is available or not.
     */
	private Boolean editable = true;
	/**
	 * Represents a timesheet.
	 */
	private Timesheet timesheet;
	
	/**
	 * This is the constructor with a boolean to deterime
	 * if timesheet is editable.
	 * @param editable
	 */
	public EditableTimesheet(Boolean editable) {
		this.editable = editable;
		this.timesheet = new Timesheet();
	}
	
	/**
	 * This is another constructor with two parameters.
	 * 
	 * @param editable
	 * @param timesheet
	 */
	public EditableTimesheet(Boolean editable, Timesheet timesheet) {
		this.editable = editable;
		this.timesheet = timesheet;
	}
	
	/**
	 * Getter that returns the editable.
	 * @return editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * Setter that sets the editable.
	 * @param editable
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * Getter for the timesheet.
	 * 
	 * @return timesheet
	 */
	public Timesheet getTimesheet() {
		return timesheet;
	}

	/**
	 * Setter for the timesheet.
	 * @param timesheet
	 */
	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}
	
}
