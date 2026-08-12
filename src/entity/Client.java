/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entity;

public class Client extends BaseEntity {

    private String nom;
    private String prenom;
    private String matricule;
    private String tel;
    private String adresse;
    private Entreprise entreprise;

    public Client() {
    }

    public Client(int id) {
        super(id);
    }

    public Client(int id, String nom, String prenom,
            String matricule, String tel,
            String adresse, Entreprise entreprise) {

        super(id);

        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.tel = tel;
        this.adresse = adresse;
        this.entreprise = entreprise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}