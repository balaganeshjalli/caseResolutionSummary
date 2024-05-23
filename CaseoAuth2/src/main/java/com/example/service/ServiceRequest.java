package com.example.service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServiceRequest {

	protected final Logger logger = LogManager.getLogger(ServiceRequest.class);
    @Value("${auth.url}")
    private String authUrl;

    @Value("${auth.grant-type}")
    private String grantType;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.client-secret}")
    private String clientSecret;
    
    @Value("${case.url}")
    private String caseUrl;

    @Value("${case.loggedId}")
    private String loggedId;

    @Value("${case.fields}")
    private String fields;
    

    public String caseResolutionSummary(String caseNumber) {
        logger.info("Execution start in caseResolutionSummary");
        String bearerToken = getBarerToken();
        logger.info("bearerToken: " + bearerToken);
        if (bearerToken != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + bearerToken);

            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
            String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            // Construct URL
            String dynamicUrl = caseUrl + caseNumber + "?loggedId=" + loggedId + "&fields=" + fields + "&clientTrxId=" + timestamp;
            logger.info("After dynamicUrl constructed: " + dynamicUrl);
            logger.info("Headers are: " + headers);
            HttpEntity<String> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<String> response = restTemplate.exchange(dynamicUrl, HttpMethod.GET, request, String.class);
                logger.debug("Response inside caseResolutionSummary: " + response);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                } else {
                    logger.error("Unexpected status code in caseResolutionSummary: " + response.getStatusCode());
                    return "Network error occured, please connect CISCO network to view Case Resolution Summary.";
                }
            } catch (RestClientException e) {
                logger.error("Error during REST call in caseResolutionSummary: " + e.getMessage());
                return "Network error occured, please connect CISCO network to view Case Resolution Summary.";
            }
        } else {
            logger.error("Failed to obtain bearer token");
            return "Network error occured, please connect CISCO network to view Case Resolution Summary.";
        }
    }
 
    public String getBarerToken() {
    	logger.info("Execution start in getBarerToken Method");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(authUrl, request, String.class);
            logger.info("Response for Response Entity:" + response);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                  logger.debug("jsonNode:"+jsonNode);
                String BarerToken = jsonNode.get("access_token").asText();
                logger.info("BarerToken :" + BarerToken);
                return BarerToken;
            } else {
                  logger.error("Unexpected status code in getBarerToken : " + response.getStatusCode());
                return "Invalid  Response for BarerToken ";
            }
        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.error("Unauthorized: " + ex.getMessage());
            return "Unauthorized Response ";
        } catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            return null;
        }
    }
}
























