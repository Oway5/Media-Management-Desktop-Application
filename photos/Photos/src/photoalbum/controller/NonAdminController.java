package photoalbum.controller;

import photoalbum.Photos.Persistence;
import photoalbum.model.Album;
import photoalbum.model.User;
import photoalbum.model.Admin;
import photoalbum.Photos.Main;
import photoalbum.controller.LogoutController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import photoalbum.Photos.Persistence;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.*;

import javax.swing.Action;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NonAdminController{

    @FXML Button createAlbum, deleteAlbum, renameAlbum, openAlbum, logout;
    @FXML TextField EnteredAlbumName;
    @FXML ListView date2list, date1list, photonums,albumsList;
    public ObservableList<String> toshow;
    public static User user;
    public static String username;
    
	public static ArrayList<Album> albumlist = new ArrayList<>();
    


    public void start() {


    }

    public void createAlbum(ActionEvent e) throws IOException{
        String AlbumName = EnteredAlbumName.getText().trim();
        Album album = new Album(AlbumName);
        if(AlbumName.isEmpty() || AlbumName == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Name input");
            alert.setContentText("Please enter an album name.");
            alert.showAndWait();
            return;
        }
    else if(user.getAlbums().contains(album)) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Album already exists.");
        alert.setContentText("Try entering a new album!");
        alert.showAndWait();
        return;
    }else {
        user.createAlbum(AlbumName);
        //user.save();
        EnteredAlbumName.clear();
    }
    //user.update() this would be the obsevable listview being updated with whatevr we just changed
}

    public void deleteAlbum(){

    }

    public void renameAlbum(){

    }

    public void openAlbum(){

    }

    public void logout(ActionEvent e)throws IOException{
        logout(e);
    }
    public void updateList(){
        user = main.driver.getcurrentuser();
        albumsList.getItems().clear();
        for(int i=0;i<user.getAlbums().size();i++){
            albumsList.getItems().add(user.getAlbums().get(i).getAlbumName());
        }
        toshow=FXCollections.observableArrayList(albu);
    }

}