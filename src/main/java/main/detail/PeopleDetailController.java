package main.detail;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.*;
import main.login.PeopleLoginController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.list.PeopleListController;
import org.json.JSONObject;

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

    @FXML
    private TableView<Audit> auditData;

    @FXML
    public TableColumn<Audit, java.sql.Timestamp> dateTime;

    @FXML
    public TableColumn<Audit, String> changedBy;

    @FXML
    public TableColumn<Audit, String> desc;

    //Model
    private People people;

    private JSONObject originalValues = new JSONObject();

    public PeopleDetailController(People people){ this.people = people; }

    private ObservableList<Audit> audits;

    public PeopleDetailController(){
        audits = FXCollections.observableArrayList();
    }

    @FXML
    void savePeople(ActionEvent event){
        if(Integer.parseInt(peopleId.getText()) != 0) {
            if(validateInfo(peopleFirstName.getText(), peopleLastName.getText(), peopleDateOfBirth.getText()) == true) {
                Period period = Period.between(LocalDate.parse(peopleDateOfBirth.getText()), LocalDate.now());
                int age = period.getYears();
                logger.info("UPDATING " + people.getFirstName() + " " + people.getLastName());
                myGateway = PeopleLoginController.getGateway();
                myGateway.setWsURL("http://localhost:8080" + "/people/" + peopleId.getText());
                JSONObject passJson = new JSONObject();

                passJson.put("id", peopleId.getText());
                passJson.put("lastName", peopleLastName.getText());
                passJson.put("firstName", peopleFirstName.getText());
                passJson.put("dateOfBirth", peopleDateOfBirth.getText());
                passJson.put("age", Integer.toString(age));
                passJson.put("lastModified", people.getLastModified());

                if(((String) passJson.get("id")).compareTo(((String) originalValues.get("id"))) == 0)
                    passJson.remove("id");
                if(((String) passJson.get("lastName")).compareTo(((String) originalValues.get("lastName"))) == 0)
                    passJson.remove("lastName");
                if(((String) passJson.get("firstName")).compareTo(((String) originalValues.get("firstName"))) == 0)
                    passJson.remove("firstName");
                if(((String) passJson.get("dateOfBirth")).compareTo(((String) originalValues.get("dateOfBirth"))) == 0)
                    passJson.remove("dateOfBirth");
                if(((String) passJson.get("age")).compareTo(((String) originalValues.get("age"))) == 0)
                    passJson.remove("age");

                if(!passJson.isEmpty()){
                    myGateway.updatePerson(passJson);
                }
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
                JSONObject passJson = new JSONObject();
                passJson.put("id", 0);
                passJson.put("lastName", peopleLastName.getText());
                passJson.put("firstName", peopleFirstName.getText());
                passJson.put("dateOfBirth", peopleDateOfBirth.getText());
                passJson.put("age", age);
                myGateway.insertPerson(passJson);
                //insert.addUser(new People(Integer.parseInt(String.valueOf(myGateway.getResponse().charAt(6))), peopleFirstName.getText(), peopleLastName.getText(), LocalDate.parse(peopleDateOfBirth.getText()), age));
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
        ArrayList<Audit> auditList = new ArrayList<Audit>();
        myGateway = PeopleLoginController.getGateway();

        peopleFirstName.setText(people.getFirstName());
        peopleLastName.setText(people.getLastName());
        peopleId.setText("" + people.getId());
        peopleDateOfBirth.setText("" + people.getDateOfBirth());
        peopleAge.setText("" + people.getAge());

        originalValues.put("id", peopleId.getText());
        originalValues.put("lastName", peopleLastName.getText());
        originalValues.put("firstName", peopleFirstName.getText());
        originalValues.put("dateOfBirth", peopleDateOfBirth.getText());
        originalValues.put("age", peopleAge.getText());

        myGateway.setWsURL("http://localhost:8080" + "/people/" + peopleId.getText() + "/audittrail");
        audits = FXCollections.observableArrayList();
        auditList = myGateway.getAudit();

        desc.setCellValueFactory(new PropertyValueFactory<>("change_msg"));
        dateTime.setCellValueFactory(new PropertyValueFactory<>("when_occurred"));
        changedBy.setCellValueFactory(new PropertyValueFactory<>("changed_by"));
        auditData.setPlaceholder(new Label("No audit trail created yet for this Person"));

        for(Audit a : auditList) {
            audits.add(a);
        }

        auditData.setItems(audits);
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
