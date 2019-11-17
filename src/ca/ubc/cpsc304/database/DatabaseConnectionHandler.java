package ca.ubc.cpsc304.database;

import ca.ubc.cpsc304.model.ReturnModel;
import ca.ubc.cpsc304.model.CustomerModel;
import ca.ubc.cpsc304.model.ReservationModel;
import ca.ubc.cpsc304.model.VehicleTypeModel;
import ca.ubc.cpsc304.model.VehicleModel;

import javax.swing.*;
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
		try  {
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
	 * Transactions Implementation Start
	 * these are from the terminalTransactions
	 * we can use them as examples
	 */
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

	public void insertReservation(ReservationModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?)");
			ps.setString(1, model.getConfNo());
			ps.setString(2, model.getVtname());
			if (model.getCellphone() == 0) {
				ps.setNull(3, java.sql.Types.INTEGER);
			} else {
				ps.setInt(3, model.getCellphone());
			}
			// ps.setString(4, model.getFromDate());
			// ps.setString(5, model.getFromTime());
			// ps.setString(6, model.getToDate());
			// ps.setString(7, model.getToTime());


			ps.executeUpdate();
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

			while(rs.next()) {
				ReservationModel model = new ReservationModel(rs.getString("confNo"),
						rs.getString("vtname"),
						rs.getInt("cellphone"),
						rs.getString("fromdate"),
						rs.getString("fromtime"),
						rs.getString("todate"),
						rs.getString("totime"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new ReservationModel[result.size()]);
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

	public DefaultTableModel getVehicleInfo(String vtname, String location, String fromDateTime, String toDateTime) {
		DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Vehicle Type","Location","Model", "Make", "Year",
				"Colour", "Features", "Current Status"}, 0);
		String from = "";
		String to = "";
		if (!fromDateTime.isBlank()) {from = "'" + fromDateTime + ":00:00'";}
		if (!toDateTime.isBlank()) {to = "'" + toDateTime + ":00:00'";}

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs;
			// TODO test this query
			if (vtname.isBlank() ^ location.isBlank() ^ fromDateTime.isBlank() ^ toDateTime.isBlank()) {
				rs = stmt.executeQuery("SELECT vtname, location, model, make, year, colour, features, status FROM vehicles ORDER  BY location");
			} else {
				boolean prev = false;
				String sqlquery = "SELECT v.vtname, location, model, make, year, colour, features, status " +
								  "FROM vehicles v, vtype vt, reservation r WHERE ";
				if (!vtname.isBlank()) {
					sqlquery = sqlquery + "v.vtname = " + "'" + vtname + "'" + " AND v.vtname=vt.vtname AND v.vtname=r.vtname";
				}
				if (!location.isBlank()) {
					sqlquery = sqlquery + "location = " + "'" + location + "'";
					prev = true;
				}
				if (!from.isBlank() ^ !to.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					sqlquery = sqlquery + "TO_TIMESTAMP(" + from + ")<= r.toDateTime AND r.fromDateTime >= "
							+ "TO_TIMESTAMP(" + to +')';
				} else if (!fromDateTime.isBlank() ^ toDateTime.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					sqlquery = sqlquery + "TO_TIMESTAMP(" + from + ")< r.fromDateTime OR " +
							"TO_TIMESTAMP(" + from + ") > r.toDateTime";
				} else if (fromDateTime.isBlank() ^ !toDateTime.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					sqlquery = sqlquery + "TO_TIMESTAMP(" + to + ")< r.fromDateTime OR " +
							"TO_TIMESTAMP(" + to + ")> r.toDateTime";
				}
				sqlquery = sqlquery + " ORDER BY vtname, location";
				rs = stmt.executeQuery(sqlquery);
			}

			while(rs.next())
			{
			    String v = rs.getString("vtname");
				String s = rs.getString("status");
				String l = rs.getString("location");
				String mo = rs.getString("model");
				String ma = rs.getString("make");
				int y = rs.getInt("year");
				String c = rs.getString("colour");
				String f = rs.getString("features");
				vmodel.addRow(new Object[]{v,l,mo,ma,y,c,f,s});
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return vmodel;
	}

	public DefaultTableModel getDailyRental(String location) {
		DefaultTableModel vmodel = new DefaultTableModel(new String[]{"rid", "vlicense", "confNo", "dlicense",
				"fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs;
			// TODO test this query
			if (location.isBlank()) {
				rs = stmt.executeQuery("SELECT rid, vlicense, confNo, dlicense, CONVERT(varchar, fromDateTime), " +
						"CONVERT(varchar, toDateTime), odometer, cardName, cardNo, expDate  FROM rentals " +
						"WHERE CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE)");
			} else {
				rs = stmt.executeQuery("SELECT rid, vlicense, confNo, dlicense, CONVERT(varchar, fromDateTime), " +
						"CONVERT(varchar, toDateTime), odometer, cardName, cardNo, expDate " +
						"FROM rentals r, vehicles v WHERE r.vlicense=v.vlicense AND " +
						"CONVERT(DATE, r.fromDateTime)=CONVERT(DATE, CURRENT_DATE) AND v.location = " + "'" + location + "'");
			}

			while(rs.next())
			{
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

			while(rs.next())
			{
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
}
