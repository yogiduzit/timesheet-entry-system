package com.corejsf.access;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.data.Timesheets;
import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;

@Named("timesheetManager")
@ConversationScoped
/**
 * This is the class called TimesheetManager that 
 * implements the interface TimesheetCollection.
 * 
 * @author Sung Na and Yogesh Verma
 *
 */
public class TimesheetManager implements TimesheetCollection {

	/**
	 * Variable for the serializable.
	 */
	private static final long serialVersionUID = -1786252399378663291L;
	
	/**
	 * Injecting the Timesheets dataSource.
	 */
	@Inject private Timesheets dataSource;

	/**
	 * Getting the Timesheets.
	 */
	@Override
	public List<Timesheet> getTimesheets() {
		return dataSource.getTimesheets();
	}

	/**
	 * Creating a Timesheet object and adding it to the collection.
	 */
	@Override
	public String addTimesheet(Timesheet timesheet) {
		dataSource.getTimesheets().add(timesheet);
		return "1";
	}

	/**
	 * Getting all the timesheets for one employee.
	 */
	@Override
	public List<Timesheet> getTimesheets(Employee e) {
		List<Timesheet> timesheets = new ArrayList<>();
		for (Timesheet timesheet: this.getTimesheets()) {
			if (timesheet.getEmployee().getEmpNumber() == e.getEmpNumber()) {
				timesheets.add(timesheet);
			}
		}
		return timesheets;
	}

	/**
	 * Getting the current timesheet for an employee.
	 */
	@Override
	public Timesheet getCurrentTimesheet(Employee e) {
		List<Timesheet> timesheets = this.getTimesheets(e);
		
		long maxWeekEnd = 0;
		int currTimesheetIndex = 0;
		for(int i = 0; i < timesheets.size() - 1; i++) {
			Timesheet timesheet = timesheets.get(i);
			
			long weekEnd = timesheet.getEndWeek().toEpochDay();
			if (weekEnd > maxWeekEnd) {
				weekEnd = maxWeekEnd;
				currTimesheetIndex = i;
			}
		}
		return timesheets.get(currTimesheetIndex);
	}
	
	/**
	 * Method that finds the timesheet depending on the date of the week.
	 * 
	 * @param e Employee
	 * @param weekEnding String
	 * @return timesheet or null
	 */
	public Timesheet findTimesheet(Employee e, String weekEnding) {
		for (Timesheet timesheet: this.getTimesheets(e)) {
			if (timesheet.getWeekEnding().equals(weekEnding.toString())) {
				return timesheet;
			}
		}
		return null;
	}
	
}
