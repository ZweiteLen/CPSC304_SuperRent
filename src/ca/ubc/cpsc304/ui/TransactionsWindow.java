package ca.ubc.cpsc304.ui;

import ca.ubc.cpsc304.delegates.TransactionsWindowDelegate;
import ca.ubc.cpsc304.model.*;
import ca.ubc.cpsc304.number_generator.RandomNumberGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;

/**
 * The class is responsible for displaying and handling the transactions GUI.
 */
public class TransactionsWindow extends JFrame {
    private static final int TEXT_FIELD_WIDTH = 10;

    // delegate
    private TransactionsWindowDelegate delegate;

    private DefaultTableModel vmodel = new DefaultTableModel();
    private DefaultTableModel searchmodel = new DefaultTableModel();

    private JButton seeVButton = new JButton(" ");

    public TransactionsWindow() {
        super("SuperRent");
    }

    public void showFrame(TransactionsWindowDelegate delegate) {
        this.delegate = delegate;

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        JTextField vtnameField;
        JTextField locationField;
        JTextField fromField;
        JTextField toField;

        JLabel vtnameLabel = new JLabel("Enter vehicle type: ");
        JLabel locationLabel = new JLabel("Enter branch: ");
        JLabel fromLabel = new JLabel("From (yyyy-mm-dd hh:00): ");
        JLabel toLabel = new JLabel("Until (yyyy-mm-dd hh:00): ");

        vtnameField = new JTextField(TEXT_FIELD_WIDTH);
        locationField = new JTextField(TEXT_FIELD_WIDTH);
        fromField = new JTextField(TEXT_FIELD_WIDTH);
        toField = new JTextField(TEXT_FIELD_WIDTH);

        JButton searchButton = new JButton("Search");

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu reports = new JMenu("Reports");
        JMenu vehicles = new JMenu("Vehicles");
        mb.add(vehicles);
        mb.add(reports);
        JMenuItem rentals = new JMenuItem("Rentals");
        JMenuItem returns = new JMenuItem("Returns");
        JMenuItem makeReservation = new JMenuItem("Reserve");
        JMenuItem makeRental = new JMenuItem("Rent");
        JMenuItem makeReturn = new JMenuItem("Return");
        reports.add(rentals);
        reports.add(returns);
        vehicles.add(makeReservation);
        vehicles.add(makeRental);
        vehicles.add(makeReturn);

        // place menu bar
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 9;
        c.gridx = 0;
        c.gridy = 0;
        gb.setConstraints(mb, c);
        contentPane.add(mb);

        // place the vehicle type label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        gb.setConstraints(vtnameLabel, c);
        contentPane.add(vtnameLabel);

        // place the text field for the vehicle type
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        gb.setConstraints(vtnameField, c);
        contentPane.add(vtnameField);

        // place location label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(locationLabel, c);
        contentPane.add(locationLabel);

        // place the location field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(locationField, c);
        contentPane.add(locationField);

        // place from label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 4;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(fromLabel, c);
        contentPane.add(fromLabel);

        // place the from field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 5;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(fromField, c);
        contentPane.add(fromField);

        // place until label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 6;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(toLabel, c);
        contentPane.add(toLabel);

        // place the until field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 7;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(toField, c);
        contentPane.add(toField);

        // place the search button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 8;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(searchButton, c);
        contentPane.add(searchButton);

        // Set up vehicle table
        JTable vehicleTable = new JTable(vmodel);
        JScrollPane sp = new JScrollPane(vehicleTable);
        vehicleTable.setFillsViewportHeight(true);
        vehicleTable.setModel(vmodel);

        // add table to the ui
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 9;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        gb.setConstraints(sp, c);
        contentPane.add(sp);

        // set see vehicles button
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        gb.setConstraints(seeVButton, c);
        contentPane.add(seeVButton);

        // register buttons with action event handler
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchmodel = delegate.showRentalVehicles(vtnameField.getText(), locationField.getText(), fromField.getText(), toField.getText());
                    JButton btn = seeVButton;
                    btn.setText(searchmodel.getRowCount() + " Vehicles found");
                    vehicleTable.setModel(vmodel);
                } catch (Exception se) {
                    inputError("Please make sure the dates and times are entered in the correct format");
                }
            }
        });
        seeVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vehicleTable.setModel(searchmodel);
            }
        });

        makeReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeReservationHelper();
            }
        });

        makeRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int method = rent();
                System.out.println(method);
                boolean worked=false;
                if (method != 2) {
                    if (method == 1) {
                        worked = makeReservationHelper();
                    }
                    if (worked || method==0) {
                        makeRentalHelper();
                    }
                }
            }
        });

        makeReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = returnInput();
                if (input != null) {
                    try {
                        int full = 0;
                        if (input[3].equals("Yes")) {
                            full = 1;
                        }
                        Timestamp returnDate = Timestamp.valueOf(input[1] + ":00.000");
                        String[] details = delegate.insertReturnVehicle(new ReturnModel(-1, returnDate,
                                Integer.parseInt(input[2]), full, 0), Integer.parseInt(input[0]));
                        // String[]{confo, calculation, Integer.toString(value)}
                        String[] receiptDetails = new String[]{"Confirmation number: " + details[0], "Return date: " +
                                returnDate.toString().substring(0, 16), "Price: " + details[1], "Total price: $" + details[2]};
                        receipt(receiptDetails);
                    } catch (Exception se) {
                        inputError(se.getMessage());
                    }
                }
            }
        });

        rentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = reportInput();
                if (input != null) {
                    try {
                        if (!input[1].trim().isEmpty()) {
                            searchmodel = delegate.showDailyRentalsReportByBranch(input[0], input[1]);
                        } else {
                            searchmodel = delegate.showDailyRentalsReport(input[0]);
                        }
                        vehicleTable.setModel(searchmodel);
                    } catch (Exception se) {
                        inputError(se.getMessage());
                    }
                }
            }
        });
        returns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = reportInput();
                if (input != null) {
                    try {
                        if (!input[1].trim().isEmpty()) {
                            searchmodel = delegate.showDailyReturnsReportByBranch(input[0], input[1]);
                        } else {
                            searchmodel = delegate.showDailyReturnsReport(input[0]);
                        }
                        vehicleTable.setModel(searchmodel);
                    } catch (Exception se) {
                        inputError(se.getMessage());
                    }
                }
            }
        });

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        vtnameField.requestFocus();
    }

    private boolean makeReservationHelper() {
        String[] input = reservationForm();
        try {
            if (input != null) {
                String customer = input[1];
                boolean exist = delegate.checkCustomer(customer);
                if (!exist) {
                    String[] customerDetails = customerInput();
                    delegate.insertCustomer(new CustomerModel
                            (customerDetails[0], customerDetails[1], customerDetails[2], customer));
                }

                int confo = RandomNumberGenerator.generateRandomReservationNumber();
                try {
                    Timestamp from = Timestamp.valueOf(input[2] + ":00.000");
                    Timestamp to = Timestamp.valueOf(input[3] + ":00.000");

                    delegate.insertReservation(new ReservationModel(confo, input[0], input[1],
                            from, to));

                    String[] receiptDetails = new String[]{"Confirmation number: " + confo, "Vehicle Type: " + input[0],
                            "Driver's License: " + input[1], "From: " + from.toString().substring(0,16), "To: " + to.toString().substring(0,16)};
                    receipt(receiptDetails);
                    return true;
                } catch (Exception ex) {
                    inputError("Please enter a valid time.");
                }
            }
        } catch (Exception se) {
            inputError(se.getMessage());
        }
        return false;
    }

    private int rent() {
        Object[] options = {"Yes", "No", "Cancel"};
        return JOptionPane.showOptionDialog(null,
                "Do you have a reservation?",
                "Rent",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
    }

    private void makeRentalHelper() {
        String[] input = rentInput();
        if (input != null) {
            try {
                ReservationModel res = delegate.showReservations(input[0], input[1]);
                int rid = RandomNumberGenerator.generateRandomReservationNumber();
                int confoNo = res.getConfNo();
                String dLicense = res.getDLicense();
                Timestamp from = res.getFromDateTime();
                Timestamp to = res.getToDateTime();
                String[] cardDetails = cardInput();
                if (cardDetails != null) {
                    RentModel rm = delegate.insertRentVehicle(new RentModel(rid, null, dLicense, from,
                            to, -1, cardDetails[0], cardDetails[1], cardDetails[2], confoNo));

                    String[] receiptDetails = new String[]{"Confirmation number: " + confoNo, "Driver's license: " +
                            dLicense, "Vehicle License: " + rm.getVlicense(), "Vehicle type: " + res.getVtname(), "From: " +
                            from.toString().substring(0, 16), "To: " + to.toString().substring(0, 16)};
                    receipt(receiptDetails);
                }
            } catch (Exception se) {
                inputError(se.getMessage());
            }
        }
    }

    private String[] cardInput() {
        JTextField nameField = new JTextField(10);
        JTextField numField = new JTextField(10);
        JTextField expField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Card Name:"));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Card Number:"));
        myPanel.add(numField);
        myPanel.add(new JLabel("Expiry Date:"));
        myPanel.add(expField);

        String[] res;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Rent", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String no = numField.getText().trim();
            String exp = expField.getText().trim();
            if (!name.isEmpty() && !no.isEmpty() && !exp.isEmpty()) {
                if (name.equals("MasterCard") || name.equals("Visa")) {
                    res = new String[]{name, no, exp};
                    return res;
                }
                else {
                    inputError("We only accept MasterCard or Visa.");
                }
            } else {
                inputError("Please fill out all the fields.");
            }
        }
        return null;
    }

    private String[] rentInput() {
        JTextField confoField = new JTextField(10);
        JTextField dLField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Confirmation Number:"));
        myPanel.add(confoField);
        myPanel.add(new JLabel("Driver's license:"));
        myPanel.add(dLField);

        String[] res;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Rent", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String cN = confoField.getText().trim();
            String dL = dLField.getText().trim();
            if (!cN.isEmpty() || !dL.isEmpty()) {
                res = new String[]{cN, dL};
                return res;
            } else {
                inputError("Please fill out at least one field.");
            }
        }
        return null;
    }

    private String tank;
    private String[] returnInput() {
        JTextField confNoField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField odometerField = new JTextField(10);

        String[] yesno = { "No", "Yes"};
        JComboBox<String> fullTank = new JComboBox<>(yesno);
        fullTank.setSelectedIndex(1);
        fullTank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                tank = (String)cb.getSelectedItem();
            }
        });

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Confirmation Number:"));
        myPanel.add(confNoField);
        myPanel.add(new JLabel("Day/time:"));
        myPanel.add(dateField);
        myPanel.add(new JLabel("Odometer:"));
        myPanel.add(odometerField);
        myPanel.add(new JLabel("Is tank full?"));
        myPanel.add(fullTank);

        String[] res = null;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Return", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String confNo = confNoField.getText().trim();
            String datetime = dateField.getText().trim();
            String od = odometerField.getText().trim();
            if (confNo.isEmpty() || datetime.isEmpty() || od.isEmpty()) {
                inputError("Please fill out all the fields");
            } else {
                res = new String[]{confNo, datetime, od, tank};
            }
        }
        return res;
    }

    private String[] customerInput() {
        JTextField cellField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField addressField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Cellphone:"));
        myPanel.add(cellField);
        myPanel.add(new JLabel("Address:"));
        myPanel.add(addressField);

        String[] res = null;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "New Customer Registration", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String cell = cellField.getText().trim();
            String name = nameField.getText().trim();
            String add = addressField.getText().trim();
            if (cell.isEmpty() || name.isEmpty() || add.isEmpty()) {
                inputError("Please fill out all the fields");
            } else {
                res = new String[]{cell, name, add};
            }
        }
        return res;
    }

    private void receipt(String[] info) {
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        for (String s: info) {
            myPanel.add(new JLabel(s));
        }
        JOptionPane.showMessageDialog(null, myPanel, "Receipt", JOptionPane.PLAIN_MESSAGE);
    }

    private String[] reservationForm() {
        JTextField vtField = new JTextField(10);
        JTextField dlField = new JTextField(10);
        JTextField fromField = new JTextField(10);
        JTextField toField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Vehicle Type:"));
        myPanel.add(vtField);
        myPanel.add(new JLabel("Driver License:"));
        myPanel.add(dlField);
        myPanel.add(new JLabel("From (Date and hour):"));
        myPanel.add(fromField);
        myPanel.add(new JLabel("To (Date and hour):"));
        myPanel.add(toField);

        String[] res = null;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Enter details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String vtname = vtField.getText().trim();
            String dlicense = dlField.getText().trim();
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            if (vtname.isEmpty() || dlicense.isEmpty() || from.isEmpty() || to.isEmpty()) {
                inputError("Please fill out all the fields");
            } else {
                res = new String[]{vtname, dlicense, from, to};
            }
        }
        return res;
    }

    private void inputError(String errorMsg) {
        JOptionPane.showMessageDialog(null,errorMsg,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String[] reportInput(){
        JTextField dateField = new JTextField(10);
        JTextField branchField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Date (yyyy-mm-dd):"));
        myPanel.add(dateField);
        myPanel.add(new JLabel("Branch (Optional):"));
        myPanel.add(branchField);

        String[] res;
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Report", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            String branch = branchField.getText();
            res = new String[]{date, branch};
        } else {
            res = null;
        }
        return res;
    }
}
