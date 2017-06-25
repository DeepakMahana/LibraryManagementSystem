
package library.controller;

import java.sql.Connection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import library.model.DBOperation;

public class Start extends Application {
    
    DBOperation db = new DBOperation();
    
    @FXML
    private Pane pane2;
    
    @FXML
    private ImageView img1;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        CheckConnection();
        
        Parent root = FXMLLoader.load(getClass().getResource("/library/view/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
    
    public void CheckConnection(){
        Connection conn = db.DbConnector();
        if(conn == null){
            System.out.println("Connection to Database Failed");
            System.exit(1);
        }
        else
        {
           System.out.println("Connection Successfull");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
