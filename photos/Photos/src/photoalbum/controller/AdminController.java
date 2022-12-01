/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.controller;

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
import photoalbum.Photos.Photos;
import photoalbum.model.Persistence;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.Optional;

import javax.swing.Action;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//NEEDS WORK ON LOGOUT, or else complete
/**
 * This class allows control the actions of the admin page
 */

public class AdminController implements LogoutController{

    @FXML ListView<String> UserList;
    @FXML Button AddUserButton;
    @FXML Button DelUserButton;
    @FXML Button LogOffButton;
    @FXML TextField Enteruser;
    /**
     * An ArrayList of strings that is used to display users
     */
    ObservableList<String> observableArrayList;

    /**
     * Refreshes ListView with users on start of scene
     */
    public void start() {
        //SHOW USERS
        //REFRESH LISTVIEW
        refresh();
        if(!observableArrayList.isEmpty()){
            UserList.getSelectionModel().select(0);
        }
    }
    /**
     * adds user to userlist unless already there
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void addUser(ActionEvent e) throws IOException{
        String userInput = Enteruser.getText().trim();
        //ADD USER TO USERLIST
        boolean alreadyInList = false;
        if(userInput == null || userInput.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input - ERROR");
			alert.setContentText("Input field is empty. Please try again.");
			alert.showAndWait();
			return;
        }
        else if(userInput.equals("admin")){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setContentText("The username admin is protected. Please try a different username!");
			alert.showAndWait();
			return;
        }
        else if(Photos.driver.admin.userExists(userInput) != -1){//check if user in list of users
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User Already Exists - ERROR");
			alert.setContentText("This Username already exists. Please enter a new username.");
			alert.showAndWait();
			return;
        }
        else{
            Photos.driver.admin.addUser(userInput);
            //REFRESH LISTVIEW
            Persistence.save(Photos.driver);
            refresh();
            UserList.getSelectionModel().select(userInput);
            Enteruser.clear();
        }
    }
    /**
     * Deletes user from userlist
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void deleteUser(ActionEvent e) throws IOException{
        int deleteIndex = UserList.getSelectionModel().getSelectedIndex();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setContentText("Confirm you want to delete "+ UserList.getSelectionModel().getSelectedItem()+ ".");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            String delUser =  UserList.getSelectionModel().getSelectedItem();
            Photos.driver.admin.deleteUser(delUser);
            //REFRESH LISTVIEW
            refresh();
            Persistence.save(Photos.driver);
            int lastuserindex = Photos.driver.admin.getUsers().size();
            if(Photos.driver.admin.getUsers().size() == 1) { 
                UserList.getSelectionModel().select(0);
            }
            else if(deleteIndex == lastuserindex) { 
                UserList.getSelectionModel().select(lastuserindex-1);
            }
            else { 
                UserList.getSelectionModel().select(deleteIndex);
            }
        }
        else{
            return;
        }
    }
    /**
     * Refreshes UserList
     */
    public void refresh(){
        UserList.refresh();
        observableArrayList = FXCollections.observableArrayList(Photos.driver.admin.getUsernameList());
        UserList.setItems(observableArrayList);
        UserList.refresh();
    }
    /**
     * Logs the admin out, returning them to the login page
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void logOut(ActionEvent e) throws IOException{
        logMeOut(e);
    }
}