/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Categorie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategorieDAOImpl extends AbstractDAO<Categorie> {

    public CategorieDAOImpl(Connection connection) {
        super(connection);
        
    }

    @Override
    protected String getTableName() {
        return "Categorie";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Categorie (nomCategorie, description) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Categorie SET nomCategorie=?, description=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Categorie entity) throws SQLException {

        ps.setString(1, entity.getNomCategorie());
        ps.setString(2, entity.getDescription());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Categorie entity) throws SQLException {

        ps.setString(1, entity.getNomCategorie());
        ps.setString(2, entity.getDescription());
        ps.setInt(3, entity.getId());

    }

    @Override
    protected Categorie mapResultSetToEntity(ResultSet rs) throws SQLException {

        return new Categorie(
                rs.getInt("id"),
                rs.getString("nomCategorie"),
                rs.getString("description")
        );

    }
    
    
    public Categorie getCategorierParName(String nomCategorie){
         Categorie categorie =null;
        try {
            String query = "SELECT * FROM " + getTableName() + " WHERE nomCategorie=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomCategorie);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categorie = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorie;
        
        
    }

}
