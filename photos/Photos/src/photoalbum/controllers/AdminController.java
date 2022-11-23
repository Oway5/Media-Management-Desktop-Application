package photoalbum.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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


public class AdminController{

    @FXML ListView userview;

    @FXML Button adduser;
    @FXML Button deleteuser;

    //ADD THIS TO THE FXML
    @FXML TextField usernameInput;

    public void start() {
        //SHOW USERS
    }

    public void addUser(ActionEvent e){
        String userInput = usernameInput.getText().trim();
        //ADD USER TO USERLIST

        boolean alreadyInList = false;
        if(userInput == null || userInput.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input - ERROR");
			alert.setContentText("Input field is empty. Please try again.");
			alert.showAndWait();
			return;
        }
        else if(alreadyInList){//check if user in list of users
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User Already Exists - ERROR");
			alert.setContentText("This Username already exists. Please enter a new username.");
			alert.showAndWait();
			return;
        }
        else{
            //Add to List

        }
    }

    public void deleteUser(ActionEvent e){
        int deleteIndex = userview.getSelectionModel().getSelectedIndex();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setContentText("Confirm you want to delete "+ userview.getSelectionModel().selectedItemProperty()+ ".");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            //Delete user in main list and refresh
        }
        else{
            return;

        }
    }

    public void logout(){
        
    }



}