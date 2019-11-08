package ca.ubc.cpsc304.controller;


import ca.ubc.cpsc304.database.DatabaseConnectionHandler;
import ca.ubc.cpsc304.delegates.LoginWindowDelegate;
import ca.ubc.cpsc304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cpsc304.model.ReservationModel;
import ca.ubc.cpsc304.ui.LoginWindow;
import ca.ubc.cpsc304.ui.TerminalTransactions;

public class SuperRent implements LoginWindowDelegate, TerminalTransactionsDelegate{
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

    public static void main(String[] args) {
        SuperRent superRent = new SuperRent();
        superRent.start();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
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
     * TermainalTransactionsDelegate Implementation
     *
     * Insert a branch with the given info
     */
    public void insertReservation(ReservationModel model) {
        dbHandler.insertReservation(model);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Delete branch with given branch ID.
     */
    public void deleteReservation(int confNo) {
        dbHandler.deleteReservation(confNo);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Update the branch name for a specific ID
     */

    public void updateReservation(int confNo, String vtname) {
        dbHandler.updateReservation(confNo, vtname);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Displays information about varies bank branches.
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

}
