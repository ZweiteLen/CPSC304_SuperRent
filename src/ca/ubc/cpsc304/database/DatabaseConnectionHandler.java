package ca.ubc.cpsc304.database;

import ca.ubc.cpsc304.model.*;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    /**
     * START OF CUSTOMER TRANSACTIONS:
     * 1) View vehicles
     * 2) Make reservation
     * 3) Delete reservation
     * 4) Update reservation
     */

    /**
     * Transactions Implementation Start
     * These are from the terminalTransactions
     * We can use them as examples
     */

    // Helper function to check if customer already exists in Customer relation.
    private boolean checkCustomerExists(PreparedStatement ps, ReservationModel reservationModel)
			throws SQLException {
        ResultSet rs = ps.executeQuery("SELECT dLicense FROM customers WHERE dLicense = "
                + reservationModel.getDLicense());

        if (!rs.next()) {
            rs.close();
            return false;
        }

        rs.close();
        return true;
    }

    public void insertReservation(ReservationModel reservationModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?)");

            ps.setString(1, reservationModel.getConfNo());
            ps.setString(2, reservationModel.getVtname());

            if (checkCustomerExists(ps, reservationModel)) {
            	ps.setString(3, reservationModel.getDLicense());
            } else {
                // TODO: Call TransactionsWindowDelefate.insertCustomer(...) from here somehow.
            	// TODO: Display separate GUI to allow a new customer to enter details.
            }
            ps.setString(4, reservationModel.getFromDateTime());
            ps.setString(5, reservationModel.getToDateTime());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteReservation(int confNo) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM reservation WHERE confNo = ?");
            ps.setInt(1, confNo);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Reservation " + confNo + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateReservation(int confNo, String vtname) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE reservation SET vtname = ? WHERE confNo = ?");
            ps.setString(1, vtname);
            ps.setInt(2, confNo);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Reservation " + confNo + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ReservationModel[] getReservationInfo() {
        ArrayList<ReservationModel> result = new ArrayList<ReservationModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservation");

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

            while (rs.next()) {
                ReservationModel model = new ReservationModel(rs.getString("confNo"),
                        rs.getString("vtname"),
                        rs.getString("dLicense"),
                        rs.getString("fromDateTime"),
                        rs.getString("toDateTime"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ReservationModel[result.size()]);
    }

    public DefaultTableModel getVehicleInfo(String vtname, String location, String fromDateTime, String toDateTime) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Vehicle Type", "Location", "Model", "Make", "Year",
                "Colour", "Features", "Current Status"}, 0);
        String from = "";
        String to = "";
        if (!fromDateTime.isBlank()) {
            from = "TO_TIMESTAMP('" + fromDateTime + ":00:00')";
        }
        if (!toDateTime.isBlank()) {
            to = "TO_TIMESTAMP('" + toDateTime + ":00:00')";
        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            // TODO test this query
            if (vtname.isBlank() ^ location.isBlank() ^ fromDateTime.isBlank() ^ toDateTime.isBlank()) {
                rs = stmt.executeQuery("SELECT v.vtname, location, model, make, year, colour, features, status " +
                        "FROM vehicles v, vtype t WHERE v.vtname=t.vtname ORDER  BY v.vtname, location");
            } else {
                boolean prev = false;
                String sqlquery = "SELECT v.vtname, location, model, make, year, colour, features, status " +
                        "FROM vehicles v, vtype vt WHERE v.vtname=t.vtname AND ";
                if (!vtname.isBlank()) {
                    sqlquery = sqlquery + "v.vtname = " + "'" + vtname + "'" + "v.vtname=r.vtname";
                    prev = true;
                }
                if (!location.isBlank()) {
                    if (prev) {
                        sqlquery = sqlquery + " AND ";
                    }
                    sqlquery = sqlquery + "location = " + "'" + location + "'";
                    prev = true;
                }
                if (!from.isBlank() ^ !to.isBlank()) {
                    if (prev) {
                        sqlquery = sqlquery + " AND ";
                    }
                    sqlquery = sqlquery + "v.VLICENSE NOT IN (select r.vlicense from rent r " +
                            "where (r.FROMDATETIME<=" + from + " AND " + from + "<=r.toDateTime) OR " +
                            "(r.fromDateTime<=" + to + " AND " + to + "<=r.TODATETIME) OR " +
                            from + "<=r.FROMDATETIME AND " + "r.FROMDATETIME<=" + to + " OR " +
                            from + "<=r.TODATETIME AND " + "r.TODATETIME<=" + to + ")";
                } else if (!from.isBlank() ^ to.isBlank()) {
                    if (prev) {
                        sqlquery = sqlquery + " AND ";
                    }
                    sqlquery = sqlquery + "v.VLICENSE NOT IN (select r.vlicense from rent r " +
                            "where r.FROMDATETIME<=" + from + " AND " + from + "<=r.toDateTime)";
                } else if (from.isBlank() ^ !to.isBlank()) {
                    if (prev) {
                        sqlquery = sqlquery + " AND ";
                    }
                    sqlquery = sqlquery + "v.VLICENSE NOT IN (select r.vlicense from rent r " +
                            "where r.fromDateTime<=" + to + " AND " + to + "<=r.TODATETIME)";
                }
                sqlquery = sqlquery + " ORDER BY v.vtname, location";
                rs = stmt.executeQuery(sqlquery);
            }

            while (rs.next()) {
                String v = rs.getString("vtname");
                String s = rs.getString("status");
                String l = rs.getString("location");
                String mo = rs.getString("model");
                String ma = rs.getString("make");
                int y = rs.getInt("year");
                String c = rs.getString("colour");
                String f = rs.getString("features");
                vmodel.addRow(new Object[]{v, l, mo, ma, y, c, f, s});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    /**
     * START OF CLERK TRANSACTIONS:
     * 1) Rent vehicle
     * 2) Return vehicle
     * 3) Generate reports: daily rentals, daily rentals for specific branch, daily return,s
     * daily returns for specific branch
     */

    // Helper function to check if a vehicle has been reserved before renting
    // by comparing confirmation numbers.
    private boolean checkConfNoIsNull(RentModel rentModel, PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery("SELECT confNo FROM reservations WHERE confNo = "
                + rentModel.getConfNo());

        if (rs.next()) {
            String confNo = rs.getString(1);
            if (confNo.equals(rentModel.getConfNo())) {
                rs.close();
                return false;
            }
        }

        rs.close();
        return true;
    }

    // Helper function to check if a vehicle is rented before returning by comparing rent ids.
    private boolean checkRidIsNull(ReturnModel returnModel, PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery("SELECT rid FROM rentals WHERE rid = "
                + returnModel.getRid());

        if (rs.next()) {
            String rid = rs.getString(1);
            if (rid.equals(returnModel.getRid())) {
                rs.close();
                return false;
            }
        }

        rs.close();
        return true;
    }

    // TODO: Display receipt (confirmation number, date of reservation, type of car, location,
    //  rental period, vehicle license, driver license).
    // TODO: Handle case where vehicle was not reserved prior to renting.
    public void rentVehicle(RentModel rentModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals " +
                    "(rid, vid, dLicense, fromDateTime, toDateTime, odometer, cardName, " +
                    "cardNo, exoDate, confNo VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, rentModel.getRid());
            ps.setString(2, rentModel.getVlicense());
            ps.setString(3, rentModel.getDlicense());
            ps.setTimestamp(4, rentModel.getFromDateTime());
            ps.setTimestamp(5, rentModel.getToDateTime());
            ps.setInt(6, rentModel.getOdometer());
            ps.setString(7, rentModel.getCardName());
            ps.setString(8, rentModel.getCardNo());
            ps.setString(9, rentModel.getExpDate());

            if (checkConfNoIsNull(rentModel, ps)) {
                System.out.println("This vehicle has not even been reserved before renting!");
            }

            if (rentModel.getConfNo() == null) {
                ps.setNull(10, Types.INTEGER);
            } else {
                ps.setString(10, rentModel.getConfNo());
            }

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // TODO: Show receipt with reservation confirmation number, date of return, how total is calculated.
    public void returnVehicle(ReturnModel returnModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO returns " +
                    "(rid, datetime, odometer, fulltank, value) VALUES (?, ?, ?, ?, ?)");

            if (checkRidIsNull(returnModel, ps)) {
                System.out.println("This vehicle has not even been rented!");
                return;
            } else {
                ps.setString(1, returnModel.getRid());
                ps.setTimestamp(2, returnModel.getDateTime());
                ps.setInt(3, returnModel.getOdometer());
                ps.setBoolean(4, returnModel.isFulltank());
                ps.setInt(5, returnModel.getValue());

                ps.executeUpdate();
                connection.commit();
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + e.getMessage());
        }
    }

    public DefaultTableModel getDailyRental(String location) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"rid", "vlicense", "confNo", "dlicense",
                "fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            // TODO:
            /* FIGURE OUT WHAT THE PROJECT DESCRIPTION MEANS WHEN IT SAYS
             * 'contains information on all the vehicles rented out during the day'
             */
            if (location.isBlank()) {
                rs = stmt.executeQuery("SELECT rid, vlicense, confNo, dlicense, CONVERT(varchar, fromDateTime), " +
                        "CONVERT(varchar, toDateTime), odometer, cardName, cardNo, expDate  FROM rent r " +
                        "WHERE CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE)");
            } else {
                rs = stmt.executeQuery("SELECT rid, vlicense, confNo, dlicense, CONVERT(varchar, fromDateTime), " +
                        "CONVERT(varchar, toDateTime), odometer, cardName, cardNo, expDate " +
                        "FROM rent r, vehicles v WHERE r.vlicense=v.vlicense AND " +
                        "CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE) AND v.location = " + "'" + location + "'");
            }

            while (rs.next()) {
                String r = rs.getString("rid");
                String v = rs.getString("vlicense");
                String c = rs.getString("confNo");
                String d = rs.getString("dlicense");
                Date from = rs.getDate("fromDateTime");
                Date to = rs.getDate("toDateTime");
                int o = rs.getInt("odometer");
                String cnm = rs.getString("cardName");
                String cno = rs.getString("cardNo");
                String e = rs.getString("expDate");
                vmodel.addRow(new Object[]{r, v, c, d, from, to, o, cnm, cno, e});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyRentalByBranch(String location) {
        return null;
    }

    public DefaultTableModel getDailyReturn(String location) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"rid", "datetime", "odometer", "fulltank?", "value"}, 0);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            // TODO test this query
            if (location.isBlank()) {
                rs = stmt.executeQuery("SELECT *  FROM returns  WHERE " +
                        "CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE)");
            } else {
                rs = stmt.executeQuery("SELECT * FROM returns r, vehicles v WHERE r.vlicense=v.vlicense AND " +
                        "CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE) AND v.location = " + "'" + location + "'");
            }

            while (rs.next()) {
                String r = rs.getString("rid");
                Date d = rs.getDate("datetime");
                int o = rs.getInt("odometer");
                String t = "No";
                if (rs.getBoolean("fulltank")) {
                    t = "Yes";
                }
                int v = rs.getInt("value");
                vmodel.addRow(new Object[]{r, d, o, t, v});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyReturnByBranch(String location) {
        return null;
    }
}
