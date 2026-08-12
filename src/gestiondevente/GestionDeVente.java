/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestiondevente;

import config.DatabaseConnection;
import dao.impl.ProduitDAOImpl;
import java.sql.Connection;


/**
 *
 * @author pc
 */
public class GestionDeVente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection cn = DatabaseConnection.getInstance().getConnection();

        ProduitDAOImpl produitDAOImpl = new ProduitDAOImpl(cn);
        System.out.println(produitDAOImpl.findById(2).getDesignation());   
        
    }
    
}
