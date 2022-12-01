/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.Photos;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import photoalbum.model.Persistence;

public class Photos extends Application {
    public Stage primary;
    /**
     * sets up persistence to save states between application restarting
     */
    public static Persistence driver = new Persistence();
    /**
	 * Starts photo album program and sets up login window.
	 */
    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            primary = primaryStage;
            FXMLLoader loginlLoader = new FXMLLoader();
            loginlLoader.setLocation(getClass().getResource("/photoalbum/view/Login.fxml"));
            AnchorPane root = loginlLoader.load();
            // FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("/photoalbum/view/Login.fxml"));
            // AnchorPane root = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(root);
            primary.setResizable(false);
            primary.setTitle("Photo-Library");
            primary.setScene(scene);
            primary.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        /**
         * this saves user information upon closing the application
         */
        primary.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				try{
                    Persistence.save(driver);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
			}
            
        });

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try{
            driver = Persistence.load();
        }catch(IOException e){
            e.printStackTrace();
        }

        launch(args);

    }
}