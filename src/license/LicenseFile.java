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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;

public class LicenseFile {

    /*
     * Change these two values for your application.
     *
     * IMPORTANT:
     * Do not use a simple key in a real commercial product.
     * We will improve this later using RSA digital signatures.
     */
    private static final String COMPANY_NAME =
            "GarbazeEnterprise";

    private static final String APPLICATION_NAME =
            "GestionVente";

    private static final String SECRET =
            "GarbazeEnterprise-License-Secret-2026";

    private static final String FILE_NAME =
            "license.dat";

    /**
     * Returns:
     *
     * C:\ProgramData\YourCompany\YourApplication
     */
    private static Path getConfigDirectory() {

        String programData =
                System.getenv("ProgramData");

        if (programData == null ||
                programData.trim().isEmpty()) {

            programData =
                    System.getProperty("user.home");
        }

        return Paths.get(
                programData,
                COMPANY_NAME,
                APPLICATION_NAME
        );
    }

    /**
     * Returns the complete license file path.
     */
    public static Path getLicensePath() {

        return getConfigDirectory()
                .resolve(FILE_NAME);
    }

    /**
     * Check if license file exists.
     */
    public static boolean exists() {

        return Files.exists(getLicensePath());
    }

    /**
     * Save LicenseConfig to encrypted file.
     */
    public static void save(
            LicenseConfig config) throws Exception {

        Path directory =
                getConfigDirectory();

        if (!Files.exists(directory)) {

            Files.createDirectories(directory);
        }

        Properties properties =
                new Properties();

        properties.setProperty(
                "installDate",
                config.getInstallDate().toString()
        );

        properties.setProperty(
                "lastRunDate",
                config.getLastRunDate().toString()
        );

        properties.setProperty(
                "trialDays",
                String.valueOf(
                        config.getTrialDays()
                )
        );

        properties.setProperty(
                "status",
                config.getStatus().name()
        );

        StringBuilder data =
                new StringBuilder();

        for (String key :
                properties.stringPropertyNames()) {

            data.append(key)
                    .append("=")
                    .append(properties.getProperty(key))
                    .append("\n");
        }

        String encrypted =
                encrypt(data.toString());

        Files.write(
                getLicensePath(),
                encrypted.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    /**
     * Load and decrypt license file.
     */
    public static LicenseConfig load()
            throws Exception {

        Path path =
                getLicensePath();

        if (!Files.exists(path)) {

            return null;
        }

        byte[] bytes =
                Files.readAllBytes(path);

        String encrypted =
                new String(
                        bytes,
                        StandardCharsets.UTF_8
                );

        String decrypted =
                decrypt(encrypted);

        Properties properties =
                new Properties();

        properties.load(
                new java.io.StringReader(
                        decrypted
                )
        );

        LicenseConfig config =
                new LicenseConfig();

        config.setInstallDate(
                java.time.LocalDate.parse(
                        properties.getProperty(
                                "installDate"
                        )
                )
        );

        config.setLastRunDate(
                java.time.LocalDate.parse(
                        properties.getProperty(
                                "lastRunDate"
                        )
                )
        );

        config.setTrialDays(
                Integer.parseInt(
                        properties.getProperty(
                                "trialDays"
                        )
                )
        );

        config.setStatus(
                LicenseStatus.valueOf(
                        properties.getProperty(
                                "status"
                        )
                )
        );

        return config;
    }

    /**
     * AES encryption.
     */
    private static String encrypt(
            String plainText) throws Exception {

        SecretKeySpec key =
                createKey();

        Cipher cipher =
                Cipher.getInstance("AES");

        cipher.init(
                Cipher.ENCRYPT_MODE,
                key
        );

        byte[] encrypted =
                cipher.doFinal(
                        plainText.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        return Base64.getEncoder()
                .encodeToString(encrypted);
    }

    /**
     * AES decryption.
     */
    private static String decrypt(
            String encryptedText) throws Exception {

        SecretKeySpec key =
                createKey();

        Cipher cipher =
                Cipher.getInstance("AES");

        cipher.init(
                Cipher.DECRYPT_MODE,
                key
        );

        byte[] encrypted =
                Base64.getDecoder()
                        .decode(encryptedText);

        byte[] decrypted =
                cipher.doFinal(encrypted);

        return new String(
                decrypted,
                StandardCharsets.UTF_8
        );
    }

    /**
     * Create AES key from secret.
     */
    private static SecretKeySpec createKey()
            throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] hash =
                digest.digest(
                        SECRET.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        /*
         * AES-256 key.
         */
        return new SecretKeySpec(
                hash,
                "AES"
        );
    }
}
