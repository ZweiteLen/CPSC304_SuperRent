package ca.ubc.cpsc304.database;

import ca.ubc.cpsc304.model.*;

import javax.management.DescriptorAccess;
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
		DefaultTableModel vmodel = new DefaultTableModel(new String[]{"Current Status","Location","Model", "Make", "Year",
				"Colour", "Features"}, 0);

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs;
			// TODO test this query
			if (vtname.isBlank() ^ location.isBlank() ^ fromDateTime.isBlank() ^ toDateTime.isBlank()) {
				rs = stmt.executeQuery("SELECT status, location, model, make, year, colour, features FROM vehicles");
			} else {
				boolean prev = false;
				String sqlquery = "SELECT status, location, model, make, year, colour, features " +
								  "FROM vehicles v, vtype vt, reservation r WHERE ";
				if (!vtname.isBlank()) {
					sqlquery = sqlquery + "v.vtname = " + "'" + vtname + "'" + " AND v.vtname=vt.vtname AND v.vtname=r.vtname";
				}
				if (!location.isBlank()) {
					sqlquery = sqlquery + "location = " + "'" + location + "'";
					prev = true;
				}
				if (!fromDateTime.isBlank() ^ !toDateTime.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					sqlquery = sqlquery + "'" + fromDateTime + "'" + "<= r.toDateTime AND r.fromDateTime >= "
							+ "'" + toDateTime+ "'";
				} else if (!fromDateTime.isBlank() ^ toDateTime.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					sqlquery = sqlquery + "'" + fromDateTime + "'" + "< r.fromDateTime OR " +
							"'" + fromDateTime + "' > r.toDateTime";
				} else if (fromDateTime.isBlank() ^ !toDateTime.isBlank()) {
					if (prev) {sqlquery = sqlquery + " AND ";}
					String todt = "'" + toDateTime + "'";
					sqlquery = sqlquery +  todt + "< r.fromDateTime OR " +
							todt + "> r.toDateTime";
				}
				rs = stmt.executeQuery(sqlquery);
			}

			while(rs.next())
			{
				String s = rs.getString("status");
				String l = rs.getString("location");
				String mo = rs.getString("model");
				String ma = rs.getString("make");
				int y = rs.getInt("year");
				String c = rs.getString("colour");
				String f = rs.getString("features");
				vmodel.addRow(new Object[]{s,l,mo,ma,y,c,f});
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

	public void rentVehicle(RentModel rentModel) {

	}

	public void returnVehicle(ReturnModel returnModel) {

	}

	public DefaultTableModel showDailyRentalsReport(String location) {
		return null;
	}

	public DefaultTableModel showDailyRentalsReportByBranch(String location) {
		return null;
	}

	public DefaultTableModel showDailyReturnsReport(String location) {
		return null;
	}

	public DefaultTableModel showDailyReturnReportByBranch(String location) {
		return null;
	}
}
