package ca.ubc.cpsc304.database;

import ca.ubc.cpsc304.model.*;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    private boolean checkVTAvailable(String vtname) {
        try {
            Statement stmt =connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VLICENSE FROM VEHICLES WHERE STATUS=" + "'available'" + " AND VTNAME = '"
                    + vtname + "'");

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


    public void insertReservation(ReservationModel reservationModel) throws Exception {
        try {
            boolean available = checkVTAvailable(reservationModel.getVtname());
            if (!available) {
                throw new Exception("Vehicle type not available");
            }

            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?)");

            ps.setInt(1, reservationModel.getConfNo());
            ps.setString(2, reservationModel.getVtname());
            ps.setString(3, reservationModel.getDLicense());
            ps.setTimestamp(4, reservationModel.getFromDateTime());
            ps.setTimestamp(5, reservationModel.getToDateTime());

            ps.executeUpdate();
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

            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ReservationModel getReservationInfo(String confo, String dLicense) throws Exception {
        ReservationModel result = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservation WHERE CONFNO =" + Integer.parseInt(confo)
                    + " OR DLICENSE='" + dLicense + "'");

            if (rs.next()) {

                result = new ReservationModel(
                        rs.getInt("confNo"),
                        rs.getString("vtname"),
                        rs.getString("dLicense"),
                        rs.getTimestamp("fromDateTime"),
                        rs.getTimestamp("toDateTime"));
            } else {
                throw new Exception("Confirmation number or driver's license is invalid");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return result;
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

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            if (vtname.trim().isEmpty() && location.trim().isEmpty() && fromDateTime.trim().isEmpty() && toDateTime.trim().isEmpty()) {
                rs = stmt.executeQuery("SELECT v.vtname, location, model, make, v.year, colour, features, status " +
                        "FROM vehicles v, vtype t WHERE v.vtname=t.vtname AND STATUS='available' ORDER BY v.vtname, location");
            } else {
                String sqlquery = "SELECT v.vtname, location, model, make, year, colour, features, status " +
                        "FROM vehicles v, vtype t WHERE v.vtname=t.vtname AND STATUS='available'";
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
    private RentModel checkRidIsNull(ReturnModel returnModel, PreparedStatement ps) throws Exception {
        ResultSet rs = ps.executeQuery("SELECT rid FROM rent WHERE rid = " + returnModel.getRid());

        if (rs.next()) {
            RentModel res = new RentModel(
                    rs.getInt("rid"),
                    rs.getString("vlicense"),
                    rs.getString("dlicense"),
                    rs.getTimestamp("fromDateTim"),
                    rs.getTimestamp("toDateTim"),
                    rs.getInt("odometer"), null, null, null,
                    rs.getInt("confNo"));
            rs.close();
            return res;
        }

        rs.close();
        return null;
    }

    private void updateVehicle(String vlicense, String status) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE VEHICLES SET STATUS= '" + status +
                    "' WHERE VEHICLES.VLICENSE ='" + vlicense + "'");

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Vehicle " + vlicense + " does not exist!");
            }

            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void rentVehicle(RentModel rentModel) throws Exception {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VLICENSE, ODOMETER FROM VEHICLES, RESERVATION " +
                    "WHERE STATUS='available' AND VEHICLES.VTNAME=RESERVATION.VTNAME AND RESERVATION.CONFNO="
                    + rentModel.getConfNo());

            String vlicense;
            int odo;
            if (rs.next()) {
                vlicense = rs.getString("Vlicense");
                odo = rs.getInt("Odometer");

                rs.close();
                stmt.close();
            } else {
                rs.close();
                stmt.close();
                throw new Exception("Desired vehicle not available");
            }

            updateVehicle(vlicense, "rented");

            PreparedStatement ps = connection.prepareStatement("INSERT INTO rent " +
                    "(rid, VLICENSE, dLicense, fromDateTime, toDateTime, odometer, cardName, " +
                    "cardNo, expDate, confNo VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setInt(1, rentModel.getRid());
            ps.setString(2, vlicense);
            ps.setString(3, rentModel.getDlicense());
            ps.setTimestamp(4, rentModel.getFromDateTime());
            ps.setTimestamp(5, rentModel.getToDateTime());
            ps.setInt(6, odo);
            ps.setString(7, rentModel.getCardName());
            ps.setString(8, rentModel.getCardNo());
            ps.setString(9, rentModel.getExpDate());
            ps.setInt(10, rentModel.getConfNo());

            if (checkConfNoIsNull(rentModel, ps)) {
                // System.out.println("This vehicle has not even been reserved before renting!");
                throw new Exception("This vehicle has not been reserved before renting!");
            }

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }
    }


    public String[] returnVehicle(ReturnModel returnModel) throws Exception {
        String[] res = null;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO returns " +
                    "(rid, datetime, odometer, fulltank, value) VALUES (?, ?, ?, ?, ?)");

            RentModel rentModel = checkRidIsNull(returnModel, ps);
            if (rentModel == null) {
                throw new Exception("This vehicle has not been rented!");
            } else {
                ps.setString(1, returnModel.getRid());
                ps.setTimestamp(2, returnModel.getDateTime());
                ps.setInt(3, returnModel.getOdometer());
                ps.setInt(4, returnModel.isFulltank());
                ps.setInt(5, returnModel.getValue());

                updateVehicle(rentModel.getVlicense(), "available");
                
                // TODO DO THE CALCULATION
                String calculation = "";
                int value = 0;
                String confo = Integer.toString(rentModel.getConfNo());
                res = new String[]{confo, calculation, Integer.toString(value)};

                ps.executeUpdate();
                connection.commit();
                ps.close();
                return res;
            }
        } catch (SQLException e) {
            System.out.println(LOG_TAG + e.getMessage());
        }
        return res;
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
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Branch", "Vehicle Type", "Returns/Type",
                "Subtotal: Type/Branch Returned", "Subtotal: Revenue/Branch", "Total Returns", "Total Revenue","Rid", "Return Time", "Odometer", "Fulltank?", "Value"}, 0);
        if (!checkValidDate(date, false)) {
            return null;
        }
        String day = "'" + date + "'";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT location, VTNAME, VTReturns, COUNT(VTReturns) over (partition " +
                    "by LOCATION) as SubtotalVehicles, SUM(VALUE) over (partition by LOCATION) as SubtotalRevenue, " +
                    "TotalReturns, TotalRevenue, rt.RID, DATETIME, rt.ODOMETER, FULLTANK, VALUE FROM RETURNS rt, RENT r, " +
                    "VEHICLES v, (SELECT VTNAME as vtype, COUNT(*) as VTReturns FROM RETURNS, RENT, VEHICLES " +
                    "WHERE RENT.VLICENSE = VEHICLES.VLICENSE AND RETURNS.RID=RENT.RID AND TRUNC(RETURNS.DATETIME) = " +
                    "TO_DATE(" + day + ") GROUP BY VTNAME), (SELECT COUNT(*) as TotalReturns, SUM(VALUE) as TotalRevenue " +
                    "FROM RETURNS WHERE TRUNC(DATETIME) = TO_DATE(" + day + ")) WHERE r.VLICENSE=v.VLICENSE AND " +
                    "vtype=v.VTNAME AND r.RID=rt.RID AND TRUNC(rt.DATETIME) = TO_DATE(" + day + ") ORDER BY LOCATION, VTNAME");

            while (rs.next()) {
                String l = rs.getString("location");
                String vt = rs.getString("VTNAME");
                String vr = rs.getString("VTReturns");
                String sv = rs.getString("SubtotalVehicles");
                String sr = rs.getString("SubtotalRevenue");
                String gv = rs.getString("TotalReturns");
                String gr = rs.getString("TotalRevenue");
                String r = rs.getString("rid");
                String d = rs.getTimestamp("datetime").toString().substring(0,16);
                int o = rs.getInt("odometer");
                String t = "No";
                if (rs.getInt("fulltank") == 1) {
                    t = "Yes";
                }
                int v = rs.getInt("value");
                vmodel.addRow(new Object[]{l, vt, vr, sv, sr, gv, gr, r, d, o, t, v});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return vmodel;
    }

    public DefaultTableModel getDailyReturnByBranch(String date, String location) {
        DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Branch", "Vehicle Type", "Returns/Type",
                "Total Returns", "Total Revenue","Rid", "Return Time", "Odometer", "Fulltank?", "Value"}, 0);
        if (!checkValidDate(date, false)) {
            return null;
        }
        String day = "'" + date + "'";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT location, VTNAME, VTReturns, COUNT(VTReturns) over (partition " +
                    "by LOCATION) as SubtotalVehicles, SUM(VALUE) over (partition by LOCATION) as SubtotalRevenue, " +
                    "TotalReturns, TotalRevenue, rt.RID, DATETIME, rt.ODOMETER, FULLTANK, VALUE " +
                    "FROM RETURNS rt, RENT r, VEHICLES v, (SELECT VTNAME as vtype, COUNT(*) as VTReturns FROM RETURNS, RENT, VEHICLES " +
                    "WHERE RENT.VLICENSE = VEHICLES.VLICENSE AND RETURNS.RID=RENT.RID AND location = '" + location + "' " +
                    "AND TRUNC(RETURNS.DATETIME) = TO_DATE(" + day + ") GROUP BY VTNAME), " +
                    "(SELECT COUNT(*) as TotalReturns, SUM(VALUE) as TotalRevenue FROM RETURNS, RENT, VEHICLES WHERE TRUNC(DATETIME) = " +
                    "TO_DATE(" + day + ") AND RENT.VLICENSE = VEHICLES.VLICENSE AND RETURNS.RID=RENT.RID AND location = '" + location + "') " +
                    "WHERE r.VLICENSE=v.VLICENSE AND vtype=v.VTNAME AND r.RID=rt.RID AND TRUNC(rt.DATETIME) = " +
                    "TO_DATE(" + day + ") AND location = '" + location + "' ORDER BY LOCATION, VTNAME");

            while (rs.next()) {
                String l = rs.getString("location");
                String vt = rs.getString("VTNAME");
                String vr = rs.getString("VTReturns");
                String sv = rs.getString("SubtotalVehicles");
                String sr = rs.getString("SubtotalRevenue");
                String r = rs.getString("rid");
                String d = rs.getTimestamp("datetime").toString().substring(0,16);
                int o = rs.getInt("odometer");
                String t = "No";
                if (rs.getInt("fulltank") == 1) {
                    t = "Yes";
                }
                int v = rs.getInt("value");
                vmodel.addRow(new Object[]{l, vt, vr, sv, sr, r, d, o, t, v});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(LOG_TAG + " " + e.getMessage());
        }

        return vmodel;
    }
}
