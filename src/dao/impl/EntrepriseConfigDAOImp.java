package dao.impl;

import dao.AbstractDAO;
import entity.EntrepriseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntrepriseConfigDAOImp extends AbstractDAO<EntrepriseConfig> {

    public EntrepriseConfigDAOImp(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "Entreprise_Info";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Entreprise_Info (nom_ar, nom_fr, desc, adresse, tel, email, num, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Entreprise_Info SET nom_ar=?, nom_fr=?, desc=?, adresse=?, tel=?, email=?, num=?, image=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EntrepriseConfig entity) throws SQLException {
        ps.setString(1, entity.getNom_ar());
        ps.setString(2, entity.getNom_fr());
        ps.setString(3, entity.getDesc());
        ps.setString(4, entity.getAdresse());
        ps.setString(5, entity.getTel());
        ps.setString(6, entity.getEmail());
        ps.setString(7, entity.getNum());
        ps.setString(8, entity.getImage()); // حفظ المسار
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, EntrepriseConfig entity) throws SQLException {
        ps.setString(1, entity.getNom_ar());
        ps.setString(2, entity.getNom_fr());
        ps.setString(3, entity.getDesc());
        ps.setString(4, entity.getAdresse());
        ps.setString(5, entity.getTel());
        ps.setString(6, entity.getEmail());
        ps.setString(7, entity.getNum());
        ps.setString(8, entity.getImage()); // تحديث المسار
        ps.setInt(9, entity.getId());
    }

    @Override
    protected EntrepriseConfig mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new EntrepriseConfig(
                rs.getInt("id"),
                rs.getString("nom_ar"),
                rs.getString("nom_fr"),
                rs.getString("desc"),
                rs.getString("adresse"),
                rs.getString("tel"),
                rs.getString("email"),
                rs.getString("num"),
                rs.getString("image") 
        );
    }
}