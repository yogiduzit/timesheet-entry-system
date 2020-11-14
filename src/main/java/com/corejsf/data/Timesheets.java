/**
 * 
 */
package com.corejsf.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.model.timesheet.Timesheet;

/**
 * This is the class called Timesheets.
 * 
 * @author Yogesh Verma and Sung Na
 * @version 1.0
 *
 */
@Named("timesheets")
@ApplicationScoped
public class Timesheets {
    /**
     * Represents a cell class of the timesheets table.
     */
	private static final String CELL_CLASS = "timesheet-table-cell,";
	/**
	 * Represents the cell classes reapting ten times.
	 */
	private static final String CELL_CLASSES = CELL_CLASS.repeat(10) + CELL_CLASS;
	
	/**
	 * Represents the timesheet in List.
	 */
	private List<Timesheet> timesheets;
	
	/**
	 * Constructor for the Timesheets.
	 */
	public Timesheets() {
		timesheets = new ArrayList<>();
	}
	
	/**
	 * Gettter for the cell classes.
	 * @return CELL_CLASSES
	 */
	public String getCellClasses() {
		return CELL_CLASSES;
	}
	
	/**
	 * Getter for the timesheets in a List.
	 * @return timesheets
	 */
	public List<Timesheet> getTimesheets() {
		return timesheets;
	}
}
