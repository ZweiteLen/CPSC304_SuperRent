/*
package ca.ubc.cpsc304.ui;

import ca.ubc.cpsc304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cpsc304.model.ReturnModel;
import ca.ubc.cpsc304.model.CustomerModel;
import ca.ubc.cpsc304.model.ReservationModel;
import ca.ubc.cpsc304.model.VehicleTypeModel;
import ca.ubc.cpsc304.model.VehicleModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

*/
/**
 * The class is only responsible for handling terminal text inputs. 
 *//*

public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	public TerminalTransactions() {
	}

	*/
/**
	 * Displays simple text interface
	 *//*

	public void showMainMenu(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 5) {
			System.out.println();
			System.out.println("1. Insert reservation");
			System.out.println("2. Delete reservation");
			System.out.println("3. Update vehicle type");
			System.out.println("4. Show reservation");
			System.out.println("5. Quit");
			System.out.print("Please choose one of the above 5 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:  
					handleInsertOption(); 
					break;
				case 2:  
					handleDeleteOption(); 
					break;
				case 3: 
					handleUpdateOption();
					break;
				case 4:  
					delegate.showReservation();
					break;
				case 5:
					handleQuitOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}
	
	private void handleDeleteOption() {
		int confNo = INVALID_INPUT;
		while (confNo == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to delete: ");
			confNo = readInteger(false);
			if (confNo != INVALID_INPUT) {
				delegate.deleteReservation(confNo);
			}
		}
	}
	
	private void handleInsertOption() {
		String confNo = null;
		while (confNo == null || confNo.length() <= 0) {
			System.out.print("Please enter the confirmation number you wish to insert: ");
			confNo = readLine().trim();
		}
		
		String vtname = null;
		while (vtname == null || vtname.length() <= 0) {
			System.out.print("Please enter the vehicle type you wish to insert: ");
			vtname = readLine().trim();
		}

		String dLicense = null;
		while (dLicense == null || dLicense.length() <= 0) {
			System.out.print("Please enter the driver's license you wish to insert: ");
			dLicense = readLine().trim();
		}

		// TODO: Change from type Timestamp to Java Date?
		Timestamp fromDateTime = null;
		while (fromDateTime == null) {
			System.out.print("Please enter the date and time you wish to reserve from: ");
//			fromDateTime = readLine().trim();
		}

		// TODO: Change from type Timestamp to Java Date?
		Timestamp toDateTime = null;
		while (toDateTime == null) {
			System.out.print("Please enter the date and time you wish to reserve until: ");
//			toDateTime = readLine().trim();
		}
		
		ReservationModel model = new ReservationModel(confNo, vtname, dLicense, fromDateTime, toDateTime);
//		delegate.insertReservation(model);
	}
	
	private void handleUpdateOption() {
		int confNo = INVALID_INPUT;
		while (confNo == INVALID_INPUT) {
			System.out.print("Please enter the confirmation no. you wish to update: ");
			confNo = readInteger(false);
		}
		
		String vtname = null;
		while (vtname == null || vtname.length() <= 0) {
			System.out.print("Please enter the vehicle type you wish to update to: ");
			vtname = readLine().trim();
		}

		delegate.updateReservation(confNo, vtname);
	}

    private void handleQuitOption() {
        System.out.println("Good Bye!");

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("IOException!");
            }
        }

        delegate.terminalTransactionsFinished();
    }
	
	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}
	
	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}
}
*/
