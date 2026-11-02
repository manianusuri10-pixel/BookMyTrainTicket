package com.bookmytrain;

/**
 * Resolves MySQL credentials from environment variables
 * or Java system properties.
 */
public final class DbCredentials {

    private static String user;
    private static String password;

    private DbCredentials() {}

    public static synchronized String user() {
        loadIfNeeded();
        return user;
    }

    public static synchronized String password() {
        loadIfNeeded();
        return password;
    }

    private static void loadIfNeeded() {

        if (user != null) {
            return;
        }

        // First try Java system property, then environment variable.
        user = firstNonBlank(
                System.getProperty("train.db.user"),
                System.getenv("TRAIN_DB_USER"),
                System.getenv("MYSQLUSER"),
                "root"
        );

        password = firstOrEmpty(
                System.getProperty("train.db.password"),
                System.getenv("TRAIN_DB_PASSWORD"),
                System.getenv("MYSQLPASSWORD")
        );
    }

    private static String firstNonBlank(String... values) {

        for (String value : values) {

            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }

        return "root";
    }

    private static String firstOrEmpty(String... values) {

        for (String value : values) {

            if (value != null) {
                return value;
            }
        }

        return "";
    }
}