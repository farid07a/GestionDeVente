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

public class VersementForm extends javax.swing.JDialog {

    HomeForm homeForm;
    Connection connection;
    AchatDAOImpl achatDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;
    ValidationMessageDialog validationMessageDialog;
    Exite exite;
    MessageDialog messageDialog;

    public VersementForm(java.awt.Frame parent, boolean modal) {
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
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        exite = new Exite(this, homeForm);
        setEntrepriseInCom();

        comboboxRound1.setSelectedIndex(-1);

        textMontantVersement.addActionListener(e -> {
            calculeCreditApresVersement();
        });

        textMontantVersement.requestFocus();
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
        double montantAugm = new Nomber().getNbDouble(labAguLsatVersemnt.getText());

        double montantAndAug = montant + montantAugm;

        double lastCredit = new Nomber().getNbDouble(labCreditLsatVersemnt.getText());
        double Credit = new Nomber().getNbDouble(labTotalAchats.getText());
        double CreditNewAndLast = lastCredit + Credit;

        double restCridetApreVersement = CreditNewAndLast - montantAndAug; // +100 ent-->credi عند دين مؤسسة// -100 ---> بزيادة
        System.out.println("CreditNewAndLast " + CreditNewAndLast);
        System.out.println("montantAndAug " + montantAndAug);
        System.out.println("restCridetApreVersement" + restCridetApreVersement);
        if (restCridetApreVersement >= 0) {
            labCredit_avec_versment.setText(formatter.format(restCridetApreVersement));
            labAug_avec_versment.setText(formatter.format(00));

        } else {
            labAug_avec_versment.setText(formatter.format(-1 * restCridetApreVersement));
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

        labEntreprice.setText(entreprise.getNom_fr() + " ");

        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        // exist cridete / NO

        double restCreditLastVersemet = new serviceVersementEntreprise(connection)
                .GetLastRestVersementEntreprise(entreprise);

        if (restCreditLastVersemet >= 0) {
            labCreditLsatVersemnt.setText(formatter.format(restCreditLastVersemet));
        } else {
            labAguLsatVersemnt.setText(formatter.format(restCreditLastVersemet * -1));
        }

        List<Achat> achats = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
        double PrixTotalCredit = 0;
        double prixTotal = 0;
        for (Achat achat : achats) {

            int id = achat.getId();
            String matricul = achat.getClient().getMatricule();
            String nom = achat.getClient().getNom();
            String prenom = achat.getClient().getPrenom();

            prixTotal = achat.getPrix_total();
            PrixTotalCredit = PrixTotalCredit + prixTotal;

            LocalDate date = achat.getDate_achat();
            Entreprise entreprice = achat.getClient().getEntreprise();

            model.addRow(new Object[]{id, date,
                formatter.format(prixTotal),
                entreprice.getNom_ar(), prenom + " " + nom,
                matricul});
        }
        double prixVersementCreditAugm = 0;
        labTotalAchats.setText(formatter.format(PrixTotalCredit));
        // laste credit + creditAchat NO IN tab    AchatClientVers
        if (restCreditLastVersemet >= 0) {
            PrixTotalCredit = PrixTotalCredit + restCreditLastVersemet;
            labCreditAvanVersement.setText(formatter.format(PrixTotalCredit));
            labAguLsatVersemnt.setText(formatter.format(00));
            labTotal.setText(formatter.format(PrixTotalCredit));

        } else {
            labCreditAvanVersement.setText(formatter.format(PrixTotalCredit));
            labCreditLsatVersemnt.setText(formatter.format(00));
            labAguLsatVersemnt.setText(formatter.format(restCreditLastVersemet * -1));
            prixVersementCreditAugm = PrixTotalCredit + restCreditLastVersemet;
            labTotal.setText(formatter.format(prixVersementCreditAugm));

        }

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
        double montantAugm = new Nomber().getNbDouble(labAguLsatVersemnt.getText());

        double montantAndAug = montant + montantAugm;

        double lastCredit = new Nomber().getNbDouble(labCreditLsatVersemnt.getText());
        double Credit = new Nomber().getNbDouble(labTotalAchats.getText());
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
            this.dispose();

            validationMessageDialog.showMessage("تـأكـيد الـدفـع", "تـمـت  عـمــليــة الـدفـع بـنـجـاح ");
            this.homeForm.getPanVersement().setVersmentOnTab();

        } else {
            exite.showMessage("خــطـأ", "حـدث خــطـأ في عـمـلــيـة تـأكـيد عـملـيـة الـدفـع");
        }

        return versementEntreprise;

    }

    public void initItems() {
        labEntreprice.setText("");
        labTotalAchats.setText(0.00 + "");
        textMontantVersement.setText(0.00 + "");
        radioEsp.setSelected(true);
        labCredit_avec_versment.setText(0.00 + "");
        labAug_avec_versment.setText(0.00 + "");
        labCreditLsatVersemnt.setText(0.00 + "");
        labAguLsatVersemnt.setText(0.00 + "");
        labTotal.setText(0.00 + "");
        labCreditAvanVersement.setText(0.00 + "");

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
        panRound2 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        textMontantVersement = new ui.card.TextFieldPrice();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        pan_gradiant2 = new ui.card.pan_gradiant();
        radioCart = new material.design.RadioButtonCustomAR();
        pan_gradiant3 = new ui.card.pan_gradiant();
        radioChequ = new material.design.RadioButtonCustomAR();
        pan_gradiant4 = new ui.card.pan_gradiant();
        radioEsp = new material.design.RadioButtonCustomAR();
        btn_save = new material.design.buttonRounder();
        btn_annuler = new material.design.buttonRounder();
        jLabel1 = new javax.swing.JLabel();
        comboboxRound1 = new material.design.ComboboxRoundNew();
        panRound6 = new ui.card.panRound();
        labCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        panRound5 = new ui.card.panRound();
        labAguLsatVersemnt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panRound3 = new ui.card.panRound();
        jLabel19 = new javax.swing.JLabel();
        labTotalAchats = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        labEntreprice = new javax.swing.JLabel();
        panRound8 = new ui.card.panRound();
        jLabel21 = new javax.swing.JLabel();
        labTotal = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        panRound4 = new ui.card.panRound();
        jLabel5 = new javax.swing.JLabel();
        labCreditAvanVersement = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        pan_gradiant1 = new ui.card.pan_gradiant();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        labCredit_avec_versment = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        pan_gradiant5 = new ui.card.pan_gradiant();
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        labAug_avec_versment = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

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
                .addGap(19, 19, 19)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pantabLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pantabLayout.setVerticalGroup(
            pantabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pantabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        panRound2.setBackground(new java.awt.Color(253, 253, 253));
        panRound2.setColor1(new java.awt.Color(246, 246, 246));
        panRound2.setMaximumSize(new java.awt.Dimension(493, 269));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("الـمـبـلــغ الـمـدفــوع ");
        jLabel7.setToolTipText("");

        textMontantVersement.setForeground(new java.awt.Color(0, 0, 0));
        textMontantVersement.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textMontantVersement.setText("00.00");
        textMontantVersement.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        textMontantVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMontantVersementActionPerformed(evt);
            }
        });
        textMontantVersement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textMontantVersementKeyReleased(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-portefeuille-35.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 204));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("طــريـقــة الــدفــع ");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        jLabel26.setText("دج");

        pan_gradiant2.setColor1(new java.awt.Color(189, 189, 255));
        pan_gradiant2.setColor2(new java.awt.Color(189, 189, 255));

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

        javax.swing.GroupLayout pan_gradiant2Layout = new javax.swing.GroupLayout(pan_gradiant2);
        pan_gradiant2.setLayout(pan_gradiant2Layout);
        pan_gradiant2Layout.setHorizontalGroup(
            pan_gradiant2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        pan_gradiant2Layout.setVerticalGroup(
            pan_gradiant2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_gradiant3.setColor1(new java.awt.Color(255, 235, 204));
        pan_gradiant3.setColor2(new java.awt.Color(255, 235, 204));

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

        javax.swing.GroupLayout pan_gradiant3Layout = new javax.swing.GroupLayout(pan_gradiant3);
        pan_gradiant3.setLayout(pan_gradiant3Layout);
        pan_gradiant3Layout.setHorizontalGroup(
            pan_gradiant3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_gradiant3Layout.setVerticalGroup(
            pan_gradiant3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_gradiant4.setColor1(new java.awt.Color(193, 255, 193));
        pan_gradiant4.setColor2(new java.awt.Color(193, 255, 193));

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

        javax.swing.GroupLayout pan_gradiant4Layout = new javax.swing.GroupLayout(pan_gradiant4);
        pan_gradiant4.setLayout(pan_gradiant4Layout);
        pan_gradiant4Layout.setHorizontalGroup(
            pan_gradiant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant4Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_gradiant4Layout.setVerticalGroup(
            pan_gradiant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(pan_gradiant3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(pan_gradiant2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(pan_gradiant4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(61, 61, 61))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound2Layout.createSequentialGroup()
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panRound2Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel26)
                        .addGap(18, 18, 18)
                        .addComponent(textMontantVersement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(49, 49, 49))
        );
        panRound2Layout.setVerticalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound2Layout.createSequentialGroup()
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(9, 9, 9)
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textMontantVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5)
                .addComponent(jLabel8)
                .addGap(3, 3, 3)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pan_gradiant3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pan_gradiant4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_gradiant2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
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

        jLabel1.setBackground(new java.awt.Color(58, 58, 173));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تـــســجــيــل دفـــعــة وتــحــديــث رصيد المــؤسـســة");
        jLabel1.setOpaque(true);

        comboboxRound1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxRound1ActionPerformed(evt);
            }
        });

        panRound6.setBackground(new java.awt.Color(255, 255, 255));
        panRound6.setColor1(new java.awt.Color(243, 243, 243));

        labCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labCreditLsatVersemnt.setForeground(new java.awt.Color(255, 0, 0));
        labCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCreditLsatVersemnt.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("الديــون اخـر عملية دفع ");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("دج");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout panRound6Layout = new javax.swing.GroupLayout(panRound6);
        panRound6.setLayout(panRound6Layout);
        panRound6Layout.setHorizontalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound6Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panRound6Layout.setVerticalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(5, 5, 5)
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labCreditLsatVersemnt)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel23))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panRound5.setColor1(new java.awt.Color(243, 243, 243));

        labAguLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labAguLsatVersemnt.setForeground(new java.awt.Color(0, 153, 0));
        labAguLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAguLsatVersemnt.setText("1000000000");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("   الفـائض من عملية دفع ");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ouvrir-le-document-35 (1).png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 153, 0));
        jLabel14.setText("دج");

        javax.swing.GroupLayout panRound5Layout = new javax.swing.GroupLayout(panRound5);
        panRound5.setLayout(panRound5Layout);
        panRound5Layout.setHorizontalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labAguLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        panRound5Layout.setVerticalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(27, 27, 27))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labAguLsatVersemnt)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panRound3.setColor1(new java.awt.Color(243, 243, 243));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 161, 20));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("ثـمن المشـتريــات");

        labTotalAchats.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labTotalAchats.setForeground(new java.awt.Color(255, 161, 20));
        labTotalAchats.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTotalAchats.setText("0.00");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 161, 20));
        jLabel13.setText("دج");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-30.png"))); // NOI18N

        javax.swing.GroupLayout panRound3Layout = new javax.swing.GroupLayout(panRound3);
        panRound3.setLayout(panRound3Layout);
        panRound3Layout.setHorizontalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labTotalAchats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel19)
                        .addContainerGap(23, Short.MAX_VALUE))))
        );
        panRound3Layout.setVerticalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labTotalAchats, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labEntreprice.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labEntreprice.setForeground(new java.awt.Color(0, 0, 102));
        labEntreprice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labEntreprice.setText("Entreprise");

        panRound8.setBackground(new java.awt.Color(0, 51, 102));
        panRound8.setForeground(new java.awt.Color(255, 255, 255));
        panRound8.setColor1(new java.awt.Color(242, 242, 242));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(69, 69, 69));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("المبلغ المستحق دفعه  :");

        labTotal.setBackground(new java.awt.Color(255, 255, 255));
        labTotal.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        labTotal.setForeground(new java.awt.Color(0, 51, 204));
        labTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTotal.setText("00.00");

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 204));
        jLabel22.setText("دج");

        javax.swing.GroupLayout panRound8Layout = new javax.swing.GroupLayout(panRound8);
        panRound8.setLayout(panRound8Layout);
        panRound8Layout.setHorizontalGroup(
            panRound8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound8Layout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        panRound8Layout.setVerticalGroup(
            panRound8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(labTotal)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panRound4.setColor1(new java.awt.Color(243, 243, 243));
        panRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 204));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("المشتريات + الديون ");
        panRound4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 30));

        labCreditAvanVersement.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labCreditAvanVersement.setForeground(new java.awt.Color(0, 0, 204));
        labCreditAvanVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCreditAvanVersement.setText("0.00");
        panRound4.add(labCreditAvanVersement, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, 20));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 204));
        jLabel20.setText("دج");
        panRound4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 20));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("إخـــتـــر الــشــــركـــــــة");

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 0, 51));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("*");

        pan_gradiant1.setBackground(new java.awt.Color(255, 246, 246));
        pan_gradiant1.setColor1(new java.awt.Color(255, 241, 241));
        pan_gradiant1.setColor2(new java.awt.Color(253, 232, 232));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("الـمـبـلـغ الـمـتــبــقــي  :");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setText("دج");

        labCredit_avec_versment.setBackground(new java.awt.Color(255, 229, 229));
        labCredit_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCredit_avec_versment.setForeground(new java.awt.Color(255, 0, 51));
        labCredit_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCredit_avec_versment.setText("0.00");

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout pan_gradiant1Layout = new javax.swing.GroupLayout(pan_gradiant1);
        pan_gradiant1.setLayout(pan_gradiant1Layout);
        pan_gradiant1Layout.setHorizontalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCredit_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(jLabel28)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        pan_gradiant1Layout.setVerticalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labCredit_avec_versment)
                        .addComponent(jLabel17))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pan_gradiant5.setColor1(new java.awt.Color(231, 246, 216));
        pan_gradiant5.setColor2(new java.awt.Color(231, 246, 216));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ajouter-29.png"))); // NOI18N

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 153, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("الـمبـلغ الـزائـد :");

        labAug_avec_versment.setBackground(new java.awt.Color(226, 250, 202));
        labAug_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labAug_avec_versment.setForeground(new java.awt.Color(51, 153, 0));
        labAug_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAug_avec_versment.setText("0.00");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 153, 0));
        jLabel18.setText("دج");

        javax.swing.GroupLayout pan_gradiant5Layout = new javax.swing.GroupLayout(pan_gradiant5);
        pan_gradiant5.setLayout(pan_gradiant5Layout);
        pan_gradiant5Layout.setHorizontalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAug_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel11)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pan_gradiant5Layout.setVerticalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_gradiant5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(labAug_avec_versment))))
                    .addComponent(jLabel11))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pan_gradiant1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))
                            .addComponent(panRound2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(panRound5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(panRound6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(panRound8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labEntreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(comboboxRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(250, 250, 250)
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(pantab, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(comboboxRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labEntreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panRound5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panRound6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(panRound8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(panRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pan_gradiant1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pantab, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboboxRound1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxRound1ActionPerformed
        initItems();
        if (comboboxRound1.getSelectedIndex() != -1) {
            String nomEntreprise = comboboxRound1.getSelectedItem().toString();
            Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
            setAchatNotVersementInTablByEntreprise(entreprise);

        } else {
            initItems();
        }
    }//GEN-LAST:event_comboboxRound1ActionPerformed

    private void btn_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annulerActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_annulerActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        
        messageDialog.ShowConfirmMessageInDialog("تـأكـيد الـدفـع", "هـل أنت متـأكـد مـن عـمـلـية الـدفـع");
        if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
            
            double montant = new Nomber().getNbDouble(textMontantVersement.getText());
            if (comboboxRound1.getSelectedIndex() != -1 && montant > 0) {
                String nomEntreprise = comboboxRound1.getSelectedItem().toString();
                Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
                saveVersement(entreprise); // save versement+ Save ClientPayeeParEntreprise      
            }
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void radioEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioEspActionPerformed

    private void radioCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioCartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioCartActionPerformed

    private void radioChequActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChequActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioChequActionPerformed

    private void textMontantVersementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMontantVersementKeyReleased

        calculeCreditApresVersement();
    }//GEN-LAST:event_textMontantVersementKeyReleased

    private void textMontantVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMontantVersementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMontantVersementActionPerformed

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
            java.util.logging.Logger.getLogger(VersementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VersementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VersementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VersementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VersementForm dialog = new VersementForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labAguLsatVersemnt;
    private javax.swing.JLabel labAug_avec_versment;
    private javax.swing.JLabel labCreditAvanVersement;
    private javax.swing.JLabel labCreditLsatVersemnt;
    private javax.swing.JLabel labCredit_avec_versment;
    private javax.swing.JLabel labEntreprice;
    private javax.swing.JLabel labTotal;
    private javax.swing.JLabel labTotalAchats;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound3;
    private ui.card.panRound panRound4;
    private ui.card.panRound panRound5;
    private ui.card.panRound panRound6;
    private ui.card.panRound panRound8;
    private ui.card.pan_gradiant pan_gradiant1;
    private ui.card.pan_gradiant pan_gradiant2;
    private ui.card.pan_gradiant pan_gradiant3;
    private ui.card.pan_gradiant pan_gradiant4;
    private ui.card.pan_gradiant pan_gradiant5;
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
