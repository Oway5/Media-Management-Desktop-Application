package photoalbum.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javax.swing.Action;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SearchController{
    @FXML TextField optionalInput1, optionalInput2, mandatoryInput1, mandatoryInput2, beginningDateInput, endDateInput;
    @FXML Button makeAlbum, logout;
    @FXML ListView searchResults;

    public void start(){

    }

    //ADD SEARCH BUTTON
    public void search(ActionEvent e){
        //Check end and beginning date
        //Check if there are any optional tags
        //Check if there any mandatory tags
        //Review to check cases on website
    }

    public void saveResultsAsAlbum(ActionEvent e){

    }

    //ADD BACK BUTTON
    public void goBack(){
        //Go back to Album page
    }
}