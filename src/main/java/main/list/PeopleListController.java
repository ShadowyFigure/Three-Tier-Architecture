package main.list;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.*;
import main.login.PeopleLoginController;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class PeopleListController implements Initializable{
    private static final String WS_URL = "http://localhost:8080";
    private static Logger logger = LogManager.getLogger();
    private static PeopleGateway myGateway;
    private GetPeoples result = new GetPeoples();
    private String query = "";

    @FXML
    private ListView<People> peopleListView;

    @FXML
    private TextField nameField;

    @FXML
    private Button FirstButton;

    @FXML
    private Button LastButton;

    @FXML
    private Button PrevButton;

    @FXML
    private Button NextButton;

    @FXML
    private Label labelText;

    private ObservableList<People> peoples;

    public PeopleListController(){
        peoples = FXCollections.observableArrayList();
    }

    @FXML
    void addPerson(ActionEvent event) {
        try {
            logger.info("READING " + peopleListView.getSelectionModel().getSelectedItem().getFirstName() + " " + peopleListView.getSelectionModel().getSelectedItem().getLastName());
            PeopleParameters.setPeopleParm(peopleListView.getSelectionModel().getSelectedItem());
            ViewSwitcher.getInstance().switchView(ViewType.PeopleDetailView);
        } catch (Exception e){
            LocalDateTime localDateTime = LocalDateTime.parse("0000-01-01T00:00:00");
            PeopleParameters.setPeopleParm(new People(0, "", "", LocalDate.of(0000, 01, 01), 0, localDateTime));
            ViewSwitcher.getInstance().switchView(ViewType.PeopleDetailView);
        }
    }

    @FXML
    void searchPerson(ActionEvent event){
        peoples.clear();
        PrevButton.setDisable(true);
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        query = nameField.getText();
        result = myGateway.getPeoples(1, nameField.getText(), result.getPageSize());
        labelText.setText("Fetched records " + (result.getCurrentPage()) + " to " + ((result.getCurrentPage()-1) + result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        if((result.getCurrentPage()-1+result.getPageSize()) < result.getNumRows())
            NextButton.setDisable(false);
        else
            NextButton.setDisable(true);
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }
        peopleListView.setItems(peoples);
    }

    @FXML
    void deletePerson(ActionEvent event) {
        try {
            PeopleParameters.setPeopleParm(peopleListView.getSelectionModel().getSelectedItem());
            myGateway = PeopleLoginController.getGateway();
            myGateway.setWsURL("http://localhost:8080"+"/people/" + peopleListView.getSelectionModel().getSelectedItem().getId());
            myGateway.deletePerson();
            peopleListView.getItems().remove(peopleListView.getSelectionModel());
            ViewSwitcher.getInstance().switchView(ViewType.PeopleListView);
            logger.info("DELETING " + peopleListView.getSelectionModel().getSelectedItem().getFirstName() + " " + peopleListView.getSelectionModel().getSelectedItem().getLastName());
        } catch (Exception e){
            logger.error("No target selected");
        }
    }

    @FXML
    void firstButton(ActionEvent event){
        peoples.clear();
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        result = myGateway.getPeoples(1, query, result.getPageSize());
        labelText.setText("Fetched records " + (result.getCurrentPage()) + " to " + (result.getCurrentPage()-1 + result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        if((result.getCurrentPage()-1+result.getPageSize()) < result.getNumRows())
            NextButton.setDisable(false);
        else
            NextButton.setDisable(true);
        PrevButton.setDisable(true);
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }
        peopleListView.setItems(peoples);
    }

    @FXML
    void lastButton(ActionEvent event){
        int curPage;
        int remainder = 0;
        int div = 0;
        peoples.clear();
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        if((result.getNumRows()+1-result.getPageSize() < 1))
            div = 1;
        else {
            NextButton.setDisable(true);
            remainder = (result.getNumRows()+1) % result.getPageSize();
            div = ((result.getNumRows()+1) / result.getPageSize())+1;
            curPage = ((result.getNumRows() + 1) - remainder+1);
        }
        result = myGateway.getPeoples(div, query, result.getPageSize());
        labelText.setText("Fetched records " + ((result.getCurrentPage()-1) * result.getPageSize()+1) + " to " + ((result.getCurrentPage()-1) * result.getPageSize() + result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        if((((result.getCurrentPage()-1) * result.getPageSize())-result.getPageSize()) < 0)
            PrevButton.setDisable(true);
        else
            PrevButton.setDisable(false);
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }
        peopleListView.setItems(peoples);
    }

    @FXML
    void nextButton(ActionEvent event){
        peoples.clear();
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        result = myGateway.getPeoples((result.getCurrentPage()+1), query, result.getPageSize());
        labelText.setText("Fetched records " + ((result.getCurrentPage()-1) * result.getPageSize()+1) + " to " + ((result.getCurrentPage()-1) * result.getPageSize() + result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        PrevButton.setDisable(false);
        if(((result.getCurrentPage())*result.getPageSize()) >= result.getNumRows())
            NextButton.setDisable(true);
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }
        peopleListView.setItems(peoples);
    }

    @FXML
    void prevButton(ActionEvent event){
        peoples.clear();
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        result = myGateway.getPeoples((result.getCurrentPage()-1), query, result.getPageSize());
        labelText.setText("Fetched records " + (result.getCurrentPage()*result.getPageSize()-result.getPageSize()+1) + " to " + ((result.getCurrentPage()-1) * result.getPageSize()+ result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        NextButton.setDisable(false);
        System.out.println(result.getCurrentPage()*result.getPageSize()-result.getPageSize()+1);
        if(result.getCurrentPage()*result.getPageSize()-result.getPageSize()+1 == 1)
            PrevButton.setDisable(true);
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }
        peopleListView.setItems(peoples);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        peopleListView.setPlaceholder(new Label("No people found"));
        PrevButton.setDisable(true);
        myGateway = PeopleLoginController.getGateway();
        myGateway.setWsURL("http://localhost:8080/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        result = myGateway.getPeoples(1, "", 10);
        labelText.setText("Fetched records " + (result.getCurrentPage()) + " to " + (result.getCurrentPage()-1 + result.getPageSize()) + " out of " + (result.getNumRows()+1) + " records.");
        peopleList = result.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }

        peopleListView.setItems(peoples);
    }

    public void addUser(People user){
        peoples.add(user);
    }
}
