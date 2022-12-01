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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import photoalbum.Photos.Photos;
import photoalbum.model.Album;
import photoalbum.model.Photo;
import photoalbum.model.Tag;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Action;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//SAVE
/**
 * Class allows user to search for photos in multiple ways
 */
public class SearchController implements LogoutController{
    @FXML TextField mandatoryTag1Name, mandatoryTag1Value, mandatoryTag2Name, mandatoryTag2Value, optionalTag1Name, optionalTag1Value, beginningDateInput, endDateInput, albumNameInput;
    @FXML Button makeAlbum, logout, searchByTagButton, searchByDateButton, backButton;
    @FXML ListView<String> searchResults;
    /**
     * observable list of all search results in String type
     */
    ObservableList<String> observablePhotoSearchResultsArray = FXCollections.observableArrayList(new ArrayList<String>());
    /**
     * Arraylist of photos used to save search results
     */
    ArrayList<Photo> photoSearchResultsArray = new ArrayList<Photo>();
    // int searchNumber = 0;
    //Print results with printDetails
    /**
     * refreshes list of photos after scene loads
     */
    public void start(){
        refresh();
    }
    /**
     * Updates list of photos and ObservableList to match what it actually is
     */
    public void refresh(){
        observablePhotoSearchResultsArray.clear();
        ArrayList<String> photoDetails = new ArrayList<String>();
        for(Photo p: photoSearchResultsArray){
            photoDetails.add(p.printDetails());
        }
        observablePhotoSearchResultsArray = FXCollections.observableArrayList(photoDetails);
        searchResults.setItems(observablePhotoSearchResultsArray);
        searchResults.refresh();
    }
    /**
     * logs current user out
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void logOut(ActionEvent e) throws IOException{
        logMeOut(e);
    }
    /**
     * User can search for photos by a tag value and name and save it to photoSearchResultsArray
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void searchByTag(ActionEvent e){
        String man1name = mandatoryTag1Name.getText().trim();
        String man1value = mandatoryTag1Value.getText().trim();
        String man2name = mandatoryTag2Name.getText().trim();
        String man2value = mandatoryTag2Value.getText().trim();
        String opt1name = optionalTag1Name.getText().trim();
        String opt1value = optionalTag1Value.getText().trim();
        // String opt2name = optionalTag2Name.getText().trim();
        // String opt2value = optionalTag2Value.getText().trim();

        mandatoryTag1Name.clear();
        mandatoryTag1Value.clear();
        mandatoryTag2Name.clear();
        mandatoryTag2Value.clear();
        optionalTag1Name.clear();
        optionalTag1Value.clear();
        // optionalTag2Name.clear();
        // optionalTag2Value.clear();



        if(man1name.isEmpty() || man1name == null || man1value.isEmpty() || man1value == null){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Mandatory Tag Empty");
			alert.setHeaderText("There must be at least 1 mandatory tag to search for!");
			alert.showAndWait();
            return;
        }
        ArrayList<Tag> searchTags = new ArrayList<Tag>();
        Tag tag1 = new Tag(man1name, man1value);
        searchTags.add(tag1);
        if(man2name.isEmpty() || man2name == null || man2value.isEmpty() || man2value == null){
            if(opt1name.isEmpty() || opt1name == null || opt1value.isEmpty() || opt1value == null){
                //Single Search
                photoSearchResultsArray = Photos.driver.getCurrentUser().orTagSearch(searchTags);
            }
            else{
                //Search with OR
                Tag tag2 = new Tag(opt1name, opt1value);
                searchTags.add(tag2);
                photoSearchResultsArray = Photos.driver.getCurrentUser().orTagSearch(searchTags);
            }
        }
        else{
            //Search with AND
            Tag tag2 = new Tag(man2name, man2value);
            searchTags.add(tag2);
            photoSearchResultsArray = Photos.driver.getCurrentUser().andTagSearch(searchTags);
        }
        refresh();
        //SAVE
        // searchNumber++;
    }
    /**
     * allows user to search for photo by date and save it to photoSearchResultsArray
     * @param e action event provided by fxml
     * @throws ParseException
     */
    public void searchByDate(ActionEvent e) throws ParseException{
        String dateBeginning = beginningDateInput.getText().trim();
        String dateEnd = endDateInput.getText().trim();

         if(dateBeginning.isEmpty() || dateBeginning == null || dateEnd.isEmpty() || dateEnd == null ) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter valid dates in mm/dd/yyyy format!");
			alert.showAndWait();
            return;
        }

        beginningDateInput.clear();
        endDateInput.clear();
        Date date1;
        Date date2;
        try{
            date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dateBeginning); 
            date2 = new SimpleDateFormat("MM/dd/yyyy").parse(dateEnd);   
        }
        catch(Exception exception){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Date Format");
			alert.setHeaderText("Please enter valid dates in mm/dd/yyyy format!");
			alert.showAndWait();
            return;
        }
        if(date2.before(date1)){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Date Range");
			alert.setHeaderText("Please enter valid dates in mm/dd/yyyy format! The first date must be less than or equal to the second date.");
			alert.showAndWait();
            return;
        }
        
        photoSearchResultsArray = Photos.driver.getCurrentUser().getPhotosInRange(date1, date2);
        refresh();
        //SAVE

        // searchNumber++;
    }
    /**
     * Allows user to save photoSearchResultsArray as an album
     * @param e action event provided by fxml
     * 
     */

    public void saveResultsAsAlbum(ActionEvent e){
        if(photoSearchResultsArray.size() == 0){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Search Results");
			alert.setHeaderText("No results that match your search.");
			alert.setContentText("Please try different search parameters");
            alert.showAndWait();
            return;
        }

        String newAlbumName = albumNameInput.getText().trim();
        albumNameInput.clear();
        if(newAlbumName.isEmpty() || newAlbumName == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter an Album name!");
			alert.showAndWait();
            return;
        }
        if(Photos.driver.getCurrentUser().findAlbum(newAlbumName) != null){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Exists");
			alert.setHeaderText("Please enter a different Album name. This album already exists!");
			alert.showAndWait();
            return;
        }

        Album newAlbum = new Album(newAlbumName, photoSearchResultsArray);
        Photos.driver.getCurrentUser().addAlbum(newAlbum);
        observablePhotoSearchResultsArray.clear();
    }

    //ADD BACK BUTTON
    /**
     * Lets the user go back to the album page
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void goBack(ActionEvent e) throws IOException{

         FXMLLoader nonadminLoader = new FXMLLoader();
        nonadminLoader.setLocation(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));

        Parent sceneManager = (Parent) nonadminLoader.load();
        NonAdminController nonadmin = nonadminLoader.getController();


        //Go back to Album page
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));
        // Parent sceneManager = (Parent) fxmlLoader.load();
        // NonAdminController nonadmin = fxmlLoader.getController();
        Scene nonadminScene = new Scene(sceneManager);
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        nonadmin.start();
        primaryStage.setScene(nonadminScene);
        primaryStage.show();
    }

    
}