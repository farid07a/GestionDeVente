/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.AchatDetailDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.AchatDetail;
import entity.Entreprise;
import entity.VersementEntreprise;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import material.design.designeTable;

public class FactureEntrepriseForm extends javax.swing.JDialog {

    Connection connection;
    Entreprise entreprise;
    AchatDAOImpl achatDAOImpl;
    AchatDetailDAOImpl achatDetailDAOImpl;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    HomeForm homeForm;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
     designeTable designeTable = new designeTable();
     List<Achat> achats;
    public FactureEntrepriseForm(java.awt.Frame parent, boolean modal, Entreprise entreprise,List<Achat> achats) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        this.entreprise = entreprise;
        this.achats = achats;
        initComponents();
        setLocationRelativeTo(this.homeForm);
        connection = DatabaseConnection.getInstance().getConnection();
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        designeTable.setDesignTable(tab, jScrollPane1);
        designeTable.setDesignTable(tabAchadetaill, jScrollPane2);
        designeTable.SearchTable(tab, txt_searcAcha);
       designeTable.SearchTable(tabAchadetaill, txt_searcAchatDetaill);
       jLabel2.setText(jLabel2.getText()+ " "+entreprise.getNom_ar());
        tab.removeColumn(tab.getColumnModel().getColumn(0));
        tabAchadetaill.removeColumn(tabAchadetaill.getColumnModel().getColumn(0));


       

        init();
    }

    public void init() {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        double TotalAchatNoPayee = 0;
        double credit=0;
        VersementEntreprise lastVersement = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);

      
        for (Achat achat : achats) {
            model.addRow( new Object[]{achat.getId(),achat.getDate_achat(),
                                    formatter.format(achat.getPrix_total()),
                                     achat.getClient().getNom()+" "+ achat.getClient().getPrenom(),
                                     achat.getClient().getMatricule()});
        }
        if (achats != null) {
            TotalAchatNoPayee = achats.stream()
                    .mapToDouble(Achat::getPrix_total)
                    .sum();
        }
        
       LabAchatNOPayee.setText(formatter.format(TotalAchatNoPayee));
       double montant =0;
       
        if(lastVersement !=null ){
            credit = lastVersement.getReste_credit();
       if(credit >=0){
            TotalAchatNoPayee=TotalAchatNoPayee + credit;
            montant = TotalAchatNoPayee + credit;
            labCreditLsatVersemnt.setText(formatter.format(TotalAchatNoPayee));
            labMantantCreditAug.setText("");
            LabMantantVersement.setText(formatter.format(TotalAchatNoPayee));
         
        }else{
            labCreditLastAug.setText(formatter.format(lastVersement.getReste_credit()*-1));         
            montant=TotalAchatNoPayee + credit;  // 500+(-200)= 300 credit / 500+(-600)=-100 Agu
            labMantantCreditAug.setText((montant>=0)? "": 
                                                      " المبلغ الزائد " +" :  " + " + "+formatter.format(montant*-1) );
            
            LabMantantVersement.setText(((montant>=0)? formatter.format(montant) : 
                                                       formatter.format(0.00) )); 

       }
         }
         
        labAchatCredit.setText(formatter.format(TotalAchatNoPayee));
        initAchatDetaille(achats);
    }
    
    public void initAchatDetaille(List<Achat> achats ){
        DefaultTableModel model = (DefaultTableModel) tabAchadetaill.getModel();
        model.setRowCount(0);
        List <AchatDetail > achatDetails;
        for (Achat achat : achats) {
          achatDetails  = achatDetailDAOImpl.getAchatDetaillByIDAchat(achat);
            for (AchatDetail achatDetail : achatDetails) {
               model.addRow(new Object[]{achatDetail.getId(),achat.getDate_achat(),
                                    formatter.format(achatDetail.getPrix_total()),
                                    achatDetail.getQty(),
                                    achatDetail.getPrix_unitaire(),
                                    achatDetail.getProduit().getDesignation() +":"+achatDetail.getProduit().getMarque(),
                                    achat.getClient().getNom()+" "+ achat.getClient().getPrenom(),
                                    achat.getClient().getMatricule()}); 
            }
           
        }
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
        jLabel2 = new javax.swing.JLabel();
        panRound23 = new ui.card.panRound2();
        jLabel6 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        labCreditLastAug = new javax.swing.JLabel();
        panRound21 = new ui.card.panRound2();
        jLabel5 = new javax.swing.JLabel();
        labAchatCredit = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panRound24 = new ui.card.panRound2();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        LabAchatNOPayee = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panAchat = new javax.swing.JPanel();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        btnDetaill = new material.design.buttonRounder();
        txt_searcAcha = new material.design.SearchTextRound();
        panAchatDetaill = new javax.swing.JPanel();
        tableScrollButton2 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabAchadetaill = new javax.swing.JTable();
        btnRetur = new material.design.buttonRounder();
        txt_searcAchatDetaill = new material.design.SearchTextRound();
        panRound25 = new ui.card.panRound2();
        jLabel7 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        labCreditLsatVersemnt = new javax.swing.JLabel();
        panRound26 = new ui.card.panRound2();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        LabMantantVersement = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labMantantCreditAug = new javax.swing.JLabel();
        btn2 = new material.design.buttonRounder();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("الديون المتبقية : ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 920, 30));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("القيمة الزائدة");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setText("دج");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 204, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("+");

        labCreditLastAug.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labCreditLastAug.setForeground(new java.awt.Color(51, 204, 0));
        labCreditLastAug.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCreditLastAug.setText("0.00");

        javax.swing.GroupLayout panRound23Layout = new javax.swing.GroupLayout(panRound23);
        panRound23.setLayout(panRound23Layout);
        panRound23Layout.setHorizontalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCreditLastAug, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(43, 43, 43)
                .addComponent(jLabel6)
                .addContainerGap())
        );
        panRound23Layout.setVerticalGroup(
            panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound23Layout.createSequentialGroup()
                .addGroup(panRound23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel15)
                    .addComponent(labCreditLastAug))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel1.add(panRound23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 310, 50));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("اجمالي المبيعات الجديدة + الديـون السابقة  ");

        labAchatCredit.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        labAchatCredit.setForeground(new java.awt.Color(0, 0, 204));
        labAchatCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAchatCredit.setText("0.00");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel20.setText("دج");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-chargé-64.png"))); // NOI18N

        javax.swing.GroupLayout panRound21Layout = new javax.swing.GroupLayout(panRound21);
        panRound21.setLayout(panRound21Layout);
        panRound21Layout.setHorizontalGroup(
            panRound21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(panRound21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound21Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(43, Short.MAX_VALUE))
                    .addGroup(panRound21Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel20)
                        .addGap(5, 5, 5)
                        .addComponent(labAchatCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addGap(188, 188, 188))))
        );
        panRound21Layout.setVerticalGroup(
            panRound21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound21Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labAchatCredit)
                            .addComponent(jLabel20)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(panRound21, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, 440, 80));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-30.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("اجمالي المبيعات الجديدة");

        LabAchatNOPayee.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAchatNOPayee.setForeground(new java.awt.Color(0, 51, 153));
        LabAchatNOPayee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAchatNOPayee.setText("0.00");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("دج");

        javax.swing.GroupLayout panRound24Layout = new javax.swing.GroupLayout(panRound24);
        panRound24.setLayout(panRound24Layout);
        panRound24Layout.setHorizontalGroup(
            panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabAchatNOPayee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(8, 8, 8))
        );
        panRound24Layout.setVerticalGroup(
            panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panRound24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(LabAchatNOPayee))
                        .addGap(6, 6, 6))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(panRound24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 310, 50));

        jPanel2.setLayout(new java.awt.CardLayout());

        panAchat.setBackground(new java.awt.Color(255, 255, 255));

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "التاريخ", "المبلغ", "اسم و اللقب", "الرقم الوظيفي"
            }
        ));
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab);

        tableScrollButton1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnDetaill.setBackground(new java.awt.Color(51, 204, 255));
        btnDetaill.setForeground(new java.awt.Color(255, 255, 255));
        btnDetaill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-recherche-immobilière-30.png"))); // NOI18N
        btnDetaill.setText("التفاصيل ");
        btnDetaill.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnDetaill.setPreferredSize(new java.awt.Dimension(130, 70));
        btnDetaill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetaillActionPerformed(evt);
            }
        });

        txt_searcAcha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcAcha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcAchaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panAchatLayout = new javax.swing.GroupLayout(panAchat);
        panAchat.setLayout(panAchatLayout);
        panAchatLayout.setHorizontalGroup(
            panAchatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAchatLayout.createSequentialGroup()
                .addGroup(panAchatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAchatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnDetaill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(txt_searcAcha, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        panAchatLayout.setVerticalGroup(
            panAchatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAchatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAchatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDetaill, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_searcAcha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(panAchat, "card2");

        panAchatDetaill.setBackground(new java.awt.Color(255, 255, 255));

        tabAchadetaill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "التاريخ", "الثمن الكلي ", "الكمية", "ثمن المنتج", "المنتج", "الاسم و اللقب", "الرقم الوظيفي"
            }
        ));
        tabAchadetaill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabAchadetaillMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tabAchadetaill);

        tableScrollButton2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        btnRetur.setBackground(new java.awt.Color(51, 204, 255));
        btnRetur.setForeground(new java.awt.Color(255, 255, 255));
        btnRetur.setText("رجوع");
        btnRetur.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnRetur.setPreferredSize(new java.awt.Dimension(130, 70));
        btnRetur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturActionPerformed(evt);
            }
        });

        txt_searcAchatDetaill.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcAchatDetaill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcAchatDetaillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panAchatDetaillLayout = new javax.swing.GroupLayout(panAchatDetaill);
        panAchatDetaill.setLayout(panAchatDetaillLayout);
        panAchatDetaillLayout.setHorizontalGroup(
            panAchatDetaillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAchatDetaillLayout.createSequentialGroup()
                .addContainerGap(308, Short.MAX_VALUE)
                .addComponent(txt_searcAchatDetaill, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221)
                .addComponent(btnRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panAchatDetaillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAchatDetaillLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panAchatDetaillLayout.setVerticalGroup(
            panAchatDetaillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAchatDetaillLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAchatDetaillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_searcAchatDetaill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(331, Short.MAX_VALUE))
            .addGroup(panAchatDetaillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAchatDetaillLayout.createSequentialGroup()
                    .addGap(51, 51, 51)
                    .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel2.add(panAchatDetaill, "card3");

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 900, 370));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("الديـون السابقة ");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setText("دج");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        labCreditLsatVersemnt.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labCreditLsatVersemnt.setForeground(new java.awt.Color(255, 0, 0));
        labCreditLsatVersemnt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labCreditLsatVersemnt.setText("0.00");

        javax.swing.GroupLayout panRound25Layout = new javax.swing.GroupLayout(panRound25);
        panRound25.setLayout(panRound25Layout);
        panRound25Layout.setHorizontalGroup(
            panRound25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound25Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCreditLsatVersemnt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(21, 21, 21))
        );
        panRound25Layout.setVerticalGroup(
            panRound25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(labCreditLsatVersemnt)
                        .addComponent(jLabel7))
                    .addComponent(jLabel24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(panRound25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 310, 50));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("المبلغ المستحق دفعه");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setText("دج");

        LabMantantVersement.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        LabMantantVersement.setForeground(new java.awt.Color(0, 153, 0));
        LabMantantVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabMantantVersement.setText("0.00");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-48.png"))); // NOI18N

        labMantantCreditAug.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labMantantCreditAug.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labMantantCreditAug.setText("0.00");

        javax.swing.GroupLayout panRound26Layout = new javax.swing.GroupLayout(panRound26);
        panRound26.setLayout(panRound26Layout);
        panRound26Layout.setHorizontalGroup(
            panRound26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabMantantVersement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound26Layout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(labMantantCreditAug, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        panRound26Layout.setVerticalGroup(
            panRound26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound26Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panRound26Layout.createSequentialGroup()
                        .addGroup(panRound26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRound26Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(11, 11, 11))
                            .addGroup(panRound26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LabMantantVersement)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labMantantCreditAug)
                        .addGap(31, 31, 31))))
        );

        jPanel1.add(panRound26, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 440, 70));

        btn2.setBackground(new java.awt.Color(51, 204, 255));
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-print-48.png"))); // NOI18N
        btn2.setText("طباعة الفاتورة");
        btn2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn2.setPreferredSize(new java.awt.Dimension(130, 70));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        jPanel1.add(btn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 280, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searcAchatDetaillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcAchatDetaillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcAchatDetaillActionPerformed
int lastRow =-1;
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

    private void tabAchadetaillMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabAchadetaillMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tabAchadetaillMouseReleased

    private void btnDetaillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetaillActionPerformed
       panAchat.setVisible(false);
       panAchatDetaill.setVisible(true);
       
    }//GEN-LAST:event_btnDetaillActionPerformed

    private void btnReturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturActionPerformed
       panAchatDetaill.setVisible(false);
        panAchat.setVisible(true);
    }//GEN-LAST:event_btnReturActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn2ActionPerformed

    private void txt_searcAchaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcAchaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcAchaActionPerformed

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
            java.util.logging.Logger.getLogger(FactureEntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FactureEntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FactureEntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FactureEntrepriseForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FactureEntrepriseForm dialog = new FactureEntrepriseForm(new javax.swing.JFrame(), true,null,null);
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
    private javax.swing.JLabel LabAchatNOPayee;
    private javax.swing.JLabel LabMantantVersement;
    private material.design.buttonRounder btn2;
    private material.design.buttonRounder btnDetaill;
    private material.design.buttonRounder btnRetur;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labAchatCredit;
    private javax.swing.JLabel labCreditLastAug;
    private javax.swing.JLabel labCreditLsatVersemnt;
    private javax.swing.JLabel labMantantCreditAug;
    private javax.swing.JPanel panAchat;
    private javax.swing.JPanel panAchatDetaill;
    private ui.card.panRound2 panRound21;
    private ui.card.panRound2 panRound23;
    private ui.card.panRound2 panRound24;
    private ui.card.panRound2 panRound25;
    private ui.card.panRound2 panRound26;
    private javax.swing.JTable tab;
    private javax.swing.JTable tabAchadetaill;
    private ui.table.TableScrollButton tableScrollButton1;
    private ui.table.TableScrollButton tableScrollButton2;
    private material.design.SearchTextRound txt_searcAcha;
    private material.design.SearchTextRound txt_searcAchatDetaill;
    // End of variables declaration//GEN-END:variables
}
