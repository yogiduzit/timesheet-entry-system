package com.corejsf.access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.corejsf.messages.MessageProvider;
import com.corejsf.model.timesheet.TimesheetRow;

@Named("rowManager")
@ConversationScoped
/**
 * This is the class called TimesheetManager that implements the interface
 * TimesheetCollection.
 *
 * @author Sung Na and Yogesh Verma
 *
 */
public class TimesheetRowManager implements Serializable {

    /**
     * Variable for the serializable.
     */
    private static final long serialVersionUID = -1786252399378663291L;
    private static String TAG = "TimesheeetRow";

    /**
     * Datasource for a project
     */
    @Resource(mappedName = "java:jboss/datasources/timesheet_entry_system")
    private DataSource dataSource;

    @Inject
    private MessageProvider msgProvider;

    /**
     * Getting the Timesheets.
     *
     * @throws SQLException
     */
    public ArrayList<TimesheetRow> getTimesheetRows(Integer timesheetId) throws SQLException {
        final ArrayList<TimesheetRow> timesheetRows = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection
                            .prepareStatement("SELECT * FROM TimesheetRows WHERE TimesheetId = ? ORDER BY TimesheetID");
                    stmt.setInt(1, timesheetId);
                    final ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        final String hours = result.getString("HoursForWeek");

                        final BigDecimal[] weeklyHours = convertToWeeklyHours(hours);
                        timesheetRows.add(new TimesheetRow(result.getInt("ProjectId"), result.getString("WorkPackage"),
                                weeklyHours, result.getString("Notes")));
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
        return timesheetRows;
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     *
     * @throws SQLException
     */
    public void create(Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        final int TimesheetID = 1;
        final int ProjectID = 2;
        final int WorkPackage = 3;
        final int Notes = 4;
        final int HoursForWeek = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO TimesheetRows VALUES(?, ?, ?, ?, ?)");
                    for (final TimesheetRow timesheetRow : timesheetRows) {
                        stmt.setInt(TimesheetID, timesheetId);
                        stmt.setInt(ProjectID, timesheetRow.getProjectID());
                        stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                        stmt.setString(Notes, timesheetRow.getNotes());
                        stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();
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
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     *
     * @throws SQLException
     */
    public void create(Integer timesheetId, TimesheetRow timesheetRow) throws SQLException {
        final int TimesheetID = 1;
        final int ProjectID = 2;
        final int WorkPackage = 3;
        final int Notes = 4;
        final int HoursForWeek = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO TimesheetRows VALUES(?, ?, ?, ?, ?)");
                    stmt.setInt(TimesheetID, timesheetId);
                    stmt.setInt(ProjectID, timesheetRow.getProjectID());
                    stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                    stmt.setString(Notes, timesheetRow.getNotes());
                    stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                    stmt.addBatch();
                    stmt.clearParameters();
                    stmt.executeUpdate();
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
    }

    /**
     * Creating a Timesheet object and adding it to the collection.
     *
     * @throws SQLException
     */
    public void update(Integer timesheetId, List<TimesheetRow> timesheetRows) throws SQLException {
        final int HoursForWeek = 1;
        final int Notes = 2;
        final int ProjectID = 3;
        final int WorkPackage = 4;
        final int TimesheetID = 5;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE TimesheetRows "
                            + "SET HoursForWeek=?, Notes=?, ProjectID=?, WorkPackage=? " + "WHERE TimesheetId = ?");
                    for (final TimesheetRow timesheetRow : timesheetRows) {
                        stmt.setInt(TimesheetID, timesheetId);
                        stmt.setInt(ProjectID, timesheetRow.getProjectID());
                        stmt.setString(WorkPackage, timesheetRow.getWorkPackage());
                        stmt.setString(Notes, timesheetRow.getNotes());
                        stmt.setString(HoursForWeek, stringifyWeeklyHours(timesheetRow.getHoursForWeek()));
                        stmt.addBatch();
                        stmt.clearParameters();
                    }
                    stmt.executeBatch();
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

    private static BigDecimal[] convertToWeeklyHours(String hours) {
        final String[] temp = hours.split(",");
        final BigDecimal[] weeklyHours = new BigDecimal[temp.length];

        for (int i = 0; i < weeklyHours.length; i++) {
            weeklyHours[i] = BigDecimal.valueOf(Long.valueOf(temp[i]));
        }
        return weeklyHours;
    }

    private static String stringifyWeeklyHours(BigDecimal[] hours) {
        String weeklyHours = "";
        for (int i = 0; i < hours.length; i++) {
            weeklyHours += (hours[i] + ",");
        }
        return weeklyHours;
    }

}