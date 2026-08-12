/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Achat;
import entity.ClientPayeParEntreprise;
import entity.VersementEntreprise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientPayeParEntrepriseDAOImpl extends AbstractDAO<ClientPayeParEntreprise> {

    public ClientPayeParEntrepriseDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Client_Paye_Par_Entreprise"; 
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Client_Paye_Par_Entreprise (id_versement, id_achat) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Client_Paye_Par_Entreprise SET id_versement=?, id_achat=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, ClientPayeParEntreprise entity) throws SQLException {

        ps.setInt(1, entity.getVersementEntreprise().getId());
        ps.setInt(2, entity.getAchat().getId());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, ClientPayeParEntreprise entity) throws SQLException {

        ps.setInt(1, entity.getVersementEntreprise().getId());
        ps.setInt(2, entity.getAchat().getId());
        ps.setInt(3, entity.getId());

    }

    @Override
    protected ClientPayeParEntreprise mapResultSetToEntity(ResultSet rs) throws SQLException {

        VersementEntreprise versement = new VersementEntrepriseDAOImpl(connection).findById(rs.getInt("id_versement")) ;
        Achat achat = new AchatDAOImpl(connection).findById(rs.getInt("id_achat"));
        return new ClientPayeParEntreprise(
                rs.getInt("id"),
                versement,
                achat
        );

    }
    
     public List<ClientPayeParEntreprise > getClientPayeeParVersement(VersementEntreprise versementEntreprise) {
        List<ClientPayeParEntreprise> clientPayeParEntreprises = new ArrayList<>();
        try {
            String query = " SELECT * FROM  " + getTableName() + " WHERE id_versement=? ";
             PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, versementEntreprise.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ClientPayeParEntreprise clientPayeParEntreprise = mapResultSetToEntity(resultSet);
                clientPayeParEntreprises.add(clientPayeParEntreprise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientPayeParEntreprises ;
    }
     public ClientPayeParEntreprise  getClientPayeeParVersementByAchat(Achat achat) {
        ClientPayeParEntreprise  clientPayeParEntreprise =null;
        try {
            String query = " SELECT * FROM  " + getTableName() + "  WHERE  id_achat=? ";
             PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, achat.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
               clientPayeParEntreprise = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientPayeParEntreprise ;
    }

}
