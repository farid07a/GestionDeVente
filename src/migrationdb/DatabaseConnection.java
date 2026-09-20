/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migrationdb;

/**
 *
 * @author farid
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection;

    private final Properties properties;

    private DatabaseConnection() {

        properties = new Properties();

        loadProperties();

        connect();
    }

    /**
     * Load config.properties from the application resources.
     */
    private void loadProperties() {

        try (InputStream fis =
                     DatabaseConnection.class
                             .getClassLoader()
                             .getResourceAsStream("config/config.properties")) {

            if (fis == null) {
                throw new RuntimeException(
                        "config.properties introuvable dans resources/config/"
                );
            }

            properties.load(fis);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Impossible de charger config.properties",
                    e
            );
        }
    }

    /**
     * Connect to the Access database.
     */
    private void connect() {

        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String configuredPath =
                    properties.getProperty("db.path");

            if (configuredPath == null ||
                    configuredPath.trim().isEmpty()) {

                throw new RuntimeException(
                        "db.path n'est pas défini dans config.properties"
                );
            }

            /*
             * Get the database that the application will actually use.
             */
            File dbFile = getWritableDatabase(configuredPath);

            System.out.println("================================");
            System.out.println("CONFIG DB PATH : " + configuredPath);
            System.out.println("REAL DB PATH   : " + dbFile.getAbsolutePath());
            System.out.println("EXISTS         : " + dbFile.exists());
            System.out.println("CAN WRITE      : " + dbFile.canWrite());
            System.out.println("================================");

            if (!dbFile.exists()) {

                throw new RuntimeException(
                        "La base de données est introuvable : "
                                + dbFile.getAbsolutePath()
                );
            }

            /*
             * UCanAccess connection
             */
            String url =
                    "jdbc:ucanaccess://"
                            + dbFile.getAbsolutePath();

            System.out.println("URL : " + url);

            connection =
                    DriverManager.getConnection(url);

            System.out.println("Connexion réussie");

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erreur de connexion à la base de données",
                    e
            );
        }
    }

    /**
     * Returns a writable copy of the database.
     *
     * If the application is installed in Program Files,
     * the database is copied to ProgramData.
     */
    private File getWritableDatabase(String configuredPath) {

        /*
         * First try to resolve the configured path.
         *
         * Example:
         *
         * resources/StoreDBDJ.accdb
         *
         */
        File originalDb =
                new File(configuredPath);

        /*
         * If the relative path exists and is writable,
         * we can use it directly.
         */
        if (originalDb.exists() &&
                originalDb.canWrite() &&
                !isInsideProgramFiles(originalDb)) {

            System.out.println(
                    "Database is writable. Using original database."
            );

            return originalDb;
        }

        /*
         * The application is probably installed in Program Files.
         *
         * We now create a writable database location:
         *
         * C:\ProgramData\<ApplicationName>\Database\
         */
        String programData =
                System.getenv("ProgramData");

        if (programData == null ||
                programData.trim().isEmpty()) {

            throw new RuntimeException(
                    "Impossible de trouver C:\\ProgramData"
            );
        }

        /*
         * Get only the database filename.
         *
         * resources/StoreDBDJ.accdb
         *
         * becomes:
         *
         * StoreDBDJ.accdb
         */
        String fileName =
                originalDb.getName();

        /*
         * Application data directory.
         *
         * Change "MyApplication" to your application name.
         */
        File applicationDataDir =
                new File(
                        programData
                                + File.separator
                                + "MyApplication"
                                + File.separator
                                + "Database"
                );

        /*
         * Create directory if it doesn't exist.
         */
        if (!applicationDataDir.exists()) {

            if (!applicationDataDir.mkdirs()) {

                throw new RuntimeException(
                        "Impossible de créer le dossier : "
                                + applicationDataDir.getAbsolutePath()
                );
            }
        }

        /*
         * Final writable database.
         */
        File writableDb =
                new File(
                        applicationDataDir,
                        fileName
                );

        /*
         * If the database doesn't exist yet,
         * copy the original database.
         */
        if (!writableDb.exists()) {

            if (!originalDb.exists()) {

                /*
                 * The relative path may be resolved incorrectly
                 * when the application is packaged.
                 *
                 * Try to find the database next to the JAR.
                 */
                originalDb =
                        findDatabaseNextToApplication(
                                configuredPath
                        );
            }

            if (!originalDb.exists()) {

                throw new RuntimeException(
                        "Base de données introuvable.\n"
                                + "Chemin configuré : "
                                + configuredPath
                                + "\n"
                                + "Chemin recherché : "
                                + originalDb.getAbsolutePath()
                );
            }

            try {

                Files.copy(
                        originalDb.toPath(),
                        writableDb.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );

                System.out.println(
                        "Database copied to:"
                );

                System.out.println(
                        writableDb.getAbsolutePath()
                );

            } catch (IOException e) {

                throw new RuntimeException(
                        "Impossible de copier la base de données vers "
                                + writableDb.getAbsolutePath(),
                        e
                );
            }
        }

        return writableDb;
    }

    /**
     * Check whether the database is located inside Program Files.
     */
    private boolean isInsideProgramFiles(File file) {

        String programFiles =
                System.getenv("ProgramFiles");

        String programFilesX86 =
                System.getenv("ProgramFiles(x86)");

        String path =
                file.getAbsolutePath()
                        .toLowerCase();

        if (programFiles != null) {

            if (path.startsWith(
                    new File(programFiles)
                            .getAbsolutePath()
                            .toLowerCase())) {

                return true;
            }
        }

        if (programFilesX86 != null) {

            if (path.startsWith(
                    new File(programFilesX86)
                            .getAbsolutePath()
                            .toLowerCase())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Find database next to the application/JAR.
     */
    private File findDatabaseNextToApplication(
            String configuredPath) {

        try {

            File location =
                    new File(
                            DatabaseConnection.class
                                    .getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI()
                    );

            File applicationDirectory;

            if (location.isFile()) {

                applicationDirectory =
                        location.getParentFile();

            } else {

                applicationDirectory =
                        location;
            }

            /*
             * Example:
             *
             * C:\Program Files\MyApplication\
             *
             * + resources/StoreDBDJ.accdb
             */
            File db =
                    new File(
                            applicationDirectory,
                            configuredPath
                    );

            return db;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Impossible de déterminer le chemin de l'application",
                    e
            );
        }
    }

    public static synchronized DatabaseConnection getInstance() {

        if (instance == null) {

            instance =
                    new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {

        return connection;
    }

    public void closeConnection() {

        try {

            if (connection != null &&
                    !connection.isClosed()) {

                connection.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
