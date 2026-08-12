package home;

import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import panels.panDashBoard;
import panels.pan_Entreprise;
import panels.PanClient;
import panels.pan_categorie;
import panels.pan_produit;
import panels.PanProduitCategorie;
import panels.panAchat;
import panels.panSetting;
import panels.panVersement;
import ui.menufr.EventMenuSelected;

public class HomeForm extends javax.swing.JFrame {

    GridBagConstraints gbc = new GridBagConstraints();
    pan_produit pan_produit;

    PanClient pan_client;
    pan_Entreprise pan_Entreprise;
    PanProduitCategorie pan_produit_categorie;

    pan_categorie pan_categorie;

    panAchat panAchat;
    panVersement panVersement;
    panSetting panSetting;
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;
    public HomeForm() {
        // pan_produit = new pan_produit(this);
        pan_client = new PanClient(this);
        pan_Entreprise = new pan_Entreprise(this);
        pan_produit = new pan_produit(this);
        pan_categorie = new pan_categorie(this);
        panAchat = new panAchat(this);
        panVersement = new panVersement(this);
        pan_produit_categorie = new PanProduitCategorie(pan_produit, pan_categorie);
        panSetting = new panSetting();
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this);
        initComponents();

        setExtendedState(MAXIMIZED_BOTH);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        setForm(new panDashBoard());
        customMenu1.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                switch (index) {
                    case 0:
                        setForm(new panDashBoard());
                        break;
                    case 2:
                        setForm(pan_Entreprise);
                        pan_Entreprise.setEntreprisesOnTab();

                        break;
                    case 3:
                        setForm(pan_client);
                        pan_client.setClientsOnTab();
                        break;
                    case 4:
                        setForm(pan_produit_categorie);

                        break;
                    case 6:
                        setForm(panAchat);
                        panAchat.setInfoAchatInTab();

                        break;
                    case 7:
                        setForm(panVersement);
                        panVersement.setVersmentOnTab();
                        break;
                    case 8:

                        break;
                    case 9:
                        setForm(panSetting);
                        break;
                    case 10:
                        messageDialog.showMessage("إغــلاق", "هـل تـريـد غـلاق الـبـرنـامـج");
                        if (messageDialog.getMessageType() == MessageDialog.MessageType.OK) {
                            dispose();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void setForm(Component com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }

    public pan_produit getPan_produit() {
        return pan_produit;
    }

    public PanClient getPan_client() {
        return pan_client;
    }

    public pan_categorie getPan_categorie() {
        return pan_categorie;
    }

    public pan_Entreprise getPan_Entreprise() {
        return pan_Entreprise;
    }

    public panAchat getPanAchat() {
        return panAchat;
    }

    public panVersement getPanVersement() {
        return panVersement;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        customMenu1 = new ui.menufr.customMenu();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        background.setBackground(new java.awt.Color(245, 245, 245));
        background.setLayout(new java.awt.BorderLayout());

        customMenu1.setMaximumSize(new java.awt.Dimension(150, 32767));
        customMenu1.setMinimumSize(new java.awt.Dimension(150, 0));
        customMenu1.setPreferredSize(new java.awt.Dimension(150, 680));
        background.add(customMenu1, java.awt.BorderLayout.EAST);

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setMinimumSize(new java.awt.Dimension(200, 0));
        body.setOpaque(false);
        body.setPreferredSize(new java.awt.Dimension(200, 0));
        body.setLayout(new java.awt.CardLayout());
        background.add(body, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             //  new splashscreen.SplashScreen(null, true).setVisible(true);
               new HomeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel body;
    private ui.menufr.customMenu customMenu1;
    // End of variables declaration//GEN-END:variables
};
