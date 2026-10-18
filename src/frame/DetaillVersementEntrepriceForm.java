/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import config.DatabaseConnection;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Client;
import entity.ClientPayeParEntreprise;
import entity.VersementEntreprise;
import enums.ModePaiement;
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
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
   

    public DetaillVersementEntrepriceForm(java.awt.Frame parent, boolean modal, VersementEntreprise versementEntreprise) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;      
    
        connection = DatabaseConnection.getInstance().getConnection();
        this.versementEntreprise = versementEntreprise;
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        initComponents();
   
        new designeTable().setDesignTable(tab, jScrollPane2);
        new designeTable().SearchTable(tab, txt_search);
        
  VersementEntreprise PreviousVersement =versementEntrepriseDAOImpl.getPreviousVersementEntreprise(versementEntreprise);
        
  
        labNomEntrep.setText(versementEntreprise.getEntreprise().getNom_ar());
        labDatVersement.setText(versementEntreprise.getDate_versement().toString());
        labSommeVersement.setText(formatter.format(versementEntreprise.getMontant()));
        
        labPreviousCredit.setText(PreviousVersement!=null && PreviousVersement.getReste_credit()>=0 ? 
                            formatter.format(PreviousVersement.getReste_credit()):
                            "0.00");
        
        //JOptionPane.showMessageDialog(null, versementEntreprise.getNumCheque());
        if (versementEntreprise.getReste_credit() < 0) {
            labRestCredit.setText("0.00");
            labAugMantant.setText(formatter.format(versementEntreprise.getReste_credit()));
        } else {
            labRestCredit.setText(formatter.format(versementEntreprise.getReste_credit()));
            labAugMantant.setText("0.00");
        }
        
        //if (versementEntreprise.getMode_paiement()==ModePaiement.CARTE_BANCAIRE)
        
        GrpBtn.add(radioCart);
        GrpBtn.add(radioEsp);
        GrpBtn.add(radioChequ);
        radioCart.setSelected(true);
        
        radioCart.setVisible(false);
        radioEsp.setVisible(false);
        radioChequ.setVisible(false);
                
        ModePaiement mode= ModePaiement.valueOf(versementEntreprise.getMode_paiement());
        switch(mode)
        {
            case CARTE_BANCAIRE:
                labModPayemen.setText("بطاقة");
                txtNumCheque.setVisible(false);
//                radioCart.setSelected(true);
                break;
            case ESPECES:
                labModPayemen.setText("نقدا");
                txtNumCheque.setVisible(false);
                //radioEsp.setSelected(true);
                break;
            case CHEQUE:
                labModPayemen.setText("صك بريدي");
                txtNumCheque.setVisible(true);
//                radioChequ.setSelected(true);            
                txtNumCheque.setText(versementEntreprise.getNumCheque());
                break;
                
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
        labAchats.setText(formatter.format(total)); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrpBtn = new javax.swing.ButtonGroup();
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
        jLabel12 = new javax.swing.JLabel();
        radioEsp = new material.design.RadioButtonCustomAR();
        radioCart = new material.design.RadioButtonCustomAR();
        radioChequ = new material.design.RadioButtonCustomAR();
        txtNumCheque = new material.design.TextField();
        jLabel13 = new javax.swing.JLabel();
        labAchats = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        labPreviousCredit = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        labModPayemen = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(700, 660));
        jPanel1.setMinimumSize(new java.awt.Dimension(700, 660));

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
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
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
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("الــشـــركــــة   :");

        labNomEntrep.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        labNomEntrep.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("قيمة الدفعة : ");

        labSommeVersement.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labSommeVersement.setForeground(new java.awt.Color(0, 0, 204));
        labSommeVersement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labSommeVersement.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("الديون المتبقية لدفعة :");

        labRestCredit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labRestCredit.setForeground(new java.awt.Color(255, 51, 51));
        labRestCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labRestCredit.setText("0.00");

        jLabel1.setBackground(new java.awt.Color(43, 43, 140));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("تفاصيل عملية الدفع");
        jLabel1.setOpaque(true);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("طريقة الدفع: ");

        labDatVersement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labDatVersement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("المبلغ الزائد لدفعة :");

        labAugMantant.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labAugMantant.setForeground(new java.awt.Color(51, 153, 0));
        labAugMantant.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAugMantant.setText("0.00");

        totalAchat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        totalAchat.setForeground(new java.awt.Color(0, 0, 153));
        totalAchat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalAchat.setText("0.00");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setText("المجموع :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setText("دج");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setText("دج");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setText("دج");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("دج");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("تـاريــخ الدفــع: ");

        radioEsp.setBackground(new java.awt.Color(51, 204, 0));
        radioEsp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-40.png"))); // NOI18N
        radioEsp.setText("نقدا");
        radioEsp.setEnabled(false);
        radioEsp.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioEsp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        radioEsp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioEsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEspActionPerformed(evt);
            }
        });

        radioCart.setBackground(new java.awt.Color(51, 204, 0));
        radioCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-carte-bancaire-40.png"))); // NOI18N
        radioCart.setText("بطاقة");
        radioCart.setEnabled(false);
        radioCart.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioCart.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        radioCart.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioCartActionPerformed(evt);
            }
        });

        radioChequ.setBackground(new java.awt.Color(51, 204, 0));
        radioChequ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cheque-de-paiement.png"))); // NOI18N
        radioChequ.setText("صك بريدي");
        radioChequ.setEnabled(false);
        radioChequ.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioChequ.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        radioChequ.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        radioChequ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioChequActionPerformed(evt);
            }
        });

        txtNumCheque.setForeground(new java.awt.Color(0, 0, 102));
        txtNumCheque.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNumCheque.setEnabled(false);
        txtNumCheque.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txtNumCheque.setLabelText("رقم الحساب البريدي");
        txtNumCheque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNumChequeMouseClicked(evt);
            }
        });
        txtNumCheque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumChequeKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel13.setText("المـبـيـعـات :");

        labAchats.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labAchats.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labAchats.setText("0.00");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setText("دج");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setText("ديون سابقة :");

        labPreviousCredit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labPreviousCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labPreviousCredit.setText("0.00");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setText("دج");

        labModPayemen.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labModPayemen.setForeground(new java.awt.Color(51, 0, 204));
        labModPayemen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labDatVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(labModPayemen, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNumCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(221, 221, 221)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labAchats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labPreviousCredit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(59, 59, 59))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labNomEntrep, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(219, 219, 219)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labSommeVersement, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(panRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(totalAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labAugMantant, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labRestCredit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(37, 37, 37))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labDatVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(labModPayemen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labNomEntrep, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(labPreviousCredit)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(labAchats)
                            .addComponent(jLabel15))))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioChequ, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioCart, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labSommeVersement, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labRestCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(labAugMantant)
                    .addComponent(jLabel10)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(panRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9))
                    .addComponent(totalAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void radioEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioEspActionPerformed

    private void radioCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioCartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioCartActionPerformed

    private void radioChequActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChequActionPerformed
        if (radioChequ.isSelected()) {
            txtNumCheque.requestFocus();
        }
    }//GEN-LAST:event_radioChequActionPerformed

    private void txtNumChequeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNumChequeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumChequeMouseClicked

    private void txtNumChequeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumChequeKeyTyped
        //        if (!Character.isDigit(evt.getKeyChar())) {
            //            evt.consume();
            //        }
    }//GEN-LAST:event_txtNumChequeKeyTyped

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
    private javax.swing.ButtonGroup GrpBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel labAchats;
    private javax.swing.JLabel labAugMantant;
    private javax.swing.JLabel labDatVersement;
    private javax.swing.JLabel labModPayemen;
    private javax.swing.JLabel labNomEntrep;
    private javax.swing.JLabel labPreviousCredit;
    private javax.swing.JLabel labRestCredit;
    private javax.swing.JLabel labSommeVersement;
    private ui.card.panRound panRound4;
    private material.design.RadioButtonCustomAR radioCart;
    private material.design.RadioButtonCustomAR radioChequ;
    private material.design.RadioButtonCustomAR radioEsp;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private javax.swing.JLabel totalAchat;
    private material.design.TextField txtNumCheque;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
