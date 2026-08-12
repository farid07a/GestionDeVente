/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.Entreprise;
import entity.VersementEntreprise;
import exception.DAOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersementEntrepriseDAOImpl extends AbstractDAO<VersementEntreprise> {

    public VersementEntrepriseDAOImpl(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Versement_Entreprise";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Versement_Entreprise (id_entreprise, montant, date_versement, mode_paiement, remarque, total_credit, reste_credit) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Versement_Entreprise SET id_entreprise=?, montant=?, date_versement=?, mode_paiement=?, remarque=?, total_credit=?, reste_credit=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, VersementEntreprise entity) throws SQLException {

        ps.setInt(1, entity.getEntreprise().getId());
        ps.setDouble(2, entity.getMontant());
        ps.setDate(3, Date.valueOf(entity.getDate_versement()));
        ps.setString(4, entity.getMode_paiement());
        ps.setString(5, entity.getRemarque());
        ps.setDouble(6, entity.getTotal_credit());
        ps.setDouble(7, entity.getReste_credit());

    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, VersementEntreprise entity) throws SQLException {

        ps.setInt(1, entity.getEntreprise().getId());
        ps.setDouble(2, entity.getMontant());
        ps.setDate(3, Date.valueOf(entity.getDate_versement()));
        ps.setString(4, entity.getMode_paiement());
        ps.setString(5, entity.getRemarque());
        ps.setDouble(6, entity.getTotal_credit());
        ps.setDouble(7, entity.getReste_credit());
        ps.setInt(8, entity.getId());

    }

    @Override
    protected VersementEntreprise mapResultSetToEntity(ResultSet rs) throws SQLException {

        Entreprise entreprise = new EntrepriseDAOImpl(connection ).findById(rs.getInt("id_entreprise"));

        return new VersementEntreprise(
                rs.getInt("id"),
                entreprise,
                rs.getDouble("montant"),
                rs.getDate("date_versement").toLocalDate(),
                rs.getString("mode_paiement"),
                rs.getString("remarque"),
                rs.getDouble("total_credit"),
                rs.getDouble("reste_credit")
        );

    }
    
    public VersementEntreprise getLastVersementEntreprise(Entreprise entreprise) {

    VersementEntreprise versementEntreprise = null;

    String query = "SELECT * FROM " + getTableName()
            + " WHERE  id_entreprise  = ? "
            + " ORDER BY id DESC";

    try (PreparedStatement ps = connection.prepareStatement(query)) {

        ps.setInt(1, entreprise.getId());

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            versementEntreprise = mapResultSetToEntity(rs);
        }

    } catch (SQLException e) {
        throw new DAOException("Erreur lors de la récupération du dernier versement.", e);
    }

    return versementEntreprise ;
}
    
    public List<VersementEntreprise> getLastVersementParEntreprise() {

    List<VersementEntreprise> list = new ArrayList<>();

    String query =
            "SELECT * FROM Versement_Entreprise " +
            "WHERE id IN (" +
            "SELECT MAX(id) FROM Versement_Entreprise " +
            "GROUP BY id_entreprise" +
            ")";

    try (PreparedStatement ps = connection.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(mapResultSetToEntity(rs));
        }

    } catch (SQLException e) {
        throw new DAOException(
                "Erreur lors de la récupération des derniers versements pour chaque entreprise.", e);
    }

    return list;
}

}