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

public class LicenseConfig {

    private LocalDate installDate;
    private LocalDate lastRunDate;
    private int trialDays;
    private LicenseStatus status;

    public LicenseConfig() {
    }

    public LicenseConfig(
            LocalDate installDate,
            LocalDate lastRunDate,
            int trialDays,
            LicenseStatus status) {

        this.installDate = installDate;
        this.lastRunDate = lastRunDate;
        this.trialDays = trialDays;
        this.status = status;
    }

    public LocalDate getInstallDate() {
        return installDate;
    }

    public void setInstallDate(LocalDate installDate) {
        this.installDate = installDate;
    }

    public LocalDate getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(LocalDate lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public int getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(int trialDays) {
        this.trialDays = trialDays;
    }

    public LicenseStatus getStatus() {
        return status;
    }

    public void setStatus(LicenseStatus status) {
        this.status = status;
    }
}