/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import Reports.PrintingService;
import Reports.ReportNames;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
    Exite exite;
     PrintingService service_print = new PrintingService();
 Map<String, Object> params = new HashMap<>();

    public Nouvelle_Achat(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        initComponents();
        setLocationRelativeTo(parent);

        new designeTable().setDesignTable(tabProduit, jScrollPane1);
        new designeTable().setDesignTable(tabProduitAchat, jScrollPane3);

        new designeTable().SearchTable(tabProduit, txt_searcP);
       new designeTable().SearchTable(tabProduitAchat, txt_searcProduitAchat);

        connection = DatabaseConnection.getInstance().getConnection();
        achatDAOImpl = new AchatDAOImpl(connection);
        clientDAOImpl = new ClientDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        
        tabProduit.removeColumn(tabProduit.getColumnModel().getColumn(0));
       tabProduitAchat.removeColumn(tabProduitAchat.getColumnModel().getColumn(0));
    
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        messageDialog = new MessageDialog(this);
        exite = new Exite(this, homeForm);
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        model.addTableModelListener(e -> {
            SetTotalAchatInLab();
            calculNbProduit();
        });

        setProduitsOnTab();
        setInfoEntreprice();

        

//        TableColumn column1 = tabProduitAchat.getColumnModel().getColumn(0);
//        tabProduitAchat.getColumnModel().removeColumn(column1);

        Clock Clock = new Clock();
        Clock.start(LabTime, labDate);
        txt_matricul.requestFocus();
//        
//        tabProduitAchat.addMouseListener(new MouseAdapter() {
//        @Override
// public void mouseClicked(MouseEvent e) {
//
//        int row = tabProduitAchat.rowAtPoint(e.getPoint());
//
//        if (row == -1) {
//            return;
//        }
//
//        boolean wasSelected = tabProduitAchat.isRowSelected(row);
//
//        SwingUtilities.invokeLater(() -> {
//
//            if (wasSelected) {
//                tabProduitAchat.clearSelection();
//            } else {
//                tabProduitAchat.setRowSelectionInterval(row, row);
//            }
//
//        });
//    }
//    });
    }

    public int calculNbProduit(){
       int nbProduit=0;
        int rowsCount = tabProduitAchat.getModel().getRowCount();
        if (rowsCount > 0) {
            for (int row = 0; row < rowsCount; row++) {
                int nb = (int) tabProduitAchat.getModel().getValueAt(row, 2);
                nbProduit = nbProduit + nb;
            }
        
            LabNBProduit.setText((nbProduit<10) ? "0"+ nbProduit: nbProduit+"");

    
        } else {
            LabNBProduit.setText("00");

        }
        return nbProduit;
    }
    public void SetTotalAchatInLab() {
        int rowsCount = tabProduitAchat.getModel().getRowCount();
        if (rowsCount > 0) {
            double prixTotal = 0;
            for (int row = 0; row < rowsCount; row++) {
                
                double prix = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(row, 1).toString());
                prixTotal = prixTotal + prix;
            }
            labPrixTotal.setText(formatter.format(prixTotal));
        } else {
            labPrixTotal.setText(formatter.format(0.0));

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
            model.insertRow(0, new Object[]{id, formatter.format(prix_vent),
              produit.getQty() , marque, desg});
        }

    }

    public int getRowById(JTable table, int id) {

    for (int viewRow = 0; viewRow < table.getRowCount(); viewRow++) {

        int modelRow = table.convertRowIndexToModel(viewRow);

        Object value = table.getModel().getValueAt(modelRow, 0);

        if (value != null && Integer.parseInt(value.toString()) == id) {
            return modelRow;
        }
    }

    return -1;
}
    
    public void saveAchatDetaille(Achat achat, int rowCont) {
        for (int row = 0; row < rowCont; row++) {

            Object valPrix = tabProduitAchat.getModel().getValueAt(row, 3);

            String cleanValue = (valPrix != null) ? valPrix.toString()
                    .replace(",", "")
                    .replace(" ", "")
                    .replace("\u00A0", "")
                    .replaceAll("\\s+", "")
                    .trim() : "0";

            double prixUnit = Double.parseDouble(cleanValue);

            int qt = Integer.parseInt(tabProduitAchat.getModel().getValueAt(row, 2).toString());

            double prixTotal = prixUnit * qt;

            int idProduit = Integer.parseInt(tabProduitAchat.getModel().getValueAt(row, 0).toString());
            Produit produit = produitDAOImpl.findById(idProduit);
            AchatDetail achatDetail = new AchatDetail(0, achat, produit, qt, prixUnit, prixTotal);
            if (achatDetailDAOImpl.save(achatDetail) > 0) {
                System.out.println("Save Achat Detaill - base ");
            }
            
            UpdateQt(produit, qt);
            setProduitsOnTab();
            homeForm.getPan_produit().setProduitsOnTab();
        }
    }

    public int existProduitAchat(Produit produit) {
        int exist = -1;
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        for (int i = 0; i < tabProduitAchat.getModel().getRowCount(); i++) {
            int idProduit = (int) tabProduitAchat.getModel().getValueAt(i, 0);
            if (produit.getId() == idProduit) {
                exist = i;
                break;
            }
        }
        return exist;
    }

    public void print(){
               params = new HashMap<>();
                Achat achat = achatDAOImpl.findLast();
             Client client = achat.getClient();
         params = new HashMap<>();
        params.put("ACHAT_ID", achat.getId());
        params.put("FName", client.getNom() + " " + client.getPrenom());
        if (!client.getEntreprise().getNom_fr().isEmpty()) {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_fr());
        } else {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_ar());
        }
        
        service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_ID_ACHAT, params);
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
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panPrix = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        labDate = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        LabTime = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        pan_gradiant6 = new ui.card.pan_gradiant();
        jLabel24 = new javax.swing.JLabel();
        LabNBProduit = new javax.swing.JLabel();
        pan_gradiant5 = new ui.card.pan_gradiant();
        jLabel21 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        labPrixTotal = new javax.swing.JLabel();
        btnSaveAndImpri = new material.design.buttonRounder();
        btnSave = new material.design.buttonRounder();
        jPanel2 = new javax.swing.JPanel();
        txt_searcP = new material.design.SearchTextRound();
        tableScrollButton3 = new ui.table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProduit = new javax.swing.JTable();
        BtnAdd1 = new material.design.buttonRounderC();
        btnAddProduit = new material.design.buttonRounderC();
        jLabel1 = new javax.swing.JLabel();
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
        panOrder = new ui.card.panRound();
        tableScrollButton2 = new ui.table.TableScrollButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabProduitAchat = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_searcProduitAchat = new material.design.SearchTextRound();
        BtnSupp = new material.design.buttonRounderC();
        BtnSupp1 = new material.design.buttonRounderC();
        BtnSupp2 = new material.design.buttonRounderC();

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
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel6.setMinimumSize(new java.awt.Dimension(0, 40));
        jPanel6.setPreferredSize(new java.awt.Dimension(1129, 50));
        jPanel6.setLayout(new java.awt.CardLayout());

        jLabel3.setBackground(new java.awt.Color(43, 43, 140));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("عـملـية بيع جديـدة               ");
        jLabel3.setOpaque(true);
        jPanel6.add(jLabel3, "card2");

        jPanel1.add(jPanel6);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        panPrix.setColor1(new java.awt.Color(255, 255, 255));
        panPrix.setMaximumSize(new java.awt.Dimension(32767, 235));
        panPrix.setMinimumSize(new java.awt.Dimension(0, 235));
        panPrix.setPreferredSize(new java.awt.Dimension(184, 235));
        panPrix.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 202, 11));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ملخص الـفـاتــورة");
        panPrix.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-recevoir-le-changement-48.png"))); // NOI18N
        panPrix.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 39));

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
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(LabTime, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)))
                        .addGap(23, 23, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(labDate, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(LabTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        panPrix.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 180, 80));

        pan_gradiant6.setColor1(new java.awt.Color(221, 221, 255));
        pan_gradiant6.setColor2(new java.awt.Color(221, 221, 255));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 204));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("عدد المنتجات");

        LabNBProduit.setBackground(new java.awt.Color(226, 250, 202));
        LabNBProduit.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabNBProduit.setForeground(new java.awt.Color(0, 102, 204));
        LabNBProduit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabNBProduit.setText("0");

        javax.swing.GroupLayout pan_gradiant6Layout = new javax.swing.GroupLayout(pan_gradiant6);
        pan_gradiant6.setLayout(pan_gradiant6Layout);
        pan_gradiant6Layout.setHorizontalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabNBProduit, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        pan_gradiant6Layout.setVerticalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant6Layout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(LabNBProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
        );

        panPrix.add(pan_gradiant6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 180, 60));

        pan_gradiant5.setColor1(new java.awt.Color(231, 246, 216));
        pan_gradiant5.setColor2(new java.awt.Color(231, 246, 216));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 153, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("الثمن الكلي ");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 153, 0));
        jLabel26.setText("دج");

        labPrixTotal.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        labPrixTotal.setForeground(new java.awt.Color(51, 153, 0));
        labPrixTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labPrixTotal.setText("00.0");

        javax.swing.GroupLayout pan_gradiant5Layout = new javax.swing.GroupLayout(pan_gradiant5);
        pan_gradiant5.setLayout(pan_gradiant5Layout);
        pan_gradiant5Layout.setHorizontalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabel21)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labPrixTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(49, 49, 49))
        );
        pan_gradiant5Layout.setVerticalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(labPrixTotal))
                .addContainerGap())
        );

        panPrix.add(pan_gradiant5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 180, 70));

        btnSaveAndImpri.setBackground(new java.awt.Color(22, 163, 74));
        btnSaveAndImpri.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAndImpri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/check-mark.png"))); // NOI18N
        btnSaveAndImpri.setText("تأكيد و طباعة الفاتورة");
        btnSaveAndImpri.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSaveAndImpri.setPreferredSize(new java.awt.Dimension(130, 70));
        btnSaveAndImpri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAndImpriActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(102, 153, 255));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-coche-emoji-48.png"))); // NOI18N
        btnSave.setText("حفظ الفاتورة");
        btnSave.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSave.setPreferredSize(new java.awt.Dimension(130, 70));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(panPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveAndImpri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panPrix, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSaveAndImpri, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(500, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(500, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 612));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searcP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcPActionPerformed(evt);
            }
        });
        jPanel2.add(txt_searcP, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 220, -1));

        tableScrollButton3.setLayout(new java.awt.CardLayout());

        tabProduit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id", "السعر", "الكمية", "النوعية", "المنتج"
            }
        ));
        tabProduit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabProduitMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabProduitMouseReleased(evt);
            }
        });
        tabProduit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabProduitKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabProduit);

        tableScrollButton3.add(jScrollPane1, "card2");

        jPanel2.add(tableScrollButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 410, 560));

        BtnAdd1.setBackground(new java.awt.Color(43, 43, 140));
        BtnAdd1.setForeground(new java.awt.Color(255, 255, 255));
        BtnAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ajouter-29.png"))); // NOI18N
        BtnAdd1.setText("إضافة المنتج");
        BtnAdd1.setBorderPainted(false);
        BtnAdd1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        BtnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAdd1ActionPerformed(evt);
            }
        });
        jPanel2.add(BtnAdd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 121, 32));

        btnAddProduit.setBackground(new java.awt.Color(204, 204, 204));
        btnAddProduit.setForeground(new java.awt.Color(255, 255, 255));
        btnAddProduit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ajouter-29.png"))); // NOI18N
        btnAddProduit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProduitActionPerformed(evt);
            }
        });
        jPanel2.add(btnAddProduit, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 37, 30));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 255));
        jLabel1.setText("الــمنـــتــجـــات ( انقر مرتين للإضافة)");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 217, 19));

        pan_client.setColor1(new java.awt.Color(255, 255, 255));
        pan_client.setMaximumSize(new java.awt.Dimension(32767, 235));
        pan_client.setMinimumSize(new java.awt.Dimension(100, 235));
        pan_client.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 255));
        jLabel11.setText("مـعـلـومـات الـزبــون");
        pan_client.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 5, 128, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 102));
        jLabel13.setText("الاسم");
        pan_client.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, -1, 30));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 102));
        jLabel14.setText("اللقـب");
        pan_client.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, -1, 30));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 102));
        jLabel15.setText("الشركة");
        pan_client.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 160, 40, -1));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("رقـم الـتـعـريـف");
        pan_client.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, -1, -1));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 102));
        jLabel17.setText("العنوان");
        pan_client.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, -1, 30));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 102));
        jLabel18.setText("الهاتف");
        pan_client.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 36, 30));

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
        pan_client.add(txt_matricul, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 246, 32));

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
        pan_client.add(txt_adress, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 160, 32));

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
        pan_client.add(txt_nom, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 160, 32));

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
        pan_client.add(txt_prenom, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 165, 32));

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
        pan_client.add(txt_tel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 165, 32));

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
        pan_client.add(com_entreprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 370, 38));

        btn.setBackground(new java.awt.Color(51, 153, 0));
        btn.setForeground(new java.awt.Color(255, 255, 255));
        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btn.setText("إضافة الزبـون");
        btn.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        btn.setPreferredSize(new java.awt.Dimension(130, 70));
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });
        pan_client.add(btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 100, 30));

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
        tabProduitAchat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabProduitAchatMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabProduitAchatMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabProduitAchatMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tabProduitAchat);

        tableScrollButton2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 0));
        jLabel4.setText("المشتريات  ");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-60.png"))); // NOI18N

        txt_searcProduitAchat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searcProduitAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcProduitAchatActionPerformed(evt);
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
                .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addGap(18, 18, 18))
            .addGroup(panOrderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(BtnSupp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(BtnSupp2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnSupp1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(txt_searcProduitAchat, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panOrderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(107, 107, 107)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );
        panOrderLayout.setVerticalGroup(
            panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOrderLayout.createSequentialGroup()
                .addGroup(panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panOrderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnSupp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_searcProduitAchat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnSupp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnSupp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pan_client, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void UpdateQt(Produit produit , int Qt){
             Qt= produit.getQty() - Qt;
             produit.setQty(Qt);
            if( produitDAOImpl.update(produit)>0){
                System.out.println("QT produit update . . . :  "+Qt);
            }else{
                System.out.println("No update . . . :  ");
            } 
    }
    private void btnSaveAndImpriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAndImpriActionPerformed
        String matricul = txt_matricul.getText();
        Client client = clientDAOImpl.getClientByMatricul(matricul);
        int rowCont = tabProduitAchat.getModel().getRowCount();
        boolean existClient = true;
        Achat achat = null;
        if (client == null && !txt_matricul.getText().isEmpty()
                && !txt_prenom.getText().isEmpty() && !txt_prenom.getText().isEmpty()
                && com_entreprice.getSelectedIndex() != -1) {    // new client ;
            String nom = txt_nom.getText();
            String prenom = txt_prenom.getText();
            String matricul1 = txt_matricul.getText();
            String tel = txt_tel.getText();
            String adress = txt_adress.getText();
            String nomEntreprise = com_entreprice.getSelectedItem().toString();
            Entreprise entreprise = new EntrepriseDAOImpl(connection).getEntrepriseParName(nomEntreprise);
            client = new Client(0, nom, prenom, matricul1, tel, adress, entreprise);
        }

        if (rowCont == 0 && client == null) {
            exite.showMessageDialog("تنبيه", "الـرجـاء إخـتـيـار الـزبون أو إدخــال معـلوماته\n و إخـتـيـار الـمـنـتـج");
            return;
        }
        if (rowCont == 0) {
            exite.showMessageDialog("تنبيه", " الـرجـاء إخـتـيـار الـمـنـتـج");
            return;
        }
        if (client == null) {
            exite.showMessageDialog("تنبيه", "الـرجـاء إخـتـيـار الـزبون أو إدخــال معـلوماته");
            return;
        }

        String cleanValue = labPrixTotal.getText()
                .replace(",", "")
                .replace(" ", "")
                .replace("\u00A0", "")
                .replaceAll("\\s+", "")
                .trim();
        double prixTotal = Double.parseDouble(cleanValue);
        achat = new Achat(0, client, prixTotal, LocalDate.now());
        if (!existClient && new ClientDAOImpl(connection).save(client) > 0) {
            client = new ClientDAOImpl(connection).findLast();
            achat.setClient(client);
        }
        messageDialog.ShowConfirmMessageInDialog("حــفـظ", "هـل تريد حفظ عملية البيع ");
        if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
            if (achatDAOImpl.save(achat) > 0) {
                System.out.println("save Achat");
                Achat lastAchat = achatDAOImpl.getLast();
                saveAchatDetaille(lastAchat, rowCont);
                this.dispose();
                validationMessageDialog.showMessagetoDialog("حـفـظ", "تـم حـفـظ عـمـلـيـة الـبـيـع بـنـجـاح");
                homeForm.getPanAchat().setInfoAchatInTab();
                print();

            }
        }

    }//GEN-LAST:event_btnSaveAndImpriActionPerformed

    private void txt_searcPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcPActionPerformed

    private void tabProduitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitMouseClicked
        
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
            if (tabProduit.getSelectedRow() != -1) {
                int viewRow = tabProduit.getSelectedRow();
                int selectedRow = tabProduit.convertRowIndexToModel(viewRow);
                int id = Integer.parseInt(tabProduit.getModel().getValueAt(selectedRow, 0).toString());
                Produit produit = new ProduitDAOImpl(connection).findById(id);
                int existProduit = existProduitAchat(produit);
               int qtproduitTab= (int) tabProduit.getModel().getValueAt(selectedRow, 2);
               if(qtproduitTab ==0){
               exite.showMessageDialog("تنبيه", "لا يمكنك اضافة المنتج \n عدد المنتجات =0");
               return;
               }
               if (existProduit == -1) {
                    String marque = "";
                    String desg = produit.getDesignation();
                    Categorie categorie = produit.getCategorie();

                    if (categorie != null) {
                        marque = categorie.getNomCategorie();
                    }
                    double prix_vent = produit.getPrix_vente();

                    model.insertRow(0,new Object[]{id,formatter.format( prix_vent), 1, 
                        formatter.format(prix_vent),
                        marque, desg});
                    qtproduitTab= produit.getQty()-1;
                    tabProduit.getModel().setValueAt(qtproduitTab, selectedRow, 2);
                } else {
                    int Qt = (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
                    double prixUnitaire = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
                    double Prix_total = prixUnitaire * Qt;
                    tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
                    tabProduitAchat.getModel().setValueAt(formatter.format(Prix_total), existProduit, 1);
                    qtproduitTab=produit.getQty()-Qt;
                    tabProduit.getModel().setValueAt(qtproduitTab, selectedRow, 2);
                }
                
                
            }
        }

    }//GEN-LAST:event_tabProduitMouseClicked

    private void txt_searcProduitAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcProduitAchatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcProduitAchatActionPerformed

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
        int viewRow = tabProduitAchat.getSelectedRow();
         if (viewRow == -1) {
                return;
          }
         
        int row = tabProduitAchat.convertRowIndexToModel(viewRow);
        if (row != -1) {
           int id =  (int) tabProduitAchat.getModel().getValueAt(row, 0);
          int qtProduitAchat =  (int) tabProduitAchat.getModel().getValueAt(row, 2);
          
          int rowTabProuit= getRowById(tabProduit, id);
          int qtProduit =  (int) tabProduit.getModel().getValueAt(rowTabProuit, 2);           
           qtProduit= qtProduit+qtProduitAchat;
           tabProduit.getModel().setValueAt(qtProduit, rowTabProuit, 2);
          model.removeRow(row);

        }
    }//GEN-LAST:event_BtnSuppActionPerformed

    private void BtnSupp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSupp1ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        int viewRow = tabProduitAchat.getSelectedRow();
        int row = tabProduitAchat.convertRowIndexToModel(viewRow);
        if (row != -1) {
            int Qt = (int) tabProduitAchat.getModel().getValueAt(row, 2) + 1;
            double prixUnitaire = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(row, 3).toString());
            double Prix_total = prixUnitaire * Qt;
            tabProduitAchat.getModel().setValueAt(Qt, row, 2);
            tabProduitAchat.getModel().setValueAt(formatter.format(Prix_total), row, 1);

        }
    }//GEN-LAST:event_BtnSupp1ActionPerformed

    private void BtnSupp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSupp2ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        int viewRow = tabProduitAchat.getSelectedRow();
        if (viewRow == -1) {
                return;
          }
        int row = tabProduitAchat.convertRowIndexToModel(viewRow);
        int id = (int) tabProduitAchat.getModel().getValueAt(row, 0);
        int rowTabProuit= getRowById(tabProduit, id);
       
        if (row != -1) {
            int Qt = (int) tabProduitAchat.getModel().getValueAt(row, 2);
            if (Qt == 1) {
                model.removeRow(row);
            } else {
                Qt = Qt - 1;
                double prixUnitair = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(row, 3).toString());
                double prix_Total_tab = prixUnitair * Qt;
                tabProduitAchat.getModel().setValueAt(formatter.format(prix_Total_tab), row, 1);
                tabProduitAchat.getModel().setValueAt(Qt, row, 2);
                
            }
            int qtTabProduit = (int) tabProduit.getModel().getValueAt(rowTabProuit, 2);
             qtTabProduit=qtTabProduit+1;
            tabProduit.getModel().setValueAt(qtTabProduit,rowTabProuit, 2);

        }
    }//GEN-LAST:event_BtnSupp2ActionPerformed

    private void com_entrepriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_entrepriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_com_entrepriceActionPerformed

    private void BtnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAdd1ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduitAchat.getModel();
        if (tabProduit.getSelectedRow() != -1) {
            int viewRow = tabProduit.getSelectedRow();
            int selectedRow = tabProduit.convertRowIndexToModel(viewRow);
            int id = Integer.parseInt(tabProduit.getModel().getValueAt(selectedRow, 0).toString());
            Produit produit = new ProduitDAOImpl(connection).findById(id);
            int existProduit = existProduitAchat(produit);
            if (existProduit == -1) {
                String marque = "";
                String desg = produit.getDesignation();
                Categorie categorie = produit.getCategorie();

                if (categorie != null) {
                    marque = categorie.getNomCategorie();
                }
                double prix_vent = produit.getPrix_vente();

                model.insertRow(0,new Object[]{id, prix_vent, 1, prix_vent,
                    marque, desg});

            } else {
                int Qt = (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
                double prixUnitaire = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
                double Prix_total = prixUnitaire * Qt;
                tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
                tabProduitAchat.getModel().setValueAt(formatter.format(Prix_total), existProduit, 1);
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
                int existProduit = existProduitAchat(produit);
                if (existProduit == -1) {
                    String marque = "";
                    String desg = produit.getDesignation();
                    Categorie categorie = produit.getCategorie();

                    if (categorie != null) {
                        marque = categorie.getNomCategorie();
                    }
                    double prix_vent = produit.getPrix_vente();

                    model.insertRow(0,new Object[]{id, prix_vent, 1, prix_vent,
                        marque, desg});

                } else {
                    int Qt = (int) tabProduitAchat.getModel().getValueAt(existProduit, 2) + 1;
                    double prixUnitaire = new Nomber().getNbDouble(tabProduitAchat.getModel().getValueAt(existProduit, 3).toString());
                    double Prix_total = prixUnitaire * Qt;
                    tabProduitAchat.getModel().setValueAt(Qt, existProduit, 2);
                    tabProduitAchat.getModel().setValueAt(formatter.format(Prix_total), existProduit, 1);
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

    private void btnAddProduitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProduitActionPerformed
        new AddProduit(this, true).setVisible(true);
    }//GEN-LAST:event_btnAddProduitActionPerformed

    private void tabProduitAchatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitAchatMouseClicked

    }//GEN-LAST:event_tabProduitAchatMouseClicked

    private void tabProduitAchatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitAchatMousePressed

    }//GEN-LAST:event_tabProduitAchatMousePressed
    private int lastRow = -1;
    private void tabProduitAchatMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitAchatMouseReleased
        if (!SwingUtilities.isLeftMouseButton(evt)) {
            return;
        }

        int row = tabProduitAchat.rowAtPoint(evt.getPoint());

        if (row == -1) {
            tabProduitAchat.clearSelection();
            lastRow = -1;
            return;
        }

        if (lastRow == row) {
            tabProduitAchat.clearSelection();
            lastRow = -1;
        } else {
            tabProduitAchat.setRowSelectionInterval(row, row);
            lastRow = row;
        }
    }//GEN-LAST:event_tabProduitAchatMouseReleased
    private void tabProduitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProduitMouseReleased

    }//GEN-LAST:event_tabProduitMouseReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         String matricul = txt_matricul.getText();
        Client client = clientDAOImpl.getClientByMatricul(matricul);
        int rowCont = tabProduitAchat.getModel().getRowCount();
        boolean existClient = true;
        Achat achat = null;
        if (client == null && !txt_matricul.getText().isEmpty()
                && !txt_prenom.getText().isEmpty() && !txt_prenom.getText().isEmpty()
                && com_entreprice.getSelectedIndex() != -1) {    // new client ;

            String nom = txt_nom.getText();
            String prenom = txt_prenom.getText();
            String matricul1 = txt_matricul.getText();
            String tel = txt_tel.getText();
            String adress = txt_adress.getText();
            String nomEntreprise = com_entreprice.getSelectedItem().toString();
            Entreprise entreprise = new EntrepriseDAOImpl(connection).getEntrepriseParName(nomEntreprise);
            client = new Client(0, nom, prenom, matricul1, tel, adress, entreprise);
        }

        if (rowCont == 0 && client == null) {
            exite.showMessageDialog("تنبيه", "الـرجـاء إخـتـيـار الـزبون أو إدخــال معـلوماته\n و إخـتـيـار الـمـنـتـج");
            return;
        }
        if (rowCont == 0) {
            exite.showMessageDialog("تنبيه", " الـرجـاء إخـتـيـار الـمـنـتـج");
            return;
        }
        if (client == null) {
            exite.showMessageDialog("تنبيه", "الـرجـاء إخـتـيـار الـزبون أو إدخــال معـلوماته");
            return;
        }

        String cleanValue = labPrixTotal.getText()
                .replace(",", "")
                .replace(" ", "")
                .replace("\u00A0", "")
                .replaceAll("\\s+", "")
                .trim();
        double prixTotal = Double.parseDouble(cleanValue);
        achat = new Achat(0, client, prixTotal, LocalDate.now());
        if (!existClient && new ClientDAOImpl(connection).save(client) > 0) {
            client = new ClientDAOImpl(connection).findLast();
            achat.setClient(client);
        }
        messageDialog.ShowConfirmMessageInDialog("حــفـظ", "هـل تريد حفظ عملية البيع ");
        if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
            if (achatDAOImpl.save(achat) > 0) {
                System.out.println("save Achat");
                Achat lastAchat = achatDAOImpl.getLast();
                saveAchatDetaille(lastAchat, rowCont);
                this.dispose();
                validationMessageDialog.showMessagetoDialog("حـفـظ", "تـم حـفـظ عـمـلـيـة الـبـيـع بـنـجـاح");
                homeForm.getPanAchat().setInfoAchatInTab();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

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
    private javax.swing.JLabel LabNBProduit;
    private javax.swing.JLabel LabTime;
    private material.design.buttonRounder btn;
    private material.design.buttonRounderC btnAddProduit;
    private material.design.buttonRounder btnSave;
    private material.design.buttonRounder btnSaveAndImpri;
    private material.design.ComboboxRoundNew com_entreprice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labDate;
    private javax.swing.JLabel labPrixTotal;
    private ui.card.panRound panOrder;
    private ui.card.panRound panPrix;
    private ui.card.panRound panRound1;
    private ui.card.panRound pan_client;
    private ui.card.pan_gradiant pan_gradiant5;
    private ui.card.pan_gradiant pan_gradiant6;
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
    private material.design.SearchTextRound txt_searcProduitAchat;
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
