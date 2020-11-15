package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;
import com.corejsf.model.timesheet.TimesheetRow;

@Named("timesheetManager")
@ConversationScoped
/**
 * This is the class called TimesheetManager that implements Serializable, the
 * interface TimesheetCollection.
 *
 * @author Sung Na and Yogesh Verma
 *
 */
public class TimesheetManager implements Serializable {

    /**
     * Variable for the serializable.
     */
    private static final long serialVersionUID = -1786252399378663291L;

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_entry_system")
    private DataSource dataSource;

    @Inject
    private TimesheetRowManager rowManager;

    @Inject
    private EmployeeManagers empManager;

    /**
     * Getting the Timesheets.
     */
    public List<Timesheet> getTimesheets() {
        final ArrayList<Timesheet> timesheets = new ArrayList<Timesheet>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    final ResultSet result = stmt.executeQuery("SELECT * FROM Timesheets ORDER BY TimesheetID");
                    while (result.next()) {
                        final int id = result.getInt("TimesheetID");
                        final List<TimesheetRow> rows = rowManager.getTimesheetRows(id);
                        final Employee employee = empManager.find(result.getInt("EmpNo"));
                        final Timesheet timesheet = new Timesheet(employee, result.getDate("EndWeek").toLocalDate(),
                                rows);
                        timesheet.setId(id);
                        timesheets.add(timesheet);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }
        return timesheets;
    }

    /**
     * Getting all the timesheets for one employee.
     */
    public List<Timesheet> getTimesheets(Integer empNo) {
        final ArrayList<Timesheet> timesheets = new ArrayList<Timesheet>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * FROM Timesheets WHERE EmpNo = ? ORDER BY TimesheetID");
                    stmt.setInt(1, empNo);
                    final ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        final int id = result.getInt("Id");
                        final List<TimesheetRow> rows = rowManager.getTimesheetRows(id);
                        final Employee employee = empManager.find(result.getInt("EmpNo"));
                        final Timesheet timesheet = new Timesheet(employee, result.getDate("EndWeek").toLocalDate(),
                                rows);
                        timesheet.setId(id);
                        timesheets.add(timesheet);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }
        return timesheets;
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     */
    public void insert(Timesheet timesheet) {
        final int EmpNo = 1;
        final int EndWeek = 2;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                try {
                    stmt = connection.prepareStatement("INSERT INTO Timesheets(EmpNo, EndWeek) VALUES(?, ?)");
                    stmt.setInt(EmpNo, timesheet.getEmployee().getEmpNumber());
                    stmt.setDate(EndWeek, java.sql.Date.valueOf(timesheet.getEndWeek()));
                    stmt.executeUpdate();
                    connection.commit();
                } catch (final Exception e) {
                    connection.rollback();
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
        }
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     */
    public void merge(Timesheet timesheet) {
        final int EmpNo = 1;
        final int EndWeek = 2;
        final int TimesheetID = 3;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Timesheets " + "SET EmpNo = ?, EndWeek = ? " + "WHERE TimesheetID = ?");
                    stmt.setInt(EmpNo, timesheet.getEmployee().getEmpNumber());
                    stmt.setDate(EndWeek, java.sql.Date.valueOf(timesheet.getEndWeek()));
                    stmt.setInt(TimesheetID, timesheet.getId());
                    stmt.executeUpdate();
                    connection.commit();
                } catch (final Exception e) {
                    connection.rollback();
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
        }
    }

    public Timesheet find(Integer empNo, String weekEnding) {
        final Timesheet timesheet = new Timesheet();

        final int EmpNo = 1;
        final int WeekEnd = 2;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "SELECT * FROM Timesheets WHERE EmpNo = ? AND EndWeek = ? ORDER BY TimesheetID");
                    stmt.setInt(EmpNo, empNo);
                    stmt.setDate(WeekEnd, java.sql.Date.valueOf(weekEnding));
                    final ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        final int id = result.getInt("Id");
                        final ArrayList<TimesheetRow> rows = rowManager.getTimesheetRows(id);
                        final Employee employee = empManager.find(result.getInt("EmpNo"));
                        timesheet.setEmployee(employee);
                        timesheet.setEndWeek(result.getDate("EndWeek").toLocalDate());
                        timesheet.setDetails(rows);
                        timesheet.setId(id);
                        return timesheet;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (final SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }
        return timesheet;
    }

}
