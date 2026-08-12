/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
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
import services.serviceVercementEntreprise;

public class VersementFormNM extends javax.swing.JDialog {

    HomeForm homeForm;
    Connection connection;
    AchatDAOImpl achatDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    public VersementFormNM(java.awt.Frame parent, boolean modal) {
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
        setEntrepriseInCom();

        comboboxRound1.setSelectedIndex(-1);

        textMontantVersement.addActionListener(e -> {
                calculeCreditApresVersement();
        });

    }
    
    public double calculeCreditApresVersement(){
        double prixVersement = new Nomber().getNbDouble(textMontantVersement.getText());
        double prixCredit = new Nomber().getNbDouble(labCreditAvanVersement.getText());
        double prixCreditApresVesement= prixCredit  -  prixVersement;
        labCredit_avec_versment.setText(formatter.format(prixCreditApresVesement ));
        return prixCreditApresVesement;
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
        
        double restCreditLastVersemet = new serviceVercementEntreprise(connection)
                                           .GetLastRestVersementEntreprise(entreprise);
        
        if(restCreditLastVersemet >=0){
            labCreditLsatVersemnt.setText(formatter.format(restCreditLastVersemet));
        }else{
           labNonCreditLsatVersemnt.setText(formatter.format(restCreditLastVersemet * -1 ));
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
                              entreprice.getNom_ar(), prenom  + " " + nom, 
                              matricul});
        }
        
        // laste credit + creditAchat NO IN tab    AchatClientVers
        
        PrixTotalCredit = PrixTotalCredit+restCreditLastVersemet;
        
        labCreditAvanVersement.setText(formatter.format(PrixTotalCredit));
    }

    public void setEntrepriseInCom() {
        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();
        comboboxRound1.removeAllItems();
        for (Entreprise entreprise : entreprises) {
            comboboxRound1.addItem(entreprise.getNom_ar());
        }

    }
    
   public   ModePaiement getModePaiement() {

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
    
   
    public  void saveVersement(Entreprise entreprise){
        List<Achat> achats = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
         
       double montant = new Nomber().getNbDouble(textMontantVersement.getText());
 
       double Credit = new Nomber().getNbDouble(labCreditAvanVersement.getText());
       
       double restCridetApreVersement =Credit - montant  ; // +100 ent-->credi عند دين مؤسسة// -100 ---> بزيادة
       
       ModePaiement mode_paiement= getModePaiement();
       
       VersementEntreprise versementEntreprise =  new VersementEntreprise(0, entreprise,montant , 
                                                  LocalDate.now() , mode_paiement.name(), "",Credit,
                                                  restCridetApreVersement);
 
        if (versementEntrepriseDAOImpl.save(versementEntreprise)>0) {
                System.out.println("save versment entreprise ..... ");
        }
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
        panRound4 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_search = new material.design.SearchTextRound();
        comboboxRound1 = new material.design.ComboboxRound();
        panRound1 = new ui.card.panRound();
        jLabel4 = new javax.swing.JLabel();
        labEntreprice = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labCreditAvanVersement = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        textMontantVersement = new ui.card.TextFieldPrice();
        jLabel8 = new javax.swing.JLabel();
        labCredit_avec_versment = new javax.swing.JLabel();
        radioEsp = new material.design.RadioButtonCustomAR();
        radioCart = new material.design.RadioButtonCustomAR();
        radioChequ = new material.design.RadioButtonCustomAR();
        jLabel2 = new javax.swing.JLabel();
        btn_save = new material.design.buttonRounder();
        btn_annuler = new material.design.buttonRounder();
        jLabel6 = new javax.swing.JLabel();
        labCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labNonCreditLsatVersemnt = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panRound4.setColor1(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout panRound4Layout = new javax.swing.GroupLayout(panRound4);
        panRound4.setLayout(panRound4Layout);
        panRound4Layout.setHorizontalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
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
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        comboboxRound1.setMaximumSize(new java.awt.Dimension(85, 41));
        comboboxRound1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxRound1ActionPerformed(evt);
            }
        });

        panRound1.setColor1(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("الــشـــركــــة   :");

        labEntreprice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("اجـمـالـي الديـون : ");

        labCreditAvanVersement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCreditAvanVersement.setForeground(new java.awt.Color(255, 51, 51));
        labCreditAvanVersement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCreditAvanVersement.setText("0.00");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labEntreprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labCreditAvanVersement, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labEntreprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labCreditAvanVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        panRound2.setColor1(new java.awt.Color(255, 255, 255));
        panRound2.setMaximumSize(new java.awt.Dimension(493, 269));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("الــمــبـلغ الــمـدفـوع ");

        textMontantVersement.setForeground(new java.awt.Color(51, 204, 0));
        textMontantVersement.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textMontantVersement.setText("00.00");
        textMontantVersement.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        textMontantVersement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMontantVersementActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("طـريـقــة الــدفـــع ");

        labCredit_avec_versment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCredit_avec_versment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCredit_avec_versment.setText("0.00");
        labCredit_avec_versment.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        radioEsp.setBackground(new java.awt.Color(51, 204, 0));
        radioEsp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-40.png"))); // NOI18N
        radioEsp.setText("نقدا");
        radioEsp.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        radioEsp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radioEsp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

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

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("الـديــون الـمـتــبـقـيـة");

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addComponent(textMontantVersement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(1, 1, 1))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRound2Layout.createSequentialGroup()
                                .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labCredit_avec_versment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        panRound2Layout.setVerticalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textMontantVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labCredit_avec_versment, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
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

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("الديــون مــن اخـر عملية دفع :");

        labCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCreditLsatVersemnt.setForeground(new java.awt.Color(255, 51, 51));
        labCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCreditLsatVersemnt.setText("0.00");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("   الفـائض من اخـر عملية دفع :");

        labNonCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labNonCreditLsatVersemnt.setForeground(new java.awt.Color(0, 51, 255));
        labNonCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labNonCreditLsatVersemnt.setText("0.00");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تـــســجــيــل دفـــعــة وتــحــديــث رصيد المــؤسـســة");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labNonCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                            .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6)))
                    .addComponent(panRound4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panRound2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboboxRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(450, 450, 450)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labCreditLsatVersemnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labNonCreditLsatVersemnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(comboboxRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(panRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(30, 30, 30))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

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

    private void radioChequActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChequActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioChequActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
       double montant = new Nomber().getNbDouble(textMontantVersement.getText());
        if (comboboxRound1.getSelectedIndex() != -1 && montant >0 ){
            String nomEntreprise = comboboxRound1.getSelectedItem().toString();
          Entreprise entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
            saveVersement(entreprise);
            //save in achaclientversemebt
        
            this.homeForm.getPanVersement().setVersmentOnTab();
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
            java.util.logging.Logger.getLogger(VersementFormNM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VersementFormNM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VersementFormNM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VersementFormNM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VersementFormNM dialog = new VersementFormNM(new javax.swing.JFrame(), true);
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
    private material.design.ComboboxRound comboboxRound1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labCreditAvanVersement;
    private javax.swing.JLabel labCreditLsatVersemnt;
    private javax.swing.JLabel labCredit_avec_versment;
    private javax.swing.JLabel labEntreprice;
    private javax.swing.JLabel labNonCreditLsatVersemnt;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound4;
    private material.design.RadioButtonCustomAR radioCart;
    private material.design.RadioButtonCustomAR radioChequ;
    private material.design.RadioButtonCustomAR radioEsp;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private ui.card.TextFieldPrice textMontantVersement;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
