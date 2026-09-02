/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import chartbar.ModelChart;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.EntrepriseConfigDAOImp;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.Client;
import entity.Entreprise;
import entity.EntrepriseConfig;
import entity.VersementEntreprise;
import java.awt.Color;
import java.sql.Connection;
import java.util.List;
import ui.card.Model_card;

/**
 *
 * @author pc
 */
public class panDashBoard extends javax.swing.JPanel {

    Connection connection;
    ClientDAOImpl clientDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    AchatDAOImpl achatDAOImpl;
    EntrepriseConfigDAOImp entrepriseConfigDAOImp;

    public panDashBoard() {
        initComponents();       
        connection = DatabaseConnection.getInstance().getConnection();
        clientDAOImpl = new ClientDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        entrepriseConfigDAOImp = new EntrepriseConfigDAOImp(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        
        UI();
        setInfoInChartBar();
        SetInfoEntreprise();
    }

    public void UI() {
        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();
        List<Client> clients = clientDAOImpl.findAll();
        List<Object[]> versementEntreprises = versementEntrepriseDAOImpl.getSommeVesementGroupByEntreprise();
        List<Achat> achats = achatDAOImpl.findAll();
        double sommeVersement = versementEntreprises.stream()
                .mapToDouble(row -> (Double) row[2])
                .sum();

        double sommeAchats = achats.stream()
                .mapToDouble(Achat::getPrix_total)
                .sum();

        cardEnreprise.setData(new Model_card(null, "الشركات", entreprises.size() + "  شركة", ""));
        cardClient.setData(new Model_card(null, "الزبائن", clients.size() + "  زبون", ""));
        cardMontant.setData(new Model_card(null, "دفعات ", sommeVersement + " دج", "المبيعات : " + sommeAchats));
       
    }
    
    public void SetInfoEntreprise(){
     List<EntrepriseConfig> entrepriseConfigs = entrepriseConfigDAOImp.findAll();
   
     if (entrepriseConfigs.isEmpty()) {
         return;
     }
         EntrepriseConfig  entrepriseConfig = entrepriseConfigs.get(0);
     
            txtNom.setText(entrepriseConfig.getNom_ar() != null ? entrepriseConfig.getNom_ar() : "");
            txtNomFr.setText(entrepriseConfig.getNom_fr() != null ? entrepriseConfig.getNom_fr() : "");
            txt_adress.setText(entrepriseConfig.getAdresse() != null ? entrepriseConfig.getAdresse() : "");
            txt_gmail.setText(entrepriseConfig.getEmail() != null ? entrepriseConfig.getEmail() : "");
            TxtTel.setText(entrepriseConfig.getTel() != null ? entrepriseConfig.getTel() : "");

    }

    public void setInfoInChartBar() {
        chart.addLegend("مبيعات", new Color(135, 189, 245));//245, 189, 135));//135, 189, 245));
        chart.addLegend("دفعات الشركة", new Color(100,199,100));//245, 189, 135));
        chart.addLegend("ديون ", new Color(255,84,84));//189, 135, 245));
         chart.addLegend("مبيعات بدون دفع", new Color(189, 135, 245));//189, 135, 245));

        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();

        for (Entreprise entreprise : entreprises) {

            List<VersementEntreprise> versementEntreprises
                    = versementEntrepriseDAOImpl
                            .getVersementEntrepriseByIdEntreprise(entreprise);

            List<Achat> achats
                    = achatDAOImpl.getAchatByEntreprise(entreprise);
            List<Achat> achatsNoPayee
                    = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
            VersementEntreprise lastCredit
                    = versementEntrepriseDAOImpl
                            .getLastVersementEntreprise(entreprise);

            double sommeVersemnt
                    = versementEntreprises.stream()
                            .mapToDouble(VersementEntreprise::getMontant)
                            .sum();

            double sommeAchat
                    = achats.stream()
                            .mapToDouble(Achat::getPrix_total)
                            .sum();
            
            double sommeAchatNoPayee
                    = achatsNoPayee.stream()
                            .mapToDouble(Achat::getPrix_total)
                            .sum();


            double restCredit = 0;

            if (lastCredit != null) {
                restCredit = lastCredit.getReste_credit();
                if(restCredit <0){
                
                }
            }

          
            chart.addData(
                    new ModelChart(
                           !entreprise.getNom_fr().isEmpty()? entreprise.getNom_fr() : entreprise.getNom_ar(),
                                new double[]{
                                sommeAchat,
                                sommeVersemnt,
                                restCredit,
                                sommeAchatNoPayee
                            }
                    )
            );
            System.out.println(
                    entreprise.getNom_fr()
                    + " | "
                    + sommeVersemnt
                    + " | "
                    + sommeAchat
                    + " | "
                    + restCredit
            );
        }

        chart.setHoverListener(data -> {

            if (data != null) {

                double versement = (double) data[2];
                double achat = (double) data[3];
                double credit = (double) data[4];

                lblIncome.setText(
                        "الدفعات : "
                        + String.format("%,.2f دج", versement)
                );

                lblExpense.setText(
                        "المبيعات : "
                        + String.format("%,.2f دج", achat)
                );

                jLabel1.setText(
                        "الديون : "
                        + String.format("%,.2f دج", credit)
                );

            } else {

                lblIncome.setText("الدفعات : -");
                lblExpense.setText("المبيعات : -");
                jLabel1.setText("الديون : -");
            }
        });

        chart.revalidate();
        chart.repaint();
          chart.start();
    }

        public void Main2() {
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

       // getContentPane().setBackground(new Color(250, 250, 250));
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

        panInfo = new javax.swing.JPanel();
        txtNom = new material.design.buttonRounder();
        txtNomFr = new material.design.buttonRounder();
        TxtTel = new material.design.buttonRounder();
        txt_adress = new material.design.buttonRounder();
        txt_gmail = new material.design.buttonRounder();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        costumChart1 = new chart.costumChart();
        cardMontant = new ui.card.Card();
        cardClient = new ui.card.Card();
        cardEnreprise = new ui.card.Card();
        chart = new chartbar.Chart();
        lblExpense = new javax.swing.JLabel();
        lblIncome = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        panInfo.setBackground(new java.awt.Color(255, 255, 255));
        panInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "مـؤسـسـتـي", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 1, 18), new java.awt.Color(153, 153, 153))); // NOI18N

        txtNom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtNom.setText("المؤسسة");
        txtNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomActionPerformed(evt);
            }
        });

        txtNomFr.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtNomFr.setForeground(new java.awt.Color(0, 51, 204));
        txtNomFr.setText("Entreprise");
        txtNomFr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomFrActionPerformed(evt);
            }
        });

        TxtTel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        TxtTel.setForeground(new java.awt.Color(204, 0, 51));
        TxtTel.setText("الهاتف");
        TxtTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtTelActionPerformed(evt);
            }
        });

        txt_adress.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_adress.setText("العنوان");
        txt_adress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_adressActionPerformed(evt);
            }
        });

        txt_gmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_gmail.setText("Gmail");
        txt_gmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panInfoLayout = new javax.swing.GroupLayout(panInfo);
        panInfo.setLayout(panInfoLayout);
        panInfoLayout.setHorizontalGroup(
            panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panInfoLayout.createSequentialGroup()
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNomFr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panInfoLayout.createSequentialGroup()
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtTel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panInfoLayout.createSequentialGroup()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_adress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_gmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panInfoLayout.createSequentialGroup()
                .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInfoLayout.createSequentialGroup()
                        .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel39))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNom, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panInfoLayout.setVerticalGroup(
            panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInfoLayout.createSequentialGroup()
                .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNomFr, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addGap(5, 5, 5)
                .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(TxtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_adress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_gmail, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        costumChart1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        cardMontant.setColor1(new java.awt.Color(217, 241, 217));
        cardMontant.setColor2(new java.awt.Color(181, 234, 181));
        cardMontant.setMinimumSize(new java.awt.Dimension(271, 158));

        cardClient.setColor1(new java.awt.Color(204, 204, 255));
        cardClient.setColor2(new java.awt.Color(51, 204, 255));
        cardClient.setMinimumSize(new java.awt.Dimension(271, 158));

        cardEnreprise.setColor1(new java.awt.Color(255, 232, 255));
        cardEnreprise.setColor2(new java.awt.Color(248, 192, 248));
        cardEnreprise.setMinimumSize(new java.awt.Dimension(271, 158));

        chart.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblExpense.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblExpense.setForeground(new java.awt.Color(51, 102, 255));
        lblExpense.setText("المبييعات");

        lblIncome.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblIncome.setForeground(new java.awt.Color(38, 158, 38));
        lblIncome.setText("الدفعات");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 84, 84));
        jLabel1.setText("ديون");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(costumChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 854, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cardMontant, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(16, 16, 16)
                                .addComponent(cardClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(16, 16, 16)
                                .addComponent(cardEnreprise, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(14, 14, 14))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addComponent(lblIncome)
                .addGap(53, 53, 53)
                .addComponent(lblExpense)
                .addGap(316, 316, 316))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(panInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cardEnreprise, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardClient, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardMontant, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(costumChart1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExpense)
                    .addComponent(lblIncome)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panInfo.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomActionPerformed

    private void txtNomFrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomFrActionPerformed
        // openWebsite(contact.getFacebook());
    }//GEN-LAST:event_txtNomFrActionPerformed

    private void TxtTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtTelActionPerformed
        // openWebsite(contact.getInstagram());
    }//GEN-LAST:event_TxtTelActionPerformed

    private void txt_adressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_adressActionPerformed
        // openWebsite(contact.getWebSite());
    }//GEN-LAST:event_txt_adressActionPerformed

    private void txt_gmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gmailActionPerformed

    }//GEN-LAST:event_txt_gmailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private material.design.buttonRounder TxtTel;
    private ui.card.Card cardClient;
    private ui.card.Card cardEnreprise;
    private ui.card.Card cardMontant;
    private chartbar.Chart chart;
    private chart.costumChart costumChart1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel lblExpense;
    private javax.swing.JLabel lblIncome;
    private javax.swing.JPanel panInfo;
    private material.design.buttonRounder txtNom;
    private material.design.buttonRounder txtNomFr;
    private material.design.buttonRounder txt_adress;
    private material.design.buttonRounder txt_gmail;
    // End of variables declaration//GEN-END:variables
}
