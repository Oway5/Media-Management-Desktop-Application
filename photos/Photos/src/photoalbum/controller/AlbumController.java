/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.controller;

import java.awt.Desktop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photoalbum.model.Album;
import photoalbum.model.Photo;
import photoalbum.model.Tag;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import photoalbum.model.Persistence;
import photoalbum.Photos.Photos;

import java.io.File;
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

//Selection
//Saving
/**
 * The purpose of this class is to allow the logged in user to control/modify thier albums
 * Implements LogOutController, allowing the user to logout at anytime
 */
public class AlbumController implements LogoutController{
    @FXML Button addPhoto, removePhoto, addCaption, addTag, deleteTag, copy_paste, movePhoto, nextPhoto, previousPhoto, logout, changeCaption, search, backbutton;
    @FXML TextField taginput, captionInput, valueinput;
    @FXML ListView<Photo> photos;

    /**
    * the purpose of this class is to allow a thumbnail view of photos in an album
    */
    public class Thumbnail extends ListCell<Photo>{
        HBox hbox = new HBox();
        Image thumbnailImage;
        ImageView thumbnailImageView;
        TextArea detailTextArea;
        Pane pane = new Pane();
        /**
         * creates thumbnail
         */
        public Thumbnail(){
            super();

            hbox.getChildren().addAll(thumbnailImageView, detailTextArea, pane);
            hbox.setHgrow(pane, Priority.ALWAYS);
        }
        
        /**
         * updates item and thumbnail 
         * @param photoItem takes in a photo to update a given item
         * @param empty ensures theres no graphical artifacts if empty
         */
        @Override
        public void updateItem(Photo photoItem, boolean empty){
            super.updateItem(photoItem, empty);
            if(empty || photoItem == null){
                setText(null);
                setGraphic(null);
            }
            else{
                File pictureFile = photoItem.getFile();
                thumbnailImage = new Image(pictureFile.toURI().toString());
                // thumbnailImage = new Image(pictureFile.toURI().toString(), 100, 100, false, false);

                thumbnailImageView = new ImageView(thumbnailImage);
                thumbnailImageView.setFitHeight(100);
                thumbnailImageView.setFitWidth(100);
                thumbnailImageView.setPreserveRatio(true);
                // thumbnailImageView.setImage(thumbnailImage);
                detailTextArea = new TextArea(photoItem.printDetails());
                setText(photoItem.printDetails());
                setGraphic(thumbnailImageView);
                // detailTextArea = setText(selectedPhoto.printDetails())
            }
        }
    }
     /**
     * An ArrayList of photos
     */
    ObservableList<Photo> observablePhotoList;

    // @FXML 
    //ADD IMAGEVIEW ATTRIBUTE HERE TO SHOW THE IMAGE IN A SEPARATE AREA
    @FXML ImageView photoDisplay;

    //ADD ALBUM NAME TEXTFIELD
    @FXML TextField albuminput;
    //THIS ISNT ADDED YET

    //ADD BUTTON TO DISPLAY IMAGE
    @FXML Button displayImage;
    @FXML TextArea detailtextarea;
    @FXML Label detailsview;

    // final
    /**
     * pulls file depending on what user chooses
     */
     FileChooser fileChooser = new FileChooser();
    private Desktop desktop = Desktop.getDesktop();
//https://www.tutorialspoint.com/javafx/javafx_images.htm
//https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
// https://stackoverflow.com/questions/35450990/how-to-get-file-path-from-javafx-filechooser
    /**
     * updates observablelist on start
     */
    public void start(){
        //Refresh Listview with photos
        //Select first photo (if any)
        //Refresh Imageview that shows the selected photo
        
        refresh();
        if(observablePhotoList.size() >0){
            photos.getSelectionModel().select(0);
        }

    }
    /**
     * refreshes observable list 
     */
    public void refresh(){
        observablePhotoList = FXCollections.observableArrayList(Photos.driver.getCurrentAlbum().getPhotos());
        photos.setItems(observablePhotoList);
        // photos.setCellFactory(param -> new Thumbnail());
        photos.setCellFactory(param -> new ListCell<Photo>(){
            private ImageView imageView = new ImageView();
            
            @Override
            public void updateItem(Photo photo, boolean empty){
                super.updateItem(photo, empty);

                if(empty || photo == null){
                    setText(null);
                    setGraphic(null);
                }
                else{
                    File pictureFile = photo.getFile();
                    Image i = new Image(pictureFile.toURI().toString());
                    imageView.setImage(i);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    String deets = photo.printDetails();
                    setText(deets);
                    setGraphic(imageView);
                }
            }
        });

        if(observablePhotoList.size() > 0){
            photos.getSelectionModel().select(0);
            if(Photos.driver.getCurrentAlbum().getDisplayPhotoIndex() == -1){
                Photos.driver.getCurrentAlbum().setDisplayPhotoIndex(0);
            }
        }

       
    }
 
    /**
     * adds photo to the album the user currently in
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void addPhoto(ActionEvent e) throws IOException{
		
        fileChooser.setTitle("Select Picture");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );                 
        fileChooser.getExtensionFilters().addAll(
            // new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("BMP", "*.bmp"),
            new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
            new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(primaryStage);

        // if (file != null) {
        //     try {
        //         desktop.open(file);
        //     } catch (IOException ex) {
        //         System.out.println("ERROR");
        //     }
        // }

        //Add current Album attribute when opening album
        Photo newPhoto = new Photo(file);
        if(!Photos.driver.getCurrentAlbum().addPhoto(newPhoto)){
            Alert a= new Alert(AlertType.ERROR);
            a.setTitle("Photo Already Exists");
            a.setContentText("This photo already exists in the selected album. Please select a different photo!");
            a.showAndWait();
            return;
        }
        else{
            //REFRESH TO SHOW PICTURES IN LIST
           Album.save(Photos.driver.getCurrentAlbum()); 
           refresh();
           photos.getSelectionModel().select(newPhoto);
           //Might not work
        }


	}
    /**
     * switches scene to back to album
     * @param e action event provided by fxml
     * @throws IOException
     */
    //Add this function to fxml
    public void goBack(ActionEvent e) throws IOException{
        //SWITCH SCENE
        //Save things here
        FXMLLoader nonadminLoader = new FXMLLoader();
		nonadminLoader.setLocation(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));

        Parent sceneManager = (Parent) nonadminLoader.load();
		NonAdminController nonadmin = nonadminLoader.getController();
        

        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/NonAdmin.fxml"));
        // Parent sceneManager = (Parent) fxmlLoader.load();
        // NonAdminController nonadmin = fxmlLoader.getController();
        Scene nonadminScene = new Scene(sceneManager);
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        nonadmin.start();
        primaryStage.setScene(nonadminScene);
        primaryStage.show();
    }
    
    /**
     * removes selected photo from album
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void removePhoto(ActionEvent e) throws IOException{
        //Getting selected Photo
        //Make sure to update currentphoto attribute in album and use that for displaying the slideshow.

        Photo selectedPhoto;
        
        int deletionIndex = photos.getSelectionModel().getSelectedIndex();
        if(deletionIndex != -1){
            Photos.driver.getCurrentAlbum().deletePhoto(deletionIndex);

            int lastuserindex = Photos.driver.getCurrentAlbum().getPhotos().size();
            if (Photos.driver.getCurrentAlbum().getPhotos().size() == 1) {
                photos.getSelectionModel().select(0);
            } else if (deletionIndex == lastuserindex) {
                photos.getSelectionModel().select(lastuserindex-1);
            } else { 
                photos.getSelectionModel().select(deletionIndex);
            }

            if(deletionIndex == Photos.driver.getCurrentAlbum().getDisplayPhotoIndex()){
                photoDisplay.setImage(null);
                //SHOWING IMAGE DETAILS
                detailtextarea.clear();
                // detailtextarea.setText(selectedPhoto.printDetails());
            }
            //REFRESH TO SHOW CHANGES
            Album.save(Photos.driver.getCurrentAlbum());
            refresh();
        }
        
    }
    /**
     * adds caption to selected photo
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void addCaption(ActionEvent e) throws IOException{
        int selectionIndex = photos.getSelectionModel().getSelectedIndex();
        
        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Photo Selected");
            a.setHeaderText(null);
            a.setContentText("There are no photos to add the caption to. Add new photos first!");
            return;
        }
        
        String newCaption = captionInput.getText().trim();
        captionInput.clear();
        if(newCaption.isEmpty() || newCaption == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter a caption");
			alert.showAndWait();
            return;
        }

        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Empty Input");
        alert.setHeaderText("Confirm that you would like to change the caption to " + newCaption);
        Optional<ButtonType> buttonClicked=alert.showAndWait();
        if (buttonClicked.get()==ButtonType.OK) {
            alert.close();
            Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
            Photos.driver.getCurrentAlbum().getSelectedPhoto().changeCaption(newCaption);

            
        //REFRESH
            refresh();
            //SAVE

            // Photos.driver.getCurrentAlbum().getSelectedPhoto();
            Photo.save(Photos.driver.getCurrentAlbum().getSelectedPhoto());
            photos.getSelectionModel().select(selectionIndex);

            if(photoDisplay != null)
            detailtextarea.setText(Photos.driver.getCurrentAlbum().getSelectedPhoto().printDetails());

        }
        else {
            alert.close();
            return;
        }

        

    }
    /**
     * adds tag to selected photo
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void addTag(ActionEvent e) throws IOException{
        int selectionIndex = photos.getSelectionModel().getSelectedIndex();

        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Photo Selected");
            a.setHeaderText(null);
            a.setContentText("There are no photos to add the tag to. Add new photos first!");
            return;
        }

        String name=taginput.getText().trim();
        String value=valueinput.getText().trim();
        taginput.clear();
        valueinput.clear();
        if(name.isEmpty()||value.isEmpty()){
            Alert a= new Alert(AlertType.ERROR);
            a.setTitle("Tag Error");
            a.setContentText("Please fill both fields");
            a.showAndWait();
            return;
        }
        
        Tag tag= new Tag(name, value);

        Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
        if(Photos.driver.getCurrentAlbum().getSelectedPhoto().hasTag(tag)){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Tag Already Exists");
			alert.setHeaderText("Please enter different values because this picture already has listed tag");
			alert.showAndWait();
            return;
        }
        else{
            Photos.driver.getCurrentAlbum().getSelectedPhoto().addTag(name, value);

            if(photoDisplay != null)
            detailtextarea.setText(Photos.driver.getCurrentAlbum().getSelectedPhoto().printDetails());
        }
        
        //SAVE AND REFRESH
        // Photo.save(Photos.driver.getCurrentAlbum().getSelectedPhoto());
        Persistence.save(Photos.driver);
        refresh();

    }
    /**
     * deletes inputed tag from selected photo
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void deleteTag(ActionEvent e) throws IOException{
        int selectionIndex = photos.getSelectionModel().getSelectedIndex();
        
        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Photo Selected");
            a.setHeaderText(null);
            a.setContentText("There are no photos to delete the tag of. Add new photos first!");
            return;
        }
        
        String name=taginput.getText().trim();
        String value=valueinput.getText().trim();
        taginput.clear();
        valueinput.clear();
        if(name.isEmpty()||value.isEmpty()){
            Alert a= new Alert(AlertType.ERROR);
            a.setTitle("Tag Error");
            a.setContentText("Please fill both fields for a valid tag.");
            a.showAndWait();
            return;
        }
        
        Tag tag= new Tag(name, value);


        Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
        if(Photos.driver.getCurrentAlbum().getSelectedPhoto().hasTag(tag)){
            Photos.driver.getCurrentAlbum().getSelectedPhoto().delTag(name, value);

            if(photoDisplay != null)
            detailtextarea.setText(Photos.driver.getCurrentAlbum().getSelectedPhoto().printDetails());
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Tag Doesn't Exist");
			alert.setHeaderText("This photo does not have the mentioned tag.");
			alert.showAndWait();
            return;
        }

        // Photo.save(Photos.driver.getCurrentAlbum().getSelectedPhoto());
        Persistence.save(Photos.driver);
        refresh();
    }

    /**
     * user can copy and paste selected photo into a seperate album
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void copy_paste(ActionEvent e) throws IOException{
        String copyAlbum = albuminput.getText().trim();
        albuminput.clear();
        if(Photos.driver.getCurrentUser().getAlbumNameList().contains(copyAlbum)){
            Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Copy Photo");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to copy this photo to " + copyAlbum + "?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) { 

                //Get index of photo
                int selectionIndex = photos.getSelectionModel().getSelectedIndex();
                //Get the photo itself
                Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
                Photo selectedPhoto = Photos.driver.getCurrentAlbum().getSelectedPhoto();
                //Go to the desired Album and add the selected photo
                Photos.driver.getCurrentUser().findAlbum(copyAlbum).addPhoto(selectedPhoto);

                //SAVE
                //REFRESH
                Album.save(Photos.driver.getCurrentAlbum());
                refresh();
            }

        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Doesn't Exist");
			alert.setHeaderText("Please enter a valid album for "+ Photos.driver.getCurrentUser().getName());
			alert.showAndWait();
            return;
        }

        


    }
    /**
     * allows user to move photo into another album
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void movePhoto(ActionEvent e) throws IOException{
        String moveToAlbum = albuminput.getText().trim();
        albuminput.clear();
        if(Photos.driver.getCurrentUser().getAlbumNameList().contains(moveToAlbum)){
            Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Move Photo");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to move this photo to " + moveToAlbum + "?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) { 

                //Get index of photo
                int selectionIndex = photos.getSelectionModel().getSelectedIndex();
                //Get the photo itself
                Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
                Photo selectedPhoto = Photos.driver.getCurrentAlbum().getSelectedPhoto();
                //Go to the desired Album and add the selected photo
                Photos.driver.getCurrentUser().findAlbum(moveToAlbum).addPhoto(selectedPhoto);

                Photos.driver.getCurrentAlbum().deletePhoto(selectionIndex);


                Album.save(Photos.driver.getCurrentUser().findAlbum(moveToAlbum));
                Album.save(Photos.driver.getCurrentAlbum());
                //SAVE
                //REFRESH
                refresh();
            }

        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Doesn't Exist");
			alert.setHeaderText("Please enter a valid album for "+ Photos.driver.getCurrentUser().getName());
			alert.showAndWait();
            return;
        }
        
    }

    //ATTACH THIS FUNCTION AS ONACTION FOR NEW BUTTON
    /**
     * displays selected photo in image viewer
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void displayPhoto(ActionEvent e){
        int selectionIndex = photos.getSelectionModel().getSelectedIndex();
        if(selectionIndex == -1){
             Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Photos To Display");
			alert.setHeaderText("No Photos to display. Try adding a photo and then displaying!");
			alert.showAndWait();
            return;
        }

        Photos.driver.getCurrentAlbum().setDisplayPhotoIndex(selectionIndex);

        //Check for NULL
        Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
        Photo selectedPhoto = Photos.driver.getCurrentAlbum().getSelectedPhoto();
        File pictureFile = selectedPhoto.getFile();
        if(selectedPhoto != null){
            Image img = new Image(pictureFile.toURI().toString());
            photoDisplay.setImage(img);
            //SHOWING IMAGE DETAILS
            detailtextarea.setText(selectedPhoto.printDetails());
            // detailsview.setText(selectedPhoto.printDetails());
        }

    
    }
    //HAVE TO SET DISPLAYPHOTOINDEX AT START

    //USE different attribute in album to keep track of photo in displaymode (or use index to move through) (make sure to move the index everytime a photo is added or deleted)
    /**
     * Changes image displayed to the next photo in the album, if capable
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void nextPhoto(ActionEvent e){
        if(Photos.driver.getCurrentAlbum().getDisplayPhotoIndex() == -1){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Photos To Display");
			alert.setHeaderText("No Photos to display. Try adding a photo to the album and then displaying!");
			alert.showAndWait();
            return;
        }
        if(Photos.driver.getCurrentAlbum().getPhotoCount() == 1){
            return;
        }
        int index = Photos.driver.getCurrentAlbum().getDisplayPhotoIndex();
        if(index+1 >= Photos.driver.getCurrentAlbum().getPhotoCount()){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("End of Slideshow");
			alert.setHeaderText("There are no photos after this!");
			alert.showAndWait();
            return;
        }
        else{
            index++;
            Photos.driver.getCurrentAlbum().setDisplayPhotoIndex(index);
            Photos.driver.getCurrentAlbum().setSelectedPhoto(index);
            Photo selectedPhoto = Photos.driver.getCurrentAlbum().getSelectedPhoto();
            File pictureFile = selectedPhoto.getFile();
            if(selectedPhoto != null){
                Image img = new Image(pictureFile.toURI().toString());
                photoDisplay.setImage(img);
                //SHOWING IMAGE DETAILS
                detailtextarea.setText(selectedPhoto.printDetails());
                // detailsview.setText(selectedPhoto.printDetails());
            }
            photos.getSelectionModel().select(index);
        }

    }
    /**
     * Changes image shown to the previous image in the album, if capable
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void previousPhoto(ActionEvent e){
        if(Photos.driver.getCurrentAlbum().getDisplayPhotoIndex() == -1){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Photos To Display");
			alert.setHeaderText("No Photos to display. Try adding a photo to the album and then displaying!");
			alert.showAndWait();
            return;
        }
        if(Photos.driver.getCurrentAlbum().getPhotoCount() == 1){
            return;
        }
        int index = Photos.driver.getCurrentAlbum().getDisplayPhotoIndex();
        if(index-1 <0){
            Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("End of Slideshow");
			alert.setHeaderText("There are no photos before this!");
			alert.showAndWait();
            return;
        }
        else{
            index--;
            Photos.driver.getCurrentAlbum().setDisplayPhotoIndex(index);
            Photos.driver.getCurrentAlbum().setSelectedPhoto(index);
            Photo selectedPhoto = Photos.driver.getCurrentAlbum().getSelectedPhoto();
            File pictureFile = selectedPhoto.getFile();
            if(selectedPhoto != null){
                Image img = new Image(pictureFile.toURI().toString());
                photoDisplay.setImage(img);
                //SHOWING IMAGE DETAILS
                detailtextarea.setText(selectedPhoto.printDetails());
                // detailsview.setText(selectedPhoto.printDetails());
            }
            photos.getSelectionModel().select(index);
        }
    }
    /**
     * Changes caption of the selected photo
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void changeCaption(ActionEvent e){
        
        int selectionIndex = photos.getSelectionModel().getSelectedIndex();
        
        if(selectionIndex < 0){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("No Photo Selected");
            a.setHeaderText(null);
            a.setContentText("There are no photos to change the caption to. Add new photos first!");
            return;
        }
        
        
        String newCaption = captionInput.getText().trim();
        captionInput.clear();
        if(newCaption.isEmpty() || newCaption == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Empty Input");
			alert.setHeaderText("Please enter a caption");
			alert.showAndWait();
            return;
        }

        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Empty Input");
        alert.setHeaderText("Confirm that you would like to change the caption to " + newCaption);
        Optional<ButtonType> buttonClicked=alert.showAndWait();
        if (buttonClicked.get()==ButtonType.OK) {
            alert.close();

            Photos.driver.getCurrentAlbum().setSelectedPhoto(selectionIndex);
            Photos.driver.getCurrentAlbum().getSelectedPhoto().changeCaption(newCaption);


            if(photoDisplay != null)
            detailtextarea.setText(Photos.driver.getCurrentAlbum().getSelectedPhoto().printDetails());

            
        //REFRESH
        //SAVE
        refresh();
        }
        else {
            alert.close();
            return;
        }

    }
    /**
     * Switches scene to Search.fxml
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void search(ActionEvent e) throws IOException{

        FXMLLoader searchLoader = new FXMLLoader();
        searchLoader.setLocation(getClass().getResource("/photoalbum/view/Search.fxml"));

        Parent sceneManager = (Parent) searchLoader.load();
        SearchController searchController = searchLoader.getController();

        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/photoalbum/view/Search.fxml"));
        // Parent sceneManager = (Parent) fxmlLoader.load();
        // SearchController searchController = fxmlLoader.getController();
        Scene searchScene = new Scene(sceneManager);
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        searchController.start();
        primaryStage.setScene(searchScene);
        primaryStage.show();
    }


    /**
     * logs current user out, returning them to login scene
     * @param e action event provided by fxml
     * @throws IOException
     */
    public void logOut(ActionEvent e) throws IOException{
        logMeOut(e);
    }
}