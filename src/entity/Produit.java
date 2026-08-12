/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class Produit extends BaseEntity {

    private String reference;
    private String designation;
    private String marque;
    private Categorie categorie;
    private int qty;
    private double prix_achat;
    private double prix_vente;

    public Produit() {
    }

    public Produit(int id) {
        super(id);
    }

    public Produit(int id, String reference, String designation,
            String marque, Categorie categorie,
            int qty, double prix_achat, double prix_vente) {

        super(id);

        this.reference = reference;
        this.designation = designation;
        this.marque = marque;
        this.categorie = categorie;
        this.qty = qty;
        this.prix_achat = prix_achat;
        this.prix_vente = prix_vente;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrix_achat() {
        return prix_achat;
    }

    public void setPrix_achat(double prix_achat) {
        this.prix_achat = prix_achat;
        
    }

    public double getPrix_vente() {
        return prix_vente;
    }

    public void setPrix_vente(double prix_vente) {
        this.prix_vente = prix_vente;
    }

    @Override
    public String toString() {
        return designation;
    }

}