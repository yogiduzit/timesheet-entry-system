package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.messages.MessageProvider;
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
    private static String TAG = "Timesheet";

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    @Inject
    private TimesheetRowManager rowManager;

    @Inject
    private EmployeeManager empManager;

    @Inject
    private MessageProvider msgProvider;

    /**
     * Getting the Timesheets.
     *
     * @throws SQLException
     */
    public List<Timesheet> getTimesheets() throws SQLException {
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.getAll", new Object[] { TAG }));
        }
        return timesheets;
    }

    /**
     * Getting all the timesheets for one employee.
     *
     * @throws SQLException
     */
    public List<Timesheet> getTimesheets(Integer empNo) throws SQLException {
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.getAll", new Object[] { TAG }));
        }
        return timesheets;
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     *
     * @throws SQLException
     */
    public int insert(Timesheet timesheet) throws SQLException {
        final int EmpNo = 1;
        final int EndWeek = 2;

        Connection connection = null;
        PreparedStatement stmt = null;
        int timesheetId = -1;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                try {
                    stmt = connection.prepareStatement("INSERT INTO Timesheets(EmpNo, EndWeek) VALUES(?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    stmt.setInt(EmpNo, timesheet.getEmployee().getEmpNumber());
                    stmt.setDate(EndWeek, java.sql.Date.valueOf(timesheet.getEndWeek()));
                    stmt.executeUpdate();

                    final ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        timesheetId = rs.getInt(1);
                    }
                    connection.commit();
                    rowManager.create(timesheetId, timesheet.getDetails());
                } catch (final SQLException e) {
                    connection.rollback();
                    throw e;
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.create", new Object[] { TAG }));
        }
        return timesheetId;
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     *
     * @throws SQLException
     */
    public void merge(Timesheet timesheet) throws SQLException {
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
                    rowManager.update(timesheet.getId(), timesheet.getDetails());
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.edit", new Object[] { TAG }));
        }
    }

    public Timesheet find(Integer empNo, String weekEnding) throws SQLException {
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
            ex.printStackTrace();
            throw new SQLDataException(msgProvider.getValue("error.find", new Object[] { TAG }));
        }
        return timesheet;
    }

}
