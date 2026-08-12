/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

import entity.Entreprise;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.ImageIcon;
import material.design.ComboboxRoundNew;
import material.design.TextField;
import material.design.button;
import ui.card.TextFieldRound;
import ui.card.panRound;

public class PanFilter2 extends panRound {

    public  TextField txtSearch;
    public ComboboxRoundNew<String> cbEntreprise;
    public ComboboxRoundNew<String> cbYear;
    public ComboboxRoundNew<String> cbMonth;
public boolean isFiltered = false;
//    public JDateChooser dateMin;
//    public JDateChooser dateMax;
    TableFilter tableFilter;
    public button btnFilter;
    //  public button btnReset;
public final ImageIcon filterIcon = new ImageIcon(getClass().getResource("/icon/icons8-filtre-35.png"));
    public final ImageIcon clearIcon = new ImageIcon(getClass().getResource("/icon/icons8-clear-filters-35.png"));
    public PanFilter2() {

        setOpaque(false);

        txtSearch = new TextField();

        cbEntreprise = new ComboboxRoundNew<>();
        cbYear = new ComboboxRoundNew<>();
        cbMonth = new ComboboxRoundNew<>();

//        dateMin = new JDateChooser();
//        dateMax = new JDateChooser();

       btnFilter = new button();
        
       btnFilter.setIcon(filterIcon);
       btnFilter.setColor2(Color.white);
        btnFilter.setColor1(Color.white);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        add(txtSearch);
        add(cbEntreprise);
        add(cbYear);
        add(cbMonth);
//        add(dateMin);
//        add(dateMax);
        add(btnFilter);
        //   add(btnReset);
        setColor1(Color.WHITE);
        txtSearch.setPreferredSize(new Dimension(180, 40));
        cbEntreprise.setPreferredSize(new Dimension(260, 40));
        cbYear.setPreferredSize(new Dimension(150, 40));       
        cbMonth.setPreferredSize(new Dimension(150, 40));
        
//        dateMin.setPreferredSize(new Dimension(120,35));
//        dateMax.setPreferredSize(new Dimension(120,35));
    

    }

    public void initYearsCombo() {
        cbYear.removeAllItems();
        cbYear.addItem("كل السنوات"); // خيار افتراضي للجميع

        int currentYear = java.time.Year.now().getValue();
        int startYear = 2020; // سنة البداية في نظامك

        for (int y = currentYear; y >= startYear; y--) {
            cbYear.addItem(String.valueOf(y));
        }
    }

    public void initMonthsCombo() {
        cbMonth.removeAllItems();
        cbMonth.addItem("كل الشهور");

//    // إما بالأرقام المنسقة (01, 02... 12) لتناسب التصفية النصية
//    for (int m = 1; m <= 12; m++) {
//        cbMonth.addItem(String.format("%02d", m));
//    }
        String[] months = {"جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان",
            "جويلية", "أوت", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};
        for (String month : months) {
            cbMonth.addItem(month);
        }

    }

    public void populateEntrepriseCombo(List<Entreprise> entreprises) {
        cbEntreprise.removeAllItems();
        cbEntreprise.addItem("الكل");
        for (Entreprise ent : entreprises) {
            // نضع اسم المؤسسة في الكومبوبوكس
            cbEntreprise.addItem(ent.getNom_ar());
        }
    }

public void resetFields() {
        txtSearch.setText("");
        cbEntreprise.setSelectedIndex(0);
        cbYear.setSelectedIndex(0);
        cbMonth.setSelectedIndex(0);
    }
}
