package database;

import java.sql.*;
import model.BankAccount;
public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // Replace with your MySQL password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static BankAccount getAccount(String username, String password) throws SQLException {
        String sql = "SELECT account_number, balance, password FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {  // Compare the plain text password
                    String accountNumber = rs.getString("account_number");
                    double balance = rs.getDouble("balance");
                    return new BankAccount(accountNumber, balance);
                }
            }
        }
        return null; // Return null if the login is invalid
    }


    public static void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE account_number = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        }
    }
}

