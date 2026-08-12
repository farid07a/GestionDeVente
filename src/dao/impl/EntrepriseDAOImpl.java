/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Entreprise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntrepriseDAOImpl extends AbstractDAO<Entreprise> {

    public EntrepriseDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Entreprise";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Entreprise (nom_ar, nom_fr, desc, adresse, tel, email) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Entreprise SET nom_ar=?, nom_fr=?, desc=?, adresse=?, tel=?, email=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Entreprise entity) throws SQLException {

        ps.setString(1, entity.getNom_ar());
        ps.setString(2, entity.getNom_fr());
        ps.setString(3, entity.getDesc());
        ps.setString(4, entity.getAdresse());
        ps.setString(5, entity.getTel());
        ps.setString(6, entity.getEmail());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Entreprise entity) throws SQLException {

        ps.setString(1, entity.getNom_ar());
        ps.setString(2, entity.getNom_fr());
        ps.setString(3, entity.getDesc());
        ps.setString(4, entity.getAdresse());
        ps.setString(5, entity.getTel());
        ps.setString(6, entity.getEmail());
        ps.setInt(7, entity.getId());

    }

    @Override
    protected Entreprise mapResultSetToEntity(ResultSet rs) throws SQLException {

        return new Entreprise(
                rs.getInt("id"),
                rs.getString("nom_ar"),
                rs.getString("nom_fr"),
                rs.getString("desc"),
                rs.getString("adresse"),
                rs.getString("tel"),
                rs.getString("email")
        );

    }
    
     public Entreprise getEntrepriseParName(String nomEntreprise){
         Entreprise entreprise =null;
        try {
            String query = "SELECT * FROM " + getTableName() + " WHERE nom_ar=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomEntreprise);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entreprise = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entreprise;
        
        
    }

}