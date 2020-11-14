package com.corejsf.access;

import java.io.Serializable;
import java.util.List;

import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;

//import ca.bcit.infosys.employee.Employee;

/**
 * A interface for accessing all existing Timesheets.
 *
 * @author Bruce Link
 * @version 1.1
 *
 */
public interface TimesheetCollection extends Serializable {
    /**
     * timesheets getter.
     * @return all of the timesheets.
     */
    List<Timesheet> getTimesheets();

    /**
     * get all timesheets for an employee.
     * @param e the employee whose timesheets are returned
     * @return all of the timesheets for an employee.
     */
    List<Timesheet> getTimesheets(Employee e);

    /**
     * get current timesheet for an employee.
     * @param e the employee whose current timesheet is returned
     * @return the current timesheet for an employee.
     */
    Timesheet getCurrentTimesheet(Employee e);

    /**
     * Creates a Timesheet object and adds it to the collection.
     *
     * @return a String representing navigation to the newTimesheet page.
     */
    String addTimesheet(Timesheet timesheet);
}

