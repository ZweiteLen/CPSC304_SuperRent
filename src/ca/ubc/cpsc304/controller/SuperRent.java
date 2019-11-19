package ca.ubc.cpsc304.controller;

import ca.ubc.cpsc304.database.DatabaseConnectionHandler;
import ca.ubc.cpsc304.delegates.LoginWindowDelegate;
import ca.ubc.cpsc304.delegates.TransactionsWindowDelegate;
import ca.ubc.cpsc304.model.*;
import ca.ubc.cpsc304.ui.LoginWindow;
import ca.ubc.cpsc304.ui.TransactionsWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SuperRent implements LoginWindowDelegate, TransactionsWindowDelegate {
    private final String LOG_TAG = SuperRent.class.getSimpleName();

    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private TransactionsWindow transactionsWindow = null;

    public SuperRent() {
        dbHandler = new DatabaseConnectionHandler();
    }

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
         loginWindow = new LoginWindow();
         loginWindow.showFrame(this);
//        transactionsWindow = new TransactionsWindow();
//        transactionsWindow.showFrame(this);
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

            TransactionsWindow transaction = new TransactionsWindow();
            transaction.showFrame(this);

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
     *//*
    public void insertReservation(ReservationModel resModel) {
        dbHandler.insertReservation(resModel);
    }

    *//**
     * TerminalTransactionsDelegate Implementation
     *
     * Delete reservation with given confNo.
     *//*
    public void deleteReservation(int confNo) {
        dbHandler.deleteReservation(confNo);
    }

    *//**
     * TerminalTransactionsDelegate Implementation
     *
     * Update reservation not actually needed, but I'll remove later
     *//*

    public void updateReservation(int confNo, String vtname) {
        dbHandler.updateReservation(confNo, vtname);
    }

    *//**
     * TerminalTransactionsDelegate Implementation
     *
     * Displays information about reservations.
     *//*
    public void showReservation() {
        ReservationModel[] models = dbHandler.getReservationInfo();

        for (int i = 0; i < models.length; i++) {
            ReservationModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getConfNo());
            System.out.printf("%-20.20s", model.getVtname());
            System.out.printf("%-20.20s", model.getDLicense());
            //System.out.printf("%-20.20s", model.getFromDate());
            //System.out.printf("%-20.20s", model.getFromTime());
            //System.out.printf("%-20.20s", model.getToDate());
            //System.out.printf("%-20.20s", model.getToTime());

            System.out.println();
        }
    }*/

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
     */

    /**
     * Customer transactions.
     */
    public void insertReservation(ReservationModel resModel) {
        dbHandler.insertReservation(resModel);
    }

    public void deleteReservation(String confNo) {

    }

    public void showReservations() {

    }

    public void insertCustomer(CustomerModel model) {

    }

    public DefaultTableModel showVehicles(String vtname, String location, String fromDateTime, String toDateTime) {
        DefaultTableModel resmodel = new DefaultTableModel(new String[]{"Current Status","Location","Model", "Make", "Year",
                "Colour", "Features"}, 0);
        resmodel = dbHandler.getVehicleInfo(vtname, location, fromDateTime, toDateTime);
//        for (int i = 0; i<5; i++)
//        {
//            resmodel.addRow(new Object[]{i, "model", "make"});
//        }
        return resmodel;
    }

    /**
     * Clerk transactions.
     */
    public void rentVehicle(RentModel model) {

    }

    public void returnVehicle(ReturnModel model) {

    }

    public DefaultTableModel showDailyRentalsReport(String date, String location) {
        DefaultTableModel res = new DefaultTableModel(new String[]{"rid", "vid", "confNo", "cellphone",
                "fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);
        // res = dbHandler.getDailyRental(location);
        return res;
    }

    public DefaultTableModel showDailyRentalsReportByBranch(String date, String location) {
        DefaultTableModel res = new DefaultTableModel(new String[]{"rid", "vid", "confNo", "cellphone",
                "fromDateTime", "toDateTime", "odometer", "cardName", "cardNo", "expDate"}, 0);
        return res;
    }

    public DefaultTableModel showDailyReturnsReport(String date, String location) {
        DefaultTableModel res = new DefaultTableModel(new String[]{"rid", "datetime", "odometer", "fulltank", "value"}, 0);
        return res;
    }

    public DefaultTableModel showDailyReturnsReportByBranch(String date, String location) {
        DefaultTableModel res = new DefaultTableModel(new String[]{"rid", "datetime", "odometer", "fulltank", "value"}, 0);
        // res = dbHandler.getDailyReturn(location);
        return res;
    }
}
