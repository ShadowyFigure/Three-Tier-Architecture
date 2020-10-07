package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.People;
import main.detail.PeopleDetailController;
import main.list.PeopleListController;
import main.login.PeopleLoginController;
import java.time.LocalDate;

public class ViewSwitcher implements Initializable {
    private static ViewSwitcher instance = null;

    @FXML
    private BorderPane rootPane;

    private ViewSwitcher() {

    }

    public static ViewSwitcher getInstance() {
        if(instance == null)
            instance = new ViewSwitcher();
        return instance;
    }

    public void switchView(ViewType viewType) {
        FXMLLoader loader = null;

        try {
            switch(viewType) {
                case PeopleLoginView:
                    loader = new FXMLLoader(ViewSwitcher.class.getResource("/peopleloginview.fxml"));
                    loader.setController(new PeopleLoginController());
                    break;

                case PeopleDetailView:
                    loader = new FXMLLoader(ViewSwitcher.class.getResource("/peopledetailview.fxml"));
                    loader.setController(new PeopleDetailController(PeopleParameters.getPeopleParm()));
                    break;

                case PeopleListView:
                    loader = new FXMLLoader(ViewSwitcher.class.getResource("/peoplelistview.fxml"));
                    loader.setController(new PeopleListController());
                    break;
                default:
                    break;
            }
            Parent rootNode = loader.load();
            rootPane.setCenter(rootNode);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // load the initial view: widget detail into the main view
        //PeopleParameters.setPeopleParm(new People(0, "Adam", "Smith", LocalDate.of(1997, 01, 03), 23));
        switchView(ViewType.PeopleLoginView);

    }
}