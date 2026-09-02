/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import config.DatabaseConnection;
import dao.AbstractDAO;
import entity.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pc
 */
public class UtilisateurDAOImp extends AbstractDAO<Utilisateur> {

    public UtilisateurDAOImp(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return " Utilisateur ";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Utilisateur ( nom, motpass) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Utilisateur SET nom = ?, motpass=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Utilisateur entity) throws SQLException {
        ps.setString(1, entity.getNom());
        ps.setString(2, entity.getMotPass());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Utilisateur entity) throws SQLException {
        ps.setString(1, entity.getNom());
        ps.setString(2, entity.getMotPass());
        ps.setInt(3, entity.getId());
    }

    @Override
    protected Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {

        return new Utilisateur(rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("motPass")
        );
    }

    public Utilisateur getUtilisateurParNomMotPass(String nom, String motpass) {
        Utilisateur utilisateur = null;
        try {
            String query = "SELECT * FROM " + getTableName() + " WHERE nom=? "
                    + " and motpass = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, motpass);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utilisateur = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;

    }

    public Utilisateur getUtilisateurParMotPass(String motpass) {
        Utilisateur utilisateur = null;
        try {
            String query = "SELECT * FROM " + getTableName() + " WHERE   motpass =? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, motpass);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utilisateur = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }
    public static void main(String[] args) {
        new UtilisateurDAOImp(DatabaseConnection.getInstance().getConnection()).getUtilisateurParMotPass("amar");
    }

    
}
