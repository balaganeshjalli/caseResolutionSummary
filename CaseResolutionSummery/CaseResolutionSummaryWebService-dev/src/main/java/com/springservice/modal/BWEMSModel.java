package com.springservice.modal;

public class BWEMSModel {
    private String engTitle;

    public String getEngTitle() {
        return engTitle;
    }

    public void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    private String engStatus;
    private String engResolutionSummary;

    public String getEngResolutionSummary() {
        return engResolutionSummary;
    }

    public void setEngResolutionSummary(String engResolutionSummary) {
        this.engResolutionSummary = engResolutionSummary;
    }

    // private String serviceId;

    private String engTransactionId;

    public String getEngStatus() {
        return engStatus;
    }

    public void setEngStatus(String engStatus) {
        this.engStatus = engStatus;
    }

    public String getEngTransactionId() {
        return engTransactionId;
    }

    public void setEngTransactionId(String engTransactionId) {
        this.engTransactionId = engTransactionId;
    }

}
