package ca.ubc.cpsc304.controller;

import ca.ubc.cpsc304.database.DatabaseConnectionHandler;
import ca.ubc.cpsc304.delegates.LoginWindowDelegate;
import ca.ubc.cpsc304.delegates.TransactionsWindowDelegate;
import ca.ubc.cpsc304.model.*;
import ca.ubc.cpsc304.ui.LoginWindow;
import ca.ubc.cpsc304.ui.TransactionsWindow;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class SuperRent implements LoginWindowDelegate, TransactionsWindowDelegate {
    private final String LOG_TAG = SuperRent.class.getSimpleName();

    private DatabaseConnectionHandler dbHandler;
    private LoginWindow loginWindow = null;

    private SuperRent() {
        dbHandler = new DatabaseConnectionHandler();
    }

    public static void main(String[] args) {
        SuperRent superRent = new SuperRent();
        superRent.start();
    }

    /**
     * loginWindow is temporarily commented out to make testing the
     * transactionsWindow gui easier on myself
     */
    private void start() {
         loginWindow = new LoginWindow();
         loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            TransactionsWindow transactionsWindow = new TransactionsWindow();
            transactionsWindow.showFrame(this);

        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    /**
     * Customer transactions.
     */
    public boolean checkCustomer(String dlicense) {
        return dbHandler.checkCustomerExists(dlicense);
    }

    public void insertReservation(ReservationModel reservationModel) throws Exception {
        dbHandler.insertReservation(reservationModel);
    }

    public void insertCustomer(CustomerModel model) {
       dbHandler.insertCustomer(model);
    }

    public DefaultTableModel showRentalVehicles(String vtname, String location, String fromDateTime,
                                                String toDateTime) throws Exception {
        DefaultTableModel resmodel = dbHandler.getVehicleInfo(vtname, location, fromDateTime, toDateTime);
        if (resmodel == null) {
            throw new SQLException("datetime");
        }
        return resmodel;
    }

    /**
     * Clerk transactions.
     */
    public RentModel insertRentVehicle(RentModel model) throws Exception {
        return dbHandler.rentVehicle(model);
    }

    public ReservationModel showReservations(String confNo, String dLicense) throws Exception {
        return dbHandler.getReservationInfo(confNo, dLicense);
    }

    public String[] insertReturnVehicle(ReturnModel model, int confNo) throws Exception {
        return dbHandler.returnVehicle(model, confNo);
    }

    public DefaultTableModel showDailyRentalsReport(String date) throws Exception {
        DefaultTableModel res = dbHandler.getDailyRental(date);
        if (res == null) {
            throw new Exception("Date not in correct format");
        }
        return res;

    }

    public DefaultTableModel showDailyRentalsReportByBranch(String date, String location) throws Exception {
        boolean valid = dbHandler.checkBranch(location);
        if (!valid) {
            throw new Exception("Branch does not exist");
        }
        DefaultTableModel res = dbHandler.getDailyRentalByBranch(date, location);
        if (res == null) {
            throw new Exception("Date not in correct format");
        }
        return res;
    }

    public DefaultTableModel showDailyReturnsReport(String date) throws Exception {
        DefaultTableModel res = dbHandler.getDailyReturn(date);
        if (res == null) {
            throw new Exception("Date not in correct format");
        }
        return res;
    }

    public DefaultTableModel showDailyReturnsReportByBranch(String date, String location) throws Exception {
        DefaultTableModel res = dbHandler.getDailyReturnByBranch(date, location);
        boolean valid = dbHandler.checkBranch(location);
        if (!valid) {
            throw new Exception("Branch does not exist");
        }
        if (res == null) {
            throw new Exception("Date not in correct format");
        }
        return res;
    }
}
