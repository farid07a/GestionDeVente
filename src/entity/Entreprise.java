/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package entity;

public class Entreprise extends BaseEntity {

    private String nom_ar;
    private String nom_fr;
    private String desc;
    private String adresse;
    private String tel;
    private String email;

    public Entreprise() {
    }

    public Entreprise(int id) {
        super(id);
    }

    public Entreprise(int id, String nom_ar, String nom_fr,
            String desc, String adresse, String tel, String email) {

        super(id);

        this.nom_ar = nom_ar;
        this.nom_fr = nom_fr;
        this.desc = desc;
        this.adresse = adresse;
        this.tel = tel;
        this.email = email;
    }

    public String getNom_ar() {
        return nom_ar;
    }

    public void setNom_ar(String nom_ar) {
        this.nom_ar = nom_ar;
    }

    public String getNom_fr() {
        return nom_fr;
    }

    public void setNom_fr(String nom_fr) {
        this.nom_fr = nom_fr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nom_ar;
    }

}