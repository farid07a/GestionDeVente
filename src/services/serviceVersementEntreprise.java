/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import config.DatabaseConnection;
import dao.impl.VersementEntrepriseDAOImpl;
import entity.Achat;
import entity.ClientPayeParEntreprise;
import entity.Entreprise;
import entity.VersementEntreprise;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author pc
 */
public class serviceVersementEntreprise {
    
    Connection connection;
    
    public serviceVersementEntreprise(Connection connection ) {
        this.connection= DatabaseConnection.getInstance().getConnection();    
    }
    
    public double GetLastRestVersementEntreprise(Entreprise entreprise){
        double restCrediteLastVersement=0;
        VersementEntreprise versementEntreprise =
                new VersementEntrepriseDAOImpl(connection).getLastVersementEntreprise(entreprise);
        if (versementEntreprise != null ) {
            restCrediteLastVersement = versementEntreprise.getReste_credit();
        }
        return restCrediteLastVersement;
    }

    
}
