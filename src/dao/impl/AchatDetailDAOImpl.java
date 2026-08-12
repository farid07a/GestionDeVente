/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.DatabaseConnection;
import dao.AbstractDAO;
import entity.Achat;
import entity.AchatDetail;
import entity.Produit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AchatDetailDAOImpl extends AbstractDAO<AchatDetail> {

    public AchatDetailDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Achat_detaille";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Achat_detaille (id_achat, id_produit, qty, prix_unitaire, prix_total) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Achat_detaille SET id_achat=?, id_produit=?, qty=?, prix_unitaire=?, prix_total=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, AchatDetail entity) throws SQLException {

        ps.setInt(1, entity.getAchat().getId());
        ps.setInt(2, entity.getProduit().getId());
        ps.setInt(3, entity.getQty());
        ps.setDouble(4, entity.getPrix_unitaire());
        ps.setDouble(5, entity.getPrix_total());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, AchatDetail entity) throws SQLException {

        ps.setInt(1, entity.getAchat().getId());
        ps.setInt(2, entity.getProduit().getId());
        ps.setInt(3, entity.getQty());
        ps.setDouble(4, entity.getPrix_unitaire());
        ps.setDouble(5, entity.getPrix_total());
        ps.setInt(6, entity.getId());

    }

    @Override
    protected AchatDetail mapResultSetToEntity(ResultSet rs) throws SQLException {
        Achat achat = new AchatDAOImpl(connection).findById(rs.getInt("id_achat"));
        Produit produit = new ProduitDAOImpl(connection).findById(rs.getInt("id_produit"));

        return new AchatDetail(
                rs.getInt("id"),
                achat,
                produit,
                rs.getInt("qty"),
                rs.getDouble("prix_unitaire"),
                rs.getDouble("prix_total")
        );
    }
//    @Override
//    protected AchatDetail mapResultSetToEntity(ResultSet rs) throws SQLException {
//
//    Achat achat = new AchatDAOImpl(connection).findById(rs.getInt("id_achat"));
//    Produit produit = new ProduitDAOImpl(connection).findById(rs.getInt("id_produit"));
//
//    return new AchatDetail(
//            rs.getInt("id"),
//            achat,
//            produit,
//            rs.getInt("qty"),
//            rs.getDouble("prix_unitaire"),
//            rs.getDouble("prix_total")
//    );
//    }

    public List<AchatDetail> getAchatDetaillByIDAchat(Achat achat) {
        List<AchatDetail> achatDetails = new ArrayList<>();

        String query = "SELECT * FROM " + getTableName() + " WHERE id_achat=?";
    
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, achat.getId());
        
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AchatDetail detail = mapResultSetToEntity(resultSet);
                achatDetails.add(detail);
            }
        
            resultSet.close();
            statement.close();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achatDetails;
    }

}
