package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

public class UserAccountFrame extends JFrame implements ActionListener {
    
	private JLabel userBalanceLabel;
    private JFormattedTextField balanceField;
    private JButton balanceBtn;
    private JLabel confirmBalanceLabel;
    private JLabel userDepositLabel;
    private JFormattedTextField depositField;
    private JButton depositBtn;
    private JLabel userWithdrawLabel;
    private JFormattedTextField withdrawField;
    private JButton withdrawBtn;
    private double userBalance = 0;
    
	UserAccountFrame() {
        GridBagConstraints positionConst;
        	
        setTitle("User Account Information");
// set number format for input fields and eliminate comma separator        
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
// balance input         	
        userBalanceLabel = new JLabel("Please Enter Your Balance.");
        balanceField = new JFormattedTextField(format);
        balanceField.setEditable(true);
        balanceField.setText("");
        balanceBtn = new JButton("Submit Balance");
        balanceBtn.addActionListener(this);
// layout for using gridbag system        	
        setLayout(new GridBagLayout());
        positionConst = new GridBagConstraints();
        positionConst.insets = new Insets (10, 10, 10, 10);
// positioning for balance items               	
        positionConst.gridx = 1;
        positionConst.gridy = 0;
        add(userBalanceLabel, positionConst);
        
        positionConst.gridy = 1;
        positionConst.fill = GridBagConstraints.HORIZONTAL;
        positionConst.weightx = 1.0;
        add(balanceField, positionConst);
        
        positionConst.gridy = 2;
        positionConst.fill = GridBagConstraints.NONE;
        positionConst.weightx = 0;
        add(balanceBtn, positionConst);
        confirmBalanceLabel = new JLabel("", SwingConstants.CENTER);
// balance items in separate panel to avoid confines of frame grid        
        JPanel confirmPanel = new JPanel(new BorderLayout());
        confirmPanel.add(confirmBalanceLabel, BorderLayout.CENTER);
        
        positionConst.gridx = 0;
        positionConst.gridy = 3;
        positionConst.gridwidth = 4;
        positionConst.fill = GridBagConstraints.HORIZONTAL;
        add(confirmPanel, positionConst);
// reset grid width for other items        
        positionConst.gridwidth = 1;
 // deposit entry        
        userDepositLabel = new JLabel("Deposit Amount: ");
        depositField = new JFormattedTextField(format);
        depositBtn = new JButton("Deposit");
        depositBtn.addActionListener(this);
        
        positionConst.gridx = 1;
        positionConst.gridy = 4;
        add(userDepositLabel, positionConst);
        
        positionConst.gridx = 2;
        positionConst.weightx = 1.0;
        positionConst.fill = GridBagConstraints.HORIZONTAL;
        add(depositField, positionConst);
        
        positionConst.gridx = 3;
        positionConst.weightx = 0;
        positionConst.fill = GridBagConstraints.NONE;
        add(depositBtn, positionConst);
// withdrawal entry        
        userWithdrawLabel = new JLabel("Withdraw Amount: ");
        withdrawField = new JFormattedTextField(format);
        withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(this);
        
        positionConst.gridx = 1;
        positionConst.gridy = 5;
        add(userWithdrawLabel, positionConst);
        
        positionConst.gridx = 2;
        positionConst.weightx = 1.0;
        positionConst.fill = GridBagConstraints.HORIZONTAL;
        add(withdrawField, positionConst);
        
        positionConst.gridx = 3;
        positionConst.weightx = 0;
        positionConst.fill = GridBagConstraints.NONE;
        add(withdrawBtn, positionConst);
// hide deposit and withdrawal items until user enters balance        
        viewTransactions(false);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// set minimum size to ensure items are visible;
        setMinimumSize(new Dimension(400, 200));
// allow frame to dynamically resize        
		pack();
		setVisible(true);
// popup for final balance when user closes frame		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null,  String.format("Goodbye. Your balance is: $%.2f", userBalance));
			}
		});
    }
	
	private void viewTransactions(boolean visible) {
		userDepositLabel.setVisible(visible);
		depositField.setVisible(visible);
		depositBtn.setVisible(visible);
		userWithdrawLabel.setVisible(visible);
		withdrawField.setVisible(visible);
		withdrawBtn.setVisible(visible);
			
		revalidate();
		repaint();
		pack();
	}
	
	private double formattedNumber(JFormattedTextField field) {
		Number number = (Number) field.getValue();
		if (number == null) throw new NumberFormatException();
		return number.doubleValue();
	}
// button action event listeners
        @Override
        public void actionPerformed(ActionEvent e) {
        	try {
        		if (e.getSource() == balanceBtn) {
        			userBalance = formattedNumber(balanceField);
        			confirmBalanceLabel.setText(String.format("Welcome. Your balance is: $%.2f", userBalance));
        			balanceField.setText("");
// make deposit and withdrawal items visible after balance has been entered        			
        			viewTransactions(true);
// hide balance items once entered        	        
        	        userBalanceLabel.setVisible(false);
        	        balanceField.setVisible(false);
        	        balanceBtn.setVisible(false);
        		} else if (e.getSource() == depositBtn) {
        			double amount = formattedNumber(depositField);
        			userBalance += amount;
        			confirmBalanceLabel.setText(String.format("Deposit Successful. New Balance: $%.2f", userBalance));
        			depositField.setText("");
        		} else if (e.getSource() == withdrawBtn) {
        			double amount = formattedNumber(withdrawField);
        			if (amount > userBalance) {
        				JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
        				withdrawField.setText("");
        			} else {
        				userBalance -= amount;
        				confirmBalanceLabel.setText(String.format("Withdrawal Successful. New Balance: $%.2f", userBalance));
        				withdrawField.setText("");
        			}
        		} 
        	} catch (NumberFormatException ex) {
        		JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
// clear fields in the event of an error
        		balanceField.setText("");
        		depositField.setText("");
        		withdrawField.setText("");
        	}     	
        }
	public static void main(String[] args) {
		new UserAccountFrame();
	}
}