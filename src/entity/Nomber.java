/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class Nomber {

    public Nomber() {
    }
     public double getNbDouble(String NomberStr){
         Object val =NomberStr;
          String cleanValue = val.toString()
                           .replace(",", "")          
                           .replace(" ", "")          
                           .replace("\u00A0", "")     
                           .replaceAll("\\s+", "")    
                           .trim();
          return Double.parseDouble(cleanValue);
          
     }
}
