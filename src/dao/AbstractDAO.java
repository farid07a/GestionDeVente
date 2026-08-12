/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    protected final Connection connection;

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    protected abstract String getTableName();

    protected abstract String getInsertQuery();

    protected abstract String getUpdateQuery();

    protected abstract void setInsertParameters(PreparedStatement ps, T entity) throws SQLException;

    protected abstract void setUpdateParameters(PreparedStatement ps, T entity) throws SQLException;

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public int save(T entity) {

        try ( PreparedStatement ps = connection.prepareStatement(getInsertQuery())) {

            setInsertParameters(ps, entity);

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

    }

    @Override
    public int update(T entity) {

        try ( PreparedStatement ps = connection.prepareStatement(getUpdateQuery())) {

            setUpdateParameters(ps, entity);

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

    }

    @Override
    public int delete(Integer id) {

        String sql = "DELETE FROM " + getTableName() + " WHERE id=?";

        try ( PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

    }

    @Override
    public T findById(Integer id) {

        String sql = "SELECT * FROM " + getTableName() + " WHERE id=?";

        try ( PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return mapResultSetToEntity(rs);

            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

        return null;

    }

    @Override
    public List<T> findAll() {

        List<T> list = new ArrayList<>();

        String sql = "SELECT * FROM " + getTableName() + "   ";

        try ( PreparedStatement ps = connection.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(mapResultSetToEntity(rs));

            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

        return list;

    }
    
    public T findLast() {
        String sql = "  SELECT TOP 1 * FROM " + getTableName()  +"   ORDER BY id DESC ";

        try ( PreparedStatement ps = connection.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                return  mapResultSetToEntity(rs);

            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement dans la table " + getTableName(), e);
        }

        return null;

    }
  

}
