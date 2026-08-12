/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package frame;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import ReportsF.PrintingService;
import ReportsF.ReportNames;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.AchatDetailDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.ProduitDAOImpl;
import entity.Achat;
import entity.AchatDetail;
import entity.Client;
import entity.ClientPayeParEntreprise;
import entity.Nomber;
import entity.Produit;
import home.HomeForm;
import java.awt.Event;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import material.design.buttonRounder;
import material.design.designeTable;
import net.sf.jasperreports.view.JasperViewer;

public class AchatDetaillForm extends javax.swing.JDialog {

    HomeForm homeForm;
    Connection connection;
    Achat achat;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    AchatDAOImpl achatDAOImpl;
    AchatDetailDAOImpl achatDetailDAOImpl;
    List<AchatDetail> achatDetails = new ArrayList<>();
    ProduitDAOImpl produitDAOImpl;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;

    public AchatDetaillForm(java.awt.Frame parent, boolean modal, Achat achat) {
        super(parent, modal);
        this.homeForm = (HomeForm) parent;
        this.achat = achat;
        initComponents();
        this.setTitle("قائمة مشتريات الزبون");
        this.setLocationRelativeTo(parent);
        connection = DatabaseConnection.getInstance().getConnection();
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);

        new designeTable().setDesignTable(tab, jScrollPane2);
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.addTableModelListener(e -> {
            SetTotalAchatInLab();
            affichebtn();
        });
        setInfoClientInLab();
        setAchatDetaillONTab();

        btnAdd.setVisible(false);
        BtnMoin.setVisible(false);
        btnSupprim.setVisible(false);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        TableColumn column = tab.getColumnModel().getColumn(0);
        tab.getColumnModel().removeColumn(column);

    }

    public void SetTotalAchatInLab() {
        int rowsCount = tab.getRowCount();
        if (rowsCount > 0) {
            double prixTotal = 0;
            for (int row = 0; row < rowsCount; row++) {
                double prix = new Nomber().getNbDouble(tab.getModel().getValueAt(row, 1).toString());
                prixTotal = prixTotal + prix;
            }
            LabPrixTotal.setText(formatter.format(prixTotal));
        } else {
            LabPrixTotal.setText(formatter.format(0.0));

        }
    }

    public int existProduitAchat(Produit produit) {
        int exist = -1;
        for (int i = tab.getRowCount() - 1; i >= 0; i--) {
            int idProduit = (int) tab.getModel().getValueAt(i, 6);
            if (produit.getId() == idProduit) {
                exist = i;
                break;
            }
        }
        return exist;
    }

    public void affichebtn() {

        for (int i = 0; i < tab.getRowCount(); i++) {
            int idAchatDetaille = (int) tab.getModel().getValueAt(i, 0);
            if (idAchatDetaille == 0) {
                btnCancel.setVisible(true);
                btnSave.setVisible(true);
                break;
            } else {
                btnCancel.setVisible(false);
                btnSave.setVisible(false);
            }
        }
    }

    public void setInfoClientInLab() {
        labNomPrenom.setText(achat.getClient().getNom() + " " + achat.getClient().getPrenom());
        labMatricul.setText(achat.getClient().getMatricule());
        if (!achat.getClient().getEntreprise().getNom_fr().isEmpty()) {
            labEntreprice.setText(achat.getClient().getEntreprise().getNom_fr());
        } else {
            labEntreprice.setText(achat.getClient().getEntreprise().getNom_ar());
        }

        labTel.setText(achat.getClient().getTel());
    }

    public void setAchatDetaillONTab() {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        double prixTotal = 0;
        achatDetails = achatDetailDAOImpl.getAchatDetaillByIDAchat(achat);
        for (AchatDetail achatDetail : achatDetails) {

            String marque = "";

            if (achatDetail.getProduit().getCategorie() != null) {
                marque = achatDetail.getProduit().getCategorie().getNomCategorie();
            }
            model.addRow(new Object[]{
                achatDetail.getId(),
                formatter.format(achatDetail.getPrix_total()),
                achatDetail.getQty(),
                formatter.format(achatDetail.getPrix_unitaire()),
                marque,
                achatDetail.getProduit().getDesignation(),
                achatDetail.getProduit().getId()});
            //prixTotal = prixTotal + achatDetail.getPrix_total();
        }

        //  LabPrixTotal.setText(formatter.format(prixTotal));
    }

    public JTable getTab() {
        return tab;
    }

    public JLabel getLabPrixTotal() {
        return LabPrixTotal;
    }

    public buttonRounder getBtnCancel() {
        return btnCancel;
    }

    public buttonRounder getBtnSave() {
        return btnSave;
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
        panRound4 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_search = new material.design.SearchTextRound();
        btn = new material.design.buttonRounder();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labNomPrenom = new javax.swing.JLabel();
        labMatricul = new javax.swing.JLabel();
        labEntreprice = new javax.swing.JLabel();
        labTel = new javax.swing.JLabel();
        btnAdd = new material.design.buttonRounder();
        btnSupprim = new material.design.buttonRounder();
        panRound1 = new ui.card.panRound();
        jLabel6 = new javax.swing.JLabel();
        LabPrixTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnSave = new material.design.buttonRounder();
        btnCancel = new material.design.buttonRounder();
        BtnMoin = new material.design.buttonRounderC();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panRound4.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tab.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tab.setForeground(new java.awt.Color(102, 102, 102));
        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idAchatDe", "السعر الكلي", "كمية", "سعر الوحدة", "النوعية", "المنتج", "idProduit "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(210, 210, 210))
            .addGroup(panRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panRound4Layout.setVerticalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        btn.setBackground(new java.awt.Color(51, 204, 255));
        btn.setForeground(new java.awt.Color(255, 255, 255));
        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-print-48.png"))); // NOI18N
        btn.setText("طباعة الفاتورة");
        btn.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn.setPreferredSize(new java.awt.Dimension(130, 70));
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(58, 58, 173));
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("قائمة مشتريات الزبون");
        jLabel2.setOpaque(true);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("رقــم التـعـريـف الـوظـيفـي");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("الاسـم و الـلـقـب :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("الــشـــركــــة   :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("هاتــف الزبــون :");

        labNomPrenom.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labNomPrenom.setForeground(new java.awt.Color(58, 58, 173));
        labNomPrenom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        labMatricul.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labMatricul.setForeground(new java.awt.Color(58, 58, 173));
        labMatricul.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        labEntreprice.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labEntreprice.setForeground(new java.awt.Color(58, 58, 173));
        labEntreprice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        labTel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labTel.setForeground(new java.awt.Color(58, 58, 173));
        labTel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        btnAdd.setBackground(new java.awt.Color(22, 163, 74));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btnAdd.setText("اضـافـة منتج");
        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnSupprim.setBackground(new java.awt.Color(220, 0, 0));
        btnSupprim.setForeground(new java.awt.Color(255, 255, 255));
        btnSupprim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-trash-64.png"))); // NOI18N
        btnSupprim.setText(" حـذف منـتـج");
        btnSupprim.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSupprim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimActionPerformed(evt);
            }
        });

        panRound1.setBackground(new java.awt.Color(255, 153, 102));
        panRound1.setColor1(new java.awt.Color(204, 204, 255));
        panRound1.setColor2(new java.awt.Color(96, 168, 241));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("فــاتورة الـمـشـتـريـات : ");

        LabPrixTotal.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        LabPrixTotal.setForeground(new java.awt.Color(255, 255, 255));
        LabPrixTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabPrixTotal.setText("0.0");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-64 (1).png"))); // NOI18N

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LabPrixTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(LabPrixTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        btnSave.setBackground(new java.awt.Color(22, 163, 74));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-coche-emoji-48.png"))); // NOI18N
        btnSave.setText("   حفظ المنتجات المضافة");
        btnSave.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(239, 68, 68));
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-cross-mark-48 (1).png"))); // NOI18N
        btnCancel.setText("الغاء المنتجات المضافة");
        btnCancel.setFont(new java.awt.Font("Cairo", 1, 16)); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        BtnMoin.setBackground(new java.awt.Color(255, 255, 153));
        BtnMoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N
        BtnMoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMoinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labNomPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labEntreprice, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labTel, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labMatricul, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(BtnMoin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labMatricul, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labNomPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labEntreprice, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labTel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(BtnMoin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (clientPayeParEntrepriseDAOImpl.getClientPayeeParVersementByAchat(achat) == null) {
            new AddNewProduit(this, true).setVisible(true);
        } else {
            new Exite(this, homeForm).showMessage("لا يـمكنك اضـافـة مـنـتـج ", "لا يـمكنك اضـافـة مـنـتـج  لانـه تـم إدراج هـذه الفاتورو ضـمن دفـعـات الـشـركـة");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnSupprimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimActionPerformed
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        if (tab.getSelectedRow() != -1) {
            int row = tab.getSelectedRow();
            int id = (int) tab.getModel().getValueAt(row, 0);
            if (id != 0) {
                AchatDetail achatDetail = achatDetailDAOImpl.findById(id);
                Achat achat = achatDetail.getAchat();
                double prixTotal = achatDetail.getPrix_total();
                double prixAchat = achat.getPrix_total() - prixTotal;
                achat.setPrix_total(prixAchat);
                MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.showMessagetoDialog("حـذف", "هـل أنت متـأكـد مـن عـمـلـية الحـذف");
                if (messageDialog.getMessageType() == MessageDialog.MessageType.OK) {
                    if (achatDetailDAOImpl.delete(achatDetail.getId()) > 0) {
                        System.out.println("delet achatDetaiil .........");
                        if (achatDAOImpl.update(achat) > 0) {
                            setAchatDetaillONTab();
                            this.homeForm.getPanAchat().setInfoAchatInTab();
                            System.out.println("delet achatDetaiil and update achat...........");
                            new ValidationMessageDialog(this, homeForm).showMessage("تأكيد الحذف", "لـقد تم حـذف المنتح من الفـاتورة");
                        }
                    }
                }
            } else {
                model.removeRow(row);
            }
        }
    }//GEN-LAST:event_btnSupprimActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        int rowCount = tab.getRowCount();
        boolean exist = false;
        for (int row = 0; row < rowCount; row++) {
            int idAchat = (int) tab.getModel().getValueAt(row, 0);
            if (idAchat == 0) {
                int id = (int) tab.getModel().getValueAt(row, 6);
                Produit produit = produitDAOImpl.findById(id);
                int Qt = (int) tab.getModel().getValueAt(row, 2);
                double prixUnitair = new Nomber().getNbDouble(tab.getModel().getValueAt(row, 3).toString());
                double prixTotal = new Nomber().getNbDouble(tab.getModel().getValueAt(row, 1).toString());

                AchatDetail NewAchatDetail = new AchatDetail(0, achat, produit, Qt, prixUnitair, prixTotal);
                double prixTOtalAchat = achat.getPrix_total() + prixTotal;
                achat.setPrix_total(prixTOtalAchat);
                achatDetailDAOImpl.save(NewAchatDetail);
                System.out.println("save new produit AchatDetaill");
                achatDAOImpl.update(achat);
                System.out.println("update prix total Achat");
            }
        }
        new ValidationMessageDialog(this, homeForm).showMessage("تأكيد الحفط", "لـقد تم اضافة المنتجات بنجاح");
        setAchatDetaillONTab();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        int rowCount = tab.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            int idAchat = (int) model.getValueAt(row, 0);
            if (idAchat == 0) {
                model.removeRow(row);
            }
        }
        new ValidationMessageDialog(this, homeForm).showMessage("تـأكيد الـحذف", "تـم حـذف المـنتجات الـمـضافةحـاليا");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void BtnMoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMoinActionPerformed
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        int row = tab.getSelectedRow();
        if (row != -1) {
            int Qt = (int) tab.getModel().getValueAt(row, 2);
            int idAchatDetaill = (int) tab.getModel().getValueAt(row, 0);
            if (Qt == 1) {
                if (idAchatDetaill == 0) {
                    model.removeRow(row);
                } else {
                    model.removeRow(row);
                    AchatDetail achatDetail = achatDetailDAOImpl.findById(idAchatDetaill);
                    double prixAchatDetaill = achatDetail.getPrix_total();
                    achatDetailDAOImpl.delete(achatDetail.getId());
                }

            } else {
                if (idAchatDetaill == 0) {
                    Qt = Qt - 1;
                    double prixUnitair = new Nomber().getNbDouble(tab.getModel().getValueAt(row, 3).toString());
                    double prixTotal = prixUnitair * Qt;
                    tab.getModel().setValueAt(formatter.format(prixTotal), row, 1);
                    tab.getModel().setValueAt(Qt, row, 2);
                } else {

                    AchatDetail achatDetail = achatDetailDAOImpl.findById(idAchatDetaill);
                    Qt = Qt - 1;
                    double prixUnitair = achatDetail.getPrix_unitaire();
                    double prixTotalNew = prixUnitair * Qt;
                    tab.getModel().setValueAt(formatter.format(prixTotalNew), row, 1);
                    tab.getModel().setValueAt(Qt, row, 2);

                    double prixTotalAchat = achat.getPrix_total() - achatDetail.getPrix_total() + prixTotalNew;
                    achat.setPrix_total(prixTotalAchat);

                    achatDetail.setPrix_total(prixTotalNew);
                    achatDetailDAOImpl.update(achatDetail);
                    achatDAOImpl.update(achat);
                }

            }
        }
    }//GEN-LAST:event_BtnMoinActionPerformed

    private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed
        Client client = achat.getClient();

        PrintingService service_print = new PrintingService();
        Map<String, Object> params = new HashMap<>();
        params.put("CLIENT_ID", client.getId());
        params.put("FName", client.getNom() + " " + client.getPrenom());
        if (!client.getEntreprise().getNom_fr().isEmpty()) {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_fr());
        } else {
            params.put("ENTERPRISE_NAME_FR", client.getEntreprise().getNom_ar());
        }

        service_print.printReportDialog(ReportNames.CLIENT_PURCHASES_BY_ID, params,this);
        
    }//GEN-LAST:event_btnActionPerformed

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
            java.util.logging.Logger.getLogger(AchatDetaillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AchatDetaillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AchatDetaillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AchatDetaillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AchatDetaillForm dialog = new AchatDetaillForm(new javax.swing.JFrame(), true, null);
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
    private material.design.buttonRounderC BtnMoin;
    private javax.swing.JLabel LabPrixTotal;
    private material.design.buttonRounder btn;
    private material.design.buttonRounder btnAdd;
    private material.design.buttonRounder btnCancel;
    private material.design.buttonRounder btnSave;
    private material.design.buttonRounder btnSupprim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labEntreprice;
    private javax.swing.JLabel labMatricul;
    private javax.swing.JLabel labNomPrenom;
    private javax.swing.JLabel labTel;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound4;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
