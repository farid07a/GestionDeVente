/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package chart;

import chart.ModelChart;
import chartshadow.ModelData;
import config.DatabaseConnection;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author client
 */

public class costumChart extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
        DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    public costumChart() {
        initComponents();
        
        chart.setTitle("دج");
        chart.addLegend("دفـعـات الـشـركـات", Color.decode("#7b4397"), Color.decode("#dc2430"));
//        chart.addLegend("Chombres", Color.decode("#e65c00"), Color.decode("#F9D423"));
//        chart.addLegend("Services", Color.decode("#0099F7"), Color.decode("#F11712"));
        
setData2();
    //    test();
    }
    private void setData2() {

    try {

        chart.clear();

        String sql =
                "SELECT e.id, e.nom_ar AS entreprise, " +
                "       COALESCE(SUM(v.montant), 0) AS total_versement " +
                "FROM Entreprise e " +
                "LEFT JOIN Versement_Entreprise v " +
                "       ON e.id = v.id_entreprise " +
                "GROUP BY e.id, e.nom_ar " +
                "ORDER BY e.id ASC";

        PreparedStatement ps =
                DatabaseConnection.getInstance()
                        .getConnection()
                        .prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String entreprise = rs.getString("entreprise");

            double totalVersement =
                    rs.getDouble("total_versement");

            chart.addData(
                    new ModelChart(
                            entreprise,
                            new double[]{
                                totalVersement
                            }
                    )
            );
        }

        rs.close();
        ps.close();

        chart.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void setData() {
        try {
            List<ModelData> lists = new ArrayList<>();
            //DatabaseConnection.getInstance().connectToDatabase();
            
            
            String sql = "select DATE_FORMAT(Date,'%M') as `Month`, SUM(TotalAmount) as Amount, SUM(TotalCost) as Cost, SUM(TotalProfit) as Profit from orders group by DATE_FORMAT(Date,'%m%Y') order by Date DESC limit 7";
           
            String s="SELECT MONTH(birth_date) AS birth_date FROM [Hotel_NailZakaria].[dbo].[client] "; //Month
            //dbConnection obj=new dbConnection();
            //Connection cnx=obj.getConnection();
            
//            PreparedStatement p = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
//            ResultSet r = p.executeQuery();
//            while (r.next()) {
//                String month = r.getString("Month");
//                double amount = r.getDouble("Amount");
//                double cost = r.getDouble("Cost");
//                double profit = r.getDouble("Profit");
//                lists.add(new ModelData(month, amount, cost, profit));
//                
//            }
//            System.out.println("test.Test.setData()" );
//            r.close();
//            p.close();
            //  Add Data to chart
            for (int i = lists.size() - 1; i >= 0; i--) {
                ModelData d = lists.get(i);
                chart.addData(new ModelChart(d.getMonth(), new double[]{d.getAmount(), d.getCost(), d.getProfit()}));
            }
            //  Start to show data with animation
            chart.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test() {
        
      
            /*********************************************/
//            
//            chart.clear();
//            
//            int current_year=LocalDate.now().getYear();
//            LocalDate date1=LocalDate.of(current_year, 1, 1);
//            LocalDate date2=LocalDate.of(current_year, 1, 31);
//            List<Booking> listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("JAN", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 2, 1);
//            date2=LocalDate.of(current_year, 2, 28);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("FÉV", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 3, 1);
//            date2=LocalDate.of(current_year, 3, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("MAR", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 4, 1);
//            date2=LocalDate.of(current_year, 4, 30);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("AVR", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 5, 1);
//            date2=LocalDate.of(current_year, 5, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("MAI", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 6, 1);
//            date2=LocalDate.of(current_year, 6, 30);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("JUIN", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 7, 1);
//            date2=LocalDate.of(current_year, 7, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("JUIL", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 8, 1);
//            date2=LocalDate.of(current_year, 8, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("AOÛT", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 9, 1);
//            date2=LocalDate.of(current_year, 9, 30);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("SEP", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 10, 1);
//            date2=LocalDate.of(current_year, 10, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("OCT", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 11, 1);
//            date2=LocalDate.of(current_year, 11, 30);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("NOV", new double[]{listData.size()}));
//            
//            date1=LocalDate.of(current_year, 12, 1);
//            date2=LocalDate.of(current_year, 12, 31);
//            listData = new BookingDao().getAllReservationByDates(date1,date2);
//            chart.addData(new ModelChart("DÉC", new double[]{listData.size()}));
//            
            
        chart.addData(new ModelChart("JANVIER", new double[]{5, 50, 100}));
        chart.addData(new ModelChart("FÉVRIER", new double[]{600, 300, 150}));
        chart.addData(new ModelChart("MARS", new double[]{200, 50, 900}));
        chart.addData(new ModelChart("AVRIL", new double[]{480, 700, 100}));
        chart.addData(new ModelChart("MAI", new double[]{350, 540, 500}));
        chart.addData(new ModelChart(" JUIN", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" JUILLET", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" AOÛT", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" SEPTEMBRE ", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" OCTOBRE", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" NOVEMBRE", new double[]{450, 800, 100}));
        chart.addData(new ModelChart(" DÉCEMBRE", new double[]{450, 800, 100}));

chart.start();
       
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelShadow1 = new chartshadow.PanelShadow();
        chart = new chart.CurveLineChart();

        panelShadow1.setShadowColor(new java.awt.Color(153, 0, 0));

        chart.setForeground(new java.awt.Color(102, 102, 102));
        chart.setFillColor(true);
        chart.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        chart.setTitleFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.CurveLineChart chart;
    private chartshadow.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
