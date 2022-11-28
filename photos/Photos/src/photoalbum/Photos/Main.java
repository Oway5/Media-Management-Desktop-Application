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

public class Main extends Application {
    public Stage primary;
    public static Persistence driver = new Persistence();
    @Override
    public void start(Stage primaryStage) throws Exception{
        primary = primaryStage;

        FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        AnchorPane root = (AnchorPane) fxmlLoader.load();
        Scene scene = new Scene(root);
        primary.setResizable(false);
        primary.setTitle("Photo-Library");
        primary.setScene(scene);
        primary.show();

        primary.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
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