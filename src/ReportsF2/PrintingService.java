/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;


import config.DatabaseConnection;
import dao.impl.ClientDAOImpl;
import entity.Client;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author farid
 */
public class PrintingService {

    private static final String REPORTS_PATH = "/reports/";

    public PrintingService() {

    }

    public void ImprimTousLesClients() {

        try {
            String TestQuery = "SELECT * FROM Client";
            JasperReport jasperreport;
            InputStream file = getClass().getResourceAsStream("/Reports/ListClient.jrxml");
            JasperDesign jasperdesign = JRXmlLoader.load(file);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(TestQuery);
            jasperdesign.setQuery(newQuery);
            Map parametres = new HashMap<String, Object>();
            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            Connection Cnx1 = DatabaseConnection.getInstance().getConnection();
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, parametres, Cnx1);
            // jp=JasperFillManager.fillReport(jr, parametres, cnx.getConnect());
            JasperViewer JspViewr = new JasperViewer(jasperprint, false);
            JspViewr.viewReport(jasperprint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in jasper Report" + e.getMessage());
        }
    }

    public void PrintClientById(int id, Map args) {

        try {
            String TestQuery = " SELECT Client.nom,Client.prenom,Produit.designation,Achat_detaille.prix_unitaire,  Achat_detaille.qty,  Achat_detaille.prix_total,  Achat_detaille.date_achat"
                    + " FROM (( Client INNER JOIN Achat ON Client.id = Achat.id_client)   "
                    + " INNER JOIN   Achat_detaille ON Achat.id = Achat_detaille.id_achat ) "
                    + " INNER JOIN Produit ON  Achat_detaille.id_produit = Produit.id  WHERE Client.id = " + id;

            JasperReport jasperreport;
            InputStream file = getClass().getResourceAsStream("/Reports/ListAchatByClientAndDateAchat.jrxml");
            JasperDesign jasperdesign = JRXmlLoader.load(file);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(TestQuery);
            jasperdesign.setQuery(newQuery);
            Map parametres = new HashMap<String, Object>();

            parametres.put("FNameClient", args.get("FNameClient"));
            parametres.put("DateAchat", args.get("DateAchat"));
            parametres.put("Enterprise", args.get("Enterprise"));

            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            Connection Cnx1 = DatabaseConnection.getInstance().getConnection();
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, parametres, Cnx1);
            // jp=JasperFillManager.fillReport(jr, parametres, cnx.getConnect());
            JasperViewer JspViewr = new JasperViewer(jasperprint, false);
            JspViewr.viewReport(jasperprint, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in jasper Report" + e.getMessage());
        }
    }

    public void printReport(String reportName) {

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream(REPORTS_PATH + reportName);

            Connection connection = DatabaseConnection.getInstance().getConnection();

            JasperPrint print = JasperFillManager.fillReport(
                    reportStream,
                    null,
                    connection
            );

            JasperViewer.viewReport(print, false);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void printReport(String reportName,
            Map<String, Object> parameters) {

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream(REPORTS_PATH + reportName);

            Connection connection = DatabaseConnection.getInstance().getConnection();

            JasperPrint print = JasperFillManager.fillReport(
                    reportStream,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(print, false);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        //new PrintingService().ImprimTousLesClients();
        PrintingService service_print = new PrintingService();
        Map<String, Object> params = new HashMap<>();
//        int clientId = 4;
//        ClientDAOImpl clientDao = new ClientDAOImpl(null);
//        Client client  = clientDao.findById(clientId);
//        
//        //params.put("CLIENT_ID", client.getId());
        params.put("CLIENT_ID", 4);
        params.put("FName", "Farid KHEBBACHE");
        params.put("ENTERPRISE_NAME_FR","CILAS");

        //service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_ID, params);
/*******************************************************************************/
        LocalDate specificDate = LocalDate.of(2026, 8,12);
//        
//        
//        params.put("Date_Achat", java.sql.Date.valueOf(specificDate));
//        params.put("ENTERPRISE_AFF",client.getEntreprise().getNom_ar());
//        
        //service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_DATE, params);
        //service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISES);
        // Map<String, Object> params = new HashMap<>();
        params = new HashMap<>();
        params.put("ENTERPRISE_NAME_FR", "CILAS");
        //service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_NAME_FR, params);

        /**
         * ***************************************************************************************
         */
        
        // pour test
        params.put("CLIENT_ID", 4);
        params.put("ENTERPRISE_AFF", "CILAS");
        params.put("YEAR", 2026);
        params.put("FName", "Farid KHEBBACHE");
        //service_print.printReport(ReportNames.CLIENT_PURCHASES_BY_YEAR, params);

        
        /**
         * *********************************************************************
         */
        // pour test
        params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
//        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
     //   service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID, params);
        
        
        /**********************************************************************/
        
        
        params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
//        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
      //  service_print.printReport(ReportNames.LIST_OF_CLIENT_BY_ID_ENTERPRISE, params);
        
        /***********************************************************************/
        
        params = new HashMap<>();
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
        
        params.put("YEAR", 2026);
//        params.put("FName", "Farid KHEBBACHE");
        //service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_ID_AND_YEAR, params);
        
        /***********************************************************************/
        //VERSEMENT_ENTERPRISE_BY_ID_AND_DATE
        params = new HashMap<>();
        specificDate = LocalDate.of(2026, 8,17);
        params.put("ENTERPRISE_ID", 2);
        params.put("ENTERPRISE_NAME_FR", "CILAS");
        params.put("DATE_VERSEMENT", java.sql.Date.valueOf(specificDate));
//        params.put("FName", "Farid KHEBBACHE");
        //service_print.printReport(ReportNames.VERSEMENT_ENTERPRISE_BY_ID_AND_DATE, params);
        
        /***********************************************************************/
        
        params = new HashMap<>();
        specificDate = LocalDate.of(2026, 8,14);

        params.put("DATE_VERSEMENT", java.sql.Date.valueOf(specificDate));
//        params.put("FName", "Farid KHEBBACHE");
//        service_print.printReport(ReportNames.ALL_VERSEMENT_ENTERPRISE_BY_DATE, params);
        
        /***********************************************************************/
    }

}
