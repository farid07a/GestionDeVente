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



import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LicenseManager {

    private static final int DEFAULT_TRIAL_DAYS = 15;

    private LicenseConfig config;

    /**
     * Validate license.
     */
    public LicenseStatus validate() throws Exception {

        /*
         * First run.
         */
        if (!LicenseFile.exists()) {

            createTrial();

            return LicenseStatus.TRIAL;
        }

        /*
         * Read configuration.
         */
        try {

            config =
                    LicenseFile.load();

        } catch (Exception e) {

            /*
             * File was modified or corrupted.
             */
            return LicenseStatus.TAMPERED;
        }

        /*
         * License file is empty or invalid.
         */
        if (config == null) {

            return LicenseStatus.TAMPERED;
        }

        /*
         * Permanent license.
         */
        if (config.getStatus()
                == LicenseStatus.LICENSED) {

            return LicenseStatus.LICENSED;
        }

        LocalDate today =
                LocalDate.now();

        /*
         * Detect Windows date rollback.
         */
        if (today.isBefore(
                config.getLastRunDate())) {

            config.setStatus(
                    LicenseStatus.TAMPERED
            );

            LicenseFile.save(config);

            return LicenseStatus.TAMPERED;
        }

        /*
         * Calculate elapsed days.
         */
        long elapsedDays =
                ChronoUnit.DAYS.between(
                        config.getInstallDate(),
                        today
                );

        /*
         * Trial expired.
         */
        if (elapsedDays >=
                config.getTrialDays()) {

            config.setStatus(
                    LicenseStatus.EXPIRED
            );

            LicenseFile.save(config);

            return LicenseStatus.EXPIRED;
        }

        /*
         * Update last run date.
         */
        config.setLastRunDate(today);

        config.setStatus(
                LicenseStatus.TRIAL
        );

        LicenseFile.save(config);

        return LicenseStatus.TRIAL;
    }

    /**
     * Create first trial.
     */
    private void createTrial()
            throws Exception {

        LocalDate today =
                LocalDate.now();

        config =
                new LicenseConfig(
                        today,
                        today,
                        DEFAULT_TRIAL_DAYS,
                        LicenseStatus.TRIAL
                );

        LicenseFile.save(config);
    }

    /**
     * Get remaining days.
     */
    public long getRemainingDays() {

        if (config == null) {

            return 0;
        }

        if (config.getStatus()
                == LicenseStatus.LICENSED) {

            return Long.MAX_VALUE;
        }

        LocalDate today =
                LocalDate.now();

        long elapsedDays =
                ChronoUnit.DAYS.between(
                        config.getInstallDate(),
                        today
                );

        long remaining =
                config.getTrialDays()
                        - elapsedDays;

        return Math.max(
                remaining,
                0
        );
    }

    /**
     * Get configuration.
     */
    public LicenseConfig getConfig() {

        return config;
    }

    /**
     * Activate permanent license.
     */
    public void activateLicense()
            throws Exception {

        if (config == null) {

            throw new IllegalStateException(
                    "License is not initialized."
            );
        }

        config.setStatus(
                LicenseStatus.LICENSED
        );

        LicenseFile.save(config);
    }

    public boolean activate(String licenseKey) throws Exception{

        if (!LicenseKeyValidator.isValid(licenseKey)) {
            return false;
        }

        LicenseConfig config = LicenseFile.load();

        if (config == null) {
            return false;
        }

        config.setStatus(LicenseStatus.LICENSED);

        LicenseFile.save(config);

        return true;
}
}