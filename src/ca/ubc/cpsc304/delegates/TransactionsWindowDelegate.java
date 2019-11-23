package ca.ubc.cpsc304.delegates;

import ca.ubc.cpsc304.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

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
     * Not all methods may be needed.
     * Uncomment methods when implementing them.
     */

    // Reservations
    void insertReservation(ReservationModel model) throws SQLException;
    //void deleteReservation(int confNo);
    //void updateReservation(int confNo, ReservationModel model);
   //DefaultTableModel showReservations(ReservationModel model);

    // Rental vehicles
//    void insertRentVehicle(RentModel model);
//    void deleteRentVehicle(String vid);
//    void updateRentVehicle(String vid, RentModel model);
      DefaultTableModel showRentalVehicles(String vtname, String location, String fromDateTime, String toDateTime) throws SQLException;

    // Just vehicles themselves
//    void insertVehicle(VehicleModel model);
//    void deleteVehicle(String vid);
//    void updateVehicle(String vid, VehicleModel model);
//    DefaultTableModel showVehicles(VehicleModel model);

    // Just vehicle types
//    void insertVehicleType(VehicleTypeModel model);
//    void deleteVehicleType(String vtName);
//    void updateVehicleType(String vtName, VehicleTypeModel model);
//    DefaultTableModel showVehicleTypes(VehicleTypeModel model);

    // Customers
    void insertCustomer(CustomerModel model);
    //void deleteCustomer(String dLicense);
   // void updateCustomer(String dLicense, CustomerModel model);
    //DefaultTableModel showCustomers(CustomerModel model);

    // Return vehicles
//    void insertReturnVehicle(ReturnModel model);
//    void deleteReturnVehicle(String rid);
//    void updateReturnVehicle(String rid, ReturnModel model);
//    DefaultTableModel showReturnVehicles(ReturnModel model);

    /**
     * Daily report transactions
     */
    public DefaultTableModel showDailyRentalsReport(String date) throws SQLException;
    public DefaultTableModel showDailyRentalsReportByBranch(String date, String location) throws SQLException;
    public DefaultTableModel showDailyReturnsReport(String date);
    public DefaultTableModel showDailyReturnsReportByBranch(String date, String location);
}
