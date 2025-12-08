package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;

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
    private JButton exitBtn;
    private StringBuilder history = new StringBuilder();
    
	public UserAccountFrame() {
        GridBagConstraints positionConst;
        setTitle("User Account Information");
// using JFormattedTextField for better input handling        
        balanceField = new JFormattedTextField();
        depositField = new JFormattedTextField();
        withdrawField = new JFormattedTextField();
        
        balanceField.setColumns(10);
        depositField.setColumns(10);
        withdrawField.setColumns(10);
// Pressing enter key performs button click         
        balanceField.addActionListener(e -> balanceBtn.doClick());
        depositField.addActionListener(e -> depositBtn.doClick());
        withdrawField.addActionListener(e -> withdrawBtn.doClick());

        balanceBtn = new JButton("Submit Balance");
        balanceBtn.addActionListener(this);
// layout for using gridbag system        	
        setLayout(new GridBagLayout());
        positionConst = new GridBagConstraints();
        positionConst.insets = new Insets (10, 10, 10, 10);
// positioning for balance items       
        userBalanceLabel = new JLabel("Enter Balance: ");
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
// Exit button added        
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        JPanel exitBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitBtnPanel.add(exitBtn);
        
        positionConst.gridx = 0;
        positionConst.gridy = 6;
        positionConst.gridwidth = 4;
        positionConst.fill = GridBagConstraints.HORIZONTAL;
        positionConst.anchor = GridBagConstraints.CENTER;
        add(exitBtnPanel, positionConst);
        positionConst.gridwidth = 1;
// hide deposit and withdrawal items until user enters balance        
        viewTransactions(false);           
// set minimum size to ensure items are visible;
        setMinimumSize(new Dimension(400, 200));
// allow frame to dynamically resize        
		pack();
		setVisible(true);
// call exitApplication method when frame closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}
		});
    }
// method to handle showing and hiding of other panel elements	
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
// method to handle input validation	
	private double formattedNumber(JFormattedTextField field) throws NumberFormatException {
		String number = field.getText().trim();
		field.setValue(null);
		field.setText("");
		if (number.isEmpty()) throw new NumberFormatException();
		return Double.parseDouble(number);
	}
// write transaction history to file	
	private void writeFile() {
		try {
			File file = new File("history.txt");
			try (PrintWriter out = new PrintWriter(file)) {
				out.println(history.toString());
			}
			System.out.println("Transaction history saved to: " + file.getAbsolutePath());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);
		}
	}
// method to exit application 	
	private void exitApplication() {
		writeFile();
		JOptionPane.showMessageDialog(this, String.format("Goodbye. Your balance is: $%.2f", userBalance));
		System.exit(0);
	}
// button action event listeners
        @Override
        public void actionPerformed(ActionEvent e) {
        	try {
        		if (e.getSource() == balanceBtn) {
        			double balance = formattedNumber(balanceField);
        			userBalance = balance;
        			confirmBalanceLabel.setText(String.format("Welcome. Your balance is: $%.2f", userBalance));
        			history.append("Initial balance: $")
        					.append(String.format("%.2f", userBalance))
        					.append("\n");
// make deposit and withdrawal items visible after balance has been entered        			
        			viewTransactions(true);
// hide balance items once entered        	        
        	        userBalanceLabel.setVisible(false);
        	        balanceField.setVisible(false);
        	        balanceBtn.setVisible(false);
        		} else if (e.getSource() == depositBtn) {
        			double amount = formattedNumber(depositField);
        			userBalance += amount;
        			history.append("Deposit: $")
        					.append(String.format("%.2f", amount))
        					.append(" | New balance: $")
        					.append(String.format("%.2f", userBalance))
        					.append("\n");
        			confirmBalanceLabel.setText(String.format("Deposit Successful. New Balance: $%.2f", userBalance));
        		} else if (e.getSource() == withdrawBtn) {
        			double amount = formattedNumber(withdrawField);
        			if (amount > userBalance) {
        				JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
        				return;
        			} else {
        				userBalance -= amount;
        				history.append("Withdraw: $")
        						.append(String.format("%.2f", amount))
        						.append(" | New balance: $")
        						.append(String.format("%.2f", userBalance))
        						.append("\n");        						
        				confirmBalanceLabel.setText(String.format("Withdrawal Successful. New Balance: $%.2f", userBalance));
        			}
// exit button will close application on click        			
        		} else if (e.getSource() == exitBtn) {
        			exitApplication();
        		}
        	} catch (NumberFormatException ex) {
        		JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        	}     	
        }
	public static void main(String[] args) {
		new UserAccountFrame();
	}
}