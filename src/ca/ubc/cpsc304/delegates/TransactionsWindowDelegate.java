package ca.ubc.cpsc304.delegates;

import ca.ubc.cpsc304.model.*;

/**
 * This interface uses the delegation design pattern where instead of having the
 * TransactionsWindow class try to do everything, it will only focus on handling the UI.
 *
 * TransactionsWindow calls the methods that we have listed below but the actual
 * logic/operation will be delegated to the controller class (SuperRent).
 *
 * Refer to the Part 3 pdf for more details on the implementation
 */
public interface TransactionsWindowDelegate {
    /**
     * TODO: figure out how we want to generate confNo for each reservation
     */
    public void insertReservation(ReservationModel model);
    public void deleteReservation(String confNo);
    public void showReservation();

    public void addCustomer(CustomerModel model);

    public void showVehicles(String vtname, String location, String fromDate,
                             String fromTime, String toDate, String toTime);
    public void rentVehicle(RentModel model);
    public void returnVehicle(ReturnModel model);

    /**
     * Daily report transactions
     * @param location: if not empty, then show a report only for that specified branch
     */
    public void showRentals(String location);
    public void showReturns(String location);

    public void transactionsWindowFinished();
}
