/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.AbstractDAO;
import entity.EntreeSortieUtilisateur;
import entity.Utilisateur;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author pc
 */
public class EntreeSortieUtilisateurDAOImp extends AbstractDAO<EntreeSortieUtilisateur> {

    public EntreeSortieUtilisateurDAOImp(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return " EntreeSortieUtilisateur ";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO EntreeSortieUtilisateur ( id_Utilisateur, date_Entree,temp_Entree,temp_Sortie) VALUES (?,?,?, ?)";

    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE EntreeSortieUtilisateur SET id_Utilisateur = ?, date_Entree=? ,temp_Entree=? , temp_Sortie=?   WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EntreeSortieUtilisateur entity) throws SQLException {
        ps.setInt(1, entity.getUtilisateur().getId());
        ps.setDate(2, Date.valueOf(entity.getDate()));
        if (entity.getHeureEntree() != null) {
            ps.setTime(3, Time.valueOf(entity.getHeureEntree()));
        } else {
            ps.setNull(3, java.sql.Types.TIME);
        }

        if (entity.getHeureSortie() != null) {
            ps.setTime(4, Time.valueOf(entity.getHeureSortie()));
        } else {
            ps.setNull(4, java.sql.Types.TIME);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, EntreeSortieUtilisateur entity) throws SQLException {
        ps.setInt(1, entity.getUtilisateur().getId());
        ps.setDate(2, Date.valueOf(entity.getDate()));
        ps.setTime(3, Time.valueOf(entity.getHeureEntree()));
        ps.setTime(4, Time.valueOf(entity.getHeureSortie()));
        ps.setInt(5, entity.getId());
    }

    @Override
    protected EntreeSortieUtilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new UtilisateurDAOImp(connection).findById(rs.getInt("id_Utilisateur"));

        LocalDate dateEntree
                = rs.getDate("date_Entree").toLocalDate();

        LocalTime tempEntree = null;
        if (rs.getTime("temp_Entree") != null) {
            tempEntree = rs.getTime("temp_Entree").toLocalTime();
        }

        LocalTime tempSortie = null;
        if (rs.getTime("temp_Sortie") != null) {
            tempSortie = rs.getTime("temp_Sortie").toLocalTime();
        }

        return new EntreeSortieUtilisateur(
                rs.getInt("id"),
                utilisateur,
                dateEntree,
                tempEntree,
                tempSortie
        );
    }

}
