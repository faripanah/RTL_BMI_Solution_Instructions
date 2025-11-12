package org.example.demo1;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class LocalizationService {
    private static final String DB_NAME = "bmi_localization";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    // Dynamically determine DB host
    private static String getDatabaseHost() {
        // Try reading from environment variable (for Docker)
        String host = System.getenv("DB_HOST");

        // Fallback to localhost if not set
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }

        return host;
    }

    // Build DB connection URL dynamically
    private static String getDatabaseUrl() {
        return "jdbc:mysql://" + getDatabaseHost() + ":3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    public static Map<String, String> getLocalizedStrings(Locale locale) {
        Map<String, String> strings = new HashMap<>();
        String lang = locale.getLanguage();
        String dbUrl = getDatabaseUrl();

        try (Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD)) {
            String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, lang);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    strings.put(rs.getString("key"), rs.getString("value"));
                }
            }
            System.out.println("✅ Loaded localization strings for language: " + lang + " from " + dbUrl);
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + dbUrl);
            e.printStackTrace();
        }

        return strings;
    }
}
