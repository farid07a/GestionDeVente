/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.logging.Logger;

/**
 *
 * @author pc
 */
public class Utilisateur  extends BaseEntity {
    private String nom;
    private String motPass;   

    public Utilisateur() {
    }

    public Utilisateur(int id) {
        super(id);
    }

    public Utilisateur(int id ,String nom, String motPass) {
       super(id);
        this.nom = nom;
        this.motPass = motPass;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMotPass(String motPass) {
        this.motPass = motPass;
    }

    public String getNom() {
        return nom;
    }

    public String getMotPass() {
        return motPass;
    }

    
    
}
