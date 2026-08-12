/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;

public interface GenericDAO<T> {

    int save(T entity);

    int update(T entity);

    int delete(Integer id);

    T findById(Integer id);

    List<T> findAll();
    
    T findLast();

}