/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logger;

/**
 *
 * @author farid
 */

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static final Logger LOGGER =
            Logger.getLogger("MyApplication");

    static {

        try {

            // Create logs directory
            File logDirectory = new File("logs");

            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            // INFO
            FileHandler infoHandler =
                    new FileHandler("logs/info.log", true);

            infoHandler.setLevel(Level.INFO);

            infoHandler.setFilter(new Filter() {
                @Override
                public boolean isLoggable(LogRecord record) {
                    return record.getLevel() == Level.INFO;
                }
            });

            infoHandler.setFormatter(new SimpleFormatter());


            // WARNING
            FileHandler warningHandler =
                    new FileHandler("logs/warning.log", true);

            warningHandler.setLevel(Level.WARNING);

            warningHandler.setFilter(new Filter() {
                @Override
                public boolean isLoggable(LogRecord record) {
                    return record.getLevel() == Level.WARNING;
                }
            });

            warningHandler.setFormatter(new SimpleFormatter());


            // ERROR
            FileHandler errorHandler =
                    new FileHandler("logs/error.log", true);

            errorHandler.setLevel(Level.SEVERE);

            errorHandler.setFilter(new Filter() {
                @Override
                public boolean isLoggable(LogRecord record) {
                    return record.getLevel() == Level.SEVERE;
                }
            });

            errorHandler.setFormatter(new SimpleFormatter());


            // Add handlers
            LOGGER.addHandler(infoHandler);
            LOGGER.addHandler(warningHandler);
            LOGGER.addHandler(errorHandler);

            LOGGER.setLevel(Level.ALL);

            // Don't print logs to console
            LOGGER.setUseParentHandlers(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void info(String message) {
        LOGGER.log(Level.INFO, message);
    }


    public static void warning(String message) {
        LOGGER.log(Level.WARNING, message);
    }


    public static void error(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }
}

/*
AppLogger.info("Application started");

AppLogger.warning("PDF not found");

try {

    // Your code

} catch (Exception e) {

    AppLogger.error("Error while processing PDF", e);
}
*/