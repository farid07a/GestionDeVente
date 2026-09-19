package main;

import home.HomeForm;
import javax.swing.JOptionPane;
import license.LicenseManager;
import license.LicenseStatus;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author farid
 */
public class Main {
 


    public static void main(String[] args) {

        LicenseManager licenseManager =
                new LicenseManager();

        try {

            LicenseStatus status =
                    licenseManager.validate();

            switch (status) {

                case TRIAL:

                    long remaining =
                            licenseManager
                                    .getRemainingDays();

                    JOptionPane.showMessageDialog(
                            null,
                            "Trial Version\n\n"
                            + "Remaining days: "
                            + remaining,
                            "License",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    openApplication();

                    break;

                case LICENSED:

                    openApplication();

                    break;

                case EXPIRED:

                    JOptionPane.showMessageDialog(
                            null,
                            "Your trial period has expired.\n\n"
                            + "Please contact the administrator.",
                            "Trial Expired",
                            JOptionPane.WARNING_MESSAGE
                    );

                    System.exit(0);

                    break;

                case TAMPERED:

                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid license or system date detected.\n\n"
                            + "Please contact the administrator.",
                            "License Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    System.exit(0);

                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Unable to start application.\n\n"
                    + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }
    }

    private static void openApplication() {

        java.awt.EventQueue.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {

                        HomeForm frame =
                                new HomeForm();

                        frame.setVisible(true);
                    }
                }
        );
    }
}
    

