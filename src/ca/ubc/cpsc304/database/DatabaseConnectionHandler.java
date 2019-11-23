package ca.ubc.cpsc304.database;

import ca.ubc.cpsc304.model.*;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String LOG_TAG = DatabaseConnectionHandler.class.getSimpleName();
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
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
            loadData();
            System.out.println("Loaded data");
            return true;
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void loadData() {
        String s            = new String();
        StringBuffer sb = new StringBuffer();

        try {
            // There are two of the same statements here.  Choose the one that works for you.
            //FileReader fr = new FileReader(new File("Project/CPSC304_SuperRent/resources/vehicletables.sql"));
            FileReader fr = new FileReader(new File("resources/vehicletables.sql"));
            // be sure to not have line starting with "--" or "/*" or any other non alphabetical character

            BufferedReader br = new BufferedReader(fr);

            while((s = br.readLine()) != null)
            {
                sb.append(s);
            }
            br.close();

            // here is our splitter! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            Statement st = connection.createStatement();

            for(int i = 0; i<inst.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst[i].trim().equals(""))
                {
                    st.executeUpdate(inst[i]);
                    System.out.println(">>"+inst[i]);
                }
            }

        } catch (Exception e) {
            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }
    }

    /**
     * START OF CUSTOMER TRANSACTIONS:
     * 1) View vehicles
     * 2) Make reservation
     * 3) Delete reservation
     * 4) Update reservation
     */

    // Helper function to check if customer already exists in Customer relation.
    public boolean checkCustomerExists(String dlicense){
        try {
            Statement stmt =connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT dLicense FROM customers WHERE dLicense = '"
                    + dlicense + "'");

            if (!rs.next()) {
                rs.close();
                return false;
            }

            rs.close();
            return true;
        } catch (SQLException e){
            System.out.println(LOG_TAG + " " + e.getMessage());
        }
         return false;
    }


    public void insertCustomer(CustomerModel customerModel){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers VALUES (?,?,?,?)");

            ps.setString(1, customerModel.getCellphone());
            ps.setString(2, customerModel.getName());
            ps.setString(3, customerModel.getAddress());
            ps.setString(4, customerModel.getdLicense());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Method to test if newly inserted reservations actually get inserted.
    private void testWithResultData() throws SQLException {
        Statement statement = null;
        String query = "SELECT * FROM reservation";

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int confNo = resultSet.getInt("confNo");
                String vtName = resultSet.getString("vtname");
                String dLicense = resultSet.getString("dLicense");
                Timestamp fromDateTime = resultSet.getTimestamp("fromDateTime");
                Timestamp toDateTime = resultSet.getTimestamp("toDateTime");

                System.out.println(confNo + ", " + vtName + ", " + dLicense + ", " + fromDateTime + ", " +
                        toDateTime);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void insertReservation(ReservationModel reservationModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?)");

            ps.setInt(1, reservationModel.getConfNo());
            ps.setString(2, reservationModel.getVtname());
            ps.setString(3, reservationModel.getDLicense());
            ps.setTimestamp(4, reservationModel.getFromDateTime());
            ps.setTimestamp(5, reservationModel.getToDateTime());

            // TODO: Should executeQuery be used here instead of executeUpdate since former returns a ResultSet,
            //  which can be used to to display details in a receipt.
            ps.executeUpdate();
            testWithResultData();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
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

            ps.executeUpdate();
            testWithResultData();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateReservation(int confNo, ReservationModel reservationModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE reservation SET confNo = ?, vtName = ?, " +
                    "dLicense = ?, fromDateTime = ?, toDateTime = ? WHERE confNo = ?");
            ps.setInt(1, confNo);
            ps.setString(2, reservationModel.getVtname());
            ps.setString(3, reservationModel.getDLicense());
            ps.setTimestamp(4, reservationModel.getFromDateTime());
            ps.setTimestamp(5, reservationModel.getToDateTime());

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Reservation " + confNo + " does not exist!");
            }

            testWithResultData();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
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
                ReservationModel model = new ReservationModel(rs.getInt("confNo"),
                        rs.getString("vtname"),
                        rs.getString("dLicense"),
                        rs.getTimestamp("fromDateTime"),
                        rs.getTimestamp("toDateTime"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return result.toArray(new ReservationModel[result.size()]);
    }

    private boolean checkValidDate(String date, boolean h) {
        String d = date.trim();
        if (d.isEmpty()){
            return true;
        }
        if (h) {
            String dateRegEx = "^((2000|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29\\s\\d\\d)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8])\\s\\d\\d)$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01])\\s\\d\\d)$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30)\\s\\d\\d)$";
            return d.matches(dateRegEx);
        } else {
            String dateRegEx = "^((2000|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$";
            return d.matches(dateRegEx);
        }

    }

    public DefaultTableModel getVehicleInfo(String vtname, String location, String fromDateTime, String toDateTime) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Vehicle Type", "Location", "Model", "Make", "Year",
                "Colour", "Features", "Current Status"}, 0);

        if (!checkValidDate(fromDateTime, true) || !checkValidDate(toDateTime, true)) { return null; }

        String from = "";
        String to = "";
        if (!fromDateTime.trim().isEmpty()) {
            from = "TO_TIMESTAMP('" + fromDateTime.trim() + ":00:00')";
        }
        if (!toDateTime.trim().isEmpty()) {
            to = "TO_TIMESTAMP('" + toDateTime.trim() + ":00:00')";
        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            // TODO add back in ' AND STATUS='available' ' when we change our data
            if (vtname.trim().isEmpty() && location.trim().isEmpty() && fromDateTime.trim().isEmpty() && toDateTime.trim().isEmpty()) {
                rs = stmt.executeQuery("SELECT v.vtname, location, model, make, v.year, colour, features, status " +
                        "FROM vehicles v, vtype t WHERE v.vtname=t.vtname ORDER BY v.vtname, location");
            } else {
                boolean prev = false;
                String sqlquery = "SELECT v.vtname, location, model, make, year, colour, features, status " +
                        "FROM vehicles v, vtype t WHERE v.vtname=t.vtname";
                if (!vtname.trim().isEmpty()) {
                    sqlquery = sqlquery+ " AND " + "v.vtname = " + "'" + vtname + "'";
                }
                if (!location.trim().isEmpty()) {
                    sqlquery = sqlquery + " AND " + "location = " + "'" + location + "'";
                }
                sqlquery = sqlquery + " ORDER BY v.vtname, location";
                System.out.println(sqlquery);
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
            System.out.println(LOG_TAG + " " + e.getMessage());
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
        ResultSet rs = ps.executeQuery("SELECT confNo FROM reservation WHERE confNo = "
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
        ResultSet rs = ps.executeQuery("SELECT rid FROM rent WHERE rid = "
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
            PreparedStatement ps = connection.prepareStatement("INSERT INTO rent " +
                    "(rid, VLICENSE, dLicense, fromDateTime, toDateTime, odometer, cardName, " +
                    "cardNo, expDate, confNo VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
            System.out.println(LOG_TAG + " " + e.getMessage());
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
            System.out.println(LOG_TAG + e.getMessage());
        }
    }

    public boolean checkBranch(String location) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT location FROM VEHICLES " +
                    "WHERE LOCATION =" + "'"+ location + "'");

            if (rs.next()) {
                rs.close();
                stmt.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println(LOG_TAG + e.getMessage());
        }
        return false;
    }

    public DefaultTableModel getDailyRental(String date) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Company Total", "Branch", "Branch Total", "Vehicle Type", "Type Total", "rid", "vlicense", "confNo", "dlicense",
                "fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);
        if (!checkValidDate(date, false) || date.trim().isEmpty()) {
            return null;
        }

        String day = "'" + date + "'";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT CompanyTotal, location, VTNAME, BRentals, VRentals, rid, CONFNO, r.VLICENSE, DLICENSE, FROMDATETIME, TODATETIME, r.ODOMETER, CARDNAME, CARDNO,EXPDATE " +
                    "FROM RENT r, VEHICLES v, (SELECT VTNAME as vtype, COUNT(*) as VRentals FROM RENT, VEHICLES " +
                    "WHERE RENT.VLICENSE = VEHICLES.VLICENSE GROUP BY VTNAME), (SELECT LOCATION as branch, " +
                    "COUNT(*) as BRentals FROM RENT, VEHICLES WHERE RENT.VLICENSE = VEHICLES.VLICENSE " +
                    "GROUP BY LOCATION),(SELECT COUNT(*) as CompanyTotal FROM RENT) " +
                    "WHERE r.VLICENSE=v.VLICENSE AND vtype=v.VTNAME AND branch=v.LOCATION " +
                    "AND TRUNC(r.fromDateTime) = TO_DATE(" + day + ") ORDER BY LOCATION, VTNAME");

            while (rs.next()) {
                String ct = rs.getString("CompanyTotal");
                String l = rs.getString("location");
                String br = rs.getString("BRentals");
                String vt = rs.getString("VTNAME");
                String vr = rs.getString("VRentals");
                String r = rs.getString("rid");
                String v = rs.getString("vlicense");
                String c = rs.getString("confNo");
                String d = rs.getString("dlicense");
                String from = rs.getTimestamp("fromDateTime").toString().substring(0,16);
                String to = rs.getTimestamp("toDateTime").toString().substring(0,16);
                int o = rs.getInt("odometer");
                String cnm = rs.getString("cardName");
                String cno = rs.getString("cardNo");
                String e = rs.getString("expDate");
                vmodel.addRow(new Object[]{ct, l,br, vt, vr, r, v, c, d, from, to, o, cnm, cno, e});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyRentalByBranch(String date, String location) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Branch","Branch Total","Vehicle Type", "Type Total" , "rid", "vlicense", "confNo", "dlicense",
                "fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);
        if (!checkValidDate(date, false)|| date.trim().isEmpty()) {
            return null;
        }
        String day = "'" + date + "'";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT location, VTNAME, BRentals, VRentals, rid, CONFNO, r.VLICENSE, DLICENSE, FROMDATETIME, TODATETIME, r.ODOMETER, CARDNAME, CARDNO,EXPDATE " +
                    "FROM RENT r, VEHICLES v, (SELECT VTNAME as vtype, COUNT(*) as VRentals FROM RENT, VEHICLES " +
                    "WHERE RENT.VLICENSE = VEHICLES.VLICENSE GROUP BY VTNAME), (SELECT LOCATION as branch, " +
                    "COUNT(*) as BRentals FROM RENT, VEHICLES WHERE RENT.VLICENSE = VEHICLES.VLICENSE " +
                    "GROUP BY LOCATION) " +
                    "WHERE r.VLICENSE=v.VLICENSE AND vtype=v.VTNAME AND branch=v.LOCATION " +
                    "AND TRUNC(r.fromDateTime)= TO_DATE(" + day + ") AND location = '" + location +
                    "' ORDER BY LOCATION, VTNAME");

            while (rs.next()) {
                String l = rs.getString("location");
                String br = rs.getString("BRentals");
                String vt = rs.getString("VTNAME");
                String vr = rs.getString("VRentals");
                String r = rs.getString("rid");
                String v = rs.getString("vlicense");
                String c = rs.getString("confNo");
                String d = rs.getString("dlicense");
                String from = rs.getTimestamp("fromDateTime").toString().substring(0,16);
                String to = rs.getTimestamp("toDateTime").toString().substring(0,16);
                int o = rs.getInt("odometer");
                String cnm = rs.getString("cardName");
                String cno = rs.getString("cardNo");
                String e = rs.getString("expDate");
                vmodel.addRow(new Object[]{l,br, vt, vr, r, v, c, d, from, to, o, cnm, cno, e});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyReturn(String date) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"rid", "datetime", "odometer", "fulltank?", "value"}, 0);
        if (!checkValidDate(date, false)) {
            return null;
        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM returns r, vehicles v WHERE r.vlicense=v.vlicense AND " +
                        "CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE)");


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
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyReturnByBranch(String date, String location) {
        return null;
    }
}
