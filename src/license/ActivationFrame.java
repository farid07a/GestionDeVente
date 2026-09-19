/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package license;

/**
 *
 * @author farid
 */
import home.HomeForm;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActivationFrame extends JFrame {

    private JTextField txtLicenseKey;
    private JButton btnActivate;

    private LicenseManager licenseManager;

    public ActivationFrame() {

        licenseManager = new LicenseManager();

        setTitle("Activate Application");
        setSize(450, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
    }

    private void createUI() {

        try {
            JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
            
            JLabel lblKey = new JLabel("License Key:");
            
            txtLicenseKey = new JTextField();
            
            btnActivate = new JButton("Activate");
            
            panel.add(lblKey);
            panel.add(txtLicenseKey);
            panel.add(btnActivate);
            
            add(panel);
            
            btnActivate.addActionListener(e -> activate());
        } catch (Exception ex) {
            Logger.getLogger(ActivationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void activate() throws Exception {

        String key = txtLicenseKey.getText();

        boolean activated = licenseManager.activate(key);

        if (activated) {

            JOptionPane.showMessageDialog(
                    this,
                    "License activated successfully."
            );

            dispose();

            new HomeForm().setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid License Key.",
                    "Activation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
