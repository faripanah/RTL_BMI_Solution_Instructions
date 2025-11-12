package org.example.demo1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class BMIResultService {
    // Use environment variables with sensible defaults
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "bmi_localization");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "admin");


    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    public static void saveResult(double weight, double height, double bmi, String language) {
        String query = "INSERT INTO bmi_results (weight, height, bmi, language) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, weight);
            stmt.setDouble(2, height);
            stmt.setDouble(3, bmi);
            stmt.setString(4, language);
            stmt.executeUpdate();
            System.out.println("✅ BMI result saved to database at " + DB_URL);
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database at " + DB_URL);
            e.printStackTrace();
        }
    }
}
