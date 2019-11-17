package ca.ubc.cpsc304.ui;

import ca.ubc.cpsc304.delegates.TransactionsWindowDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The class is responsible for displaying and handling the transactions GUI.
 */
public class TransactionsWindow extends JFrame {
    private static final int TEXT_FIELD_WIDTH = 10;

    // components of the login window
    private JTextField vtnameField;
    private JTextField locationField;
    private JTextField fromField;
    private JTextField toField;

    // delegate
    private TransactionsWindowDelegate delegate;


    DefaultTableModel vmodel = new DefaultTableModel();
    DefaultTableModel searchmodel = new DefaultTableModel();

    JButton seeVButton = new JButton("See vehicles");
    JLabel foundButtonlabel;

    public TransactionsWindow() {
        super("SuperRent");
    }

    public void showFrame(TransactionsWindowDelegate delegate) {
        this.delegate = delegate;

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        JLabel vtnameLabel = new JLabel("Enter vehicle type: ");
        JLabel locationLabel = new JLabel("Enter branch: ");
        JLabel fromLabel = new JLabel("From (yyyymmddhh): ");
        JLabel toLabel = new JLabel("Until (yyyymmddhh): ");

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

        foundButtonlabel = new JLabel(" ");
        c.fill = GridBagConstraints.RELATIVE;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        gb.setConstraints(foundButtonlabel, c);
        contentPane.add(foundButtonlabel);
        c.gridx = 1;
        c.gridy = 2;
        gb.setConstraints(seeVButton, c);
        contentPane.add(seeVButton);

        contentPane.revalidate();

        // register buttons with action event handler
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchmodel = delegate.showVehicles(vtnameField.getText(),locationField.getText(),fromField.getText(),
                        toField.getText());
                int num = searchmodel.getRowCount();
                foundButtonlabel.setText(num + " Vehicles found");
                vehicleTable.setModel(vmodel);
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
                // TODO
            }
        });
        makeRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        makeReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        rentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String branch = JOptionPane.showInputDialog("Which branch? (optional)");
                searchmodel = delegate.showDailyRentalsReport(branch);
                vehicleTable.setModel(searchmodel);
            }
        });
        returns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String branch = JOptionPane.showInputDialog("Which branch? (optional)");
                searchmodel = delegate.showDailyReturnsReport(branch);
                vehicleTable.setModel(searchmodel);
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
}
