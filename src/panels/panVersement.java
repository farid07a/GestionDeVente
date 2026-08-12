/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import DialogFram.Exite;
import DialogFram.MessageDialog;
import DialogFram.ValidationMessageDialog;
import Reports.PrintingService;
import Reports.ReportNames;
import config.DatabaseConnection;
import dao.impl.AchatDAOImpl;
import dao.impl.EntrepriseDAOImpl;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.Entreprise;
import entity.Nomber;
import entity.VersementEntreprise;
import enums.TableFilter;
import enums.excel;
import frame.DetaillVersementEntrepriceForm;
import frame.EtatInitialForm;
import frame.VersementForm;
import frame.VersementFormNM;
import home.HomeForm;
import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import material.design.designeTable;

/**
 *
 * @author pc
 */
public class panVersement extends javax.swing.JPanel {

    Connection connection;
    VersementEntrepriseDAOImpl versementEntrepriseDAOImpl;
    AchatDAOImpl achatDAOImpl;
    DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    HomeForm homeForm;
    EntrepriseDAOImpl entrepriseDAOImpl;
    TableFilter filter;

    PrintingService service_print = new PrintingService();
    Map<String, Object> params = new HashMap<>();
    ValidationMessageDialog validationMessageDialog;
    MessageDialog messageDialog;
    Exite exite;

    public panVersement(HomeForm homeForm) {

        initComponents();
        this.homeForm = homeForm;
        connection = DatabaseConnection.getInstance().getConnection();
        versementEntrepriseDAOImpl = new VersementEntrepriseDAOImpl(connection);
        entrepriseDAOImpl = new EntrepriseDAOImpl(connection);
        achatDAOImpl = new AchatDAOImpl(connection);
        validationMessageDialog = new ValidationMessageDialog(homeForm);
        messageDialog = new MessageDialog(homeForm);
        exite = exite = new Exite(homeForm);

        setVersmentOnTab();
        List<Entreprise> listEntreprises = entrepriseDAOImpl.findAll();

        panFilterNew1.initYearsCombo();
        panFilterNew1.initMonthsCombo();
        panFilterNew1.populateEntrepriseCombo(listEntreprises);
        filter = new TableFilter(tab);
        panFilterNew1.setTable(tab);

        //   new designeTable().SearchTable(tab, txt_search);
        new designeTable().setDesignTable(tab, jScrollPane2);

        tab.getRowSorter().addRowSorterListener(e -> {
            SwingUtilities.invokeLater(() -> {
                LabNombre.setText(String.valueOf(tab.getRowCount()));
                CalculMontonVersementApreFiltre();
            });
        });

        tab.getModel().addTableModelListener(e -> {
            SwingUtilities.invokeLater(() -> {
                LabNombre.setText(String.valueOf(tab.getRowCount()));
                CalculMontonVersementApreFiltre();
            });
        });

        TableColumn column = tab.getColumnModel().getColumn(0);
        tab.getColumnModel().removeColumn(column);
        print();
    }

    public void print() {

        btnImp.addPopupItem("قـائـمـة مـدفـوعـات كل الـشـركـات", e -> {
            params = new HashMap<>();
            //ALL_VERSEMENT_ENTERPRISES
            service_print.printReport(Reports.ReportNames.ALL_VERSEMENT_ENTERPRISES, null);
        });

        btnImp.addPopupItem("قـائـمـة مـدفـوعـات كل الـشـركـات لسنة", e -> {
            
            if (panFilterNew1.cbYear.getSelectedIndex()==-1) {
                exite.showMessage("تنبيه", "الرجاء اختيار السنة المحددة");
                return;
            }
            
            params = new HashMap<>();
            String year = panFilterNew1.getYear();
            params.put("YEAR", Integer.parseInt(year));
            //ALL_VERSEMENT_ENTERPRISES
            service_print.printReport(Reports.ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_YEAR, null);
        });
        
        
        btnImp.addPopupItem("قـائــمـة مـدفـوعـات الـشـركة المحددة", e -> {
            if (tab.getSelectedRow() != -1) {
                int row = tab.getSelectedRow();
                int id = (int) tab.getModel().getValueAt(row, 0);

                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
                Entreprise entreprise = versementEntreprise.getEntreprise();
                params.put("ENTERPRISE_ID", entreprise.getId());
                params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty()) ? entreprise.getNom_fr() : entreprise.getNom_ar());
                service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID, params);
            }
        });

        btnImp.addPopupItem("قائـمة مـدفـوعــات للشركة في سنة ", e -> {
            //ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR
            
            
            if (tab.getSelectedRow() == -1 &&  panFilterNew1.cbEntreprise.getSelectedIndex() == -1) {
            
                exite.showMessage("تنبيه", "الرجاء اختيار الشركة");
                return;
            }
            
            if (panFilterNew1.cbYear.getSelectedIndex()==-1) {
                exite.showMessage("تنبيه", "الرجاء اختيار السنة المحددة");
                return;
            }
            
            int id_enterprise ;
            Entreprise entreprise;
            if (tab.getSelectedRow() != -1){
                int row = tab.getSelectedRow();
                int id_versement = (int) tab.getModel().getValueAt(row, 0);
                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id_versement);
                entreprise = versementEntreprise.getEntreprise();
            }else
            {
             entreprise = entrepriseDAOImpl.getEntrepriseParName(panFilterNew1.cbYear.getSelectedItem().toString());
            }
            
                String year = panFilterNew1.getYear();
                params = new HashMap<>();

                params.put("ENTERPRISE_ID", entreprise.getId());
                params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty()) ? entreprise.getNom_fr() : entreprise.getNom_ar());
                params.put("YEAR", Integer.parseInt(year));

                service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR, params);
            
        }
        );

        btnImp.addPopupItem("قائـمة مـدفـوعــات بتاريخ ", e -> {
            //ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR

        if (tab.getSelectedRow() == -1 &&  panFilterNew1.cbEntreprise.getSelectedIndex() == -1) {
            
                exite.showMessage("تنبيه", "الرجاء اختيار الشركة");
                return;
            }
            
            if (panFilterNew1.cbYear.getSelectedIndex()==-1) {
                exite.showMessage("تنبيه", "الرجاء اختيار التاريخ");
                return;
            }
            
            int id_enterprise ;
            Entreprise entreprise;
            if (tab.getSelectedRow() != -1){
                int row = tab.getSelectedRow();
                int id_versement = (int) tab.getModel().getValueAt(row, 0);
                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id_versement);
                entreprise = versementEntreprise.getEntreprise();
            }else
            {
             entreprise = entrepriseDAOImpl.getEntrepriseParName(panFilterNew1.cbYear.getSelectedItem().toString());
            }
                String dateFormat = "dd-MM-yyyy";
                DateTimeFormatter format_date = DateTimeFormatter.ofPattern(dateFormat);
                LocalDate Date = LocalDate.parse(panFilterNew1.getDate(),format_date);
                params = new HashMap<>();
                params.put("ENTERPRISE_ID", entreprise.getId());
                params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty())? entreprise.getNom_fr() : entreprise.getNom_ar() );
                params.put("DATE_VERSEMENT", java.sql.Date.valueOf(Date));

                service_print.printReport(ReportNames.VERSEMENT_ENTERPRISE_BY_ID_AND_DATE, params);
        
        }
        );
        
        
        
        
        

        /*params = new HashMap<>();
        specificDate = LocalDate.of(2026, 8,17);
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
        params.put("DATE_VERSEMENT", java.sql.Date.valueOf(specificDate));
//        params.put("FName", "Farid KHEBBACHE");
        //service_print.printReport(ReportNames.VERSEMENT_ENTERPRISE_BY_ID_AND_DATE, params);**/
    }

    public void setVersmentOnTab() {
        double Montant = 0;
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);

        List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.findAll();
        for (VersementEntreprise versementEntreprise : versementEntreprises) {

            double montant = versementEntreprise.getMontant();

            double Credite = versementEntreprise.getReste_credit();
            String CrediteVal = formatter.format(Credite);
            if (Credite < 0) {
                CrediteVal = "+ " + formatter.format(-1 * Credite);
            }
            model.addRow(new Object[]{versementEntreprise.getId(),
                versementEntreprise.getRemarque(),
                versementEntreprise.getDate_versement(),
                CrediteVal,
                formatter.format(versementEntreprise.getTotal_credit()),
                formatter.format(montant),
                versementEntreprise.getMode_paiement(),
                versementEntreprise.getEntreprise().getNom_ar()});

            Montant = Montant + versementEntreprise.getMontant();
        }
        LabMontanTotal.setText(formatter.format(Montant));
        MontantAchatAndCreditInLab();
    }

    public void MontantAchatAndCreditInLab() {
        double MontantTotalAchat = 0;
        double MontantTotalLastCredit = 0;
        List<Achat> Achats = achatDAOImpl.findAll();
        List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getLastVersementParEntreprise();

        for (Achat achat : Achats) {
            MontantTotalAchat = MontantTotalAchat + achat.getPrix_total();
        }
        for (VersementEntreprise versementEntreprise : versementEntreprises) {
            MontantTotalLastCredit = MontantTotalLastCredit + versementEntreprise.getReste_credit();
        }

        LabAchatTotal.setText(formatter.format(MontantTotalAchat));
        if (MontantTotalLastCredit > 0) {
            LabAllCreditRest.setText(formatter.format(MontantTotalLastCredit));
        } else {
            LabAllCreditRest.setText(formatter.format(00));
            LabAugmentations.setText(formatter.format(00));
            if (MontantTotalLastCredit < 0) {
                LabAugmentations.setText("+ " + formatter.format(-1 * MontantTotalLastCredit));
            }

        }

    }

    public void CalculMontonVersementApreFiltre() {
        double Montant = 0;
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        int rows = model.getRowCount();
        System.out.println(" count " + tab.getRowCount());
        for (int i = 0; i < tab.getRowCount(); i++) {
            int modelRow = tab.convertRowIndexToModel(i);
            double versment = new Nomber().getNbDouble(tab.getModel().getValueAt(modelRow, 5).toString());
            Montant = Montant + versment;
        }
        LabTotalVresementFiltre.setText(formatter.format(Montant));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panTop = new javax.swing.JPanel();
        panRound1 = new ui.card.panRound();
        LabAchatTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panRound2 = new ui.card.panRound();
        LabMontanTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panRound5 = new ui.card.panRound();
        jLabel5 = new javax.swing.JLabel();
        LabAllCreditRest = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        panRound3 = new ui.card.panRound();
        LabAugmentations = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        panCenter = new javax.swing.JPanel();
        panRound4 = new ui.card.panRound();
        tableScrollButton1 = new ui.table.TableScrollButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        panFilterNew1 = new enums.PanFilterNew();
        btnAdd = new material.design.buttonRounder();
        btnAdd1 = new material.design.buttonRounder();
        btnAdd2 = new material.design.buttonRounder();
        btnImp = new material.design.buttonMenu();
        LabNombre = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        LabTotalVresementFiltre = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnSupprim = new material.design.buttonRounder();
        panButtom = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        button1 = new material.design.button();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        panTop.setBackground(new java.awt.Color(255, 255, 255));
        panTop.setMaximumSize(new java.awt.Dimension(32767, 90));
        panTop.setMinimumSize(new java.awt.Dimension(100, 90));
        panTop.setName(""); // NOI18N
        panTop.setPreferredSize(new java.awt.Dimension(0, 90));

        panRound1.setColor1(new java.awt.Color(255, 255, 255));
        panRound1.setColor2(new java.awt.Color(191, 204, 225));

        LabAchatTotal.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAchatTotal.setForeground(new java.awt.Color(0, 51, 153));
        LabAchatTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAchatTotal.setText("0.00");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-panier-rapide-48.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("اجـمــالي الـمــبيــعــات");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("دج");

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
                        .addComponent(jLabel6)
                        .addGap(0, 0, 0)
                        .addComponent(LabAchatTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(panRound1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
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
                            .addComponent(LabAchatTotal)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
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
        jLabel1.setText("اجـمــالي الدفـعـات");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 102, 0));
        jLabel14.setText("دج");

        javax.swing.GroupLayout panRound2Layout = new javax.swing.GroupLayout(panRound2);
        panRound2.setLayout(panRound2Layout);
        panRound2Layout.setHorizontalGroup(
            panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addGroup(panRound2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel14)
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
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound5.setColor1(new java.awt.Color(255, 255, 255));
        panRound5.setColor2(new java.awt.Color(255, 237, 231));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(236, 58, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("الــديـــون الــمــتـبـقــية ");

        LabAllCreditRest.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        LabAllCreditRest.setForeground(new java.awt.Color(236, 58, 102));
        LabAllCreditRest.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAllCreditRest.setText("0.00");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-cash-48.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(236, 58, 102));
        jLabel15.setText("دج");

        javax.swing.GroupLayout panRound5Layout = new javax.swing.GroupLayout(panRound5);
        panRound5.setLayout(panRound5Layout);
        panRound5Layout.setHorizontalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(0, 0, 0)
                        .addComponent(LabAllCreditRest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(16, 16, 16))
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addGap(36, 36, 36))))
        );
        panRound5Layout.setVerticalGroup(
            panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panRound5Layout.createSequentialGroup()
                        .addGroup(panRound5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAllCreditRest, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panRound3.setColor1(new java.awt.Color(255, 255, 255));
        panRound3.setColor2(new java.awt.Color(224, 248, 237));

        LabAugmentations.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        LabAugmentations.setForeground(new java.awt.Color(0, 102, 0));
        LabAugmentations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabAugmentations.setText("0.00");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-dollar-bag-48.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("اجمالي الزيـادات ");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 0));
        jLabel16.setText("دج");

        javax.swing.GroupLayout panRound3Layout = new javax.swing.GroupLayout(panRound3);
        panRound3.setLayout(panRound3Layout);
        panRound3Layout.setHorizontalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                        .addGap(16, 16, 16))
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, 0)
                        .addComponent(LabAugmentations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panRound3Layout.setVerticalGroup(
            panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound3Layout.createSequentialGroup()
                .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRound3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabAugmentations)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panTopLayout = new javax.swing.GroupLayout(panTop);
        panTop.setLayout(panTopLayout);
        panTopLayout.setHorizontalGroup(
            panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panRound5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panTopLayout.setVerticalGroup(
            panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panRound5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );

        add(panTop);

        panCenter.setBackground(new java.awt.Color(255, 255, 255));
        panCenter.setPreferredSize(new java.awt.Dimension(100, 573));

        panRound4.setColor1(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tab.setFont(new java.awt.Font("Cairo", 1, 15)); // NOI18N
        tab.setForeground(new java.awt.Color(102, 102, 102));
        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idEntreprise", "المـلاحــضــات", "التاريخ", "ديون المتبقية", "ديون الزبائن", "المبلغ", "نوع الدفع", "الشركة"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tab);

        tableScrollButton1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout panRound4Layout = new javax.swing.GroupLayout(panRound4);
        panRound4.setLayout(panRound4Layout);
        panRound4Layout.setHorizontalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRound4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
            .addGroup(panRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panFilterNew1, javax.swing.GroupLayout.DEFAULT_SIZE, 992, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );
        panRound4Layout.setVerticalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panFilterNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        btnAdd.setBackground(new java.awt.Color(204, 0, 204));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-recherche-immobilière-30.png"))); // NOI18N
        btnAdd.setText("مــعــايــنــة");
        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnAdd1.setBackground(new java.awt.Color(22, 163, 74));
        btnAdd1.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btnAdd1.setText(" دفـع جـديــد");
        btnAdd1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
            }
        });

        btnAdd2.setBackground(new java.awt.Color(76, 143, 143));
        btnAdd2.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-plus-64 (2).png"))); // NOI18N
        btnAdd2.setText("دفعة إسـتــثــنــا ئــيــة");
        btnAdd2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ActionPerformed(evt);
            }
        });

        btnImp.setBackground(new java.awt.Color(51, 204, 255));
        btnImp.setForeground(new java.awt.Color(255, 255, 255));
        btnImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button/icons8-print-48.png"))); // NOI18N
        btnImp.setText("طــباعـة التـقـاريــر");
        btnImp.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpActionPerformed(evt);
            }
        });

        LabNombre.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabNombre.setForeground(new java.awt.Color(0, 0, 153));
        LabNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabNombre.setText("00");
        LabNombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 153)));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("الــعــدد :");

        LabTotalVresementFiltre.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabTotalVresementFiltre.setForeground(new java.awt.Color(51, 153, 0));
        LabTotalVresementFiltre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabTotalVresementFiltre.setText("00");
        LabTotalVresementFiltre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 204, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("دج");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel12.setText("مــجــمــوع الــدفــعات :");

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

        javax.swing.GroupLayout panCenterLayout = new javax.swing.GroupLayout(panCenter);
        panCenter.setLayout(panCenterLayout);
        panCenterLayout.setHorizontalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(LabNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(147, 147, 147)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabTotalVresementFiltre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addContainerGap())
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(111, 111, 111)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))))
        );
        panCenterLayout.setVerticalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCenterLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSupprim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(panRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LabTotalVresementFiltre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );

        add(panCenter);

        panButtom.setBackground(new java.awt.Color(255, 255, 255));
        panButtom.setMaximumSize(new java.awt.Dimension(32767, 50));
        panButtom.setMinimumSize(new java.awt.Dimension(100, 50));
        panButtom.setPreferredSize(new java.awt.Dimension(100, 50));

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ms-excel-55.png"))); // NOI18N
        button1.setColor1(new java.awt.Color(255, 255, 255));
        button1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        button1.setMaximumSize(new java.awt.Dimension(48, 40));
        button1.setMinimumSize(new java.awt.Dimension(48, 40));
        button1.setPreferredSize(new java.awt.Dimension(48, 40));
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panButtomLayout = new javax.swing.GroupLayout(panButtom);
        panButtom.setLayout(panButtomLayout);
        panButtomLayout.setHorizontalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panButtomLayout.createSequentialGroup()
                .addContainerGap(789, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jLabel7)
                .addGap(169, 169, 169))
        );
        panButtomLayout.setVerticalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panButtomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panButtomLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(panButtom);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (tab.getSelectedRow() != -1) {
            int row = tab.getSelectedRow();
            int id = (int) tab.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            new DetaillVersementEntrepriceForm(this.homeForm, true, versementEntreprise).setVisible(true);

        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd1ActionPerformed
        new VersementForm(this.homeForm, true).setVisible(true);
    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        List<VersementEntreprise> list
                = versementEntrepriseDAOImpl.findAll();

        JFileChooser chooser = new JFileChooser();

        chooser.setSelectedFile(new File("تقرير_دفعات_المؤسسات .xlsx"));

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                new excel().exportVersementEntreprise(list, chooser.getSelectedFile());

                JOptionPane.showMessageDialog(null,
                        "Le fichier Excel a été créé avec succès.");
            } catch (Exception ex) {
                Logger.getLogger(panVersement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_button1ActionPerformed

    private void btnAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ActionPerformed
        new EtatInitialForm(homeForm, true).setVisible(true);
    }//GEN-LAST:event_btnAdd2ActionPerformed

    private void btnImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImpActionPerformed

    private void btnSupprimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimActionPerformed
        if (tab.getSelectedRow() != -1) {
            int row = tab.getSelectedRow();
            int id = (int) tab.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            messageDialog.ShowConfirmMessageInFrame("تـأكـيد الـحـذف", "هـل أنت متـأكـد مـن حـذف الـنـوعـيـة");
            if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
                if (versementEntrepriseDAOImpl.delete(id) > 0) {
                    validationMessageDialog.showMessage("حـذف", "تم حذف الـنـوعـيـة بنجاح");
                    setVersmentOnTab();
                } else {
                    exite.showMessage("خــطـأ", "لا يمكنك حذف الـنـوعـية");
                }
            }
        }
    }//GEN-LAST:event_btnSupprimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabAchatTotal;
    private javax.swing.JLabel LabAllCreditRest;
    private javax.swing.JLabel LabAugmentations;
    private javax.swing.JLabel LabMontanTotal;
    private javax.swing.JLabel LabNombre;
    private javax.swing.JLabel LabTotalVresementFiltre;
    private material.design.buttonRounder btnAdd;
    private material.design.buttonRounder btnAdd1;
    private material.design.buttonRounder btnAdd2;
    private material.design.buttonMenu btnImp;
    private material.design.buttonRounder btnSupprim;
    private material.design.button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panButtom;
    private javax.swing.JPanel panCenter;
    private enums.PanFilterNew panFilterNew1;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound3;
    private ui.card.panRound panRound4;
    private ui.card.panRound panRound5;
    private javax.swing.JPanel panTop;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    // End of variables declaration//GEN-END:variables
}
