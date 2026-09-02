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
import frame.AllVersementCreditVent;
import frame.DetaillVersementEntrepriceForm;
import frame.EtatInitialForm;
import frame.VersementForm;
import home.HomeForm;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Window;
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
import javax.swing.JDialog;
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

        List<Entreprise> listEntreprises = entrepriseDAOImpl.findAll();
        panFilter.populateEntrepriseCombo(listEntreprises);

        panFilter.initYearsCombo();
        panFilter.initMonthsCombo();
        filter = new TableFilter(tab);
        panFilter.setTable(tab);

        panTop.setVisible(false);

        //   new designeTable().SearchTable(tab, txt_search);
        new designeTable().setDesignTable(tab, jScrollPane2);

        tab.getRowSorter().addRowSorterListener(e -> {
            SwingUtilities.invokeLater(() -> {
                LabNombre.setText(String.valueOf(tab.getRowCount()));
            });
        });
//        tab.getModel().addTableModelListener(e -> {
//            SwingUtilities.invokeLater(() -> {
//                LabNombre.setText(String.valueOf(tab.getRowCount()));
//                CalculMontonVersementApreFiltre();
//            });
//        });
        TableColumn column = tab.getColumnModel().getColumn(0);
        tab.getColumnModel().removeColumn(column);

        setVersmentOnTab();
        print();
        tab.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int selectedRow = tab.getSelectedRow();
            if (selectedRow == -1) {
                clearFields();
                return;
            }
            remplirLabEntreprise();
        });
      
    }

    public void print() {

        btnImp.addPopupItem("قـائـمـة مـدفـوعـات كل الـشـركـات", e -> {
            params = new HashMap<>();
            //ALL_VERSEMENT_ENTERPRISES
            service_print.printReport(Reports.ReportNames.ALL_VERSEMENT_ENTERPRISES, null);
        });

        btnImp.addPopupItem("قـائـمـة مـدفـوعـات كل الـشـركـات لسنة", e -> {

            if (panFilter.cbYear.getSelectedIndex() == 0) {
                exite.showMessage("تنبيه", "الرجاء اختيار السنة المحددة");
                return;
            }

            params = new HashMap<>();
            String year = panFilter.getYear();
            params.put("YEAR", Integer.parseInt(year));
            //ALL_VERSEMENT_ENTERPRISES
            service_print.printReport(Reports.ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_YEAR, params);
        });

        btnImp.addPopupItem("قـائــمـة مـدفـوعـات الـشـركة المحددة", e -> {
            if (panFilter.cbEntreprise.getSelectedIndex() == 0 && tab.getSelectedRow() == -1) {
                exite.showMessage("تنبيه", "الرجاء اختيار الـشـركة");
                return;
            }
            Entreprise entreprise = null;
            if (tab.getSelectedRow() != -1) {
                int viewRow = tab.getSelectedRow();
                int row = tab.convertRowIndexToModel(viewRow);
                int id = (int) tab.getModel().getValueAt(row, 0);
                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
                entreprise = versementEntreprise.getEntreprise();
            } else {
                String nomEntreprise = panFilter.getEntreprise();
                entreprise = entrepriseDAOImpl.getEntrepriseParName(nomEntreprise);
            }
            params.put("ENTERPRISE_ID", entreprise.getId());
            params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty()) ? entreprise.getNom_fr() : entreprise.getNom_ar());
            service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID, params);

        });

        btnImp.addPopupItem("قائـمة مـدفـوعــات للشركة في سنة ", e -> {
            //ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR
            if (tab.getSelectedRow() == -1 && panFilter.cbEntreprise.getSelectedIndex() == 0) {
                exite.showMessage("تنبيه", "الرجاء اختيار الشركة");
                return;
            }

            if (panFilter.cbYear.getSelectedIndex() == 0) {
                exite.showMessage("تنبيه", "الرجاء اختيار السنة المحددة");
                return;
            }
            Entreprise entreprise;
            if (tab.getSelectedRow() != -1) {
                int viewRow = tab.getSelectedRow();
                int row = tab.convertRowIndexToModel(viewRow);
                int id_versement = (int) tab.getModel().getValueAt(row, 0);
                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id_versement);
                entreprise = versementEntreprise.getEntreprise();
            } else {
                entreprise = entrepriseDAOImpl.getEntrepriseParName(panFilter.cbYear.getSelectedItem().toString());
            }

            String year = panFilter.getYear();
            params = new HashMap<>();

            params.put("ENTERPRISE_ID", entreprise.getId());
            params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty()) ? entreprise.getNom_fr() : entreprise.getNom_ar());
            params.put("YEAR", Integer.parseInt(year));

            service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR, params);

        }
        );

        btnImp.addPopupItem("قائـمة مـدفـوعــات بتاريخ ", e -> {
            //ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR

            if (tab.getSelectedRow() == -1 && panFilter.cbEntreprise.getSelectedIndex() == 0) {

                exite.showMessage("تنبيه", "الرجاء اختيار الشركة");
                return;
            }

            if (panFilter.txtDate.getText().isEmpty()) {
                exite.showMessage("تنبيه", "الرجاء اختيار التاريخ");
                return;
            }

            Entreprise entreprise;
            if (tab.getSelectedRow() != -1) {
                int viewRow = tab.getSelectedRow();
                int row = tab.convertRowIndexToModel(viewRow);
                int id_versement = (int) tab.getModel().getValueAt(row, 0);
                VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id_versement);
                entreprise = versementEntreprise.getEntreprise();
            } else {
                entreprise = entrepriseDAOImpl.getEntrepriseParName(panFilter.cbEntreprise.getSelectedItem().toString());
            }
            String dateFormat = "yyyy-MM-dd";
            DateTimeFormatter format_date = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate Date = LocalDate.parse(panFilter.getDate(), format_date);
            params = new HashMap<>();
            params.put("ENTERPRISE_ID", entreprise.getId());
            params.put("ENTERPRISE_NAME_FR", (!entreprise.getNom_fr().isEmpty()) ? entreprise.getNom_fr() : entreprise.getNom_ar());
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

    public void remplirLabEntreprise() {
        int selectedRow = tab.getSelectedRow();
        if (selectedRow == -1) {
            clearFields();
            return;
        }
        
            int row = tab.convertRowIndexToModel(selectedRow);
            int idVersement = (int) tab.getModel().getValueAt(row, 0);
        VersementEntreprise versement = versementEntrepriseDAOImpl.findById(idVersement);
        Entreprise entreprise = versement.getEntreprise();
        double totalVersementEntreprise = 0;
        List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getVersementEntrepriseByIdEntreprise(entreprise);
        VersementEntreprise LsatVersmentEntrepr = versementEntrepriseDAOImpl.getLastVersementEntreprise(entreprise);
        for (VersementEntreprise versementEntreprise : versementEntreprises) {
            totalVersementEntreprise = totalVersementEntreprise + versementEntreprise.getMontant();
        }
        LabSomVersemEntrep.setText((totalVersementEntreprise > 0) ? formatter.format(totalVersementEntreprise) : "0.0");
        LabVresement.setText(formatter.format(versement.getMontant()));
        labCredit.setText(formatter.format(LsatVersmentEntrepr.getReste_credit()));

    }

    public void clearFields() {
        LabSomVersemEntrep.setText("0.0");
        labCredit.setText("0.0");
        LabSomVersemEntrep.setText("0.0");

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
            model.insertRow(0, new Object[]{versementEntreprise.getId(),
                versementEntreprise.getRemarque(),
                versementEntreprise.getDate_versement(),
                CrediteVal,
                formatter.format(versementEntreprise.getTotal_credit()),
                formatter.format(montant),
                versementEntreprise.getMode_paiement(),
                versementEntreprise.getEntreprise().getNom_ar()});

            Montant = Montant + versementEntreprise.getMontant();
        }
        LabNombre.setText("" + versementEntreprises.size());
        LabMontanTotal.setText(formatter.format(Montant));
        MontantAchatAndCreditInLab();
    }

    public void MontantAchatAndCreditInLab() {
        double MontantTotalAchat = 0;
        double MontantTotalLastCredit = 0;
        List<Achat> Achats = achatDAOImpl.findAll();
        List<VersementEntreprise> versementEntreprises = versementEntrepriseDAOImpl.getLastVersementParEntreprises();

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
        LabSomVersemEntrep.setText(formatter.format(Montant));
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
        panFilter = new enums.PanFilterNew();
        btnAdd = new material.design.buttonRounder();
        btnAdd1 = new material.design.buttonRounder();
        btnAdd2 = new material.design.buttonRounder();
        btnImp = new material.design.buttonMenu();
        btnSupprim = new material.design.buttonRounder();
        jLabel2 = new javax.swing.JLabel();
        panButtom = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        button1 = new material.design.button();
        pan_gradiant5 = new ui.card.pan_gradiant();
        jLabel20 = new javax.swing.JLabel();
        LabSomVersemEntrep = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pan_gradiant6 = new ui.card.pan_gradiant();
        jLabel22 = new javax.swing.JLabel();
        LabVresement = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        pan_gradiant1 = new ui.card.pan_gradiant();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        labCredit = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        LabNombre = new javax.swing.JLabel();

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
        jLabel3.setText(" الـمــبيــعــات");

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
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
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
        jLabel5.setText("ديون الشركات");

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
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
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
        jLabel4.setText(" الزيـادات ");

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
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
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
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panTopLayout = new javax.swing.GroupLayout(panTop);
        panTop.setLayout(panTopLayout);
        panTopLayout.setHorizontalGroup(
            panTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(panRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panRound5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMouseReleased(evt);
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
                .addComponent(panFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );
        panRound4Layout.setVerticalGroup(
            panRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addGap(33, 33, 33))
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

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 255));
        jLabel2.setText("مـلـخـص مدفوعات الـشـركــات & الـديــون & الـمـبـيــعــات  ");
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 51, 255)));
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jLabel2FocusLost(evt);
            }
        });
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panCenterLayout = new javax.swing.GroupLayout(panCenter);
        panCenter.setLayout(panCenterLayout);
        panCenterLayout.setHorizontalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panCenterLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        panCenterLayout.setVerticalGroup(
            panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panCenterLayout.createSequentialGroup()
                .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnImp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSupprim, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panCenterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addGap(15, 15, 15)
                .addComponent(panRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
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

        pan_gradiant5.setColor1(new java.awt.Color(231, 246, 216));
        pan_gradiant5.setColor2(new java.awt.Color(231, 246, 216));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 153, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("مجموع الدفعات :");

        LabSomVersemEntrep.setBackground(new java.awt.Color(226, 250, 202));
        LabSomVersemEntrep.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabSomVersemEntrep.setForeground(new java.awt.Color(51, 153, 0));
        LabSomVersemEntrep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabSomVersemEntrep.setText("0.00");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 153, 0));
        jLabel21.setText("دج");

        javax.swing.GroupLayout pan_gradiant5Layout = new javax.swing.GroupLayout(pan_gradiant5);
        pan_gradiant5.setLayout(pan_gradiant5Layout);
        pan_gradiant5Layout.setHorizontalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabSomVersemEntrep, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(18, 18, 18))
        );
        pan_gradiant5Layout.setVerticalGroup(
            pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pan_gradiant5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(LabSomVersemEntrep, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_gradiant6.setColor1(new java.awt.Color(221, 221, 255));
        pan_gradiant6.setColor2(new java.awt.Color(221, 221, 255));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 102, 204));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("مبلغ الدفعة :");

        LabVresement.setBackground(new java.awt.Color(226, 250, 202));
        LabVresement.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabVresement.setForeground(new java.awt.Color(0, 102, 204));
        LabVresement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabVresement.setText("0.00");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 204));
        jLabel23.setText("دج");

        javax.swing.GroupLayout pan_gradiant6Layout = new javax.swing.GroupLayout(pan_gradiant6);
        pan_gradiant6.setLayout(pan_gradiant6Layout);
        pan_gradiant6Layout.setHorizontalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabVresement, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        pan_gradiant6Layout.setVerticalGroup(
            pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pan_gradiant6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(LabVresement, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pan_gradiant1.setBackground(new java.awt.Color(255, 246, 246));
        pan_gradiant1.setColor1(new java.awt.Color(255, 241, 241));
        pan_gradiant1.setColor2(new java.awt.Color(253, 232, 232));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("ديــون المتبقية  :");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 0, 51));
        jLabel18.setText("دج");

        labCredit.setBackground(new java.awt.Color(255, 229, 229));
        labCredit.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        labCredit.setForeground(new java.awt.Color(255, 0, 51));
        labCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labCredit.setText("0.00");

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-moins-29.png"))); // NOI18N

        javax.swing.GroupLayout pan_gradiant1Layout = new javax.swing.GroupLayout(pan_gradiant1);
        pan_gradiant1.setLayout(pan_gradiant1Layout);
        pan_gradiant1Layout.setHorizontalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gradiant1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labCredit, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(5, 5, 5)
                .addComponent(jLabel28))
        );
        pan_gradiant1Layout.setVerticalGroup(
            pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gradiant1Layout.createSequentialGroup()
                .addGroup(pan_gradiant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labCredit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setText("الــعــدد :");

        LabNombre.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        LabNombre.setForeground(new java.awt.Color(0, 0, 153));
        LabNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabNombre.setText("00");
        LabNombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 153)));

        javax.swing.GroupLayout panButtomLayout = new javax.swing.GroupLayout(panButtom);
        panButtom.setLayout(panButtomLayout);
        panButtomLayout.setHorizontalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panButtomLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(LabNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(71, 71, 71)
                .addComponent(pan_gradiant1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_gradiant6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panButtomLayout.setVerticalGroup(
            panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panButtomLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panButtomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panButtomLayout.createSequentialGroup()
                        .addGroup(panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pan_gradiant5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pan_gradiant6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panButtomLayout.createSequentialGroup()
                .addGroup(panButtomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_gradiant1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LabNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(panButtom);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        
        if (tab.getSelectedRow() != -1) {
            int viewRow = tab.getSelectedRow();
            int row = tab.convertRowIndexToModel(viewRow);
            int id = (int) tab.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            new DetaillVersementEntrepriceForm(homeForm,true, versementEntreprise).setVisible(true);

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
            int viewRow = tab.getSelectedRow();
            int row = tab.convertRowIndexToModel(viewRow);
            int id = (int) tab.getModel().getValueAt(row, 0);
            VersementEntreprise versementEntreprise = versementEntrepriseDAOImpl.findById(id);
            messageDialog.ShowConfirmMessageInFrame("تـأكـيد الـحـذف", "هـل أنت متـأكـد مـن حـذف دفعة الشركة");
            if (messageDialog.getMessageType() == MessageDialog.MessageType.YES) {
                if (versementEntrepriseDAOImpl.delete(id) > 0) {
                    validationMessageDialog.showMessage("حـذف", "تم حذف دفعة الشركة بنجاح");
                    setVersmentOnTab();
                } else {
                    exite.showMessage("خــطـأ", "لا يمكنك حذف دفعة الشركة");
                }
            }
        }
    }//GEN-LAST:event_btnSupprimActionPerformed

    private void jLabel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel2FocusGained
    }//GEN-LAST:event_jLabel2FocusGained

    private void jLabel2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel2FocusLost
    }//GEN-LAST:event_jLabel2FocusLost

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12));

    }//GEN-LAST:event_jLabel2MouseExited
    private int lastRow = -1;
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

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        new AllVersementCreditVent(homeForm, true).setVisible(true);
    }//GEN-LAST:event_jLabel2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabAchatTotal;
    private javax.swing.JLabel LabAllCreditRest;
    private javax.swing.JLabel LabAugmentations;
    private javax.swing.JLabel LabMontanTotal;
    private javax.swing.JLabel LabNombre;
    private javax.swing.JLabel LabSomVersemEntrep;
    private javax.swing.JLabel LabVresement;
    private material.design.buttonRounder btnAdd;
    private material.design.buttonRounder btnAdd1;
    private material.design.buttonRounder btnAdd2;
    private material.design.buttonMenu btnImp;
    private material.design.buttonRounder btnSupprim;
    private material.design.button button1;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labCredit;
    private javax.swing.JPanel panButtom;
    private javax.swing.JPanel panCenter;
    private enums.PanFilterNew panFilter;
    private ui.card.panRound panRound1;
    private ui.card.panRound panRound2;
    private ui.card.panRound panRound3;
    private ui.card.panRound panRound4;
    private ui.card.panRound panRound5;
    private javax.swing.JPanel panTop;
    private ui.card.pan_gradiant pan_gradiant1;
    private ui.card.pan_gradiant pan_gradiant5;
    private ui.card.pan_gradiant pan_gradiant6;
    private javax.swing.JTable tab;
    private ui.table.TableScrollButton tableScrollButton1;
    // End of variables declaration//GEN-END:variables
}
