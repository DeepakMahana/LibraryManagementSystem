
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import library.model.DBOperation;

public class LoginValidation implements Initializable {
    
    String uname;
    String pass;
    
    DBOperation db = new DBOperation();
    
    @FXML
    private Label lblMessage;
    
    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXTextField username;
    
    
    @FXML
    void doLogin (ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        
        uname = username.getText();
        pass = password.getText();
        db.DbConnector();
        boolean result = db.loginCheck(uname,pass);
         
         if (result == true)
         {  
            Parent MainPage = FXMLLoader.load(getClass().getResource("/library/view/main.fxml"));
            Scene MainScene = new Scene(MainPage);
            Stage MainStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
            MainStage.hide();
            MainStage.setScene(MainScene);
            MainStage.setTitle("Welcome To Library Management System");
            MainStage.show();
        }
         else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Username and Password");
            alert.showAndWait();
            password.clear();
            username.clear();
              //lblMessage.setText("Invalid Username and Password");
             }
   }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         //To change body of generated methods, choose Tools | Templates.
    }
}   
