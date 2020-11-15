/**
 *
 */
package com.corejsf;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.access.EmployeeManager;
import com.corejsf.access.TimesheetManager;
import com.corejsf.access.TimesheetRowManager;
import com.corejsf.model.timesheet.Timesheet;

/**
 * This class represents the timesheet controller.
 *
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 */
@Named("timesheetController")
@ConversationScoped
public class TimesheetController implements Serializable {

    /**
     * Injecting the timesheet manager.
     */
    @Inject
    private TimesheetManager manager;

    /**
     * Injecting the employee manager.
     */
    @Inject
    private EmployeeManager empManager;

    @Inject
    private TimesheetRowManager rowManager;
    /**
     * Injecting the conversation scope.
     */
    @Inject
    private Conversation conversation;

    /**
     * Represents the editable timesheet.
     */
    private EditableTimesheet editTimesheet;

    /**
     * List of timesheets
     */
    private List<Timesheet> timesheets;

    /**
     * Variable for implementing serializable.
     */
    private static final long serialVersionUID = -8334771555481885625L;

    /**
     * Constructor for the TimesheetController.
     */
    public TimesheetController() {
    }

    /**
     * Getter for the edit timesheets.
     *
     * @return edit timesheets
     */
    public EditableTimesheet getEditTimesheet() {
        return editTimesheet;
    }

    /**
     * @return the timesheets
     */
    public List<Timesheet> getTimesheets() {
        if (timesheets == null) {
            if (empManager.isAdminLogin()) {
                timesheets = manager.getTimesheets();
            } else {
                timesheets = manager.getTimesheets(empManager.getCurrentEmployee().getEmpNumber());
            }
        }
        return timesheets;
    }

    /**
     * Method that prepares to creates a new editable timesheet and starts the
     * conversation.
     *
     * @return directory to create.
     */
    public String prepareCreate() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        editTimesheet = new EditableTimesheet(true);
        return "/timesheet/create";
    }

    /**
     * Method that prepares to edit a new editable timesheet and starts the
     * conversation.
     *
     * @param timesheet to be edited
     * @return route to edit page
     */
    public String prepareEdit(Timesheet t) {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (t.getEmployee().getEmpNumber() != empManager.getCurrentEmployee().getEmpNumber()) {
            if (empManager.isAdminLogin()) {
                editTimesheet = new EditableTimesheet(true, t);
            } else {
                return null;
            }
        }
        editTimesheet = new EditableTimesheet(true, t);
        return "/timesheet/edit";
    }

    /**
     * Method that prepares to view the timesheet of an employee and starts the
     * conversation.
     *
     * @param timesheet to be edited
     * @return route to the view
     */
    public String prepareView(Timesheet t) {
        if (!conversation.isTransient()) {
            conversation.end();
        }
        if (t.getEmployee().getEmpNumber() != empManager.getCurrentEmployee().getEmpNumber()) {
            if (empManager.isAdminLogin()) {
                editTimesheet = new EditableTimesheet(true, t);
            } else {
                return null;
            }
        }
        editTimesheet = new EditableTimesheet(false, t);
        return "/timesheet/view";
    }

    /**
     * Prepares a list of timesheets
     *
     * @return route to list timesheets page
     */
    public String prepareList() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (empManager.isAdminLogin()) {
            timesheets = manager.getTimesheets();
        } else {
            timesheets = manager.getTimesheets(empManager.getCurrentEmployee().getEmpNumber());
        }
        return "/timesheet/list";
    }

    /**
     * Method that adds a new row of timesheet.
     *
     * @return adds a row
     */
    public String onAddRow() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (!(editTimesheet.getTimesheet().getDetails().size() == 7)) {
            editTimesheet.getTimesheet().addRow();
        }
        return null;
    }

    /**
     * Method that creates a timesheet.
     *
     * @return directory to list of timesheets
     */
    public String onCreate() {
        editTimesheet.getTimesheet().setEmployee(empManager.getCurrentEmployee());
        manager.insert(editTimesheet.getTimesheet());
        rowManager.upsert(editTimesheet.getTimesheet().getId(), editTimesheet.getTimesheet().getDetails());

        editTimesheet = null;
        conversation.end();
        return prepareList();
    }

    /**
     * Method that leads you to edit the timesheets.
     *
     * @return directory to list of timesheets
     */
    public String onEdit() {
        manager.merge(editTimesheet.getTimesheet());
        rowManager.upsert(editTimesheet.getTimesheet().getId(), editTimesheet.getTimesheet().getDetails());

        editTimesheet = null;
        conversation.end();
        return prepareList();
    }

}
