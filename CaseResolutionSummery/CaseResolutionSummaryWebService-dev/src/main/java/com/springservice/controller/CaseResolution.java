/**
 * 
 */
package com.springservice.controller;

/**
 * @author rapalaku
 *
 */
public class CaseResolution {

    private String resolutionSummary;
    private String caseNumber;
    private String srStatus;
    private String caseOwner;
    private String caseOwnerId;

    public CaseResolution(String resolutionSummary, String caseNumber, String srStatus, String caseOwner,
            String caseOwnerId) {
        super();
        this.resolutionSummary = resolutionSummary;
        this.caseNumber = caseNumber;
        this.srStatus = srStatus;
        this.caseOwner = caseOwner;
        this.caseOwnerId = caseOwnerId;
    }

    public String getResolutionSummary() {
        return resolutionSummary;
    }

    public void setResolutionSummary(String resolutionSummary) {
        this.resolutionSummary = resolutionSummary;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getSrStatus() {
        return srStatus;
    }

    public void setSrStatus(String srStatus) {
        this.srStatus = srStatus;
    }

    public String getCaseOwner() {
        return caseOwner;
    }

    public void setCaseOwner(String caseOwner) {
        this.caseOwner = caseOwner;
    }

    public String getCaseOwnerId() {
        return caseOwnerId;
    }

    public void setCaseOwnerId(String caseOwnerId) {
        this.caseOwnerId = caseOwnerId;
    }

}
