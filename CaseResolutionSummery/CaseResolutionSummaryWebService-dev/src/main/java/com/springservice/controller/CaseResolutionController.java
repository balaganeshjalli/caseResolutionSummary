package com.springservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springservice.modal.BWEMSModel;

/**
 * @author rapalaku
 *
 */
@RestController
@RequestMapping("/getCaseResolution")

public class CaseResolutionController {
    static final Logger logger = Logger.getLogger(CaseResolutionController.class.getName());

    @CrossOrigin
    @RequestMapping("/bems/{srNum}")
    public @ResponseBody static Map<String, Object> getBemsCaseResolution(@PathVariable long srNum) {
        HttpUtils httpUtils = new HttpUtils();
        Map<String, Object> res = new HashMap<String, Object>();
        List<BWEMSModel> srStatus = httpUtils.getBEMSCaseResolution(srNum);
        res.put("transactions", srStatus);
        res.put("count", srStatus.size());
        return res;

    }

    @CrossOrigin
    @RequestMapping(value = "/{srNum}", method = RequestMethod.GET)
    public @ResponseBody static CaseResolution getGreeting(@PathVariable long srNum) {
        // long srNum=682584005;
        String bdbUrl = "https://scripts.cisco.com/api/v2/auth/login";
        HttpUtils httpUtils = new HttpUtils();
        String gettype = "Status";
        logger.info("BDB URL is: " + bdbUrl);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "text/plain");
        String obssoCookie = doBdbLogin(bdbUrl, headers);
        String srStatus = httpUtils.httpPOSTBdb(obssoCookie, srNum, gettype);
        String resolutionSummury = "No Summary";
        String caseNumber = "No caseNumber";
        if (srStatus != "NULL" && (srStatus.equals("Closed") || srStatus.equals("Closed w/o Customer Confirm"))) {
            gettype = "Shadow_Notes__r";
            resolutionSummury = httpUtils.httpPOSTBdb(obssoCookie, srNum, gettype);
            logger.info(resolutionSummury);
            if (resolutionSummury.startsWith("\""))
                resolutionSummury = resolutionSummury.substring(1, resolutionSummury.length() - 1);
        }
        // if(resolutionSummury=="NULL" && resolutionSummury.equals("No
        // Summary")){
        // logger.info("No summary, Not making call to get the case number");
        // }else{
        gettype = "CaseNumber";
        caseNumber = httpUtils.httpPOSTBdb(obssoCookie, srNum, gettype);
        if (caseNumber.startsWith("\""))
            caseNumber = caseNumber.substring(1, caseNumber.length() - 1);
        logger.info(caseNumber);
        String caseOwner = "Case owner name";
        gettype = "Case_Owner_Name__c";
        caseOwner = httpUtils.httpPOSTBdb(obssoCookie, srNum, gettype);
        if (caseOwner.startsWith("\""))
            caseOwner = caseOwner.substring(1, caseOwner.length() - 1);
        logger.info(caseOwner);

        String caseOwnerId = "Owner.Alias";
        gettype = "Owner.Alias";
        caseOwnerId = httpUtils.httpPOSTBdb(obssoCookie, srNum, gettype);
        if (caseOwnerId.startsWith("\""))
            caseOwnerId = caseOwnerId.substring(1, caseOwnerId.length() - 1);
        logger.info(caseOwnerId);

        // }
        // List<CaseResolution> caseResoultion=new ArrayList<CaseResolution>();
        // caseResoultion.
        logger.info(resolutionSummury);
        resolutionSummury = resolutionSummury.replaceAll("(\\\\r\\\\n|\\\\n)", "<br/>");
        logger.info("case owner name : " + caseOwner);
        logger.info("Test after replacing " + resolutionSummury);
        logger.info("case owner name : " + caseOwner);
        logger.info("Case owner id : " + caseOwnerId);
        CaseResolution caseResolution = new CaseResolution(resolutionSummury, caseNumber, srStatus, caseOwner,
                caseOwnerId);
        return caseResolution;
    }

    // public static void main(String args[]){
    //
    // long srNum = 684920761;
    // CaseResolutionController c=new CaseResolutionController();
    // CaseResolution testing=c.getGreeting(srNum);
    // System.out.println(testing.getCaseNumber());
    // System.out.println(testing.getResolutionSummary());
    // System.out.println(testing.getSrStatus());
    // }

    private static String doBdbLogin(String bdbUrl, Map<String, String> headers) {
        String cookie = null;
        String obssoCookie = null;
        String[] tokens = null;
        try {
            logger.info("@@@ Trying to pull authentication cookie from cache @@@");
            logger.info("### No authentication cookie in the cache, performing alternate login ###");
            HttpResponse response = HttpUtils.httpGetObssoCookie(bdbUrl, headers);
            logger.info("Respose is: " + response);
            logger.info("Alternate Login URL = " + bdbUrl);
            logger.info("Alternate Login Response status = " + response.getStatusLine().getStatusCode());
            if ((null != response) && 201 == response.getStatusLine().getStatusCode()) {
                org.apache.http.Header[] respHeaders = response.getAllHeaders();
                if (null != respHeaders) {
                    logger.info("@@@@ Alternate Authentication Headers @@@@@@@");
                    for (org.apache.http.Header respHeader : respHeaders) {
                        // logger.info("@@@ heders :"+respHeader.getName() + ":
                        // " + respHeader.getValue());
                        if (respHeader.getName().equals("Set-Cookie")) {
                            obssoCookie = respHeader.getValue();
                            logger.info("@@## Obsso Cookie value is " + obssoCookie);
                        }

                    }
                    logger.info("The Obsso Cookie Value is: " + obssoCookie);
                    return obssoCookie;
                }
            }
            return null;

        } catch (Exception e) {
            logger.error("Exception getting obsso cookie: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        }
        return null;

    }

    // public static void main(String[] args) {
    // getGreeting(685779507);
    //
    // }

}
