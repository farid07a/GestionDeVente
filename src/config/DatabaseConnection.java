/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author pc
 */

import java.io.FileInputStream;
import java.io.IOException;
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

    private void loadProperties() {

        try (FileInputStream fis = new FileInputStream("resources/config.properties")) {

            properties.load(fis);

        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger config.properties", e);
        }

    }

    private void connect() {

        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            String url = "jdbc:ucanaccess://" + properties.getProperty("db.path");

            /*
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
            */

            connection = DriverManager.getConnection(url);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }

    }

    public static synchronized DatabaseConnection getInstance() {

        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;

    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {

        try {

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    

    public static void main(String[] args) {

        try {

            Connection cn = DatabaseConnection
                    .getInstance()
                    .getConnection();

            if (cn != null && !cn.isClosed()) {

                System.out.println("==============================");
                System.out.println("Connexion réussie.");
                System.out.println("Base de données connectée.");
                System.out.println("==============================");

            } else {

                System.out.println("Connexion échouée.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}