package home;

import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import config.DatabaseConnection;
import dao.impl.EntreeSortieUtilisateurDAOImp;
import dao.impl.UtilisateurDAOImp;
import entity.EntreeSortieUtilisateur;
import entity.Utilisateur;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import panels.panDashBoard;
import panels.pan_Entreprise;
import panels.PanClient;
import panels.pan_categorie;
import panels.pan_produit;
import panels.PanProduitCategorie;
import panels.panAchat;
import panels.panSetting;
import panels.panVersement;
import services.Clock;
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
        initComponents();
        pan_client = new PanClient(this);
        pan_Entreprise = new pan_Entreprise(this);
        pan_produit = new pan_produit(this);
        pan_categorie = new pan_categorie(this);
        panAchat = new panAchat(this);
        panVersement = new panVersement(this);
        pan_produit_categorie = new PanProduitCategorie(pan_produit, pan_categorie);
        panSetting = new panSetting(this);
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this);

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
                        pan_produit_categorie.getPan_produit().setProduitsOnTab();
                        pan_produit_categorie.getPan_categorie().setCategoriesOnTab();

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
                        messageDialog.ShowConfirmMessageInFrame("إغــلاق", "هـل تـريـد غـلاق الـبـرنـامـج");
                        if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
                            Connection connection = DatabaseConnection.getInstance().getConnection();
                            int id = Integer.parseInt(LabIdUser.getText());
                            Utilisateur utilisateur = new UtilisateurDAOImp(connection).findById(id);
                            EntreeSortieUtilisateurDAOImp entreeSortieUtilisateurDAOImp = new EntreeSortieUtilisateurDAOImp(connection);
                            EntreeSortieUtilisateur esu = entreeSortieUtilisateurDAOImp.findLast();
                            esu.setHeureSortie(LocalTime.now());
                            entreeSortieUtilisateurDAOImp.update(esu);

                            dispose();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        Clock Clock = new Clock();
        Clock.start(LabTime, labDate);
    }

    public void setIdUser(int iduser) {
        LabIdUser.setText(iduser + "");
    }

    public int getIdUser() {
        return Integer.parseInt(LabIdUser.getText());
    }

    public void setNomUserInlab(String User) {
        LabNomUeser.setText(User);
    }

    public JLabel getjLabel20() {
        return jLabel20;
    }

////    public String getUser() {
////        return this.LabNomUeser.getText();
////    }
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
        jPanel1 = new javax.swing.JPanel();
        customMenu1 = new ui.menufr.customMenu();
        body = new javax.swing.JPanel();
        panButton = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        labDate = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        LabTime = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        LabNomUeser = new javax.swing.JLabel();
        LabIdUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setPreferredSize(new java.awt.Dimension(900, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        background.setBackground(new java.awt.Color(245, 245, 245));
        background.setLayout(new java.awt.BorderLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1124, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        background.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        customMenu1.setMaximumSize(new java.awt.Dimension(150, 32767));
        customMenu1.setMinimumSize(new java.awt.Dimension(150, 0));
        customMenu1.setPreferredSize(new java.awt.Dimension(150, 0));
        background.add(customMenu1, java.awt.BorderLayout.EAST);

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setMinimumSize(new java.awt.Dimension(0, 0));
        body.setOpaque(false);
        body.setLayout(new java.awt.CardLayout());
        background.add(body, java.awt.BorderLayout.CENTER);

        panButton.setMaximumSize(new java.awt.Dimension(32767, 40));
        panButton.setMinimumSize(new java.awt.Dimension(0, 40));
        panButton.setPreferredSize(new java.awt.Dimension(1249, 40));
        panButton.setLayout(new java.awt.CardLayout());

        jPanel5.setBackground(new java.awt.Color(0, 0, 70));
        jPanel5.setMinimumSize(new java.awt.Dimension(0, 29));
        jPanel5.setPreferredSize(new java.awt.Dimension(629, 29));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("التاريـخ :");

        labDate.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labDate.setForeground(new java.awt.Color(255, 255, 255));
        labDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("التوقيت :");

        LabTime.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabTime.setForeground(new java.awt.Color(255, 255, 255));
        LabTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LabTime.setText("00:00:00");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("V1.0.0");

        LabNomUeser.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabNomUeser.setForeground(new java.awt.Color(255, 255, 255));
        LabNomUeser.setText("المستخدم");

        LabIdUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabIdUser.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(LabTime)
                .addGap(5, 5, 5)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(LabNomUeser, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(528, 528, 528)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabNomUeser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(LabIdUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panButton.add(jPanel5, "card2");

        background.add(panButton, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(background, "card2");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Connection connection = DatabaseConnection.getInstance().getConnection();
        int id = Integer.parseInt(LabIdUser.getText());
        Utilisateur utilisateur = new UtilisateurDAOImp(connection).findById(id);
        EntreeSortieUtilisateurDAOImp entreeSortieUtilisateurDAOImp = new EntreeSortieUtilisateurDAOImp(connection);
        EntreeSortieUtilisateur esu = entreeSortieUtilisateurDAOImp.findLast();
        esu.setHeureSortie(LocalTime.now());
        entreeSortieUtilisateurDAOImp.update(esu);
    }//GEN-LAST:event_formWindowClosing

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

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new splashscreen.SplashScreen(null, true).setVisible(true);
                new HomeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabIdUser;
    private javax.swing.JLabel LabNomUeser;
    private javax.swing.JLabel LabTime;
    private javax.swing.JPanel background;
    private javax.swing.JPanel body;
    private ui.menufr.customMenu customMenu1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel labDate;
    private javax.swing.JPanel panButton;
    // End of variables declaration//GEN-END:variables
};
