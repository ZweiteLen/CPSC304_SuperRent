package ca.ubc.cpsc304.delegates;

import ca.ubc.cpsc304.model.ReturnModel;
import ca.ubc.cpsc304.model.CustomerModel;
import ca.ubc.cpsc304.model.ReservationModel;
import ca.ubc.cpsc304.model.VehicleTypeModel;
import ca.ubc.cpsc304.model.VehicleModel;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
	public void deleteReservation(int confNo);
	public void insertReservation(ReservationModel model);
	public void showReservation();
	public void updateReservation(int confNo, String name);
	
	public void terminalTransactionsFinished();
}
