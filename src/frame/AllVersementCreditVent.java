/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.AchatDetailDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.ProduitDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.Entreprise;
import entity.VersementEntreprise;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import material.design.designeTable;

public class AllVersementCreditVent extends javax.swing.JDialog {

    Connection connection;
    HomeForm homeForm;
    AchatDAOImpl achatDAOImpl;
    ClientDAOImpl clientDAOImpl;
    ProduitDAOImpl produitDAOImpl;
    AchatDetailDAOImpl achatDetailDAOImpl;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;
    Exite exite;

    public AllVersementCreditVent(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        initComponents();
        setLocationRelativeTo(parent);

        new designeTable().setDesignTable(tab, jScrollPane1);

        new designeTable().SearchTable(tab, txt_searc);

        connection = DatabaseConnection.getInstance().getConnection();
        achatDAOImpl = new AchatDAOImpl(connection);
        clientDAOImpl = new ClientDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        tab.removeColumn(tab.getColumnModel().getColumn(0));

        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        messageDialog = new MessageDialog(this);
        exite = new Exite(this, homeForm);

        setVersmentOnTab();
    }

    public void setVersmentOnTab() {
        double Montant = 0;
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        double TotalVersement = 0.0;
        double TotalCredit = 0.0;
        double TotalAchat = 0.0;
        double sommeVersement = 0.0;
        double credit = 0.0;
        double sommeAchat=0.0;
        double sommeAchatNoPay=0.0;
        List<Entreprise> Entreprises = entrepriseDAOImpl.findAll();
        for (Entreprise entreprise : Entreprises) {
            sommeVersement = 0.0;
            credit = 0.0;
            sommeAchat=0.0;
            List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getVersementEntrepriseByIdEntreprise(entreprise);
            if (versementEntreprises != null) {
                sommeVersement = versementEntreprises.stream()
                    .mapToDouble(VersementEntreprise::getMontant)
                    .sum();
            }
            
            VersementEntreprise lastVersement = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);

            credit = (lastVersement != null) ? lastVersement.getReste_credit() : 0.0;
            List<Achat> achats = achatDAOImpl.getAchatByEntreprise(entreprise);
           
           List<Achat> achatsNoPayee =  achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
            if (achats != null) {
                sommeAchat = achats.stream()
                    .mapToDouble(Achat::getPrix_total)
                    .sum();
            }
            if (achatsNoPayee != null) {
                sommeAchatNoPay = achatsNoPayee.stream()
                    .mapToDouble(Achat::getPrix_total)
                    .sum();
            }
            
            double MontantCredit= (credit>=0)? sommeAchatNoPay+credit : sommeAchatNoPay ;
                    model.insertRow(0, new Object[]{entreprise.getId(), 
                formatter.format(MontantCredit),
                (credit<0)? " + "+formatter.format( credit*-1 ) : formatter.format( credit ),
                formatter.format(sommeAchatNoPay),
                formatter.format(sommeAchat),
                formatter.format(sommeVersement),
                entreprise.getNom_ar()});
            TotalVersement = TotalVersement + sommeVersement;
            TotalCredit = TotalCredit + credit;
            TotalAchat = TotalAchat+sommeAchat;
        }

        LabMontanTotal.setText(formatter.format(TotalVersement));
        LabAllCreditRest.setText(formatter.format(TotalCredit));
        LabAchatTotal.setText(formatter.format(TotalAchat));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panRound6 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        LabAllCreditRest = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        LabMontanTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        panRound1 = new ui.card.panRound();
        LabAchatTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_searc = new material.design.SearchTextRound();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panRound6.setColor1(new java.awt.Color(255, 255, 255));
        panRound6.setColor2(new java.awt.Color(255, 237, 231));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(236, 58, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ديون الشركات");

        LabAllCreditRest.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        LabAllCreditRest.setForeground(new java.awt.Color(236, 58, 102));
        LabAllCreditRest.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAllCreditRest.setText("0.00");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-48.png"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(236, 58, 102));
        jLabel19.setText("دج");

        javax.swing.GroupLayout panRound6Layout = new javax.swing.GroupLayout(panRound6);
        panRound6.setLayout(panRound6Layout);
        panRound6Layout.setHorizontalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 0, 0)
                        .addComponent(LabAllCreditRest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(16, 16, 16))
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(36, 36, 36))))
        );
        panRound6Layout.setVerticalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAllCreditRest, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound2.setColor1(new java.awt.Color(255, 255, 255));
        panRound2.setColor2(new java.awt.Color(170, 232, 212));

        LabMontanTotal.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabMontanTotal.setForeground(new java.awt.Color(51, 102, 0));
        LabMontanTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabMontanTotal.setText("0.00");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-dollar-bag-48.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" مدفوعات الشركات");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 102, 0));
        jLabel20.setText("دج");

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel20)
                        .addGap(0, 0, 0)
                        .addComponent(LabMontanTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        panRound2Layout.setVerticalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabMontanTotal)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound1.setColor1(new java.awt.Color(255, 255, 255));
        panRound1.setColor2(new java.awt.Color(191, 204, 225));

        LabAchatTotal.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAchatTotal.setForeground(new java.awt.Color(0, 51, 153));
        LabAchatTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAchatTotal.setText("0.00");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-48.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("مشتريات الشركات");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("دج");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel11)
                        .addGap(0, 0, 0)
                        .addComponent(LabAchatTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAchatTotal)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "ديون الشركة", "دين/زيادة", "مشتريات الشركة الغيرمدفوعة", " مشتريات الشركة", "اجمالي المدفوعات", "الشركة"
            }
        ));
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab);

        tableScrollButton1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        txt_searc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("مـلـخـص مدفوعات الـشـركــات & الـديــون & الـمـبـيــعــات  ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(397, 397, 397)
                        .addComponent(txt_searc, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(355, 355, 355))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(panRound6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(45, 45, 45)
                .addComponent(panRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(46, 46, 46)
                .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(134, 134, 134))
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panRound1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_searc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcActionPerformed
    int lastRow=-1;
    private void tabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseReleased
         if (!SwingUtilities.isLeftMouseButton(evt)) {
        return;
    }

    int row = tab.rowAtPoint(evt.getPoint());

    if (row == -1) {
        tab.clearSelection();
        lastRow = -1;
        return;
    }

    if (lastRow == row) {
        tab.clearSelection();
        lastRow = -1;
    } else {
        tab.setRowSelectionInterval(row, row);
        lastRow = row;
    }
    }//GEN-LAST:event_tabMouseReleased

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(AllVersementCreditVent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllVersementCreditVent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllVersementCreditVent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllVersementCreditVent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AllVersementCreditVent dialog = new AllVersementCreditVent(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabAchatTotal;
    private javax.swing.JLabel LabAllCreditRest;
    private javax.swing.JLabel LabMontanTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound6;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private material.design.SearchTextRound txt_searc;
    // End of variables declaration//GEN-END:variables
}
