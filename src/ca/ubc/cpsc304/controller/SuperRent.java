package ca.ubc.cpsc304.controller;

import ca.ubc.cpsc304.database.DatabaseConnectionHandler;
import ca.ubc.cpsc304.delegates.LoginWindowDelegate;
import ca.ubc.cpsc304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cpsc304.delegates.TransactionsWindowDelegate;
import ca.ubc.cpsc304.model.CustomerModel;
import ca.ubc.cpsc304.model.RentModel;
import ca.ubc.cpsc304.model.ReservationModel;
import ca.ubc.cpsc304.model.ReturnModel;
import ca.ubc.cpsc304.ui.LoginWindow;
import ca.ubc.cpsc304.ui.TransactionsWindow;
import ca.ubc.cpsc304.ui.TerminalTransactions;

public class SuperRent implements LoginWindowDelegate, TerminalTransactionsDelegate, TransactionsWindowDelegate{
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private TransactionsWindow transactionsWindow = null;

    public static void main(String[] args) {
        SuperRent superRent = new SuperRent();
        superRent.start();
    }

    /**
     * loginWindow is temporarily commented out to make testing the
     * transactionsWindow gui easier on myself
     * TODO: uncomment and remove transactionsWindow when everything is complete
     */
    private void start() {
        // loginWindow = new LoginWindow();
        // loginWindow.showFrame(this);
        transactionsWindow = new TransactionsWindow();
        transactionsWindow.showFrame(this);
    }


    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            TerminalTransactions transaction = new TerminalTransactions();
            transaction.showMainMenu(this);

            /**
             * TODO: Uncomment when everything is done
             */
            // transactionsWindow = new TransactionsWindow();
            // transactionsWindow.showFrame(this);

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
     * TerminalTransactionsDelegate Implementation
     *
     * Insert a reservation with the given info
     */
    public void insertReservation(ReservationModel model) {
        dbHandler.insertReservation(model);
    }

    /**
     * TerminalTransactionsDelegate Implementation
     *
     * Delete reservation with given confNo.
     */
    public void deleteReservation(int confNo) {
        dbHandler.deleteReservation(confNo);
    }

    /**
     * TerminalTransactionsDelegate Implementation
     *
     * Update reservation not actually needed, but I'll remove later
     */

    public void updateReservation(int confNo, String vtname) {
        dbHandler.updateReservation(confNo, vtname);
    }

    /**
     * TerminalTransactionsDelegate Implementation
     *
     * Displays information about reservations.
     */
    public void showReservation() {
        ReservationModel[] models = dbHandler.getReservationInfo();

        for (int i = 0; i < models.length; i++) {
            ReservationModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getConfNo());
            System.out.printf("%-20.20s", model.getVtname());
            System.out.printf("%-20.20s", model.getCellphone());
            System.out.printf("%-20.20s", model.getFromDate());
            System.out.printf("%-20.20s", model.getFromTime());
            System.out.printf("%-20.20s", model.getToDate());
            System.out.printf("%-20.20s", model.getToTime());

            System.out.println();
        }
    }

    /**
     * TerminalTransactionsDelegate Implementation
     *
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

    /**
     * TransactionsWindowDelegate Implementation Start
     *
     */


    public void makeReservation(ReservationModel model) {
        dbHandler.makeReservation(model);
    }

    public void removeReservation(String confNo) {
        dbHandler.removeReservation(confNo);
    }

    public void showReservations() {
        ReservationModel[] models = dbHandler.getReservationInfo();

        for (int i = 0; i < models.length; i++) {
            ReservationModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getConfNo());
            System.out.printf("%-20.20s", model.getVtname());
            System.out.printf("%-20.20s", model.getCellphone());
            System.out.printf("%-20.20s", model.getFromDate());
            System.out.printf("%-20.20s", model.getFromTime());
            System.out.printf("%-20.20s", model.getToDate());
            System.out.printf("%-20.20s", model.getToTime());

            System.out.println();
        }
    }

    public void addCustomer(CustomerModel model) {
      dbHandler.addCustomer(model);
    }

    public void showVehicles(String vtname, String location, String fromDate, String fromTime, String toDate, String toTime) {

    }

    public void rentVehicle(RentModel model) {

    }

    public void returnVehicle(ReturnModel model) {

    }

    public void showRentals(String location) {

    }

    public void showReturns(String location) {

    }

    public void transactionsWindowFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

}
