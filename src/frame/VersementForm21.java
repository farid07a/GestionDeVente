/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.MessageDialog;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.ClientPayeParEntreprise;
import entity.Entreprise;
import entity.Nomber;
import entity.VersementEntreprise;
import enums.ModePaiement;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javafx.scene.control.RadioButton;
import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;
import material.design.designeTable;
import services.serviceVersementEntreprise;

public class VersementForm21 extends javax.swing.JDialog {

    HomeForm homeForm;
    Connection connection;
    AchatDAOImpl achatDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;

    public VersementForm21(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        initComponents();
        ButtonGroup group = new ButtonGroup();
        group.add(radioEsp);
        group.add(radioCart);
        group.add(radioChequ);

        setLocationRelativeTo(this.homeForm);

        new designeTable().setDesignTable(tab, jScrollPane2);
        connection = DatabaseConnection.getInstance().getConnection();
        achatDAOImpl = new AchatDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);
        setEntrepriseInCom();

        comboboxRound1.setSelectedIndex(-1);

        textMontantVersement.addActionListener(e -> {
            calculeCreditApresVersement();
        });
        pantab.setVisible(false);
    }

    public double calculeCreditApresVersement() {
        if (textMontantVersement.getText().isEmpty()) {
            textMontantVersement.setText(0.00 + "");
        }
//        double prixVersement = new Nomber().getNbDouble(textMontantVersement.getText());
//        double prixCredit = new Nomber().getNbDouble(labCreditAvanVersement.getText());
//        double prixCreditApresVesement= prixCredit  -  prixVersement;

        double montant = new Nomber().getNbDouble(textMontantVersement.getText());
        double montantAugm = new Nomber().getNbDouble(labNonCreditLsatVersemnt.getText());

        double montantAndAug = montant + montantAugm;

        double lastCredit = new Nomber().getNbDouble(labCreditLsatVersemnt.getText());
        double Credit = new Nomber().getNbDouble(labCreditAvanVersement.getText());
        double CreditNewAndLast = lastCredit + Credit;

        double restCridetApreVersement = CreditNewAndLast - montantAndAug; // +100 ent-->credi عند دين مؤسسة// -100 ---> بزيادة
        if (restCridetApreVersement >= 0) {
            labCredit_avec_versment.setText(formatter.format(restCridetApreVersement));
           labAug_avec_versment.setText(formatter.format(00));

        } else {
            labAug_avec_versment.setText(formatter.format(restCridetApreVersement));
            labCredit_avec_versment.setText(formatter.format(00));
        }

        return restCridetApreVersement;
    }

    public void setAchatNoVersementInTabl() {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        List<Achat> achats = achatDAOImpl.getAchatNotInTabVersement();

        for (Achat achat : achats) {

            int id = achat.getId();
            String matricul = achat.getClient().getMatricule();
            String nom = achat.getClient().getNom();
            String prenom = achat.getClient().getPrenom();

            double prixTotal = achat.getPrix_total();

            LocalDate date = achat.getDate_achat();
            Entreprise entreprice = achat.getClient().getEntreprise();

            model.addRow(new Object[]{id, date, formatter.format(prixTotal),
                entreprice.getNom_ar(), prenom
                + " " + nom, matricul});

        }
    }

    public void setAchatNotVersementInTablByEntreprise(Entreprise entreprise) {

        labEntreprice.setText(entreprise.getNom_ar() + " " + entreprise.getNom_fr() + " ");

        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        // exist cridete / NO

        double restCreditLastVersemet = new serviceVersementEntreprise(connection)
                .GetLastRestVersementEntreprise(entreprise);

        if (restCreditLastVersemet >= 0) {
            labCreditLsatVersemnt.setText(formatter.format(restCreditLastVersemet));
        } else {
            labNonCreditLsatVersemnt.setText(formatter.format(restCreditLastVersemet * -1));
        }

        List<Achat> achats = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
        double PrixTotalCredit = 0;
        for (Achat achat : achats) {

            int id = achat.getId();
            String matricul = achat.getClient().getMatricule();
            String nom = achat.getClient().getNom();
            String prenom = achat.getClient().getPrenom();

            double prixTotal = achat.getPrix_total();
            PrixTotalCredit = PrixTotalCredit + prixTotal;
            LocalDate date = achat.getDate_achat();
            Entreprise entreprice = achat.getClient().getEntreprise();

            model.addRow(new Object[]{id, date,
                formatter.format(prixTotal),
                entreprice.getNom_ar(), prenom + " " + nom,
                matricul});
        }

        // laste credit + creditAchat NO IN tab    AchatClientVers
        PrixTotalCredit = PrixTotalCredit + restCreditLastVersemet;

        labCreditAvanVersement.setText(formatter.format(PrixTotalCredit));
    }

    public void setEntrepriseInCom() {
        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();
        comboboxRound1.removeAllItems();
        for (Entreprise entreprise : entreprises) {
            comboboxRound1.addItem(entreprise.getNom_ar());
        }

    }

    public ModePaiement getModePaiement() {

        if (radioEsp.isSelected()) {
            return ModePaiement.ESPECES;
        }

        if (radioChequ.isSelected()) {
            return ModePaiement.CHEQUE;
        }

        if (radioCart.isSelected()) {
            return ModePaiement.CARTE_BANCAIRE;
        }

        return null;
    }

    public void saveClientPayeeParEntreprise(Entreprise entreprise, VersementEntreprise versementEntreprise) {
        List<Achat> achats = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
        for (Achat achat : achats) {
            ClientPayeParEntreprise clientPayeParEntreprise = new ClientPayeParEntreprise(0,
                    versementEntreprise, achat);
            if (clientPayeParEntrepriseDAOImpl.save(clientPayeParEntreprise) > 0) {
                System.out.println("Save clientPayeParEntreprise ......");
            }

        }
    }

    // exist Achat  + last exist credit
    public VersementEntreprise saveVersement(Entreprise entreprise) {

        double montant = new Nomber().getNbDouble(textMontantVersement.getText());
        double montantAugm = new Nomber().getNbDouble(labNonCreditLsatVersemnt.getText());

        double montantAndAug = montant + montantAugm;

        double lastCredit = new Nomber().getNbDouble(labCreditLsatVersemnt.getText());
        double Credit = new Nomber().getNbDouble(labCreditAvanVersement.getText());
        double CreditNewAndLast = lastCredit + Credit;

        double restCridetApreVersement = CreditNewAndLast - montantAndAug; // +100 ent-->credi عند دين مؤسسة// -100 ---> بزيادة

        ModePaiement mode_paiement = getModePaiement();
        VersementEntreprise versementEntreprise = null;
        if (restCridetApreVersement >= 0) {
            versementEntreprise = new VersementEntreprise(0, entreprise, montant,
                    LocalDate.now(), mode_paiement.name(), "", Credit,
                    restCridetApreVersement);
        } else {
            versementEntreprise = new VersementEntreprise(0, entreprise, montant,
                    LocalDate.now(), mode_paiement.name(), "بزيادة", Credit,
                    restCridetApreVersement);
        }

        if (versementEntrepriseDAOImpl.save(versementEntreprise) > 0) {
            System.out.println("save versment entreprise ..... ");
            versementEntreprise = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);
            saveClientPayeeParEntreprise(entreprise, versementEntreprise);// save saveClientPayeeParEntreprise

        }

        return versementEntreprise;

    }

    public void initItems() {
        labEntreprice.setText("");
        labCreditAvanVersement.setText(0.00 + "");
        textMontantVersement.setText(0.00 + "");
        radioEsp.setSelected(true);
        labCredit_avec_versment.setText(0.00 + "");

        labCreditLsatVersemnt.setText(0.00 + "");
        labNonCreditLsatVersemnt.setText(0.00 + "");

        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pantab = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_search = new material.design.SearchTextRound();
        panRound1 = new ui.card.panRound();
        jLabel4 = new javax.swing.JLabel();
        labEntreprice = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labCreditAvanVersement = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        textMontantVersement = new ui.card.TextFieldPrice();
        jLabel12 = new javax.swing.JLabel();
        radioChequ = new material.design.RadioButtonCustomAR();
        radioCart = new material.design.RadioButtonCustomAR();
        radioEsp = new material.design.RadioButtonCustomAR();
        jLabel8 = new javax.swing.JLabel();
        btn_save = new material.design.buttonRounder();
        btn_annuler = new material.design.buttonRounder();
        jLabel1 = new javax.swing.JLabel();
        panRound7 = new ui.card.panRound();
        jLabel2 = new javax.swing.JLabel();
        labCredit_avec_versment = new javax.swing.JLabel();
        labAug_avec_versment = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        comboboxRound1 = new material.design.ComboboxRoundNew();
        jLabel11 = new javax.swing.JLabel();
        panRound6 = new ui.card.panRound();
        labCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        panRound5 = new ui.card.panRound();
        labNonCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        pantab.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tab.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tab.setForeground(new java.awt.Color(102, 102, 102));
        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "التاريخ", "المـبـلغ", "الشركة ", "الاسم و اللقب", "رقـم الـتـعـريـف"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        javax.swing.GroupLayout pantabLayout = new javax.swing.GroupLayout(pantab);
        pantab.setLayout(pantabLayout);
        pantabLayout.setHorizontalGroup(
            pantabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pantabLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pantabLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pantabLayout.setVerticalGroup(
            pantabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pantabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        panRound1.setColor1(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("الــشـــركــــة   ");

        labEntreprice.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labEntreprice.setForeground(new java.awt.Color(0, 0, 102));
        labEntreprice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labEntreprice.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 102)));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("الـمـبلغ الإجـمـالي ");

        labCreditAvanVersement.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labCreditAvanVersement.setForeground(new java.awt.Color(204, 0, 0));
        labCreditAvanVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCreditAvanVersement.setText("0.00");
        labCreditAvanVersement.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 0, 0)));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("دج");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labCreditAvanVersement, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                    .addComponent(labEntreprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(16, 16, 16))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(labEntreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRound1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panRound1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labCreditAvanVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(31, 31, 31))
        );

        panRound2.setColor1(new java.awt.Color(255, 255, 255));
        panRound2.setMaximumSize(new java.awt.Dimension(493, 269));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("الـمـبـلغ الـمدفوع ");
        jLabel7.setToolTipText("");

        textMontantVersement.setBackground(new java.awt.Color(235, 235, 255));
        textMontantVersement.setForeground(new java.awt.Color(0, 0, 0));
        textMontantVersement.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textMontantVersement.setText("00.00");
        textMontantVersement.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        textMontantVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMontantVersementActionPerformed(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-portefeuille-35.png"))); // NOI18N

        radioChequ.setBackground(new java.awt.Color(51, 204, 0));
        radioChequ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cheque-de-paiement.png"))); // NOI18N
        radioChequ.setText("صك بريدي");
        radioChequ.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioChequ.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioChequ.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioChequ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioChequActionPerformed(evt);
            }
        });

        radioCart.setBackground(new java.awt.Color(51, 204, 0));
        radioCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-carte-bancaire-40.png"))); // NOI18N
        radioCart.setText("بطاقة");
        radioCart.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioCart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioCart.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioCartActionPerformed(evt);
            }
        });

        radioEsp.setBackground(new java.awt.Color(51, 204, 0));
        radioEsp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-40.png"))); // NOI18N
        radioEsp.setText("نقدا");
        radioEsp.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioEsp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioEsp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioEsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEspActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 204));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("طريـقـة الـدفـع ");

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(textMontantVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panRound2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)))))
                .addGap(34, 34, 34))
        );
        panRound2Layout.setVerticalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textMontantVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        btn_save.setBackground(new java.awt.Color(22, 163, 74));
        btn_save.setForeground(new java.awt.Color(255, 255, 255));
        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-coche-emoji-48.png"))); // NOI18N
        btn_save.setText("تأكيد الدفـع");
        btn_save.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_annuler.setBackground(new java.awt.Color(239, 68, 68));
        btn_annuler.setForeground(new java.awt.Color(255, 255, 255));
        btn_annuler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-cross-mark-48 (1).png"))); // NOI18N
        btn_annuler.setText("الغاء");
        btn_annuler.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btn_annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_annulerActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تـــســجــيــل دفـــعــة وتــحــديــث رصيد المــؤسـســة");

        panRound7.setColor1(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 51));
        jLabel2.setText("الـمـبـلـغ الـمـتــبــقــي ");

        labCredit_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCredit_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCredit_avec_versment.setText("0.00");
        labCredit_avec_versment.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));

        labAug_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labAug_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAug_avec_versment.setText("0.00");
        labAug_avec_versment.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(102, 204, 0)));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 153, 0));
        jLabel16.setText("الـمبـلغ الـزائـد");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setText("دج");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setText("دج");

        javax.swing.GroupLayout panRound7Layout = new javax.swing.GroupLayout(panRound7);
        panRound7.setLayout(panRound7Layout);
        panRound7Layout.setHorizontalGroup(
            panRound7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAug_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addGap(46, 46, 46)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCredit_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18))
        );
        panRound7Layout.setVerticalGroup(
            panRound7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panRound7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel16)))
                    .addGroup(panRound7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labCredit_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labAug_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );

        comboboxRound1.setLabelText("إخــتــر الـشـركــة");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-organisation-35.png"))); // NOI18N

        panRound6.setColor1(new java.awt.Color(204, 204, 255));

        labCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCreditLsatVersemnt.setForeground(new java.awt.Color(255, 51, 51));
        labCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCreditLsatVersemnt.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("الديــون مــن اخـر عملية دفع ");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ouvrir-le-document-35.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setText("دج");

        javax.swing.GroupLayout panRound6Layout = new javax.swing.GroupLayout(panRound6);
        panRound6.setLayout(panRound6Layout);
        panRound6Layout.setHorizontalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(62, 62, 62))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        panRound6Layout.setVerticalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addGap(11, 11, 11))
            .addGroup(panRound6Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panRound5.setColor1(new java.awt.Color(204, 255, 204));

        labNonCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labNonCreditLsatVersemnt.setForeground(new java.awt.Color(0, 153, 0));
        labNonCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNonCreditLsatVersemnt.setText("0.00");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("   الفـائض من اخـر عملية دفع ");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ouvrir-le-document-35 (1).png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setText("دج");

        javax.swing.GroupLayout panRound5Layout = new javax.swing.GroupLayout(panRound5);
        panRound5.setLayout(panRound5Layout);
        panRound5Layout.setHorizontalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel14)
                        .addGap(0, 0, 0)
                        .addComponent(labNonCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panRound5Layout.setVerticalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(labNonCreditLsatVersemnt))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(pantab, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(comboboxRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addGap(13, 13, 13))
                            .addComponent(panRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(panRound5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(panRound6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panRound7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboboxRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panRound6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panRound5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRound7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pantab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void radioChequActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChequActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioChequActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed

        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.showMessagetoDialog("تـأكـيد الـدفـع", "هـل أنت متـأكـد مـن عـمـلـية الـدفـع");

        double montant = new Nomber().getNbDouble(textMontantVersement.getText());
        if (comboboxRound1.getSelectedIndex() != -1 && montant > 0) {
            String nomEntreprise = comboboxRound1.getSelectedItem().toString();
            Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
            if (messageDialog.getMessageType() == MessageDialog.MessageType.OK) {

                saveVersement(entreprise); // save versement+ Save ClientPayeeParEntreprise       
                this.homeForm.getPanVersement().setVersmentOnTab();
            }
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annulerActionPerformed
        this.dispose();

    }//GEN-LAST:event_btn_annulerActionPerformed

    private void radioCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioCartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioCartActionPerformed

    private void textMontantVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMontantVersementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMontantVersementActionPerformed

    private void radioEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioEspActionPerformed

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
            java.util.logging.Logger.getLogger(VersementForm21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VersementForm21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VersementForm21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VersementForm21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VersementForm21 dialog = new VersementForm21(new javax.swing.JFrame(), true);
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
    private material.design.buttonRounder btn_annuler;
    private material.design.buttonRounder btn_save;
    private material.design.ComboboxRoundNew comboboxRound1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel labAug_avec_versment;
    private javax.swing.JLabel labCreditAvanVersement;
    private javax.swing.JLabel labCreditLsatVersemnt;
    private javax.swing.JLabel labCredit_avec_versment;
    private javax.swing.JLabel labEntreprice;
    private javax.swing.JLabel labNonCreditLsatVersemnt;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound5;
    private ui.card.panRound panRound6;
    private ui.card.panRound panRound7;
    private ui.card.panRound pantab;
    private material.design.RadioButtonCustomAR radioCart;
    private material.design.RadioButtonCustomAR radioChequ;
    private material.design.RadioButtonCustomAR radioEsp;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private ui.card.TextFieldPrice textMontantVersement;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
