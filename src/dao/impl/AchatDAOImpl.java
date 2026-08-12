/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Achat;
import entity.AchatDetail;
import entity.Client;
import entity.Entreprise;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AchatDAOImpl extends AbstractDAO<Achat> {

    public AchatDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Achat";
    }
    // Achat
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Achat (id_client, prix_total, date_achat) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Achat SET id_client=?, prix_total=?, date_achat=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Achat entity) throws SQLException {

        ps.setInt(1, entity.getClient().getId());
        ps.setDouble(2, entity.getPrix_total());
        ps.setDate(3, Date.valueOf(entity.getDate_achat()));

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Achat entity) throws SQLException {

        ps.setInt(1, entity.getClient().getId());
        ps.setDouble(2, entity.getPrix_total());
        ps.setDate(3, Date.valueOf(entity.getDate_achat()));
        ps.setInt(4, entity.getId());

    }

    @Override
    protected Achat mapResultSetToEntity(ResultSet rs) throws SQLException {

        Client client = new ClientDAOImpl(connection).findById(rs.getInt("id_client"));

        return new Achat(
                rs.getInt("id"),
                client,
                rs.getDouble("prix_total"),
                rs.getDate("date_achat").toLocalDate()
        );

    }

    public Achat getLast() {
        Achat achat = null;
        try {
            String query = "SELECT * FROM " + getTableName() + " ORDER BY id DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                achat = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achat;

    }

    public List<Achat> getAchatNotInTabVersementByEntreprise(Entreprise entreprise) {
        List<Achat> achats = new ArrayList<>();
        try {

            String query = "SELECT * FROM " + getTableName() + " "
                    + "INNER JOIN Client ON Achat.id_client = Client.id "
                    + "WHERE Client.id_entreprise = ? "
                    + "AND Achat.id NOT IN ( "
                    + "    SELECT id_achat "
                    + "    FROM Client_Paye_Par_Entreprise "
                    + "    WHERE id_achat IS NOT NULL"
                    + ")";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, entreprise.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Achat achat = mapResultSetToEntity(resultSet);
                achats.add(achat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achats;

    }
    public List<Achat> getAchatNotInTabVersement() {
        List<Achat> achats = new ArrayList<>();
        try {

            String query = "SELECT * FROM " + getTableName() + " "
        + "WHERE id NOT IN ( "
        + "    SELECT id_achat "
        + "    FROM Client_Paye_Par_Entreprise "
        + "    WHERE id_achat IS NOT NULL"
        + ")";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Achat achat = mapResultSetToEntity(resultSet);
                achats.add(achat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achats;

    }
    

}
