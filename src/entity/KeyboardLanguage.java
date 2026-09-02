/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import com.sun.jna.Native;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public class KeyboardLanguage {

    interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class);

        Pointer LoadKeyboardLayoutA(String pwszKLID, int Flags);
        Pointer ActivateKeyboardLayout(Pointer hkl, int Flags);
    }

    public static void setArabic() {
        changeLanguage("00000401");
    }

    public static void setFrench() {
        changeLanguage("0000040C");
    }

    private static void changeLanguage(String language) {

        Pointer hkl = User32.INSTANCE.LoadKeyboardLayoutA(
                language,
                0x00000001
        );

        if (hkl != null) {
            User32.INSTANCE.ActivateKeyboardLayout(hkl, 0);
        }
    }
}