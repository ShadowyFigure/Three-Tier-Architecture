package main.login;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import main.People;
import main.PeopleParameters;
import main.ViewSwitcher;
import main.ViewType;
import main.PeopleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import java.net.URL;
import java.util.ResourceBundle;

public class PeopleLoginController {
    private static Logger logger = LogManager.getLogger();
    private static final String WS_URL = "http://localhost:8080";
    private static String token;
    private static PeopleGateway myGateway;
    private static CloseableHttpClient httpclient;
    ArrayList<People> peoples = new ArrayList<People>();

    public static String getResponseAsString(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        // use org.apache.http.util.EntityUtils to read json as string
        String strResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        EntityUtils.consume(entity);

        return strResponse;
    }


    @FXML
    private Button loginButton;

    @FXML private TextField username;

    @FXML private TextField password;

    @FXML
    void changeView(ActionEvent event) {
        CloseableHttpResponse response = null;

        try {

            // 1. authenticate and get back session token

            httpclient = HttpClients.createDefault();

            // assemble credentials into a JSON encoded string
            JSONObject credentials = new JSONObject();
            credentials.put("username", username.getText());
            credentials.put("password", password.getText());
            String credentialsString = credentials.toString();
            logger.info("credentials: " + credentialsString);

            // build the request
            //HttpGet httpGet = new HttpGet(WS_URL + "/dogs");
            HttpPost loginRequest = new HttpPost(WS_URL + "/login");
            // put credentials string into request body (as raw json)
            // this requires setting it up as a request entity where we can describe what the text is
            StringEntity reqEntity = new StringEntity(credentialsString);
            loginRequest.setEntity(reqEntity);
            loginRequest.setHeader("Accept", "application/json");
            loginRequest.setHeader("Content-type", "application/json");

            response = httpclient.execute(loginRequest);

            // a special response for invalid credentials
            if(response.getStatusLine().getStatusCode() == 401) {

                // create a scene
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("Invalid user credentials!");
                alert.setContentText("Please try to login again.");

                alert.showAndWait();
                logger.error("I DON'T KNOW YOU!!!");
                httpclient.close();
                return;
            }
            if(response.getStatusLine().getStatusCode() != 200) {
                logger.error("Non-200 status code returned: " + response.getStatusLine());
                httpclient.close();
                return;
            }

            // get the session token
            String responseString = getResponseAsString(response);
            logger.info("Login response as a string: " + responseString);

            try {
                JSONObject responseJSON = new JSONObject(responseString);
                token = responseJSON.getString("session_id");
            } catch(Exception e) {
                logger.error("could not get session token: " + e.getMessage());
                httpclient.close();
                return;
            }
            myGateway = new PeopleGateway(WS_URL+"/people", token);
            logger.info("Session token: " + token);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch(IOException e) {
                // ignore
            }
        }
        logger.info(username.getText() + " LOGGED IN");
        ViewSwitcher.getInstance().switchView(ViewType.PeopleListView);

    }

    public static PeopleGateway getGateway(){
        return myGateway;
    }
}

