package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import database.DatabaseHelper;
import model.BankAccount;

public class BankApp extends Frame {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private BankAccount account;

    public BankApp(BankAccount account) {
        this.account = account;

        setTitle("Bank Account Management");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));

        Label balanceLabel = new Label("Current Balance:");
        TextField balanceField = new TextField(String.valueOf(account.getBalance()), 20);
        balanceField.setEditable(false);

        Label depositLabel = new Label("Deposit Amount:");
        TextField depositField = new TextField();

        Label withdrawLabel = new Label("Withdraw Amount:");
        TextField withdrawField = new TextField();

        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");

        add(balanceLabel);
        add(balanceField);
        add(depositLabel);
        add(depositField);
        add(withdrawLabel);
        add(withdrawField);
        add(depositButton);
        add(withdrawButton);

        depositButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(depositField.getText());
                account.deposit(amount);
                DatabaseHelper.updateBalance(account.getAccountNumber(), account.getBalance());
                balanceField.setText(String.valueOf(account.getBalance()));
                JOptionPane.showMessageDialog(this, "Deposit successful!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(withdrawField.getText());
                if (account.withdraw(amount)) {
                    DatabaseHelper.updateBalance(account.getAccountNumber(), account.getBalance());
                    balanceField.setText(String.valueOf(account.getBalance()));
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Simulate login process
        try {
            // Prompt for username and password
            String username = JOptionPane.showInputDialog("Enter your username:");
            String password = JOptionPane.showInputDialog("Enter your password:");

            // Fetch the user account from the database
            BankAccount account = DatabaseHelper.getAccount(username, password);
            
            // If the account exists, show the bank app
            if (account != null) {
                new BankApp(account);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid login!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
