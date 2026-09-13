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
import dao.impl.ClientDAOImpl;
import dao.impl.ClientPayeParEntrepriseDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.ProduitDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.ClientPayeParEntreprise;
import entity.Entreprise;
import enums.ModePaiement;
import home.HomeForm;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import material.design.designeTable;

public class VersementEntrepriseForme extends javax.swing.JDialog {

    Connection connection;
    HomeForm homeForm;
    AchatDAOImpl achatDAOImpl;
    ClientDAOImpl clientDAOImpl;
    ProduitDAOImpl produitDAOImpl;
    AchatDetailDAOImpl achatDetailDAOImpl;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    EntrepriseDAOImpl entrepriseDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    MessageDialog messageDialog;
    ValidationMessageDialog validationMessageDialog;
    Exite exite;
    Entreprise entreprise;
    ClientPayeParEntrepriseDAOImpl clientPayeParEntrepriseDAOImpl;
    int lastRow = -1;

    public VersementEntrepriseForme(java.awt.Frame parent, boolean modal, Entreprise entreprise) {
        super(parent, modal);

        this.homeForm = (HomeForm) parent;
        this.entreprise = entreprise;
        messageDialog = new MessageDialog(this);
        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        initComponents();

        setLocationRelativeTo(parent);

        new designeTable().setDesignTable(tab, jScrollPane1);

        new designeTable().SearchTable(tab, txt_searc);

        connection = DatabaseConnection.getInstance().getConnection();
        achatDAOImpl = new AchatDAOImpl(connection);
        clientDAOImpl = new ClientDAOImpl(connection);
        achatDetailDAOImpl = new AchatDetailDAOImpl(connection);
        produitDAOImpl = new ProduitDAOImpl(connection);
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        clientPayeParEntrepriseDAOImpl = new ClientPayeParEntrepriseDAOImpl(connection);
        tab.removeColumn(tab.getColumnModel().getColumn(0));

        validationMessageDialog = new ValidationMessageDialog(this, homeForm);
        messageDialog = new MessageDialog(this);
        exite = new Exite(this, homeForm);
       jLabel2.setText(jLabel2.getText()+" "+entreprise.getNom_ar() +" "+entreprise.getNom_fr() );
       
       setVersmentOnTab();
    }

    public void setVersmentOnTab() {
        double Montant = 0;
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        double TotalVersement = 0.0;
        double TotalAchatNoPayee = 0.0;
        double TotalAchat = 0.0;
        double credit = 0.0;
        double sommeAchat = 0.0;
        double Augment = 0.0;
        double crediteAchatNoPaye=0.0;

        List<entity.VersementEntreprise> versementEntreprises
                = versementEntrepriseDAOImpl.getVersementEntrepriseByIdEntreprise(entreprise);
        if (versementEntreprises != null) {
            TotalVersement = versementEntreprises.stream()
                    .mapToDouble(entity.VersementEntreprise::getMontant)
                    .sum();
        }

        entity.VersementEntreprise lastVersement = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);

        // credit || aug
        credit = (lastVersement != null && lastVersement.getReste_credit() >= 0) ? lastVersement.getReste_credit() : 0.0;
        Augment = (lastVersement != null && lastVersement.getReste_credit() < 0) ? lastVersement.getReste_credit() * -1 : 0.0;

        for (entity.VersementEntreprise versementEntreprise : versementEntreprises) {

            // List<Achat> achats = achatDAOImpl.getAchat;
            List<ClientPayeParEntreprise> clientPayeParEntreprises
                    = clientPayeParEntrepriseDAOImpl.getClientPayeeParVersement(versementEntreprise);
            if (clientPayeParEntreprises != null) {
                sommeAchat = clientPayeParEntreprises.stream()
                        .mapToDouble(c -> c.getAchat().getPrix_total())
                        .sum();
            }

            model.insertRow(0, new Object[]{entreprise.getId(),
                versementEntreprise.getMode_paiement().equals(ModePaiement.CHEQUE)?
                    versementEntreprise.getNumCheque():"/",
                  versementEntreprise.getMode_paiement(),
                versementEntreprise.getDate_versement(),
                formatter.format(credit),
                formatter.format(versementEntreprise.getMontant()),
                formatter.format(sommeAchat),
                entreprise.getNom_ar()
            });

            TotalAchat = TotalAchat + sommeAchat;
        }
        
        List<Achat>  achats = achatDAOImpl.getAchatNotInTabVersementByEntreprise(entreprise);
       
         if (achats != null) {
            TotalAchatNoPayee = achats.stream()
                    .mapToDouble(Achat::getPrix_total)
                    .sum();
        }
         crediteAchatNoPaye = credit+TotalAchatNoPayee;
        LabMontanTotal.setText(formatter.format(TotalVersement));
        LabAllCreditRest.setText(formatter.format(credit));
        LabAchatPayee.setText(formatter.format(TotalAchat));
        LabAugmentations2.setText(formatter.format(Augment));
        labNewAchat.setText(formatter.format(TotalAchatNoPayee));
        LabCreditAchatNoPayee.setText(formatter.format(crediteAchatNoPaye));
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
        panRound5 = new ui.card.panRound();
        LabAugmentations2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        panRound6 = new ui.card.panRound();
        jLabel7 = new javax.swing.JLabel();
        LabAllCreditRest = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labNewAchat = new javax.swing.JLabel();
        LabCreditAchatNoPayee = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        LabMontanTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        panRound1 = new ui.card.panRound();
        LabAchatPayee = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        txt_searc = new material.design.SearchTextRound();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panRound5.setColor1(new java.awt.Color(255, 255, 255));
        panRound5.setColor2(new java.awt.Color(224, 248, 237));

        LabAugmentations2.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAugmentations2.setForeground(new java.awt.Color(0, 102, 0));
        LabAugmentations2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAugmentations2.setText("0.00");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-dollar-bag-48.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(" الزيـادات ");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 0));
        jLabel18.setText("دج");

        javax.swing.GroupLayout panRound5Layout = new javax.swing.GroupLayout(panRound5);
        panRound5.setLayout(panRound5Layout);
        panRound5Layout.setHorizontalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addGap(16, 16, 16))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, 0)
                        .addComponent(LabAugmentations2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panRound5Layout.setVerticalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAugmentations2)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        panRound6.setColor1(new java.awt.Color(255, 255, 255));
        panRound6.setColor2(new java.awt.Color(255, 237, 231));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(236, 58, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("الديون من آخر دفعة ");

        LabAllCreditRest.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        LabAllCreditRest.setForeground(new java.awt.Color(236, 58, 102));
        LabAllCreditRest.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAllCreditRest.setText("0.00");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-48.png"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(236, 58, 102));
        jLabel19.setText("دج");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(236, 58, 102));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("المبيعات الجديدة");

        labNewAchat.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        labNewAchat.setForeground(new java.awt.Color(236, 58, 102));
        labNewAchat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNewAchat.setText("0.0");

        LabCreditAchatNoPayee.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        LabCreditAchatNoPayee.setForeground(new java.awt.Color(236, 58, 102));
        LabCreditAchatNoPayee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabCreditAchatNoPayee.setText("0.0");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(236, 58, 102));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("ديون+مبيعات");

        javax.swing.GroupLayout panRound6Layout = new javax.swing.GroupLayout(panRound6);
        panRound6.setLayout(panRound6Layout);
        panRound6Layout.setHorizontalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 0, 0)
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRound6Layout.createSequentialGroup()
                                .addComponent(LabAllCreditRest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2))
                            .addGroup(panRound6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(labNewAchat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(panRound6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(LabCreditAchatNoPayee, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panRound6Layout.setVerticalGroup(
            panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panRound6Layout.createSequentialGroup()
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabAllCreditRest, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labNewAchat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(panRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabCreditAchatNoPayee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        panRound2.setColor1(new java.awt.Color(255, 255, 255));
        panRound2.setColor2(new java.awt.Color(170, 232, 212));

        LabMontanTotal.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabMontanTotal.setForeground(new java.awt.Color(51, 102, 0));
        LabMontanTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabMontanTotal.setText("0.00");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-dollar-bag-48.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" مدفوعات الشركات");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 102, 0));
        jLabel20.setText("دج");

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel20)
                        .addGap(0, 0, 0)
                        .addComponent(LabMontanTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        panRound2Layout.setVerticalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabMontanTotal)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound1.setColor1(new java.awt.Color(255, 255, 255));
        panRound1.setColor2(new java.awt.Color(191, 204, 225));

        LabAchatPayee.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAchatPayee.setForeground(new java.awt.Color(0, 51, 153));
        LabAchatPayee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAchatPayee.setText("0.00");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-48.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("اجمالي المبيعات المدرجة في دفعات");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("دج");

        javax.swing.GroupLayout panRound1Layout = new javax.swing.GroupLayout(panRound1);
        panRound1.setLayout(panRound1Layout);
        panRound1Layout.setHorizontalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel11)
                        .addGap(0, 0, 0)
                        .addComponent(LabAchatPayee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );
        panRound1Layout.setVerticalGroup(
            panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAchatPayee)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "رقم الصك البريدي", "نوع الدفع", "تاريخ الدفع", "الديون", "مبلغ الدفعة", "قيمة المبيعات", "الشركة"
            }
        ));
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab);

        tableScrollButton1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        txt_searc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_searc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searcActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" مدفوعات   ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(panRound5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRound6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_searc, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(355, 355, 355))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 1169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panRound1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addComponent(txt_searc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1189, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 6, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 652, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void txt_searcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searcActionPerformed

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
            java.util.logging.Logger.getLogger(VersementEntrepriseForme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VersementEntrepriseForme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VersementEntrepriseForme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VersementEntrepriseForme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VersementEntrepriseForme dialog = new VersementEntrepriseForme(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel LabAchatPayee;
    private javax.swing.JLabel LabAllCreditRest;
    private javax.swing.JLabel LabAugmentations2;
    private javax.swing.JLabel LabCreditAchatNoPayee;
    private javax.swing.JLabel LabMontanTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labNewAchat;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound5;
    private ui.card.panRound panRound6;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    private material.design.SearchTextRound txt_searc;
    // End of variables declaration//GEN-END:variables
}
