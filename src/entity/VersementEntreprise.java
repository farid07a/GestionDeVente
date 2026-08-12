/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;

public class VersementEntreprise extends BaseEntity {

    private Entreprise entreprise;
    private double montant;
    private LocalDate date_versement;
    private String mode_paiement;
    private String remarque;

    private double total_credit;
    private double reste_credit;

    public VersementEntreprise() {
    }

    public VersementEntreprise(int id) {
        super(id);
    }

    public VersementEntreprise(int id, Entreprise entreprise,
            double montant, LocalDate date_versement,
            String mode_paiement, String remarque,
            double total_credit, double reste_credit) {

        super(id);

        this.entreprise = entreprise;
        this.montant = montant;
        this.date_versement = date_versement;
        this.mode_paiement = mode_paiement;
        this.remarque = remarque;
        this.total_credit = total_credit;
        this.reste_credit = reste_credit;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDate_versement() {
        return date_versement;
    }

    public void setDate_versement(LocalDate date_versement) {
        this.date_versement = date_versement;
    }

    public String getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public double getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(double total_credit) {
        this.total_credit = total_credit;
    }

    public double getReste_credit() {
        return reste_credit;
    }

    public void setReste_credit(double reste_credit) {
        this.reste_credit = reste_credit;
    }
}