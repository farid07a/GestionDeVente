/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class EntreeSortieUtilisateur extends BaseEntity {

    private Utilisateur utilisateur;
    private LocalDate date;
    private LocalTime heureEntree;
    private LocalTime heureSortie;

    public EntreeSortieUtilisateur() {
    }

    public EntreeSortieUtilisateur(int id) {
        super(id);
    }

    public EntreeSortieUtilisateur(
            int id,
            Utilisateur utilisateur,
            LocalDate date,
            LocalTime heureEntree,
            LocalTime heureSortie) {

        super(id);
        this.utilisateur = utilisateur;
        this.date = date;
        this.heureEntree = heureEntree;
        this.heureSortie = heureSortie;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureEntree() {
        return heureEntree;
    }

    public void setHeureEntree(LocalTime heureEntree) {
        this.heureEntree = heureEntree;
    }

    public LocalTime getHeureSortie() {
        return heureSortie;
    }

    public void setHeureSortie(LocalTime heureSortie) {
        this.heureSortie = heureSortie;
    }

    @Override
    public String toString() {
        return "EntreeSortieUtilisateur{"
                + "id=" + getId()
                + ", utilisateur=" + utilisateur
                + ", date=" + date
                + ", heureEntree=" + heureEntree
                + ", heureSortie=" + heureSortie
                + '}';
    }
}