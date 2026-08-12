/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

import entity.Entreprise;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import material.design.ComboboxRound;
import ui.card.TextFieldRound;

public class FilterPanel extends JPanel {

    public TextFieldRound txtSearch;

    public ComboboxRound<String> cbEntreprise;
    public ComboboxRound<String> cbYear;
    public ComboboxRound<String> cbMonth;

//    public JDateChooser dateMin;
//    public JDateChooser dateMax;

    public JButton btnFilter;
    public JButton btnReset;

    public FilterPanel() {

        setOpaque(false);

        txtSearch = new TextFieldRound();

        cbEntreprise = new ComboboxRound<>();
        cbYear = new ComboboxRound<>();
        cbMonth = new ComboboxRound<>();

//        dateMin = new JDateChooser();
//        dateMax = new JDateChooser();

        btnFilter = new JButton("فلترة");
        btnReset = new JButton("إعادة");

        setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));

        add(txtSearch);
        add(cbEntreprise);
        add(cbYear);
        add(cbMonth);
//        add(dateMin);
//        add(dateMax);
        add(btnFilter);
        add(btnReset);

        txtSearch.setPreferredSize(new Dimension(180,35));
        cbEntreprise.setPreferredSize(new Dimension(170,35));
        cbYear.setPreferredSize(new Dimension(100,35));
        cbMonth.setPreferredSize(new Dimension(120,35));
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

}