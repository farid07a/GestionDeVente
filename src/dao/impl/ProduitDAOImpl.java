/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Categorie;
import entity.Produit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduitDAOImpl extends AbstractDAO<Produit> {

    public ProduitDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Produit";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Produit (reference, designation, marque, id_categorie, qty, prix_achat, prix_vente) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Produit SET reference=?, designation=?, marque=?, id_categorie=?, qty=?, prix_achat=?, prix_vente=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Produit entity) throws SQLException {

        ps.setString(1, entity.getReference());
        ps.setString(2, entity.getDesignation());
        ps.setString(3, entity.getMarque());
       // ps.setInt(4, entity.getCategorie().getId());
        if (entity.getCategorie() == null) {
            ps.setNull(4, java.sql.Types.INTEGER);
        } else {
            ps.setInt(4, entity.getCategorie().getId());
        }
        ps.setInt(5, entity.getQty());
        ps.setDouble(6, entity.getPrix_achat());
        ps.setDouble(7, entity.getPrix_vente());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Produit entity) throws SQLException {

        ps.setString(1, entity.getReference());
        ps.setString(2, entity.getDesignation());
        ps.setString(3, entity.getMarque());
        if (entity.getCategorie() == null) {
            ps.setNull(4, java.sql.Types.INTEGER);
        } else {
            ps.setInt(4, entity.getCategorie().getId());
        }
       // ps.setInt(4, entity.getCategorie().getId());
        ps.setInt(5, entity.getQty());
        ps.setDouble(6, entity.getPrix_achat());
        ps.setDouble(7, entity.getPrix_vente());
        ps.setInt(8, entity.getId());

    }

    @Override
    protected Produit mapResultSetToEntity(ResultSet rs) throws SQLException {

        Categorie categorie = new CategorieDAOImpl(connection).findById(rs.getInt("id_categorie"));

        return new Produit(
                rs.getInt("id"),
                rs.getString("reference"),
                rs.getString("designation"),
                rs.getString("marque"),
                categorie,
                rs.getInt("qty"),
                rs.getDouble("prix_achat"),
                rs.getDouble("prix_vente")
        );

    }

}
