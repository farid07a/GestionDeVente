/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import config.DatabaseConnection;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import entity.Client;
import entity.ClientPayeParEntreprise;
import entity.VersementEntreprise;
import home.HomeForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import material.design.DialogShake;
import material.design.DropShadowPanel;
import material.design.OutsideClickHandler;
import material.design.TitleBarPanel;
import material.design.TitleBarPanel1;
import material.design.designeTable;

/**
 *
 * @author pc
 */
public class DetaillVersementEntrepriceForm extends javax.swing.JDialog {

    VersementEntreprise versementEntreprise;
    Connection connection;
    HomeForm homeForm;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
   

    public DetaillVersementEntrepriceForm(java.awt.Frame parent, boolean modal, VersementEntreprise versementEntreprise) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;      
    
        connection = DatabaseConnection.getInstance().getConnection();
        this.versementEntreprise = versementEntreprise;
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);
        initComponents();
        
        
        new designeTable().setDesignTable(tab, jScrollPane2);
        new designeTable().SearchTable(tab, txt_search);
        labNomEntrep.setText(versementEntreprise.getEntreprise().getNom_ar());
        labDatVersement.setText(versementEntreprise.getDate_versement().toString());
        labSommeVersement.setText(formatter.format(versementEntreprise.getMontant()));
        if (versementEntreprise.getReste_credit() < 0) {
            labRestCredit.setText("0.00");
            labAugMantant.setText(formatter.format(versementEntreprise.getReste_credit()));
        } else {
            labRestCredit.setText(formatter.format(versementEntreprise.getReste_credit()));
            labAugMantant.setText("0.00");
        }
        setClientPayeeParVersementInTab();
        setLocationRelativeTo(homeForm);
        TableColumn column = tab.getColumnModel().getColumn(0);
        tab.getColumnModel().removeColumn(column);
}

    public void setClientPayeeParVersementInTab() {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);

        List<ClientPayeParEntreprise> clientPayeParEntreprises = clientPayeParEntrepriseDAOImpl.getClientPayeeParVersement(versementEntreprise);

        double total = 0;
        for (ClientPayeParEntreprise clientPayeParEntreprise : clientPayeParEntreprises) {
            Client client = clientPayeParEntreprise.getAchat().getClient();
            total = total + clientPayeParEntreprise.getAchat().getPrix_total();
            model.addRow(new Object[]{clientPayeParEntreprise.getId(),
                clientPayeParEntreprise.getAchat().getDate_achat(),
                formatter.format(clientPayeParEntreprise.getAchat().getPrix_total()),
                client.getPrenom() + " " + client.getNom(),
                client.getMatricule()
            });
        }
        totalAchat.setText(formatter.format(total));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panRound4 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_search = new material.design.SearchTextRound();
        jLabel4 = new javax.swing.JLabel();
        labNomEntrep = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labSommeVersement = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labRestCredit = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labDatVersement = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        labAugMantant = new javax.swing.JLabel();
        totalAchat = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(700, 660));
        jPanel1.setMinimumSize(new java.awt.Dimension(700, 660));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panRound4.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tab.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tab.setForeground(new java.awt.Color(102, 102, 102));
        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "التاريخ", "المـبـلغ", "الاسم و اللقب", "رقـم الـتـعـريـف"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tab);

        tableScrollButton1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panRound4Layout = new javax.swing.GroupLayout(panRound4);
        panRound4.setLayout(panRound4Layout);
        panRound4Layout.setHorizontalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panRound4Layout.setVerticalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jPanel1.add(panRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 680, 390));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("الــشـــركــــة   :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 87, 36));

        labNomEntrep.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labNomEntrep.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(labNomEntrep, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 50, 350, 36));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("قيمة الدفعة : ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, 94, 27));

        labSommeVersement.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labSommeVersement.setForeground(new java.awt.Color(0, 0, 204));
        labSommeVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labSommeVersement.setText("0.00");
        jPanel1.add(labSommeVersement, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 90, 32));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("الديون المتبقية لدفعة :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 130, -1, 27));

        labRestCredit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labRestCredit.setForeground(new java.awt.Color(255, 51, 51));
        labRestCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labRestCredit.setText("0.00");
        jPanel1.add(labRestCredit, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 132, 90, 30));

        jLabel1.setBackground(new java.awt.Color(43, 43, 140));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تفاصيل عملية الدفع");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 40));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("تـاريــخ الدفــع  : ");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 100, 27));

        labDatVersement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labDatVersement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(labDatVersement, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 100, 32));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("المبلغ الزائد لدفعة :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 170, 130, -1));

        labAugMantant.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labAugMantant.setForeground(new java.awt.Color(51, 153, 0));
        labAugMantant.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAugMantant.setText("0.00");
        jPanel1.add(labAugMantant, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, 90, -1));

        totalAchat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        totalAchat.setForeground(new java.awt.Color(0, 0, 153));
        totalAchat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalAchat.setText("0.00");
        jPanel1.add(totalAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 590, 40, 40));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setText("المجموع :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 590, 110, 40));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setText("دج");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 20, 20));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setText("دج");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 600, 20, 20));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setText("دج");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, 20, 20));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("دج");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, 20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

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
            java.util.logging.Logger.getLogger(DetaillVersementEntrepriceForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetaillVersementEntrepriceForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetaillVersementEntrepriceForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetaillVersementEntrepriceForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DetaillVersementEntrepriceForm dialog = new DetaillVersementEntrepriceForm(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labAugMantant;
    private javax.swing.JLabel labDatVersement;
    private javax.swing.JLabel labNomEntrep;
    private javax.swing.JLabel labRestCredit;
    private javax.swing.JLabel labSommeVersement;
    private ui.card.panRound panRound4;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private javax.swing.JLabel totalAchat;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
