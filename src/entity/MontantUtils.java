/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.util.Locale;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MontantUtils {
//
//    private static final RuleBasedNumberFormat FR =
//            new RuleBasedNumberFormat(
//                    Locale.FRENCH,
//                    RuleBasedNumberFormat.SPELLOUT);
//
//    private static final RuleBasedNumberFormat AR =
//            new RuleBasedNumberFormat(
//                    new Locale("ar"),
//                    RuleBasedNumberFormat.SPELLOUT);
//
//    // Conversion du montant en lettres en français
//    public static String fonctionFr(double Montant) {
//        long totalCentimes = BigDecimal.valueOf(Montant)
//                .setScale(2, RoundingMode.HALF_UP)
//                .movePointRight(2)
//                .longValueExact();
//
//        long dinars = totalCentimes / 100;
//        long centimes = totalCentimes % 100;
//
//        StringBuilder texte = new StringBuilder(
//                FR.format(dinars)
//                + (dinars == 1
//                    ? " dinar algérien"
//                    : " dinars algériens"));
//
//        if (centimes > 0) {
//            texte.append(" et ")
//                    .append(FR.format(centimes))
//                    .append(centimes == 1 ? " centime" : " centimes");
//        }
//
//        return texte.append(" seulement").toString();
//    }
//
//    // تحويل المبلغ إلى حروف بالعربية
//    public static String fonctionAr(double Montant) {
//        long totalCentimes = BigDecimal.valueOf(Montant)
//                .setScale(2, RoundingMode.HALF_UP)
//                .movePointRight(2)
//                .longValueExact();
//
//        long dinars = totalCentimes / 100;
//        long centimes = totalCentimes % 100;
//
//        StringBuilder texte = new StringBuilder(
//                AR.format(dinars) + " دينار جزائري");
//
//        if (centimes > 0) {
//            texte.append(" و ")
//                    .append(AR.format(centimes))
//                    .append(" سنتيم");
//        }
//
//        return texte.append(" فقط لا غير").toString();
//    }
//
//    // استخراج أول حرف من كل كلمة
//    public static String getInitiales(String phrase) {
//        if (phrase == null || phrase.trim().isEmpty()) {
//            return "";
//        }
//
//        StringBuilder resultat = new StringBuilder();
//
//        for (String mot : phrase.trim().split("\\s+")) {
//            resultat.append(
//                    mot.substring(0, 1).toUpperCase(Locale.ROOT));
//        }
//
//        return resultat.toString();
//    }
}

