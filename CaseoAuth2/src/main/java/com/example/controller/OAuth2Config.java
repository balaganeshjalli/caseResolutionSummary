package com.example.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ServiceRequest;



@RestController
@RequestMapping
@CrossOrigin(originPatterns = "https://*.cisco.com")
public class OAuth2Config {
 
	
	protected final Logger logger = LogManager.getLogger(OAuth2Config.class);
	@Autowired
    private final ServiceRequest servicerequest;

    @Autowired
    public OAuth2Config(ServiceRequest servicerequest) {
        this.servicerequest = servicerequest;
    }

 
    @GetMapping("/srnumber/{caseNumber}")
    public String caseResolution(@PathVariable String caseNumber) {
        logger.info("case Resolution method start Execution");
        
        if (caseNumber != null ) {
            try {
                Long.parseLong(caseNumber); 
                String resolutionSummary = servicerequest.caseResolutionSummary(caseNumber);
                return resolutionSummary;
                
            } catch (NumberFormatException e) {
            	String errormessage="Invalid case number: " + caseNumber;
                logger.error(errormessage);
                return  errormessage;
            }
        } else {
        	String errormessage="Invalid case number: " + caseNumber;
            logger.error(errormessage);
            return  errormessage;
        }
    }

}
