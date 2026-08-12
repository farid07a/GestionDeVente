/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entity;

public class ClientPayeParEntreprise extends BaseEntity {

    private VersementEntreprise versementEntreprise;
    private Achat achat;

    public ClientPayeParEntreprise() {
    }

    public ClientPayeParEntreprise(int id) {
        super(id);
    }

    public ClientPayeParEntreprise(int id,
            VersementEntreprise versementEntreprise,
            Achat achat) {

        super(id);

        this.versementEntreprise = versementEntreprise;
        this.achat = achat;
    }

    public VersementEntreprise getVersementEntreprise() {
        return versementEntreprise;
    }

    public void setVersementEntreprise(VersementEntreprise versementEntreprise) {
        this.versementEntreprise = versementEntreprise;
    }

    public Achat getAchat() {
        return achat;
    }

    public void setAchat(Achat achat) {
        this.achat = achat;
    }

}
