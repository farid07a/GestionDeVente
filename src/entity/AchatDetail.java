/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class AchatDetail extends BaseEntity {

    private Achat achat;
    private Produit produit;
    private int qty;
    private double prix_unitaire;
    private double prix_total;

    public AchatDetail() {
    }

    public AchatDetail(int id) {
        super(id);
    }

    public AchatDetail(int id, Achat achat, Produit produit,
            int qty, double prix_unitaire, double prix_total) {

        super(id);

        this.achat = achat;
        this.produit = produit;
        this.qty = qty;
        this.prix_unitaire = prix_unitaire;
        this.prix_total = prix_total;
    }

    public Achat getAchat() {
        return achat;
    }

    public void setAchat(Achat achat) {
        this.achat = achat;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public double getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(double prix_total) {
        this.prix_total = prix_total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AchatDetail{");
        sb.append("achat=").append(achat);
        sb.append(", produit=").append(produit);
        sb.append(", qty=").append(qty);
        sb.append(", prix_unitaire=").append(prix_unitaire);
        sb.append(", prix_total=").append(prix_total);
        sb.append('}');
        return sb.toString();
    }
    
    

}