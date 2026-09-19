/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package license;

/**
 *
 * @author farid
 */
public class LicenseKeyValidator {

    private static final String VALID_KEY =
            "APP-2026-ABCD-1234";

    public static boolean isValid(String key) {

        if (key == null || key.trim().isEmpty()) {
            return false;
        }

        return VALID_KEY.equals(key.trim());
    }
}
