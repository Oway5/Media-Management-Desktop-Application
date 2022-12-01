/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.controller;

import photoalbum.model.Album;
import photoalbum.model.User;
import photoalbum.model.Admin;
import photoalbum.Photos.Photos;
import photoalbum.controller.LogoutController;
import photoalbum.controller.LogoutController;

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
import photoalbum.model.Persistence;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.*;

import javax.swing.Action;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Add default selection
//Save (Persistence calls)
/**
 * Class allows the user to add, delete, and modify albums. Also lets the user logout and switch to search scene
 */
public class NonAdminController implements LogoutController{

    @FXML Button createAlbum, deleteAlbum, renameAlbum, openAlbum, logout, search;
    @FXML TextField EnteredAlbumName;
    @FXML ListView<String> albumsList;
    @FXML Label usertitle;
    /**
     * An ArrayList of strings that is used to display albums
     */
    private ObservableList<String> toshow;
    // private static User user;
    // private String username;
    // private String display;
    /**
     * An ArrayList of strings that is used to save and manipulate a user's alubms
     */
	private static ArrayList<String> albumlist = new ArrayList<>();
    /**
     * Switches scene to Search.fxml
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void search(ActionEvent e) throws IOException{
        FXMLLoader searchLoader = new FXMLLoader();
        searchLoader.setLocation(getClass().getResource("/photoalbum/view/Search.fxml"));

        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/Search.fxml"));
        // Parent sceneManager = (Parent) fxmlLoader.load();
        // SearchController searchController = fxmlLoader.getController();

        Parent sceneManager = (Parent) searchLoader.load();
        SearchController searchController = searchLoader.getController();

        Scene searchScene = new Scene(sceneManager);
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        searchController.start();
        primaryStage.setScene(searchScene);
        primaryStage.show();
    }

//Fix Index and Selection

    /**
    * Refreshes current user's albums, when scene loads
    */
    public void start() {
        refresh();

        if(toshow.size() > 0){
            albumsList.getSelectionModel().select(0);
        }
        usertitle.setText(Photos.driver.getCurrentUser().getName() + "'s Albums");

    }

    /**
     * Creates album according to inputted specification
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void createAlbum(ActionEvent e) throws IOException{
        String AlbumName = EnteredAlbumName.getText().trim();
        // if(AlbumName.equals("")){
        //     System.out.println("SOMETHING GOES WRONG HERE");
        // }
        // Album album = new Album(AlbumName);
        if(AlbumName.isEmpty() || AlbumName == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Name input");
            alert.setContentText("Please enter an album name.");
            alert.showAndWait();
            return;
        }
        else if(Photos.driver.getCurrentUser().findAlbum(AlbumName) != null) {
            EnteredAlbumName.clear();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album already exists.");
            alert.setContentText("Try entering a new album name!");
            alert.showAndWait();
            return;
        }else {
            Photos.driver.getCurrentUser().createAlbum(AlbumName);
            //user.save();
            EnteredAlbumName.clear();

            //REFRESH
            //SAVE
            User.save(Photos.driver.getCurrentUser());
            refresh();

        }
    //user.update() this would be the obsevable listview being updated with whatevr we just changed
}

    //Check index errors
    /**
     * Deletes album specified by input
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void deleteAlbum(ActionEvent e) throws IOException{
        int index = albumsList.getSelectionModel().getSelectedIndex();
        if(index < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Albums to Delete");
            a.setHeaderText(null);
            a.setContentText("There are no albums to delete. Add new albums first!");
            return;
        }

        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Delete Confirmation");
        a.setHeaderText(null);
        a.setContentText("Are you sure you want to delete " + Photos.driver.getCurrentUser().getAlbumAt(index).getAlbumName());
        Optional<ButtonType> r = a.showAndWait();
        if (r.get() == ButtonType.OK) {
			Photos.driver.getCurrentUser().deleteAlbum(index);
            User.save(Photos.driver.getCurrentUser());
			refresh();
			//User.save(user);
            //Im not sure how we're ensuring that something is saved
			   
            
			if (Photos.driver.getCurrentUser().getAlbums().size() == 0) {
				// deleteAlbum.setVisible(false);
		    } else {
		    	int lastuserindex = Photos.driver.getCurrentUser().getAlbums().size();
				if (Photos.driver.getCurrentUser().getAlbums().size() == 1) {
					albumsList.getSelectionModel().select(0);
				} else if (index == lastuserindex) {
					albumsList.getSelectionModel().select(lastuserindex-1);
				} else { 
					albumsList.getSelectionModel().select(index);
				}
			}
		} else {
			return;
		}
		return;   
    }
    /**
     * Changes selected album's name
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void renameAlbum(ActionEvent e) throws IOException{
        int selectionIndex = albumsList.getSelectionModel().getSelectedIndex();

        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Albums to Rename");
            a.setHeaderText(null);
            a.setContentText("There are no albums to rename. Add new albums first!");
            return;
        }

        String newAlbumName = EnteredAlbumName.getText().trim();
        if(newAlbumName.isEmpty() || newAlbumName == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter a album name to rename to");
			alert.showAndWait();
            return;
        }
        EnteredAlbumName.clear();
        

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Empty Input");
        alert.setHeaderText("Confirm that you would like to change "+ Photos.driver.getCurrentUser().getAlbums().get(selectionIndex).getAlbumName() + " to " + newAlbumName);

        Optional<ButtonType> buttonClicked=alert.showAndWait();
        if (buttonClicked.get()==ButtonType.OK) {
            alert.close();
            //  int selectionIndex = albumsList.getSelectionModel().getSelectedIndex();
            Photos.driver.getCurrentUser().getAlbums().get(selectionIndex).setAlbumName(newAlbumName);

            
            //REFRESH
            refresh();
            //SAVE
            User.save(Photos.driver.getCurrentUser());
            albumsList.getSelectionModel().select(selectionIndex);
        }
        else {
            alert.close();
            return;
        }

    }

    //Check index & empty album list
    /**
     * opens selected album by switching scenes to Album.fxml with selected album in view
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void openAlbum(ActionEvent e) throws IOException{
        //SWITCH TO ALBUM CONTROLLER
        //SET CURRENT ALBUM TO THE SELECTED ALBUM

        int selectionIndex = albumsList.getSelectionModel().getSelectedIndex();

        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Albums to Open");
            a.setHeaderText(null);
            a.setContentText("There are no albums to open. Add new albums first!");
            return;
        }


        FXMLLoader albumloader = new FXMLLoader();
        albumloader.setLocation(getClass().getResource("/photoalbum/view/Album.fxml"));
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/Album.fxml"));
        // Parent sceneManager = (Parent) fxmlLoader.load();
        Parent sceneManager = (Parent) albumloader.load();
        // AlbumController albumController = fxmlLoader.getController();
        AlbumController albumController = albumloader.getController();

        Scene albumScene = new Scene(sceneManager);
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
       
        Photos.driver.setCurrentAlbum(Photos.driver.getCurrentUser().getAlbums().get(selectionIndex));

        albumController.start();
        primaryStage.setScene(albumScene);
        primaryStage.show();
    }
    /**
     * logs current user out, returning them to login scene
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void logOut(ActionEvent e)throws IOException{
        logMeOut(e);
        //no idea what to do 
    }

    /**
     * Refreshes observable arraylist and albumlist with current albums
     */
    public void refresh(){

        albumlist.clear();
        for(Album a: Photos.driver.getCurrentUser().getAlbums()){
            albumlist.add(a.printAlbumDetails());
        }

        toshow=FXCollections.observableArrayList(albumlist);
        albumsList.setItems(toshow);


        // Photos.driver.getCurrentUser().getAlbums();

        // user = Photos.driver.getCurrentUser();
        // albumsList.getItems().clear();
        // for(int i=0;i<user.getAlbums().size();i++){
        //     display+=user.getAlbums().get(i).toString();
        //     display+=" "+user.getAlbums().get(i).getPhotoCount();
        //     display+=" "+user.getAlbums().get(i).getEarliestPhoto().toString();
        //     display+=" "+user.getAlbums().get(i).getLatestPhoto().toString();
        //     albumlist.add(display);
        //     display="";
        // }
        
        
        albumsList.refresh();
    }


}