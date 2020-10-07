package main;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;


public class PeopleGateway {
    private String wsURL;
    private String sessionId;
    private String response;
    private static Logger logger = LogManager.getLogger();

    public PeopleGateway(String url, String sessionId){
        this.sessionId = sessionId;
        this.wsURL = url;
    }

    public void deletePerson(){
        try {
            // we know this is a GET request so create a get request and pass it to getResponseAsString
            // build the request
            HttpDelete request = new HttpDelete(wsURL);
            // specify Authorization header
            request.setHeader("Authorization", sessionId);

            String response = waitForResponseAsString(request);

        } catch (Exception e) {
            throw new PeopleException(e);
        }
    }

    public void insertPerson(){
        try {
            // we know this is a GET request so create a get request and pass it to getResponseAsString
            // build the request
            HttpPost request = new HttpPost(wsURL);
            // specify Authorization header
            request.setHeader("Authorization", sessionId);
            request.setHeader("Body", "[{\"id\":1,\"firstName\":Doug, \"lastName\":Smith,\"dateOfBirth\":2000-01-03,\"age\":20,}]");

            response = waitForResponseAsString(request);

        } catch (Exception e) {
            throw new PeopleException(e);
        }
    }

    public void updatePerson(){
        try {
            // we know this is a GET request so create a get request and pass it to getResponseAsString
            // build the request
            HttpPut putRequest = new HttpPut(wsURL);
            // specify Authorization header
            putRequest.setHeader("Authorization", sessionId);
            putRequest.setHeader("Body", "[{\"id\":1,\"firstName\":Bobby}]");

            response = waitForResponseAsString(putRequest);

        } catch (Exception e) {
            throw new PeopleException(e);
        }
    }

    public ArrayList<People> getPeoples() {
        ArrayList<People> peoples = new ArrayList<People>();

        try {
            // we know this is a GET request so create a get request and pass it to getResponseAsString
            // build the request
            HttpGet request = new HttpGet(wsURL);
            // specify Authorization header
            request.setHeader("Authorization", sessionId);

            response = waitForResponseAsString(request);

            for(Object obj : new JSONArray(response)) {
                JSONObject jsonObject = (JSONObject) obj;
                LocalDate localDate = LocalDate.parse(jsonObject.getString("dateOfBirth"));
                peoples.add(new People(jsonObject.getInt("id"), jsonObject.getString("firstName"), jsonObject.getString("lastName"), localDate, jsonObject.getInt("age")));
            }
        } catch (Exception e) {
            throw new PeopleException(e);
        }

        return peoples;
    }

    private String waitForResponseAsString(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {

            httpclient = HttpClients.createDefault();
            response = httpclient.execute(request);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    logger.info("200 response received");
                    break;
                case 401:
                    logger.error("401 response received");
                    throw new PeopleException("401");
                default:
                    logger.error("non-200 response received");
                    throw new PeopleException("Non-200 status code returned: " + response.getStatusLine());
            }

            return parseResponseToString(response);

        } catch(Exception e) {
            throw new PeopleException(e);
        } finally {
            if(response != null)
                response.close();
            if(httpclient != null)
                httpclient.close();
        }
    }

    private String parseResponseToString(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        // use org.apache.http.util.EntityUtils to read json as string
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

    public void setWsURL(String wsURL){
        this.wsURL = wsURL;
    }

    public String getResponse(){
        return response;
    }

}
