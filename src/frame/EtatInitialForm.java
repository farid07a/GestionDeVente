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
import dao.impl.CategorieDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.ProduitDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.AchatDetail;
import entity.Categorie;
import entity.Client;
import entity.ClientPayeParEntreprise;
import entity.Entreprise;
import entity.Nomber;
import entity.Produit;
import entity.VersementEntreprise;
import enums.ModePaiement;
import home.HomeForm;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class EtatInitialForm extends javax.swing.JDialog {

    Connection connection;
    EntrepriseDAOImpl entrepriseDAOImpl;
    HomeForm homeForm;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    Exite exite;
    Nomber Nomber = new Nomber();
    ClientDAOImpl clientDAOImpl;
    AchatDAOImpl achatDAOImpl;
    ProduitDAOImpl produitDAOImpl;
    AchatDetailDAOImpl achatDetailDAOImpl;
    CategorieDAOImpl categorieDAOImpl;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    MessageDialog messageDialog;

    public EtatInitialForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        initComponents();
        ButtonGroup group = new ButtonGroup();
        group.add(radioEsp);
        group.add(radioCart);
        group.add(radioChequ);
        radioChequ.setSelected(true);

        ButtonGroup groupDate = new ButtonGroup();
        groupDate.add(radioDate);
        groupDate.add(radioMonthYear);
        radioMonthYear.setSelected(true);

        setLocationRelativeTo(this.homeForm);

        connection = DatabaseConnection.getInstance().getConnection();
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        clientDAOImpl = new ClientDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        categorieDAOImpl = new CategorieDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        messageDialog = new MessageDialog(this);

        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);

        exite = new Exite(this, homeForm);

        setEntrepriseInCom();
        initcomBoxYear();
        combEntreprise.setSelectedIndex(-1);
        cleanItems();
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

    public void setEntrepriseInCom() {
        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();
        combEntreprise.removeAllItems();
        for (Entreprise entreprise : entreprises) {
            combEntreprise.addItem(entreprise.getNom_ar());
        }

    }

    public Client getClientEntreprise(Entreprise entreprise) {
        Client client = clientDAOImpl.getClientByNameAndEntrepriseForInit(entreprise, "Clients");
        if (client == null) {
            client = new Client(0, "Clients", "", "/", "", "", entreprise);
            clientDAOImpl.save(client);
            client = clientDAOImpl.findLast();
        }
        return client;
    }

    public Produit getProduitEntreprise(String nomproduit, String marque) {
        Produit produit = produitDAOImpl.getProduitForInit(nomproduit, marque);
        if (produit == null) {

            Categorie categorie = categorieDAOImpl.getCategorierParName("/");
            if (categorie == null) {
                categorie = new Categorie(0, "/", "/");
                categorieDAOImpl.save(categorie);
                categorie = categorieDAOImpl.findLast();
            }
            produit = new Produit(0, "", nomproduit, categorie.getNomCategorie(), categorie, 0, 0, 0);
            produitDAOImpl.save(produit);
            produit = produitDAOImpl.findLast();
        }
        return produit;
    }

    public void initcomBoxYear() {

        int currentYear
                = java.time.Year.now().getValue();

        int startYear = 2020;

        for (int y = currentYear;
                y >= startYear;
                y--) {

            cbYear.addItem(
                    String.valueOf(y)
            );
        }

        cbYear.setSelectedItem(LocalDate.now());

    }

    public void calculeAchatAndCredit() {

        double prixAchat = Nomber.getNbDouble((txtPrixAchat.getText().isEmpty())?"0.00":txtPrixAchat.getText());
        double CreditLsatVersemnt = Nomber.getNbDouble(labCreditLsatVersemnt.getText());
        double prixAchatAndCredit = prixAchat + CreditLsatVersemnt;
        labAchatCredit.setText(formatter.format(prixAchatAndCredit));
    }

    public void calculeNewCredit() {
        double MontantVersement = Nomber.getNbDouble(textMontantVersement.getText());
        double prixAchatAndCredit = Nomber.getNbDouble(labAchatCredit.getText());
        double newcredit = prixAchatAndCredit - MontantVersement;
        labNewCredit.setText(newcredit >= 0 ? formatter.format(newcredit) : "00.00");
        labAug_avec_versment.setText(newcredit < 0 ? formatter.format(newcredit * -1) : "00.00");
    }

    public void cleanItems() {
        labCreditLsatVersemnt.setText("0.00");
        txtPrixAchat.setText("0.00");
        labAchatCredit.setText("0.00");
        textMontantVersement.setText("0.00");
        labNewCredit.setText("0.00");
        labAug_avec_versment.setText("0.00");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new datechooser.DateChooser();
        dateChooser2 = new datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_save = new material.design.buttonRounder();
        btn_annuler = new material.design.buttonRounder();
        combEntreprise = new material.design.ComboboxRoundNew();
        jLabel4 = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        textMontantVersement = new ui.card.TextFieldPrice();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPrixAchat = new ui.card.TextFieldPrice();
        jLabel19 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pan_gradiant1 = new ui.card.pan_gradiant();
        labNewCredit = new javax.swing.JLabel();
        pan_gradiant8 = new ui.card.pan_gradiant();
        labAchatCredit = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        panRound23 = new ui.card.panRound2();
        jLabel10 = new javax.swing.JLabel();
        labCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        pan_gradiant7 = new ui.card.pan_gradiant();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        labAug_avec_versment = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        panRound22 = new ui.card.panRound2();
        jLabel8 = new javax.swing.JLabel();
        pan_gradiant6 = new ui.card.pan_gradiant();
        radioMonthYear = new material.design.RadioButtonCustomAR();
        cbYear = new material.design.ComboboxRoundNew();
        cbMonth = new material.design.ComboboxRoundNew();
        pan_gradiant5 = new ui.card.pan_gradiant();
        radioDate = new material.design.RadioButtonCustomAR();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDateDeb = new material.design.TextField();
        txtDateFin = new material.design.TextField();
        jLabel13 = new javax.swing.JLabel();
        pan_radioChequ = new ui.card.pan_gradiant();
        radioChequ = new material.design.RadioButtonCustomAR();
        pan_radioCart = new ui.card.pan_gradiant();
        radioCart = new material.design.RadioButtonCustomAR();
        pan_radioEsp = new ui.card.pan_gradiant();
        radioEsp = new material.design.RadioButtonCustomAR();
        txtNumCheque = new material.design.TextField();
        chekLastVersement = new material.design.JCheckBoxCustomAR();
        jLabel2 = new javax.swing.JLabel();

        dateChooser1.setForeground(new java.awt.Color(51, 153, 255));
        dateChooser1.setTextRefernce(txtDateDeb);

        dateChooser2.setForeground(new java.awt.Color(51, 153, 255));
        dateChooser2.setTextRefernce(txtDateFin);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(58, 58, 173));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("دفــعــة قــديـمــــة");
        jLabel1.setOpaque(true);

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

        combEntreprise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combEntrepriseActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setText("الــشـركــات ");

        panRound2.setColor1(new java.awt.Color(255, 255, 255));
        panRound2.setMaximumSize(new java.awt.Dimension(493, 269));
        panRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("المـبـيـعـات +الـديـون ");
        jLabel7.setToolTipText("");
        panRound2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 100, -1, 25));

        textMontantVersement.setBackground(new java.awt.Color(235, 235, 255));
        textMontantVersement.setForeground(new java.awt.Color(0, 0, 0));
        textMontantVersement.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textMontantVersement.setText("0.00");
        textMontantVersement.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        textMontantVersement.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textMontantVersementFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textMontantVersementFocusLost(evt);
            }
        });
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
        panRound2.add(textMontantVersement, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, 447, 80));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-portefeuille-35.png"))); // NOI18N
        panRound2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 190, -1, 25));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("دج");
        panRound2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 20, 60));

        txtPrixAchat.setBackground(new java.awt.Color(235, 235, 255));
        txtPrixAchat.setForeground(new java.awt.Color(0, 0, 0));
        txtPrixAchat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrixAchat.setText("0.00");
        txtPrixAchat.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        txtPrixAchat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrixAchatFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrixAchatFocusLost(evt);
            }
        });
        txtPrixAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrixAchatActionPerformed(evt);
            }
        });
        txtPrixAchat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrixAchatKeyReleased(evt);
            }
        });
        panRound2.add(txtPrixAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 440, 60));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 204));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("الـمـبـلغ الـكـلـي للمـبـيـعـات");
        panRound2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 30, -1, 22));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("دج");
        panRound2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 20, 50));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 204));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("الـمـبـلغ الـمدفوع ");
        jLabel14.setToolTipText("");
        panRound2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 170, -1, 25));

        pan_gradiant1.setBackground(new java.awt.Color(255, 246, 246));
        pan_gradiant1.setColor1(new java.awt.Color(255, 241, 241));
        pan_gradiant1.setColor2(new java.awt.Color(253, 232, 232));

        labNewCredit.setBackground(new java.awt.Color(255, 229, 229));
        labNewCredit.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labNewCredit.setForeground(new java.awt.Color(255, 0, 51));
        labNewCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNewCredit.setText("0.00");

        javax.swing.GroupLayout pan_gradiant1Layout = new javax.swing.GroupLayout(pan_gradiant1);
        pan_gradiant1.setLayout(pan_gradiant1Layout);
        pan_gradiant1Layout.setHorizontalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(labNewCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        pan_gradiant1Layout.setVerticalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labNewCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panRound2.add(pan_gradiant1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, 450, 50));

        pan_gradiant8.setColor1(new java.awt.Color(221, 221, 255));
        pan_gradiant8.setColor2(new java.awt.Color(221, 221, 255));

        labAchatCredit.setBackground(new java.awt.Color(226, 250, 202));
        labAchatCredit.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labAchatCredit.setForeground(new java.awt.Color(0, 102, 204));
        labAchatCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAchatCredit.setText("0.00");

        javax.swing.GroupLayout pan_gradiant8Layout = new javax.swing.GroupLayout(pan_gradiant8);
        pan_gradiant8.setLayout(pan_gradiant8Layout);
        pan_gradiant8Layout.setHorizontalGroup(
            pan_gradiant8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labAchatCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan_gradiant8Layout.setVerticalGroup(
            pan_gradiant8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labAchatCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panRound2.add(pan_gradiant8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 440, 50));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 204));
        jLabel24.setText("دج");
        panRound2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, -1, 40));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("ديــون المتبقية ");
        panRound2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 260, 100, 38));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 0, 51));
        jLabel18.setText("دج");
        panRound2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 260, -1, 38));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N
        panRound2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 250, -1, 44));

        panRound23.setColor1(new java.awt.Color(255, 238, 238));
        panRound23.setColor2(new java.awt.Color(255, 238, 238));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("الديـون السـابـقـة ");

        labCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labCreditLsatVersemnt.setForeground(new java.awt.Color(255, 0, 0));
        labCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCreditLsatVersemnt.setText("0.00");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setText("دج");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout panRound23Layout = new javax.swing.GroupLayout(panRound23);
        panRound23.setLayout(panRound23Layout);
        panRound23Layout.setHorizontalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
            .addGroup(panRound23Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
        panRound23Layout.setVerticalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound23Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labCreditLsatVersemnt)
                            .addComponent(jLabel15)))
                    .addComponent(jLabel23))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panRound2.add(panRound23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 370, 60));

        pan_gradiant7.setColor1(new java.awt.Color(231, 246, 216));
        pan_gradiant7.setColor2(new java.awt.Color(231, 246, 216));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ajouter-29.png"))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 153, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("الـمبـلغ الـزائـد :");

        labAug_avec_versment.setBackground(new java.awt.Color(226, 250, 202));
        labAug_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labAug_avec_versment.setForeground(new java.awt.Color(51, 153, 0));
        labAug_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAug_avec_versment.setText("0.00");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 153, 0));
        jLabel21.setText("دج");

        javax.swing.GroupLayout pan_gradiant7Layout = new javax.swing.GroupLayout(pan_gradiant7);
        pan_gradiant7.setLayout(pan_gradiant7Layout);
        pan_gradiant7Layout.setHorizontalGroup(
            pan_gradiant7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant7Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAug_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_gradiant7Layout.setVerticalGroup(
            pan_gradiant7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant7Layout.createSequentialGroup()
                .addGroup(pan_gradiant7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_gradiant7Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pan_gradiant7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan_gradiant7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(labAug_avec_versment))))
                    .addComponent(jLabel16))
                .addGap(14, 14, 14))
        );

        panRound2.add(pan_gradiant7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 350, -1));

        panRound22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 204));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("تاريخ الدفعة");
        panRound22.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 90, -1, -1));

        pan_gradiant6.setColor1(new java.awt.Color(240, 240, 240));
        pan_gradiant6.setColor2(new java.awt.Color(240, 240, 240));

        radioMonthYear.setBackground(new java.awt.Color(51, 204, 0));
        radioMonthYear.setText("حسب الشهر والسنة");
        radioMonthYear.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioMonthYear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioMonthYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioMonthYearActionPerformed(evt);
            }
        });

        cbYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbYearActionPerformed(evt);
            }
        });

        cbMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان", "جويلية", "أوت", "سبتمبر", "أكتوبر", " نوفمبر", "ديسمبر" }));
        cbMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_gradiant6Layout = new javax.swing.GroupLayout(pan_gradiant6);
        pan_gradiant6.setLayout(pan_gradiant6Layout);
        pan_gradiant6Layout.setHorizontalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant6Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioMonthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan_gradiant6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        pan_gradiant6Layout.setVerticalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioMonthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panRound22.add(pan_gradiant6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 70, 350, 98));

        pan_gradiant5.setColor1(new java.awt.Color(240, 240, 240));
        pan_gradiant5.setColor2(new java.awt.Color(240, 240, 240));

        radioDate.setBackground(new java.awt.Color(51, 204, 0));
        radioDate.setText("حسب فترة زمنية");
        radioDate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDateActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setText("من:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setText("الى :");

        txtDateDeb.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDateDeb.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtDateDeb.setLabelText("");
        txtDateDeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateDebActionPerformed(evt);
            }
        });

        txtDateFin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDateFin.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtDateFin.setLabelText("");
        txtDateFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateFinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_gradiant5Layout = new javax.swing.GroupLayout(pan_gradiant5);
        pan_gradiant5.setLayout(pan_gradiant5Layout);
        pan_gradiant5Layout.setHorizontalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan_gradiant5Layout.createSequentialGroup()
                        .addGap(0, 160, Short.MAX_VALUE)
                        .addComponent(radioDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_gradiant5Layout.createSequentialGroup()
                        .addComponent(txtDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateDeb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );
        pan_gradiant5Layout.setVerticalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDateDeb, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound22.add(pan_gradiant5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 320, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 204));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("طريـقـة الـدفـع ");
        panRound22.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, -1, -1));

        pan_radioChequ.setColor1(new java.awt.Color(255, 102, 0));
        pan_radioChequ.setColor2(new java.awt.Color(255, 102, 0));
        pan_radioChequ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pan_radioChequMouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout pan_radioChequLayout = new javax.swing.GroupLayout(pan_radioChequ);
        pan_radioChequ.setLayout(pan_radioChequLayout);
        pan_radioChequLayout.setHorizontalGroup(
            pan_radioChequLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioChequ, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
        pan_radioChequLayout.setVerticalGroup(
            pan_radioChequLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioChequ, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        panRound22.add(pan_radioChequ, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 140, -1));

        pan_radioCart.setColor1(new java.awt.Color(189, 189, 255));
        pan_radioCart.setColor2(new java.awt.Color(189, 189, 255));
        pan_radioCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pan_radioCartMouseClicked(evt);
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

        javax.swing.GroupLayout pan_radioCartLayout = new javax.swing.GroupLayout(pan_radioCart);
        pan_radioCart.setLayout(pan_radioCartLayout);
        pan_radioCartLayout.setHorizontalGroup(
            pan_radioCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioCart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
        );
        pan_radioCartLayout.setVerticalGroup(
            pan_radioCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioCart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, Short.MAX_VALUE)
        );

        panRound22.add(pan_radioCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, -1));

        pan_radioEsp.setColor1(new java.awt.Color(193, 255, 193));
        pan_radioEsp.setColor2(new java.awt.Color(193, 255, 193));
        pan_radioEsp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pan_radioEspMouseClicked(evt);
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

        javax.swing.GroupLayout pan_radioEspLayout = new javax.swing.GroupLayout(pan_radioEsp);
        pan_radioEsp.setLayout(pan_radioEspLayout);
        pan_radioEspLayout.setHorizontalGroup(
            pan_radioEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioEsp, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
        );
        pan_radioEspLayout.setVerticalGroup(
            pan_radioEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, Short.MAX_VALUE)
        );

        panRound22.add(pan_radioEsp, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, -1, -1));

        txtNumCheque.setForeground(new java.awt.Color(0, 0, 102));
        txtNumCheque.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNumCheque.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtNumCheque.setLabelText("رقم الحساب البريدي");
        txtNumCheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumChequeActionPerformed(evt);
            }
        });
        txtNumCheque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumChequeKeyTyped(evt);
            }
        });
        panRound22.add(txtNumCheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 50));

        chekLastVersement.setBackground(new java.awt.Color(51, 204, 0));
        chekLastVersement.setText("آخر دفـعـة قديمة لشركة");
        chekLastVersement.setActionCommand("أخر دفعة استـثـنـائيــة");
        chekLastVersement.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        chekLastVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chekLastVersementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(385, 385, 385)
                .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chekLastVersement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addComponent(combEntreprise, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panRound22, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panRound2, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combEntreprise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chekLastVersement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(panRound2, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRound22, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textMontantVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMontantVersementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMontantVersementActionPerformed

    private void radioChequActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChequActionPerformed
        if (radioChequ.isSelected()) {
            txtNumCheque.requestFocusInWindow();
        }
    }//GEN-LAST:event_radioChequActionPerformed

    private void radioCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioCartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioCartActionPerformed

    private void radioEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioEspActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        double montant = new Nomber().getNbDouble(textMontantVersement.getText());
        if (combEntreprise.getSelectedIndex() == -1 && montant <= 0) {
            exite.showMessageDialog("تـنـبـيه", "الـرجـاء إدخـال مـبـلـغ الـدفـعـة و إخـتـيـار الـشركـة");
            return;
        }
        if (combEntreprise.getSelectedIndex() == -1) {
            exite.showMessageDialog("تـنـبـيه", "الـرجـاء  إخـتـيـار الـشركـة");
            return;
        }
        if (montant <= 0) {
            exite.showMessageDialog("تـنـبـيه", "الـرجـاء إدخـال مـبـلـغ الـدفـعـة ");
            return;
        }
        if (radioChequ.isSelected() && txtNumCheque.getText().isEmpty()) {
            exite.showMessageDialog("تـنـبـيه", "الرجاء إدخال رقم الصـك البريدي");
            return;
        }

        String nomEntreprise = combEntreprise.getSelectedItem().toString();
        Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
        double Credit = new Nomber().getNbDouble(labNewCredit.getText());
        double prixAchat = Nomber.getNbDouble(txtPrixAchat.getText());
        ModePaiement mode_paiement = getModePaiement();
        System.out.println(" cbMonth.getSelectedIndex() + 1 " + cbMonth.getSelectedIndex() + 1);
        String month = ((cbMonth.getSelectedIndex() + 1) < 10) ? "0" + (cbMonth.getSelectedIndex() + 1) : (cbMonth.getSelectedIndex() + 1) + "";
        String remarq = radioMonthYear.isSelected()
                ? cbYear.getSelectedItem().toString() + "-" + month + "-01"
                : txtDateDeb.getText() + " " + txtDateFin.getText();

        String numCheque = txtNumCheque.getText();

        LocalDate dateVersement = radioMonthYear.isSelected() ? LocalDate.parse(remarq)
                : LocalDate.parse(txtDateDeb.getText());
        VersementEntreprise versementEntreprise
                = new VersementEntreprise(0, entreprise, montant, dateVersement, mode_paiement.name(),
                        remarq, prixAchat, Credit, numCheque);

        System.out.println(remarq);

        VersementEntreprise lasteVersement = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);
        
        if (lasteVersement != null  && lasteVersement.getDate_versement().isAfter(dateVersement)) {

            exite.showMessageDialog(
                    "تنبيه: تاريخ غير صحيح",
                    "تاريخ الدفعة غير مقبول!\n"
                    + "يجب أن يكون تاريخ الدفعة الحالية بعد تاريخ الدفعة السابقة.\n"
                    + "تاريخ الدفعة السابقة: "
                    + lasteVersement.getDate_versement()
            );
            return;
        }

        String etat = "non";
        if (chekLastVersement.isSelected()) {
            messageDialog.showMessagetoDialog("أخر دفعة قديمة", "هل أنت متأكد أن هذه أخر دفعة قديمة لشركة\n" + "لأنه لا يمكنك إدخال دفعات قديمة بعد الحفظ");
            if (messageDialog.getMessageType() != MessageDialog.MessageType.YES) {
                exite.showMessageDialog("تنبيه", "قم بإلغاء تحديد أخر دفعة قديمة");
                return;
            }
            etat = "oui";
        }

        messageDialog.showMessagetoDialog("حــفـظ", "هـل أنت متـأكـد مـن عـمـلـيـة الحـفـظ");

        if (messageDialog.getMessageType() != MessageDialog.MessageType.YES) {
            return;
        }

        if (versementEntrepriseDAOImpl.save(versementEntreprise) > 0) {
            versementEntreprise = versementEntrepriseDAOImpl.findLast();
            versementEntrepriseDAOImpl.setEtat(versementEntreprise, etat);

            Client client = getClientEntreprise(entreprise);

            Achat achat = new Achat(0, client, prixAchat, dateVersement);

            achatDAOImpl.save(achat);

            achat = achatDAOImpl.getLast();

            Produit produit = getProduitEntreprise("Produits", "/");

            AchatDetail achatDetail = new AchatDetail(0, achat, produit, 1, prixAchat, prixAchat);

            achatDetailDAOImpl.save(achatDetail);
            ClientPayeParEntreprise clientPayeParEntreprise = new ClientPayeParEntreprise(0, versementEntreprise, achat);
            clientPayeParEntrepriseDAOImpl.save(clientPayeParEntreprise);
            this.dispose();
            new ValidationMessageDialog(this, homeForm).showMessage("تأكيد", "تـم الحـفـظ بـنـجـاح");
            // this.homeForm.getPanVersement().setVersmentOnTab();
            this.homeForm.getpanVersementSpecialAchat().setVersementSpecialOnTab();
        }

    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annulerActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_annulerActionPerformed

    private void txtDateDebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateDebActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_txtDateDebActionPerformed

    private void txtDateFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateFinActionPerformed
        dateChooser2.showPopup();
    }//GEN-LAST:event_txtDateFinActionPerformed

    private void pan_radioEspMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pan_radioEspMouseClicked
        if (radioEsp.isSelected()) {
            radioEsp.setSelected(false);
        } else {
            radioEsp.setSelected(true);

        }
    }//GEN-LAST:event_pan_radioEspMouseClicked

    private void pan_radioCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pan_radioCartMouseClicked
        if (radioCart.isSelected()) {
            radioCart.setSelected(false);
        } else {
            radioCart.setSelected(true);

        }
    }//GEN-LAST:event_pan_radioCartMouseClicked

    private void pan_radioChequMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pan_radioChequMouseClicked
        if (radioEsp.isSelected()) {
            radioEsp.setSelected(false);
            txtNumCheque.requestFocus();
        } else {
            radioEsp.setSelected(true);

        }
    }//GEN-LAST:event_pan_radioChequMouseClicked

    private void txtNumChequeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumChequeKeyTyped
//       if (!Character.isDigit(evt.getKeyChar())) {
//        evt.consume();
//    }
    }//GEN-LAST:event_txtNumChequeKeyTyped

    private void txtNumChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumChequeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumChequeActionPerformed

    private void textMontantVersementFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textMontantVersementFocusGained
        if (textMontantVersement.getText().equals("00.00")
                || textMontantVersement.getText().equals("00")
                || textMontantVersement.getText().equals("0.00")
                || textMontantVersement.getText().equals("0")) {
            textMontantVersement.setText("");
        }
    }//GEN-LAST:event_textMontantVersementFocusGained

    private void radioDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioDateActionPerformed

    private void radioMonthYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioMonthYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioMonthYearActionPerformed

    private void cbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbYearActionPerformed

    private void cbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMonthActionPerformed

    private void txtPrixAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrixAchatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrixAchatActionPerformed

    private void txtPrixAchatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrixAchatFocusGained
        if (txtPrixAchat.getText().equals("00.00")
                || txtPrixAchat.getText().equals("00")
                || txtPrixAchat.getText().equals("0")
                || txtPrixAchat.getText().equals("0.00")) {
            txtPrixAchat.setText("");
        }
    }//GEN-LAST:event_txtPrixAchatFocusGained

    private void txtPrixAchatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrixAchatKeyReleased
//        if (txtPrixAchat.getText().isEmpty()) {
//            return;
//        }
        calculeAchatAndCredit();
        calculeNewCredit();
    }//GEN-LAST:event_txtPrixAchatKeyReleased

    private void textMontantVersementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMontantVersementKeyReleased
        if (txtPrixAchat.getText().isEmpty() || textMontantVersement.getText().isEmpty()) {
            return;
        }
        calculeAchatAndCredit();
        calculeNewCredit();
    }//GEN-LAST:event_textMontantVersementKeyReleased

    private void combEntrepriseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combEntrepriseActionPerformed
        if (combEntreprise.getSelectedIndex() != -1) {
            cleanItems();
            String nomEntreprise = combEntreprise.getSelectedItem().toString();
            Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
            // VersementEntreprise versement = versementEntrepriseDAOImpl.getLastVersementSpeciale(entreprise);
            VersementEntreprise versement = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);
            if (((versement != null && "oui".equals(versementEntrepriseDAOImpl.getEtat(versement)))
                    || (versement != null && versementEntrepriseDAOImpl.getEtat(versement) == null)) && this.isVisible()) {

                if (versement != null && versementEntrepriseDAOImpl.getEtat(versement) == null) {
                    exite.showMessageDialog("تنبيه", "لا يمكنك تسجيل دفعة قديمة لهذه الشركة \nلقد تم تسجيل دفعة عادية سابقا ");
                    combEntreprise.setSelectedIndex(-1);
                    return;
                }
                exite.showMessageDialog("تنبيه", "لا يمكنك تسجيل دفعة قديمة لهذه الشركة \n تم تسجيل آخر دفعة قديمة مسبقا");
                combEntreprise.setSelectedIndex(-1);
                return;
            }

            labCreditLsatVersemnt.setText(versement != null
                    ? formatter.format(versement.getReste_credit())
                    : "0.00");
        }
    }//GEN-LAST:event_combEntrepriseActionPerformed

    private void txtPrixAchatFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrixAchatFocusLost
        if (txtPrixAchat.getText().isEmpty())
            txtPrixAchat.setText("0.00");
    }//GEN-LAST:event_txtPrixAchatFocusLost

    private void textMontantVersementFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textMontantVersementFocusLost
        if (textMontantVersement.getText().isEmpty())
            textMontantVersement.setText("0.00");
    }//GEN-LAST:event_textMontantVersementFocusLost

    private void chekLastVersementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chekLastVersementActionPerformed
        if (chekLastVersement.isSelected()) {
            messageDialog.ShowConfirmMessageInDialog("تـنـبـيه", "هل أنت متأكد أن هذه أخر دفعة قديمة لشركة \n لأنه لا يمكنك ادخال دفعة قديمة بعدها الحفظ");
            if (messageDialog.getMessageType() == MessageDialog.MessageType.CANCEL) {
                chekLastVersement.setSelected(false);
            }
        }

    }//GEN-LAST:event_chekLastVersementActionPerformed

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
            java.util.logging.Logger.getLogger(EtatInitialForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EtatInitialForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EtatInitialForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EtatInitialForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EtatInitialForm dialog = new EtatInitialForm(new javax.swing.JFrame(), true);
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
    private material.design.ComboboxRoundNew cbMonth;
    private material.design.ComboboxRoundNew cbYear;
    private material.design.JCheckBoxCustomAR chekLastVersement;
    private material.design.ComboboxRoundNew combEntreprise;
    private datechooser.DateChooser dateChooser1;
    private datechooser.DateChooser dateChooser2;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labAchatCredit;
    private javax.swing.JLabel labAug_avec_versment;
    private javax.swing.JLabel labCreditLsatVersemnt;
    private javax.swing.JLabel labNewCredit;
    private ui.card.panRound panRound2;
    private ui.card.panRound2 panRound22;
    private ui.card.panRound2 panRound23;
    private ui.card.pan_gradiant pan_gradiant1;
    private ui.card.pan_gradiant pan_gradiant5;
    private ui.card.pan_gradiant pan_gradiant6;
    private ui.card.pan_gradiant pan_gradiant7;
    private ui.card.pan_gradiant pan_gradiant8;
    private ui.card.pan_gradiant pan_radioCart;
    private ui.card.pan_gradiant pan_radioChequ;
    private ui.card.pan_gradiant pan_radioEsp;
    private material.design.RadioButtonCustomAR radioCart;
    private material.design.RadioButtonCustomAR radioChequ;
    private material.design.RadioButtonCustomAR radioDate;
    private material.design.RadioButtonCustomAR radioEsp;
    private material.design.RadioButtonCustomAR radioMonthYear;
    private ui.card.TextFieldPrice textMontantVersement;
    private material.design.TextField txtDateDeb;
    private material.design.TextField txtDateFin;
    private material.design.TextField txtNumCheque;
    private ui.card.TextFieldPrice txtPrixAchat;
    // End of variables declaration//GEN-END:variables
}
