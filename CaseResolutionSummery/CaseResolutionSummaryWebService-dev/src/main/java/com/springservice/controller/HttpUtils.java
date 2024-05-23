/**
 * 
 */
package com.springservice.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.springservice.modal.BWEMSModel;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * @author rapalaku
 *
 */
public class HttpUtils {

    static final Logger logger = Logger.getLogger(HttpUtils.class.getName());

    static HttpClientContext context = null;
    static HttpClientConnectionManager connMgr = null;
    static HttpRoute route = null;
    static CloseableHttpClient httpCloseableClient = null;

    public static HttpResponse httpGetObssoCookie(String url, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        HttpGet method = null;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        try {
            method = new HttpGet(url);

            String unhashedString = "tz_query.gen:Tech4$one";
            byte[] bytes = unhashedString.getBytes();
            String hashedString = com.sun.org.apache.xml.internal.security.utils.Base64.encode(bytes);

            String userpass = unhashedString;
            String basicAuth = new String(new org.apache.commons.codec.binary.Base64().encode(userpass.getBytes()));

            if (null != headers) {
                for (String key : headers.keySet()) {
                    logger.info("Adding request Header: " + key + "=" + headers.get(key));
                    method.addHeader(key, headers.get(key));
                }
            }
            method.addHeader("Authorization", "Basic " + basicAuth);
            HttpResponse response = client.execute(method);
            System.out.println(response);
            return response;
        } catch (IOException e) {
            logger.info("Exception pulling data via HTTP GET: " + e, e);
            // SessionToken.getInstance().setSession(null);
            logger.error("Exception pulling data via HTTP GET: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception making HTTP GET request: " + e.getMessage());
        } finally {
            if (null != method) {
                method.releaseConnection();
                // }
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > (5 * 60 * 1000)) { // log any request that
                                                     // took longer than 5
                                                     // minutes.
                    logger.info("HTTP GET (" + url + ") elapsed time: " + elapsedTime + " ms.");
                }
            }
        }
        return null;

    }

    public static String httpPOSTBdb(String obssoCookie, long srNumber, String gettype) {

        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {

            HttpPost request = new HttpPost("https://scripts.cisco.com/api/v2/csone/" + srNumber);// https://scripts.cisco.com/api/v2/csone/680898452
            // HttpPost request = new
            // HttpPost("https://scripts.cisco.com/api/v2/csone/633809251");
            JSONArray status = new JSONArray();

            status.put(gettype);
            // status.put("Owner.Alias");

            StringEntity params = new StringEntity(status.toString());
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.addHeader("Cookie", obssoCookie);
            request.setEntity(params);
            CloseableHttpResponse res = httpClient.execute(request);
            logger.info("The status code is: " + res.getStatusLine().getStatusCode());
            // String json1 = EntityUtils.toString(res.getEntity(), "UTF-8");
            if (200 == res.getStatusLine().getStatusCode()) {
                String json = "";
                if (gettype.equals("Shadow_Notes__r")) {
                    json = EntityUtils.toString(res.getEntity(), "UTF-8");
                    // logger.info(json);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    logger.info(jsonNode);
                    String resolutionSummary = "No Summary";
                    for (JsonNode jsonNode2 : jsonNode.get("Shadow_Notes__r").get("records")) {
                        String noteType_c = jsonNode2.get("NoteType__c").toString();
                        if (jsonNode2.has("NoteType__c")) {
                            // logger.info("in noteType");
                            if (noteType_c.equalsIgnoreCase("\"RESOLUTION SUMMARY\"")) {
                                resolutionSummary = jsonNode2.get("Note__c").toString();
                                logger.info(resolutionSummary);

                            }
                        }
                        // logger.info(jsonNode2.get("NoteType__c").toString());
                    }
                    return resolutionSummary;
                } else if (gettype.equals("CaseNumber")) {
                    String caseNumber = "No caseNumber";
                    json = EntityUtils.toString(res.getEntity(), "UTF-8");
                    logger.info(json);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    logger.info(jsonNode);
                    caseNumber = jsonNode.get("CaseNumber").toString();
                    return caseNumber;

                } else if (gettype.equals("Status")) {
                    json = EntityUtils.toString(res.getEntity());
                    logger.info(json);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    String srStatus = jsonNode.path(gettype).asText();
                    logger.info("The sr status: " + srStatus);
                    return srStatus;
                } else if (gettype.equals("Case_Owner_Name__c")) {
                    json = EntityUtils.toString(res.getEntity(), "UTF-8");
                    logger.info(json);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    logger.info(jsonNode);
                    String srStatus = jsonNode.path(gettype).asText();
                    logger.info("The sr status: " + srStatus);
                    return srStatus;
                    // for (JsonNode jsonNode2 :
                    // jsonNode.get("Case_Owner_Name__c")) {
                    // String
                    // noteType_c=jsonNode2.get("NoteType__c").toString();
                    // if(jsonNode2.has("NoteType__c")){
                    // logger.info("in noteType");
                    // if(noteType_c.equalsIgnoreCase("\"RESOLUTION
                    // SUMMARY\"")){
                    // String
                    // resolutionSummary=jsonNode2.get("Note__c").toString();
                    // logger.info(resolutionSummary);
                    // return resolutionSummary;
                    // }
                    // }
                    // logger.info(jsonNode2.get("NoteType__c").toString());
                    // }
                } else if (gettype.equals("Owner.Alias")) {
                    json = EntityUtils.toString(res.getEntity());
                    logger.info(json);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    String srStatus = jsonNode.path(gettype).asText();
                    logger.info("The sr status: " + srStatus);
                    return srStatus;
                }

            }

            // handle response here...
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported Exception for SrNumber: " + srNumber + ". " + e.getMessage());
        }

        catch (IOException e) {
            logger.error("IO Exception for SrNumber: " + srNumber + ". " + e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("Exception getting the SrStatus for SrNumber: " + srNumber + ". " + e.getMessage());
        }
        String value = "NoValue";
        return value;

    }

    public static List<BWEMSModel> getBEMSCaseResolution(long srNumber) {
        BWEMSModel model = new BWEMSModel();
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        List<BWEMSModel> bemsList = new ArrayList<BWEMSModel>();
        try {

            HttpPost request = new HttpPost(
                    "https://clpsvs.cloudapps.cisco.com/services/clip/main/api/myclip/query?pageNumber=0&pageSize=500&query=from%20engagement%20select%20engResolutionSummary%2CengTransactionId%2CengTitle%2CengStatus%20where%20serviceName%20%3D%20BEMS%20and%20supportInfoSrNums%3D"
                            + srNumber);// https://scripts.cisco.com/api/v2/csone/680898452
            // HttpPost request = new
            // HttpPost("https://scripts.cisco.com/api/v2/csone/633809251");

            // status.put("Owner.Alias");

            Gson gson = new Gson();

            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.addHeader(HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64.encode(("tz-bems.gen:Tz&7B*E*M*S").getBytes()));
            CloseableHttpResponse res = httpClient.execute(request);
            logger.info("The status code is: " + res.getStatusLine().getStatusCode());
            String json1 = EntityUtils.toString(res.getEntity(), "UTF-8");
            System.out.println(json1);

            // ObjectMapper mapper = new ObjectMapper();
            JSONObject obj = new JSONObject(json1);

            JSONArray arr = obj.getJSONArray("transactions");
            for (int i = 0; i < arr.length(); i++) {

                String internalobj = arr.getString(i);

                if (200 == res.getStatusLine().getStatusCode()) {
                    model = gson.fromJson(internalobj, BWEMSModel.class);
                    if (model.getEngStatus().equals("Open")) {
                        String summary = "The case " + srNumber + " - " + model.getEngTitle()
                                + " has been escalated to the BU via " + model.getEngTransactionId();
                        model.setEngResolutionSummary(summary);
                        bemsList.add(model);
                    } else if (model.getEngStatus().equals("Closed")) {
                        bemsList.add(model);
                    }

                }
            }

            // handle response here...
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported Exception for SrNumber: " + srNumber + ". " + e.getMessage());
        }

        catch (IOException e) {
            logger.error("IO Exception for SrNumber: " + srNumber + ". " + e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("Exception getting the SrStatus for SrNumber: " + srNumber + ". " + e.getMessage());
        }
        String value = "NoValue";
        return bemsList;

    }
}
