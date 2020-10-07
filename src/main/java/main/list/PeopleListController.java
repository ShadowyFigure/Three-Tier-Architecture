package main.list;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import main.People;
import main.ViewSwitcher;
import main.ViewType;
import main.PeopleParameters;
import main.PeopleGateway;
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

    @FXML
    private Button myButton;

    @FXML
    private ListView<People> peopleListView;

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
            PeopleParameters.setPeopleParm(new People(0, "", "", LocalDate.of(0000, 01, 01), 0));
            ViewSwitcher.getInstance().switchView(ViewType.PeopleDetailView);
        }
    }

    @FXML
    void deletePerson(ActionEvent event) {
        try {
            PeopleParameters.setPeopleParm(peopleListView.getSelectionModel().getSelectedItem());
            myGateway = PeopleLoginController.getGateway();
            myGateway.setWsURL("http://localhost:8080"+"/people/1");
            myGateway.deletePerson();
            peopleListView.getItems().remove(peopleListView.getSelectionModel());
            logger.info("DELETING " + peopleListView.getSelectionModel().getSelectedItem().getFirstName() + " " + peopleListView.getSelectionModel().getSelectedItem().getLastName());
        } catch (Exception e){
            logger.error("No target selected");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        myGateway = PeopleLoginController.getGateway();
        myGateway.setWsURL("http://localhost:8080"+"/people");
        ArrayList<People> peopleList = new ArrayList<People>();
        peopleList = myGateway.getPeoples();
        for(People p : peopleList) {
            peoples.add(p);
        }

        peopleListView.setItems(peoples);
    }

    public void addUser(People user){
        peoples.add(user);
    }
}
