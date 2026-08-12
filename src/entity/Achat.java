/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;

public class Achat extends BaseEntity {

    private Client client;
    private double prix_total;
    private LocalDate date_achat;

    public Achat() {
    }

    public Achat(int id) {
        super(id);
    }

    public Achat(int id, Client client, double prix_total, LocalDate date_achat) {

        super(id);

        this.client = client;
        this.prix_total = prix_total;
        this.date_achat = date_achat;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(double prix_total) {
        this.prix_total = prix_total;
    }

    public LocalDate getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(LocalDate date_achat) {
        this.date_achat = date_achat;
    }

}
