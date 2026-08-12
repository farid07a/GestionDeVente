/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frame;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ArabicComparator implements Comparator<String> {

    private final Collator collator =
            Collator.getInstance(new Locale("ar"));

    @Override
    public int compare(String o1, String o2) {
        return collator.compare(o1, o2);
    }
}
