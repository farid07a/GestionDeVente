/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Client;
import entity.Entreprise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAOImpl extends AbstractDAO<Client> {

    public ClientDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Client";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Client (nom, prenom, matricule, tel, adresse, id_entreprise) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Client SET nom=?, prenom=?, matricule=?, tel=?, adresse=?, id_entreprise=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Client entity) throws SQLException {

        ps.setString(1, entity.getNom());
        ps.setString(2, entity.getPrenom());
        ps.setString(3, entity.getMatricule());
        ps.setString(4, entity.getTel());
        ps.setString(5, entity.getAdresse());
      //  ps.setInt(6, entity.getEntreprise().getId());
      if (entity.getEntreprise() == null) {
            ps.setNull(6, java.sql.Types.INTEGER);
        } else {
            ps.setInt(6, entity.getEntreprise().getId());
        }

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Client entity) throws SQLException {

        ps.setString(1, entity.getNom());
        ps.setString(2, entity.getPrenom());
        ps.setString(3, entity.getMatricule());
        ps.setString(4, entity.getTel());
        ps.setString(5, entity.getAdresse());
      //  ps.setInt(6, entity.getEntreprise().getId());
        if (entity.getEntreprise() == null) {
            ps.setNull(6, java.sql.Types.INTEGER);
        } else {
            ps.setInt(6, entity.getEntreprise().getId());
        }
        ps.setInt(7, entity.getId());

    }

    @Override
    protected Client mapResultSetToEntity(ResultSet rs) throws SQLException {

        Entreprise entreprise = new EntrepriseDAOImpl(connection).findById(rs.getInt("id_entreprise"));

        return new Client(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("matricule"),
                rs.getString("tel"),
                rs.getString("adresse"),
                entreprise
        );

    }
public  Client getClientByMatricul(String matricul){
    Client  client = null;
      try {
            String query = "SELECT * FROM " + getTableName() + " WHERE matricule =?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricul );
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                client = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return client;
}
}
