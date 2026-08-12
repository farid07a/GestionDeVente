/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class Categorie extends BaseEntity {

    private String nomCategorie;
    private String description;

    public Categorie() {
    }

    public Categorie(int id) {
        super(id);
    }

    public Categorie(int id, String nomCategorie, String description) {
        super(id);
        this.nomCategorie = nomCategorie;
        this.description = description;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return nomCategorie;
    }

}