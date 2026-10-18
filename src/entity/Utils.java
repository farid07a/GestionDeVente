/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author pc
 */
public class Utils {

    public Utils() {
    }
    
    public String getInitiales(String phrase) {
    if (phrase == null || phrase.trim().isEmpty()) {
        return "";
    }

    StringBuilder resultat = new StringBuilder();
    String[] mots = phrase.trim().split("\\s+");

    for (String mot : mots) {
        if (resultat.length() > 0) {
            resultat.append(" ");
        }

        resultat.append(mot.substring(0, 1).toUpperCase());
    }

    return resultat.toString();
}
    
    
//   public String getInitiales(String phrase) {
//    if (phrase == null || phrase.trim().isEmpty()) {
//        return "";
//    }
//
//    StringBuilder resultat = new StringBuilder();
//    String[] mots = phrase.trim().split("\\s+");
//
//    for (String mot : mots) {
//        resultat.append(mot.substring(0, 1).toUpperCase());
//    }
//
//    return resultat.toString();
//}
   
}
