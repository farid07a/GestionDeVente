/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chartbar;

import chartbar.ModelChart;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.Entreprise;
import entity.VersementEntreprise;
import java.awt.Color;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author RAVEN
 */
public class Main extends javax.swing.JFrame {

    Connection connection;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    AchatDAOImpl achatDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;

    public Main() {
        initComponents();
        connection = DatabaseConnection.getInstance().getConnection();
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        //   List<Object[]> versementEntreprises = versementEntrepriseDAOImpl.getSommeVesementGroupByEntreprise();

        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();

        //  List<Achat> achats = achatDAOImpl.getAchatByEntreprise(null);
        for (Entreprise entreprise : entreprises) {
            List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getVersementEntrepriseByIdEntreprise(entreprise);

            List<Achat> achats = achatDAOImpl.getAchatByEntreprise(entreprise);

            VersementEntreprise LastCredit = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);
            double sommeVersemnt = versementEntreprises.stream()
                    .mapToDouble(VersementEntreprise::getMontant)
                    .sum();
            double sommeAchat = achats.stream()
                    .mapToDouble(Achat::getPrix_total)
                    .sum();

            //
            double restCredit = 0;

            if (LastCredit != null) {
                restCredit = LastCredit.getReste_credit();
            }

            chart.addData(new ModelChart(
                    entreprise.getNom_fr(),
                    new double[]{
                        sommeVersemnt,
                        sommeAchat,
                        restCredit
                    }
            )
            );
        }

        getContentPane().setBackground(new Color(250, 250, 250));
        chart.addLegend("دفعات الشركة", new Color(245, 189, 135));
        chart.addLegend("مبيعات", new Color(135, 189, 245));
        chart.addLegend("ديون الشركة", new Color(189, 135, 245));

//chart.setHoverListener(data -> {
//
//    if (data != null) {
//
//        String entreprise = (String) data[0];
//
//        double versement = (double) data[1];
//        double achat = (double) data[2];
//        double credit = (double) data[3];
//
//        lblVersement.setText(
//                "الدفعات : "
//                + String.format("%,.2f دج", versement)
//        );
//
//        lblAchat.setText(
//                "المبيعات : "
//                + String.format("%,.2f دج", achat)
//        );
//
//        lblCredit.setText(
//                "الرصيد : "
//                + String.format("%,.2f دج", credit)
//        );
//
//    } else {
//
//        lblVersement.setText("الدفعات : -");
//        lblAchat.setText("المبيعات : -");
//        lblCredit.setText("الرصيد : -");
//    }
//});
//        chart.addLegend("Income", new Color(245, 189, 135));
//        chart.addLegend("Expense", new Color(135, 189, 245));
//        chart.addLegend("Profit", new Color(189, 135, 245));
//        chart.addLegend("Cost", new Color(139, 229, 222));
//        chart.addLegend("New", new Color(139, 229, 222));
//        
//        chart.addData(new ModelChart("January", new double[]{500, 200, 80, 0, 40}));
//        chart.addData(new ModelChart("February", new double[]{600, 750, 90, 150, 40}));
//        chart.addData(new ModelChart("March", new double[]{200, 350, 460, 900, 40}));
//        chart.addData(new ModelChart("April", new double[]{480, 150, 750, 700, 40}));
//        chart.addData(new ModelChart("May", new double[]{12, 16, 300, 150, 40, 40}));
//        chart.addData(new ModelChart("June", new double[]{1000, 280, 81, 200, 40}));
        chart.setHoverListener(data -> {

            if (data != null) {

                String entreprise
                        = (String) data[1];

                double versement
                        = (double) data[2];

                double achat
                        = (double) data[3];

                double credit
                        = (double) data[4];

                lblIncome.setText(
                        "الدفعات : "
                        + String.format(
                                "%,.2f دج",
                                versement
                        )
                );

                lblExpense.setText(
                        "المبيعات : "
                        + String.format(
                                "%,.2f دج",
                                achat
                        )
                );

                jLabel1.setText(
                        "الديون : "
                        + String.format(
                                "%,.2f دج",
                                credit
                        )
                );

            } else {

                lblIncome.setText(
                        "الدفعات : -"
                );

                lblExpense.setText(
                        "المبيعات : -"
                );

                jLabel1.setText(
                        "الديون : -"
                );
            }
        });
//
//        chart.setHoverListener(data -> {
//
//            if (data != null) {
//
//                String month
//                        = (String) data[0];
//
//                double income
//                        = (double) data[1];
//
//                double expense
//                        = (double) data[2];
//
//                lblIncome.setText(
//                        " مدفوعات: "
//                        + String.format(
//                                "%,.2f DA",
//                                income
//                        )
//                );
//
//                lblExpense.setText(
//                        "مبيعات : "
//                        + String.format(
//                                "%,.2f DA",
//                                expense
//                        )
//                );
//                jLabel1.setText(
//                        "ديون : "
//                        + String.format(
//                                "%,.2f DA",
//                                month
//                        )
//                );
//            } else {
//
//                lblIncome.setText(" مدفوعات: -");
//
//                lblExpense.setText("مبيعات : -");
//                jLabel1.setText("ديون : - ");
//            }
//        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chart = new chartbar.Chart();
        jButton1 = new javax.swing.JButton();
        lblIncome = new javax.swing.JLabel();
        lblExpense = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        chart.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        jButton1.setText("Refresh And Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblIncome.setText("jLabel1");

        lblExpense.setText("jLabel1");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(170, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblExpense)
                .addGap(31, 31, 31)
                .addComponent(lblIncome)
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(295, 295, 295))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIncome)
                    .addComponent(lblExpense)
                    .addComponent(jLabel1))
                .addGap(77, 77, 77)
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addGap(97, 97, 97))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        chart.start();
    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        chart.clear();
        chart.addData(new ModelChart("January", new double[]{500, 200, 80, 89}));
        chart.addData(new ModelChart("February", new double[]{600, 750, 90, 150}));
        chart.addData(new ModelChart("March", new double[]{200, 350, 460, 900}));
        chart.addData(new ModelChart("April", new double[]{480, 150, 750, 700}));
        chart.addData(new ModelChart("May", new double[]{350, 540, 300, 150}));
        chart.addData(new ModelChart("June", new double[]{190, 280, 81, 200}));
        chart.start();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chartbar.Chart chart;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblExpense;
    private javax.swing.JLabel lblIncome;
    // End of variables declaration//GEN-END:variables
}
