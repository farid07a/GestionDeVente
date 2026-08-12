/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import material.design.TextField;
import ui.card.panRound;

import entity.Entreprise;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import material.design.ComboboxRoundNew;
import material.design.SearchTextRound;
import material.design.TextField;
import material.design.button;
import ui.card.TextFieldRound;
import ui.card.panRound;

public class PanFilterNew extends JPanel {

    // public TextField txtSearch;
    public SearchTextRound txtSearch;
    public ComboboxRoundNew<String> cbEntreprise;
    public ComboboxRoundNew<String> cbYear;
    public ComboboxRoundNew<String> cbMonth;
    public button btnFilter;
    public TextFieldRound txtDate;

    private JTable table;
    private TableRowSorter<?> sorter;

    private boolean initializing = false;

    public final ImageIcon filterAnnul
            = new ImageIcon(getClass().getResource(
                    "/icon/icons8-cancel-32.png"));
    public final ImageIcon filterIcon
            = new ImageIcon(getClass().getResource(
                    "/icon/icons8-filtre-35.png"));

    public final ImageIcon clearIcon
            = new ImageIcon(getClass().getResource(
                    "/icon/icons8-clear-filters-35.png"));

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public PanFilterNew() {

        setOpaque(false);

        txtSearch = new SearchTextRound();

        cbEntreprise = new ComboboxRoundNew<>();
        cbYear = new ComboboxRoundNew<>();
        cbMonth = new ComboboxRoundNew<>();
        txtDate = new TextFieldRound();
        btnFilter = new button();

        btnFilter.setIcon(filterAnnul);
        btnFilter.setColor2(Color.WHITE);
        btnFilter.setColor1(Color.WHITE);
        txtSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//        setLayout(new FlowLayout(
//                FlowLayout.RIGHT,
//                15,
//                15
//        ));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 13, 13);
        gbc.fill = GridBagConstraints.HORIZONTAL;
// Button
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(btnFilter, gbc);
        
         gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtDate, gbc);

// Month
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(cbMonth, gbc);

// Year
        gbc.gridx = 3;
        gbc.weightx = 0;
        add(cbYear, gbc);

// Entreprise
        gbc.gridx = 4;
        gbc.weightx = 0;
        add(cbEntreprise, gbc);

// Search
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtSearch, gbc);

        txtSearch.setPreferredSize(new Dimension(200, 40));
        txtSearch.setMinimumSize(new Dimension(200, 40));
//        add(btnFilter);
//        add(cbMonth);
//        add(cbYear);
//        add(cbEntreprise);
//        add(txtSearch);

    //    setColor1(Color.WHITE);

//        txtSearch.setPreferredSize(
//                new Dimension(180, 40)
//        );
        cbEntreprise.setPreferredSize(
                new Dimension(260, 40)
        );
        cbEntreprise.setMinimumSize(new Dimension(200, 40));

        cbYear.setPreferredSize(
                new Dimension(150, 40)
        );
        cbYear.setMinimumSize(new Dimension(200, 40));

        cbMonth.setPreferredSize(
                new Dimension(150, 40)
        );
        cbMonth.setMinimumSize(new Dimension(200, 40));

        initYearsCombo();
        initMonthsCombo();

        initListeners();
    }

    public String getEntreprise() {
        return cbEntreprise.getSelectedItem().toString();

    }

    public String getYear() {
        return cbYear.getSelectedItem().toString();
    }

    public String getMonth() {
        return cbMonth.getSelectedItem().toString();
    }
    
    public String getDate(){
    
        return txtDate.getText();
    }
    

    // =========================================================
    // SET TABLE
    // =========================================================
    public void setTable(JTable table) {

        this.table = table;

        if (table != null) {

            sorter = new TableRowSorter<>(
                    table.getModel()
            );

            table.setRowSorter(sorter);
        }
    }

    // =========================================================
    // LISTENERS
    // =========================================================
    private void initListeners() {

        // البحث مباشرة أثناء الكتابة
        txtSearch.getDocument().addDocumentListener(
                new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        // المؤسسة
        cbEntreprise.addActionListener(e -> {

            if (!initializing) {
                applyFilter();
            }
        });

        // السنة
        cbYear.addActionListener(e -> {

            if (!initializing) {
                applyFilter();
            }
        });

        // الشهر
        cbMonth.addActionListener(e -> {

            if (!initializing) {
                applyFilter();
            }
        });

        // زر Filter / Clear
        btnFilter.addActionListener(e -> {

            //   if (isFiltered()) {
            resetFields();

            //   } else {
            //      applyFilter();
            //   }
        });
    }

    // =========================================================
    // APPLY FILTER
    // =========================================================
    private void applyFilter() {

        if (initializing) {
            return;
        }

        if (sorter == null) {
            return;
        }

        final String search
                = txtSearch.getText()
                        .trim()
                        .toLowerCase();

        final String entreprise
                = cbEntreprise.getSelectedItem() == null
                ? "الــشركــات"
                : cbEntreprise.getSelectedItem()
                        .toString();

        final String year
                = cbYear.getSelectedItem() == null
                ? "كل السنوات"
                : cbYear.getSelectedItem()
                        .toString();

        final String month
                = cbMonth.getSelectedItem() == null
                ? "كل الشهور"
                : cbMonth.getSelectedItem()
                        .toString();

        sorter.setRowFilter(
                new RowFilter<Object, Object>() {

            @Override
            public boolean include(
                    Entry<?, ?> entry) {

                // =================================================
                // SEARCH في كامل الجدول
                // =================================================
                if (!search.isEmpty()) {

                    boolean found = false;

                    for (int column = 0;
                            column < entry.getValueCount();
                            column++) {

                        Object value
                                = entry.getValue(column);

                        if (value != null
                                && value.toString()
                                        .toLowerCase()
                                        .contains(search)) {

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        return false;
                    }
                }

                // =================================================
                // ENTREPRISE - COLUMN 7
                // =================================================
                if (!entreprise.equals("الــشركــات")) {

                    Object value
                            = entry.getValue(7);

                    if (value == null) {
                        return false;
                    }

                    if (!value.toString()
                            .equalsIgnoreCase(entreprise)) {

                        return false;
                    }
                }

                // =================================================
                // YEAR - COLUMN 2
                // =================================================
                if (!year.equals("كل السنوات")) {

                    Object value
                            = entry.getValue(2);

                    if (value == null) {
                        return false;
                    }

                    if (!value.toString()
                            .contains(year)) {

                        return false;
                    }
                }

                // =================================================
                // MONTH - COLUMN 2
                // =================================================
                if (!month.equals("كل الشهور")) {

                    Object value = entry.getValue(2);

                    if (value == null) {
                        return false;
                    }

                    String date = value.toString().trim();

                    if (date.length() >= 7) {

                        String monthNumber = date.substring(5, 7);

                        String selectedMonth = getMonthNumber(month);

                        if (!monthNumber.equals(selectedMonth)) {
                            return false;
                        }

                    } else {
                        return false;
                    }
                }

                return true;
            }
        });

        // updateButton();
    }

    // =========================================================
    // MONTH NUMBER
    // =========================================================
    private String getMonthNumber(String month) {

        switch (month) {
            case "جانفي":
                return "01";

            case "فيفري":
                return "02";

            case "مارس":
                return "03";

            case "أفريل":
                return "04";

            case "ماي":
                return "05";

            case "جوان":
                return "06";

            case "جويلية":
                return "07";

            case "أوت":
                return "08";

            case "سبتمبر":
                return "09";

            case "أكتوبر":
                return "10";

            case "نوفمبر":
                return "11";

            case "ديسمبر":
                return "12";

            default:
                return null;
        }
    }

    // =========================================================
    // YEARS
    // =========================================================
    public void initYearsCombo() {

        initializing = true;

        cbYear.removeAllItems();

        cbYear.addItem("كل السنوات");

        int currentYear
                = java.time.Year.now().getValue();

        int startYear = 2020;

        for (int y = currentYear;
                y >= startYear;
                y--) {

            cbYear.addItem(
                    String.valueOf(y)
            );
        }

        cbYear.setSelectedIndex(0);

        initializing = false;
    }

    // =========================================================
    // MONTHS
    // =========================================================
    public void initMonthsCombo() {

        initializing = true;

        cbMonth.removeAllItems();

        cbMonth.addItem("كل الشهور");

        String[] months = {
            "جانفي",
            "فيفري",
            "مارس",
            "أفريل",
            "ماي",
            "جوان",
            "جويلية",
            "أوت",
            "سبتمبر",
            "أكتوبر",
            "نوفمبر",
            "ديسمبر"
        };

        for (String month : months) {
            cbMonth.addItem(month);
        }

        cbMonth.setSelectedIndex(0);

        initializing = false;
    }

    // =========================================================
    // ENTREPRISE
    // =========================================================
    public void populateEntrepriseCombo(
            List<Entreprise> entreprises) {

        initializing = true;

        cbEntreprise.removeAllItems();

        cbEntreprise.addItem("الــشركــات");

        if (entreprises != null) {

            for (Entreprise ent : entreprises) {

                cbEntreprise.addItem(
                        ent.getNom_ar()
                );
            }
        }

        cbEntreprise.setSelectedIndex(0);

        initializing = false;
    }

    // =========================================================
    // CHECK FILTER
    // =========================================================
    public boolean isFiltered() {

        return !txtSearch.getText()
                .trim()
                .isEmpty()
                || (cbEntreprise.getSelectedItem() != null
                && !cbEntreprise.getSelectedItem()
                        .toString()
                        .equals("الــشركــات"))
                || (cbYear.getSelectedItem() != null
                && !cbYear.getSelectedItem()
                        .toString()
                        .equals("كل السنوات"))
                || (cbMonth.getSelectedItem() != null
                && !cbMonth.getSelectedItem()
                        .toString()
                        .equals("كل الشهور"));
    }

    // =========================================================
    // RESET
    // =========================================================
    public void resetFields() {

        initializing = true;

        txtSearch.setText("");

        if (cbEntreprise.getItemCount() > 0) {
            cbEntreprise.setSelectedIndex(0);
        }

        if (cbYear.getItemCount() > 0) {
            cbYear.setSelectedIndex(0);
        }

        if (cbMonth.getItemCount() > 0) {
            cbMonth.setSelectedIndex(0);
        }

        initializing = false;

        if (sorter != null) {
            sorter.setRowFilter(null);
        }

        // updateButton();
    }

    // =========================================================
    // BUTTON ICON
    // =========================================================
//    private void updateButton() {
//
//       // if (isFiltered()) {
//            btnFilter.setIcon(clearIcon);
//        } else {
//            btnFilter.setIcon(filterIcon);
//        }
//    }
}
