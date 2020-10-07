package main.detail;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import main.PeopleGateway;
import main.login.PeopleLoginController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.People;
import main.ViewSwitcher;
import main.ViewType;
import main.list.PeopleListController;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.Period;

public class PeopleDetailController implements Initializable{
    private static Logger logger = LogManager.getLogger();
    private static PeopleGateway myGateway;

    @FXML
    private TextField peopleFirstName;

    @FXML
    private TextField peopleLastName;

    @FXML
    private TextField peopleId;

    @FXML
    private TextField peopleDateOfBirth;

    @FXML
    private TextField peopleAge;

    //Model
    private People people;

    public PeopleDetailController(People people){ this.people = people; }

    @FXML
    void savePeople(ActionEvent event){
        if(Integer.parseInt(peopleId.getText()) != 0) {
            if(validateInfo(peopleFirstName.getText(), peopleLastName.getText(), peopleDateOfBirth.getText()) == true) {
                logger.info("UPDATING " + people.getFirstName() + " " + people.getLastName());
                myGateway = PeopleLoginController.getGateway();
                myGateway.setWsURL("http://localhost:8080" + "/people/1");
                myGateway.updatePerson();
                //Switch View
                ViewSwitcher.getInstance().switchView(ViewType.PeopleListView);
            }
            else{
                //Invalid edit fields (Logged in validateInfo)
            }
        }
        else
        {
            if(validateInfo(peopleFirstName.getText(), peopleLastName.getText(), peopleDateOfBirth.getText()) == true) {
                Period period = Period.between(LocalDate.parse(peopleDateOfBirth.getText()), LocalDate.now());
                int age = period.getYears();
                PeopleListController insert = new PeopleListController();
                logger.info("CREATING " + peopleFirstName.getText() + " " + peopleLastName.getText());
                myGateway = PeopleLoginController.getGateway();
                myGateway.setWsURL("http://localhost:8080" + "/people");
                myGateway.insertPerson();
                insert.addUser(new People(Integer.parseInt(String.valueOf(myGateway.getResponse().charAt(6))), peopleFirstName.getText(), peopleLastName.getText(), LocalDate.parse(peopleDateOfBirth.getText()), age));
                //Switch View
                ViewSwitcher.getInstance().switchView(ViewType.PeopleListView);
            }
            else{
                //Invalid fields (Logged in valdiateInfo)
            }
        }
    }

    @FXML
    void cancelPeople(ActionEvent event){
        //Switch View
        ViewSwitcher.getInstance().switchView(ViewType.PeopleListView);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        peopleFirstName.setText(people.getFirstName());
        peopleLastName.setText(people.getLastName());
        peopleId.setText("" + people.getId());
        peopleDateOfBirth.setText("" + people.getDateOfBirth());
        peopleAge.setText("" + people.getAge());
    }

    private boolean validateInfo(String firstName, String lastName, String dateOfBirth){
        LocalDate localDate = LocalDate.parse(dateOfBirth);
        LocalDate now = LocalDate.now();

        if(firstName.length() < 1 || firstName.length() > 100) {
            logger.error("First Name length error");
            return false;
        }
        else if(lastName.length() < 1 || lastName.length() > 100){
            logger.error("Last Name length error");
            return false;
        }
        else if(localDate.compareTo(now) > 0){
            logger.error("DOB is after Today");
            return false;
        }
        return true;
    }
}
