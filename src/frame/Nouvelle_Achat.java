/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.AchatDetailDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.ProduitDAOImpl;
import entity.Achat;
import entity.AchatDetail;
import entity.Categorie;
import entity.Client;
import entity.Entreprise;
import entity.Nomber;
import entity.Produit;
import home.HomeForm;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import material.design.ComboboxRound;
import material.design.ComboboxRoundNew;
import material.design.designeTable;
import services.Clock;
import ui.card.TextFieldRound;

/**
 *
 * @author pc
 */
public class Nouvelle_Achat extends javax.swing.JDialog {

     Connection connection;
     HomeForm homeForm;
     AchatDAOImpl achatDAOImpl;
     ClientDAOImpl clientDAOImpl;
     ProduitDAOImpl produitDAOImpl;
     AchatDetailDAOImpl achatDetailDAOImpl;
     
     DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));    
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;
     public Nouvelle_Achat(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
         this.homeForm = (HomeForm) parent;
         messageDialog = new MessageDialog(this);
         validationMessageDialog = new ValidationMessageDialog(this,homeForm);
        initComponents();
        setLocationRelativeTo(parent);

        new designeTable().setDesignTable(tabProduit, jScrollPane1);
        new designeTable().setDesignTable(tabProduitAchat, jScrollPane3);

        new designeTable().SearchTable(tabProduit, txt_searcP);

        connection = DatabaseConnection.getInstance().getConnection();
        achatDAOImpl= new AchatDAOImpl(connection);
        clientDAOImpl= new ClientDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        tabProduit.removeColumn(tabProduit.getColumnModel().getColumn(0));
        tabProduitAchat.removeColumn(tabProduitAchat.getColumnModel().getColumn(0));

        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        model.addTableModelListener(e -> {
               SetTotalAchatInLab();
        });

        setProduitsOnTab();
        setInfoEntreprice();
        
        TableColumn column = tabProduit.getColumnModel().getColumn(0);
        tabProduit.getColumnModel().removeColumn(column);
        
        TableColumn column1 = tabProduitAchat.getColumnModel().getColumn(0);
        tabProduitAchat.getColumnModel().removeColumn(column1);
        
         Clock Clock = new Clock();
         Clock.start(LabTime, labDate);
         txt_matricul.requestFocus();
    }

    public void SetTotalAchatInLab() {
        int rowsCount = tabProduitAchat.getRowCount();
        if(rowsCount > 0 ){
        double prixTotal = 0;
        for (int row = 0; row < rowsCount; row++) {           
          int modelRow = tabProduitAchat.convertRowIndexToModel(row);
            double prix = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(modelRow, 1).toString());           
            prixTotal = prixTotal + prix;
        }
        labPrixTotal.setText(formatter.format(prixTotal ));
        }else{
       labPrixTotal.setText(formatter.format(0.0 ));

        }
    }

    public void setInfoEntreprice() {
        List<Entreprise> entreprises = new EntrepriseDAOImpl(connection).findAll();
        com_entreprice.removeAllItems();
        if (entreprises != null) {
            for (Entreprise entreprise : entreprises) {
                com_entreprice.addItem(entreprise.getNom_ar());
            }

        }
    }

    public void setProduitsOnTab() {
        DefaultTableModel model = (DefaultTableModel) tabProduit.getModel();
        model.setRowCount(0);
        List<Produit> produits = produitDAOImpl.findAll();
        String marque = "";
        for (Produit produit : produits) {
            int id = produit.getId();
            String desg = produit.getDesignation();
            Categorie categorie = produit.getCategorie();

            if (categorie != null) {
                marque = categorie.getNomCategorie();
            }
            double prix_vent = produit.getPrix_vente();
            int qt = produit.getQty(); 
            model.addRow(new Object[]{id, formatter.format(prix_vent),
                marque, desg});
        }

    }
    
    public  void saveAchatDetaille(Achat achat , int rowCont){
        for (int row = 0; row < rowCont; row++) {
            int modelRow = tabProduitAchat.convertRowIndexToModel(row);

Object valPrix = tabProduitAchat.getModel().getValueAt(modelRow, 3);

String cleanValue = (valPrix != null) ? valPrix.toString()
                           .replace(",", "")           
                           .replace(" ", "")           
                           .replace("\u00A0", "")     
                           .replaceAll("\\s+", "")     
                           .trim() : "0";

            
           double prixUnit = Double.parseDouble(cleanValue);
           
           int qt =Integer.parseInt(tabProduitAchat.getModel().getValueAt(modelRow, 2).toString());                     
          
           double prixTotal = prixUnit*qt;        
          
           
           int idProduit = Integer.parseInt(tabProduitAchat.getModel().getValueAt(row, 0).toString());        
           Produit produit = produitDAOImpl.findById(idProduit);
            AchatDetail achatDetail = new AchatDetail(0, achat, produit, qt, prixUnit, prixTotal);
            if (achatDetailDAOImpl.save(achatDetail)>0) {
                System.out.println("Save Achat Detaill - base ");
            }
        }
    }
    
    public int existProduitAchat(Produit produit){
        int exist =-1;
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();           
        for (int i = 0; i < tabProduitAchat.getRowCount(); i++) {
            int idProduit =(int) tabProduitAchat.getModel().getValueAt(i, 0);
            if(produit.getId()==idProduit){
                exist= i;
                break;
            }
        }
        return exist;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        panRound1 = new ui.card.panRound();
        tableCustom1 = new ui.table.TableCustom();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panOrder = new ui.card.panRound();
        tableScrollButton2 = new ui.table.TableScrollButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabProduitAchat = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_searcP1 = new material.design.SearchTextRound();
        BtnSupp = new material.design.buttonRounderC();
        BtnSupp1 = new material.design.buttonRounderC();
        BtnSupp2 = new material.design.buttonRounderC();
        panPrix = new ui.card.panRound();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labPrixTotal = new javax.swing.JLabel();
        btnAdd = new material.design.buttonRounder();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        labDate = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        LabTime = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_searcP = new material.design.SearchTextRound();
        tableScrollButton3 = new ui.table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProduit = new javax.swing.JTable();
        BtnAdd1 = new material.design.buttonRounderC();
        pan_client = new ui.card.panRound();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_matricul = new ui.card.TextFieldRound();
        txt_adress = new ui.card.TextFieldRound();
        txt_nom = new ui.card.TextFieldRound();
        txt_prenom = new ui.card.TextFieldRound();
        txt_tel = new ui.card.TextFieldRound();
        com_entreprice = new material.design.ComboboxRoundNew();
        btn = new material.design.buttonRounder();
        jLabel3 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        panOrder.setColor1(new java.awt.Color(255, 255, 255));

        tabProduitAchat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "السعر الاجمالي", "كمية", "السعر الوحدة", "النوعية", "المنتج"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabProduitAchat.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                tabProduitAchatComponentAdded(evt);
            }
        });
        jScrollPane3.setViewportView(tabProduitAchat);

        tableScrollButton2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 0));
        jLabel4.setText("المشتريات  ");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-60.png"))); // NOI18N

        txt_searcP1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcP1ActionPerformed(evt);
            }
        });

        BtnSupp.setBackground(new java.awt.Color(204, 204, 204));
        BtnSupp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-supprimer-pour-toujours-29.png"))); // NOI18N
        BtnSupp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSuppActionPerformed(evt);
            }
        });

        BtnSupp1.setBackground(new java.awt.Color(204, 204, 204));
        BtnSupp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ajouter-29.png"))); // NOI18N
        BtnSupp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSupp1ActionPerformed(evt);
            }
        });

        BtnSupp2.setBackground(new java.awt.Color(204, 204, 204));
        BtnSupp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N
        BtnSupp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSupp2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panOrderLayout = new javax.swing.GroupLayout(panOrder);
        panOrder.setLayout(panOrderLayout);
        panOrderLayout.setHorizontalGroup(
            panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
            .addGroup(panOrderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(BtnSupp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(BtnSupp2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnSupp1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(txt_searcP1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panOrderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(230, 230, 230))
        );
        panOrderLayout.setVerticalGroup(
            panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOrderLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panOrderLayout.createSequentialGroup()
                        .addGroup(panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnSupp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_searcP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnSupp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableScrollButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BtnSupp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panPrix.setColor1(new java.awt.Color(255, 255, 255));
        panPrix.setMaximumSize(new java.awt.Dimension(32767, 235));
        panPrix.setMinimumSize(new java.awt.Dimension(0, 235));
        panPrix.setPreferredSize(new java.awt.Dimension(184, 235));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("المــبــلــغ الــكـــلــــي ");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 202, 11));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ملخص الـفـاتــورة");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-recevoir-le-changement-48.png"))); // NOI18N

        labPrixTotal.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        labPrixTotal.setForeground(new java.awt.Color(0, 51, 255));
        labPrixTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labPrixTotal.setText("00.0");
        labPrixTotal.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(102, 204, 0)));

        btnAdd.setBackground(new java.awt.Color(22, 163, 74));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("تأكيد و طباعة الفاتورة");
        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAdd.setPreferredSize(new java.awt.Dimension(130, 70));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("د ج");

        javax.swing.GroupLayout panPrixLayout = new javax.swing.GroupLayout(panPrix);
        panPrix.setLayout(panPrixLayout);
        panPrixLayout.setHorizontalGroup(
            panPrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPrixLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labPrixTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(76, 76, 76))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPrixLayout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panPrixLayout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPrixLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        panPrixLayout.setVerticalGroup(
            panPrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPrixLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panPrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addGap(16, 16, 16)
                .addGroup(panPrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labPrixTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        jPanel5.setBackground(new java.awt.Color(43, 43, 140));
        jPanel5.setMinimumSize(new java.awt.Dimension(0, 29));
        jPanel5.setPreferredSize(new java.awt.Dimension(629, 29));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("التاريـخ :");

        labDate.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labDate.setForeground(new java.awt.Color(255, 255, 255));
        labDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("التوقيت :");

        LabTime.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        LabTime.setForeground(new java.awt.Color(255, 255, 255));
        LabTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LabTime.setText("00:00:00");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("User ");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("المستـخـدم :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(LabTime, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addGap(46, 46, 46))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22))
            .addComponent(labDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(LabTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panPrix, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(panPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(500, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(500, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 612));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("الــمنـــتــجـــات ( انقر مرتين للإضافة)");

        txt_searcP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcPActionPerformed(evt);
            }
        });

        tabProduit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id", "السعر", "النوعية", "المنتج"
            }
        ));
        tabProduit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabProduitMouseClicked(evt);
            }
        });
        tabProduit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabProduitKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabProduit);

        tableScrollButton3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        BtnAdd1.setBackground(new java.awt.Color(204, 204, 0));
        BtnAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-double-gauche-32.png"))); // NOI18N
        BtnAdd1.setText("إضافة المنتج");
        BtnAdd1.setToolTipText("إضافة المنتج");
        BtnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAdd1ActionPerformed(evt);
            }
        });

        pan_client.setColor1(new java.awt.Color(255, 255, 255));
        pan_client.setMaximumSize(new java.awt.Dimension(32767, 235));
        pan_client.setMinimumSize(new java.awt.Dimension(100, 235));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("مـعـلـومـات الـزبــون");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("الاسم");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("اللقـب");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("الشركة");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("رقـم الـتـعـريـف");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("العنوان");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("الهاتف");

        txt_matricul.setBorder(null);
        txt_matricul.setForeground(new java.awt.Color(0, 102, 102));
        txt_matricul.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_matricul.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_matricul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_matriculActionPerformed(evt);
            }
        });
        txt_matricul.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_matriculKeyPressed(evt);
            }
        });

        txt_adress.setBorder(null);
        txt_adress.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_adress.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_adress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_adressActionPerformed(evt);
            }
        });
        txt_adress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_adressKeyPressed(evt);
            }
        });

        txt_nom.setBorder(null);
        txt_nom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nom.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nomActionPerformed(evt);
            }
        });
        txt_nom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nomKeyPressed(evt);
            }
        });

        txt_prenom.setBorder(null);
        txt_prenom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_prenom.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_prenom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_prenomActionPerformed(evt);
            }
        });
        txt_prenom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_prenomKeyPressed(evt);
            }
        });

        txt_tel.setBorder(null);
        txt_tel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_tel.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txt_tel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telActionPerformed(evt);
            }
        });
        txt_tel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_telKeyPressed(evt);
            }
        });

        com_entreprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                com_entrepriceActionPerformed(evt);
            }
        });
        com_entreprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                com_entrepriceKeyPressed(evt);
            }
        });

        btn.setBackground(new java.awt.Color(51, 153, 0));
        btn.setForeground(new java.awt.Color(255, 255, 255));
        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btn.setText("إضـافـة الزبــون");
        btn.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn.setPreferredSize(new java.awt.Dimension(130, 70));
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_clientLayout = new javax.swing.GroupLayout(pan_client);
        pan_client.setLayout(pan_clientLayout);
        pan_clientLayout.setHorizontalGroup(
            pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_clientLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_clientLayout.createSequentialGroup()
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_matricul, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_clientLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_clientLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(80, 80, 80))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_clientLayout.createSequentialGroup()
                                .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_clientLayout.createSequentialGroup()
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_clientLayout.createSequentialGroup()
                                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pan_clientLayout.createSequentialGroup()
                                        .addComponent(txt_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14))
                                    .addGroup(pan_clientLayout.createSequentialGroup()
                                        .addComponent(txt_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel18)))
                                .addGap(13, 13, 13)
                                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_nom, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .addComponent(txt_adress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(com_entreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel17)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))))
        );
        pan_clientLayout.setVerticalGroup(
            pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_clientLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_clientLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_matricul, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)))
                    .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)))
                .addGap(3, 3, 3)
                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txt_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_clientLayout.createSequentialGroup()
                        .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_adress, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(3, 3, 3)
                .addGroup(pan_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(com_entreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        jLabel3.setBackground(new java.awt.Color(43, 43, 140));
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(BtnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_searcP, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tableScrollButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pan_client, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_searcP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(311, Short.MAX_VALUE)
                    .addComponent(tableScrollButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20)))
        );

        jPanel1.add(jPanel2);

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
      
        int rowCont = tabProduitAchat.getModel().getRowCount();     
        if (rowCont!=0) {
            
          String cleanValue = labPrixTotal.getText()
                           .replace(",", "")          
                           .replace(" ", "")          
                           .replace("\u00A0", "")    
                            .replaceAll("\\s+", "")    
                           .trim();
            double prixTotal = Double.parseDouble( cleanValue);
            
            
            String matricul= txt_matricul.getText();
            Client client = clientDAOImpl.getClientByMatricul(matricul);
            Achat achat= null;
            if (client != null) {   // client exist ;
                achat = new Achat(0, client, prixTotal, LocalDate.now());
                 
            }else{  // new client ;
                  String nom = txt_nom.getText();
                  String prenom = txt_prenom.getText();
                  String matricul1 = txt_matricul.getText();
                  String tel = txt_tel.getText();
                  String adress = txt_adress.getText();
                  String nomEntreprise= com_entreprice.getSelectedItem().toString();
                  Entreprise entreprise = new EntrepriseDAOImpl(connection).getEntrepriseParName(nomEntreprise);
                  Client newClient= new Client(0, nom, prenom, matricul1, tel, adress, entreprise);
                  if(new ClientDAOImpl(connection).save(newClient)>0){
                      newClient = new ClientDAOImpl(connection).findLast();
                      achat = new Achat(0, newClient, prixTotal, LocalDate.now());
                  }
            }
            
            messageDialog = new MessageDialog(this);
            messageDialog.ShowConfirmMessageInDialog("حــفـظ", "هـل أنت متـأكـد مـن العـمـلـية ");
            if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {

             if (achatDAOImpl.save(achat) >0 ) {
                  System.out.println("save Achat");
                  Achat lastAchat = achatDAOImpl.getLast();  
                  saveAchatDetaille(lastAchat ,rowCont);
                  this.dispose();
                  validationMessageDialog.showMessagetoDialog("حـفـظ", "تـم حـفـظ عـمـلـيـة الـبـيـع بـنـجـاح");
                  homeForm.getPanAchat().setInfoAchatInTab();
                  
                }
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void txt_searcPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcPActionPerformed

    private void tabProduitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitMouseClicked
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
                DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
            int selectedRow = tabProduit.getSelectedRow();
            
            if (selectedRow != -1) {
                int id = Integer.parseInt(tabProduit.getModel().getValueAt(selectedRow, 0).toString());
                Produit produit = new ProduitDAOImpl(connection).findById(id);
                int existProduit =existProduitAchat(produit);
                if (existProduit==-1){              
                String marque = "";
                String desg = produit.getDesignation();
                Categorie categorie = produit.getCategorie();

                if (categorie != null) {
                    marque = categorie.getNomCategorie();
                }
                double prix_vent = produit.getPrix_vente();

                model.addRow(new Object[]{id,prix_vent,1, prix_vent,
                    marque, desg});

                 }else{
              int Qt= (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
              double prixUnitaire= new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
              double Prix_total= prixUnitaire*Qt;
              tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
             tabProduitAchat.getModel().setValueAt(Prix_total, existProduit, 1);
              }
                
            }

        }

    }//GEN-LAST:event_tabProduitMouseClicked

    private void txt_searcP1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcP1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcP1ActionPerformed

    private void txt_matriculActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_matriculActionPerformed
          
    }//GEN-LAST:event_txt_matriculActionPerformed

    private void txt_adressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_adressActionPerformed
        // TODO add your ha8ndling code here:
    }//GEN-LAST:event_txt_adressActionPerformed

    private void txt_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nomActionPerformed

    private void txt_prenomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_prenomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_prenomActionPerformed

    private void txt_telActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telActionPerformed

    private void tabProduitAchatComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_tabProduitAchatComponentAdded
    }//GEN-LAST:event_tabProduitAchatComponentAdded

    private void BtnSuppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSuppActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        int row= tabProduitAchat.getSelectedRow();
        model.removeRow(row);
    }//GEN-LAST:event_BtnSuppActionPerformed

    private void BtnSupp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSupp1ActionPerformed
            int row = tabProduitAchat.getSelectedRow();
            if(row!=-1){                
              int Qt= (int) tabProduitAchat.getModel().getValueAt(row, 2) + 1;
              double prixUnitaire= new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(row, 3).toString());
              double Prix_total= prixUnitaire*Qt;
              tabProduitAchat.getModel().setValueAt(Qt, row, 2);
             tabProduitAchat.getModel().setValueAt(Prix_total, row, 1);
              
            }
    }//GEN-LAST:event_BtnSupp1ActionPerformed

    private void BtnSupp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSupp2ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        int row= tabProduitAchat.getSelectedRow();
      if(row!=-1){
          int Qt= (int) tabProduitAchat.getModel().getValueAt(row, 2); 
          if(Qt ==1){
            model.removeRow(row);
          }else{
            Qt= Qt-1;
            double prixUnitair= new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(row, 3).toString());
            double prix_Total_tab= prixUnitair * Qt;           
            tabProduitAchat.getModel().setValueAt(formatter.format(prix_Total_tab), row, 1);
            tabProduitAchat.getModel().setValueAt(Qt, row,2);
          }
        }
    }//GEN-LAST:event_BtnSupp2ActionPerformed

    private void com_entrepriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_entrepriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_com_entrepriceActionPerformed

    private void BtnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAdd1ActionPerformed
         DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
            int selectedRow = tabProduit.getSelectedRow();
            
            if (selectedRow != -1) {
                int id = Integer.parseInt(tabProduit.getModel().getValueAt(selectedRow, 0).toString());
                Produit produit = new ProduitDAOImpl(connection).findById(id);
                int existProduit =existProduitAchat(produit);
                if (existProduit==-1){              
                String marque = "";
                String desg = produit.getDesignation();
                Categorie categorie = produit.getCategorie();

                if (categorie != null) {
                    marque = categorie.getNomCategorie();
                }
                double prix_vent = produit.getPrix_vente();

                model.addRow(new Object[]{id,prix_vent,1, prix_vent,
                    marque, desg});

                 }else{
              int Qt= (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
              double prixUnitaire= new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
              double Prix_total= prixUnitaire*Qt;
              tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
             tabProduitAchat.getModel().setValueAt(Prix_total, existProduit, 1);
              }
            }
    }//GEN-LAST:event_BtnAdd1ActionPerformed

    private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed
                new ClientForm(this, true).setVisible(true);

    }//GEN-LAST:event_btnActionPerformed

    private void tabProduitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabProduitKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

                  DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
            int selectedRow = tabProduit.getSelectedRow();
            
            if (selectedRow != -1) {
                int id = Integer.parseInt(tabProduit.getModel().getValueAt(selectedRow, 0).toString());
                Produit produit = new ProduitDAOImpl(connection).findById(id);
                int existProduit =existProduitAchat(produit);
                if (existProduit==-1){              
                String marque = "";
                String desg = produit.getDesignation();
                Categorie categorie = produit.getCategorie();

                if (categorie != null) {
                    marque = categorie.getNomCategorie();
                }
                double prix_vent = produit.getPrix_vente();

                model.addRow(new Object[]{id,prix_vent,1, prix_vent,
                    marque, desg});

                 }else{
              int Qt= (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
              double prixUnitaire= new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
              double Prix_total= prixUnitaire*Qt;
              tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
             tabProduitAchat.getModel().setValueAt(Prix_total, existProduit, 1);
              }             
            }
        }
    }//GEN-LAST:event_tabProduitKeyPressed

    private void txt_matriculKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_matriculKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    txt_nom.requestFocusInWindow();
          }
    }//GEN-LAST:event_txt_matriculKeyPressed

    private void txt_nomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    txt_prenom.requestFocusInWindow();
          }
    }//GEN-LAST:event_txt_nomKeyPressed

    private void txt_prenomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_prenomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    txt_adress.requestFocusInWindow();
          }
    }//GEN-LAST:event_txt_prenomKeyPressed

    private void txt_adressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_adressKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    txt_tel.requestFocusInWindow();
          }
    }//GEN-LAST:event_txt_adressKeyPressed

    private void com_entrepriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_com_entrepriceKeyPressed
      
    }//GEN-LAST:event_com_entrepriceKeyPressed

    private void txt_telKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    com_entreprice.requestFocusInWindow();
          }
    }//GEN-LAST:event_txt_telKeyPressed

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
            java.util.logging.Logger.getLogger(Nouvelle_Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Nouvelle_Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Nouvelle_Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Nouvelle_Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Nouvelle_Achat dialog = new Nouvelle_Achat(new javax.swing.JFrame(), true);
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
    private material.design.buttonRounderC BtnAdd1;
    private material.design.buttonRounderC BtnSupp;
    private material.design.buttonRounderC BtnSupp1;
    private material.design.buttonRounderC BtnSupp2;
    private javax.swing.JLabel LabTime;
    private material.design.buttonRounder btn;
    private material.design.buttonRounder btnAdd;
    private material.design.ComboboxRoundNew com_entreprice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labDate;
    private javax.swing.JLabel labPrixTotal;
    private ui.card.panRound panOrder;
    private ui.card.panRound panPrix;
    private ui.card.panRound panRound1;
    private ui.card.panRound pan_client;
    private javax.swing.JTable tabProduit;
    private javax.swing.JTable tabProduitAchat;
    private ui.table.TableCustom tableCustom1;
    private ui.table.TableScrollButton tableScrollButton2;
    private ui.table.TableScrollButton tableScrollButton3;
    private ui.card.TextFieldRound txt_adress;
    private ui.card.TextFieldRound txt_matricul;
    private ui.card.TextFieldRound txt_nom;
    private ui.card.TextFieldRound txt_prenom;
    private material.design.SearchTextRound txt_searcP;
    private material.design.SearchTextRound txt_searcP1;
    private ui.card.TextFieldRound txt_tel;
    // End of variables declaration//GEN-END:variables

    public ComboboxRoundNew getCom_entreprice() {
        return com_entreprice;
    }

    public TextFieldRound getTxt_adress() {
        return txt_adress;
    }

    public TextFieldRound getTxt_matricul() {
        return txt_matricul;
    }

    public TextFieldRound getTxt_nom() {
        return txt_nom;
    }

    public TextFieldRound getTxt_prenom() {
        return txt_prenom;
    }

    public TextFieldRound getTxt_tel() {
        return txt_tel;
    }

   


}
