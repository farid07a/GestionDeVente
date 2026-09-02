/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author pc
 */
public class EntrepriseConfig extends Entreprise {

    private String num;
    private String image;

    public EntrepriseConfig(int id, String nom_ar, String nom_fr,
            String desc, String adresse, String tel, String email,String num,String image) {

        super( id,  nom_ar,  nom_fr, desc, adresse, tel, email);

        this.num = num;
        this.image=image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
