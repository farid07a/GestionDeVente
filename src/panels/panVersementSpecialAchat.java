/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.Entreprise;
import entity.VersementEntreprise;
import frame.ConfirmationSupprim;
import frame.DetaillVersementEntrepriceForm;
import frame.EtatInitialForm;
import frame.ModifyVersementEntreprise;
import frame.VersementSpecialForm;
import home.HomeForm;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import material.design.button;
import material.design.designeTable;




public class panVersementSpecialAchat extends javax.swing.JPanel {

    HomeForm homeForm;

    Connection  connection;
    VersementEntrepriseDAOImpl  versementEntrepriseDAOImpl;
    AchatDAOImpl achatDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;
    designeTable designeTable=new designeTable();
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    ValidationMessageDialog validationMessageDialog;
    MessageDialog messageDialog;
    Exite exite;
    
    public panVersementSpecialAchat(HomeForm homeForm) {
        initComponents();
        this.homeForm = homeForm;
        connection = DatabaseConnection.getInstance().getConnection();
        versementEntrepriseDAOImpl= new VersementEntrepriseDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        entrepriseDAOImpl= new EntrepriseDAOImpl(connection);
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);
        exite= new Exite(homeForm);
        messageDialog = new MessageDialog(homeForm);
        validationMessageDialog = new ValidationMessageDialog(homeForm);
        
        designeTable.setDesignTable(tabVersementSpecial, jScrollPane2);
        designeTable.setDesignTable(tabAchatSpecial, jScrollPane3);
        designeTable.SearchTable(tabVersementSpecial, txt_search);
        TableColumn column = tabVersementSpecial.getColumnModel().getColumn(0);
        tabVersementSpecial.getColumnModel().removeColumn(column);
      
        
        column=tabVersementSpecial.getColumnModel().getColumn(0);
        tabVersementSpecial.getColumnModel().removeColumn(column);
        
        tabVersementSpecial.getRowSorter().addRowSorterListener(e -> {
            SwingUtilities.invokeLater(() -> {
                LabNombre.setText(String.valueOf(tabVersementSpecial.getRowCount()));
            });
        });
        SearchTableAndComboBox(tabVersementSpecial, txt_search, combEntreprise);
        setVersementSpecialOnTab();
        setAchatSpecialOnTab();
        setEntrepriseInCom();
    }
    
    public void setAchatSpecialOnTab(){
        DefaultTableModel model = (DefaultTableModel) tabAchatSpecial.getModel();
        model.setRowCount(0);
        List<Achat> achats = achatDAOImpl.getAchatSpecial("Clients");
        for (Achat achat : achats) {
         model.insertRow(0, new Object[]{
             achat.getId(),
             achat.getDate_achat(),
             formatter.format(achat.getPrix_total()),
             achat.getClient().getEntreprise().getNom_ar()
         });   
        }
    }
    
    public void setVersementSpecialOnTab(){        
        double Montant = 0;
        DefaultTableModel model = (DefaultTableModel) tabVersementSpecial.getModel();
        model.setRowCount(0);
        int i=0;
        List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getAllVersementSpeciale();
        for (VersementEntreprise versementEntreprise : versementEntreprises) {
            if(!versementEntreprise.getRemarque().isEmpty()){
            double montant = versementEntreprise.getMontant();
            double Credite = versementEntreprise.getReste_credit();
            String CrediteVal = formatter.format(Credite);
            if (Credite < 0) {
                CrediteVal = "+ " + formatter.format(-1 * Credite);
            }
            model.insertRow(0, new Object[]{versementEntreprise.getId(),
                versementEntrepriseDAOImpl.getEtat(versementEntreprise),
                versementEntreprise.getDate_versement(),
                (Credite<0)? "+ " + formatter.format(-1 * Credite):" /",
                (Credite>=0)? formatter.format(Credite):" /",
                formatter.format(versementEntreprise.getTotal_credit()),
                formatter.format(montant),
                versementEntreprise.getMode_paiement(),
                versementEntreprise.getEntreprise().getNom_ar()});

            Montant = Montant + versementEntreprise.getMontant();
             i=i+1;
            }
        }  
        LabNombre.setText(""+i);
    }
   
    public void setEntrepriseInCom() {
        List<Entreprise> entreprises = entrepriseDAOImpl.findAll();
        combEntreprise.removeAllItems();
         combEntreprise.addItem("كـل الشـركـات");
        for (Entreprise entreprise : entreprises) {
            combEntreprise.addItem(entreprise.getNom_ar());
        }

    }
    private void setForm(JComponent com) {
//        pan_centre.removeAll();
//        pan_centre.add(com);
//        pan_centre.repaint();
//        pan_centre.revalidate();
        pan_centre.removeAll();

        pan_centre.setLayout(new java.awt.BorderLayout());
        pan_centre.add(com, java.awt.BorderLayout.CENTER);

        // تحديث وإعادة رسم اللوحة بشكل آمن ومضمون
        pan_centre.revalidate();
        pan_centre.repaint();
    }

    private void setMenu(JPanel pan, button btn) {
        for (Component component : pan.getComponents()) {
            if (component instanceof button) {
                button button = (button) component;
                button.setColor1(new Color(0, 0, 102));
                button.setColor2(new Color(0, 0, 102));
                // button.repaint();
                // button.revalidate();
            }
        }
        btn.setColor2(new Color(51, 51, 255));
        btn.setColor1(new Color(0, 0, 102));     //  btn.setColor1(new Color(0,204,204));
        pan.repaint();
        pan.revalidate();

    }
    
    
public void SearchTableAndComboBox( JTable table, JTextField textField,  JComboBox<String> comboBox) {

    TableRowSorter<TableModel> sorter =
            new TableRowSorter<>(table.getModel());

    table.setRowSorter(sorter);

    Runnable filter = () -> {

        java.util.List<RowFilter<TableModel, Integer>> filters =
                new java.util.ArrayList<>();

        // Filtrage par JTextField
        String text = textField.getText().trim();

        if (!text.isEmpty()) {
            filters.add(RowFilter.regexFilter(
                    "(?i)" + java.util.regex.Pattern.quote(text)
            ));
        }

        // Filtrage par JComboBox
        int index = comboBox.getSelectedIndex();

        if (index > 0) {
            Object selected = comboBox.getSelectedItem();

            if (selected != null) {
                String value = selected.toString();

                filters.add(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(
                            Entry<? extends TableModel, ? extends Integer> entry) {

                        for (int i = 0; i < entry.getValueCount(); i++) {
                            Object cell = entry.getValue(i);

                            if (cell != null &&
                                    cell.toString().equalsIgnoreCase(value)) {
                                return true;
                            }
                        }

                        return false;
                    }
                });
            }
        }

        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    };

    textField.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filter.run();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filter.run();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filter.run();
        }
    });

    comboBox.addActionListener(e -> filter.run());

    filter.run();
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_centre = new javax.swing.JPanel();
        panVersementSpecial = new javax.swing.JPanel();
        btnAdd2 = new material.design.buttonRounder();
        labVersementSepcial = new javax.swing.JLabel();
        btn_annuler = new material.design.buttonRounder();
        btnSupprim = new material.design.buttonRounder();
        btn_annuler1 = new material.design.buttonRounder();
        panRound1 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabVersementSpecial = new javax.swing.JTable();
        combEntreprise = new material.design.ComboboxRoundNew();
        txt_search = new material.design.SearchTextRound();
        jLabel11 = new javax.swing.JLabel();
        LabNombre = new javax.swing.JLabel();
        panAchatSpecial = new javax.swing.JPanel();
        tableScrollButton2 = new ui.table.TableScrollButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabAchatSpecial = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        pan_centre.setBackground(new java.awt.Color(255, 255, 255));
        pan_centre.setLayout(new java.awt.CardLayout());

        panVersementSpecial.setBackground(new java.awt.Color(255, 255, 255));

        btnAdd2.setBackground(new java.awt.Color(51, 153, 0));
        btnAdd2.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btnAdd2.setText("تسجيل دفعة قديمة");
        btnAdd2.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ActionPerformed(evt);
            }
        });

        labVersementSepcial.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labVersementSepcial.setForeground(new java.awt.Color(0, 51, 255));
        labVersementSepcial.setText("الدفـعـات الإسـتـثــنـائـيـة لشركة");
        labVersementSepcial.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 51, 255)));
        labVersementSepcial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labVersementSepcial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                labVersementSepcialFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                labVersementSepcialFocusLost(evt);
            }
        });
        labVersementSepcial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labVersementSepcialMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labVersementSepcialMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labVersementSepcialMouseExited(evt);
            }
        });

        btn_annuler.setBackground(new java.awt.Color(204, 0, 204));
        btn_annuler.setForeground(new java.awt.Color(255, 255, 255));
        btn_annuler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/validation-du-billet (1).png"))); // NOI18N
        btn_annuler.setText("مــعــايــنــة");
        btn_annuler.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        btn_annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_annulerActionPerformed(evt);
            }
        });

        btnSupprim.setBackground(new java.awt.Color(220, 0, 0));
        btnSupprim.setForeground(new java.awt.Color(255, 255, 255));
        btnSupprim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-trash-64.png"))); // NOI18N
        btnSupprim.setText("حذف ");
        btnSupprim.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSupprim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimActionPerformed(evt);
            }
        });

        btn_annuler1.setBackground(new java.awt.Color(112, 112, 193));
        btn_annuler1.setForeground(new java.awt.Color(255, 255, 255));
        btn_annuler1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-pencil-drawing-64.png"))); // NOI18N
        btn_annuler1.setText("تعديل الدفعة");
        btn_annuler1.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        btn_annuler1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_annuler1ActionPerformed(evt);
            }
        });

        panRound1.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tabVersementSpecial.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tabVersementSpecial.setForeground(new java.awt.Color(102, 102, 102));
        tabVersementSpecial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idEntreprise", "المـلاحــضــات", "التاريخ", "الزيادة", "الدين المتبقية", "مشتريات الزبائن ", "المبلغ الدفعة", "نوع الدفع", "الشركة"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabVersementSpecial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabVersementSpecialMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tabVersementSpecial);

        tableScrollButton1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        combEntreprise.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        combEntreprise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combEntrepriseActionPerformed(evt);
            }
        });

        txt_search.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(combEntreprise, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combEntreprise, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("الــعــدد :");

        LabNombre.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabNombre.setForeground(new java.awt.Color(0, 0, 153));
        LabNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabNombre.setText("00");
        LabNombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 153)));

        javax.swing.GroupLayout panVersementSpecialLayout = new javax.swing.GroupLayout(panVersementSpecial);
        panVersementSpecial.setLayout(panVersementSpecialLayout);
        panVersementSpecialLayout.setHorizontalGroup(
            panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panVersementSpecialLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(labVersementSepcial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_annuler1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
            .addGroup(panVersementSpecialLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panVersementSpecialLayout.createSequentialGroup()
                        .addComponent(LabNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(948, 948, 948))
                    .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panVersementSpecialLayout.setVerticalGroup(
            panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panVersementSpecialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labVersementSepcial)
                    .addGroup(panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_annuler1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panVersementSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pan_centre.add(panVersementSpecial, "card2");

        panAchatSpecial.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBorder(null);

        tabAchatSpecial.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tabAchatSpecial.setForeground(new java.awt.Color(102, 102, 102));
        tabAchatSpecial.setModel(new javax.swing.table.DefaultTableModel(
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
        tabAchatSpecial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabAchatSpecialMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tabAchatSpecial);

        tableScrollButton2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout panAchatSpecialLayout = new javax.swing.GroupLayout(panAchatSpecial);
        panAchatSpecial.setLayout(panAchatSpecialLayout);
        panAchatSpecialLayout.setHorizontalGroup(
            panAchatSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1149, Short.MAX_VALUE)
            .addGroup(panAchatSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panAchatSpecialLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE)
                    .addGap(26, 26, 26)))
        );
        panAchatSpecialLayout.setVerticalGroup(
            panAchatSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
            .addGroup(panAchatSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAchatSpecialLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(tableScrollButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                    .addGap(14, 14, 14)))
        );

        pan_centre.add(panAchatSpecial, "card3");

        add(pan_centre);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ActionPerformed
        new EtatInitialForm(homeForm, true).setVisible(true);
    }//GEN-LAST:event_btnAdd2ActionPerformed

    private void labVersementSepcialFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labVersementSepcialFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_labVersementSepcialFocusGained

    private void labVersementSepcialFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labVersementSepcialFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_labVersementSepcialFocusLost

    private void labVersementSepcialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labVersementSepcialMouseClicked
        if (tabVersementSpecial.getSelectedRow() != -1) {
            int viewRow = tabVersementSpecial.getSelectedRow();
            int row = tabVersementSpecial.convertRowIndexToModel(viewRow);
            int id = (int) tabVersementSpecial.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            new VersementSpecialForm(homeForm, true, versementEntreprise.getEntreprise()).setVisible(true);
        }
    }//GEN-LAST:event_labVersementSepcialMouseClicked

    private void labVersementSepcialMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labVersementSepcialMouseEntered
        labVersementSepcial.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13));
    }//GEN-LAST:event_labVersementSepcialMouseEntered

    private void labVersementSepcialMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labVersementSepcialMouseExited
        labVersementSepcial.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12));
    }//GEN-LAST:event_labVersementSepcialMouseExited
    int lastRow = -1 ;
    private void tabVersementSpecialMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabVersementSpecialMouseReleased
        if (!SwingUtilities.isLeftMouseButton(evt)) {
            return;
        }

        int row = tabVersementSpecial.rowAtPoint(evt.getPoint());

        if (row == -1) {
            tabVersementSpecial.clearSelection();
            lastRow = -1;
            return;
        }

        if (lastRow == row) {
            tabVersementSpecial.clearSelection();
            lastRow = -1;
        } else {
            tabVersementSpecial.setRowSelectionInterval(row, row);
            lastRow = row;
        }
    }//GEN-LAST:event_tabVersementSpecialMouseReleased

    private void tabAchatSpecialMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabAchatSpecialMouseReleased
        if (!SwingUtilities.isLeftMouseButton(evt)) {
            return;
        }

        int row = tabAchatSpecial.rowAtPoint(evt.getPoint());

        if (row == -1) {
            tabAchatSpecial.clearSelection();
            lastRow = -1;
            return;
        }

        if (lastRow == row) {
            tabAchatSpecial.clearSelection();
            lastRow = -1;
        } else {
            tabAchatSpecial.setRowSelectionInterval(row, row);
            lastRow = row;
        }
    }//GEN-LAST:event_tabAchatSpecialMouseReleased

    private void btn_annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annulerActionPerformed
      if (tabVersementSpecial.getSelectedRow() != -1) {
            int viewRow = tabVersementSpecial.getSelectedRow();
            int row = tabVersementSpecial.convertRowIndexToModel(viewRow);
            int id = (int) tabVersementSpecial.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            new DetaillVersementEntrepriceForm(homeForm,true, versementEntreprise).setVisible(true);
        }
    }//GEN-LAST:event_btn_annulerActionPerformed

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased

    }//GEN-LAST:event_txt_searchKeyReleased

    private void combEntrepriseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combEntrepriseActionPerformed

    }//GEN-LAST:event_combEntrepriseActionPerformed

    private void btnSupprimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimActionPerformed
        if (tabVersementSpecial.getSelectedRow() != -1) {
            int viewRow = tabVersementSpecial.getSelectedRow();
            int row = tabVersementSpecial.convertRowIndexToModel(viewRow);
            int id = (int) tabVersementSpecial.getModel().getValueAt(row, 0);
            
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            VersementEntreprise LastVersementEntreprise=versementEntrepriseDAOImpl.getLastVersementEntreprise(versementEntreprise.getEntreprise());
    
            if(!versementEntreprise.equals(LastVersementEntreprise) &&
                    versementEntrepriseDAOImpl.getEtat(LastVersementEntreprise)==null ){
             exite.showMessage("خطأ", "لايمكنك الحذف لأنه توجد دفعات عادية تم تسجيلها بعد هذه الدفعة");
                return;
            }
            
            if(!versementEntreprise.equals(LastVersementEntreprise)){
                exite.showMessage("خطأ", "لايمكنك الحذف لأنه توجد دفعات مسجلة بعدها");
                return;
            }

            messageDialog.ShowConfirmMessageInFrame("تـأكـيد الـحـذف", "هـل أنت متـأكـد مـن حـذف دفعة الشركة");

            if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
                if (versementEntrepriseDAOImpl.delete(id) > 0) {
                    validationMessageDialog.showMessage("حـذف", "تم حذف دفعة الشركة بنجاح");
                    setVersementSpecialOnTab();
                } else {
                    boolean userExist = false;
                    ConfirmationSupprim confirmationSupprim = new ConfirmationSupprim(homeForm, true);
                    confirmationSupprim.setVisible(true);
                    userExist = confirmationSupprim.isExistUser();
                    if (userExist) {
                        if ( clientPayeParEntrepriseDAOImpl.deleteVersementEntreprise(id) > 0
                            && versementEntrepriseDAOImpl.delete(id) > 0) {
                            validationMessageDialog.showMessage("حـذف", "تم حذف عـمـلـيـة الدفع بنجاح");
                            setVersementSpecialOnTab();
                        }
                    }else{
                        exite.showMessage("خــطـأ", "لا يمكنك حذف دفعة الشركة");

                    }
                }
            }
        }
    }//GEN-LAST:event_btnSupprimActionPerformed

    private void btn_annuler1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_annuler1ActionPerformed
         if (tabVersementSpecial.getSelectedRow() != -1) {
            int viewRow = tabVersementSpecial.getSelectedRow();
            int row = tabVersementSpecial.convertRowIndexToModel(viewRow);
            int id = (int) tabVersementSpecial.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            new ModifyVersementEntreprise(homeForm, true, versementEntreprise).setVisible(true);
         }
    }//GEN-LAST:event_btn_annuler1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabNombre;
    private material.design.buttonRounder btnAdd2;
    private material.design.buttonRounder btnSupprim;
    private material.design.buttonRounder btn_annuler;
    private material.design.buttonRounder btn_annuler1;
    private material.design.ComboboxRoundNew combEntreprise;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labVersementSepcial;
    private javax.swing.JPanel panAchatSpecial;
    private ui.card.panRound panRound1;
    private javax.swing.JPanel panVersementSpecial;
    private javax.swing.JPanel pan_centre;
    private javax.swing.JTable tabAchatSpecial;
    private javax.swing.JTable tabVersementSpecial;
    private ui.table.TableScrollButton tableScrollButton1;
    private ui.table.TableScrollButton tableScrollButton2;
    private material.design.SearchTextRound txt_search;
    // End of variables declaration//GEN-END:variables
}
