
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import library.model.DBOperation;

public class AddBookController implements Initializable {
    
    PreparedStatement pst;
    ResultSet rs;
    DBOperation db = new DBOperation();
    
    @FXML
    private JFXTextField id;
    
    @FXML
    private JFXTextField name;
    
    @FXML
    private JFXTextField author;
    
    @FXML
    private JFXTextField publisher;
    
    @FXML
    private ChoiceBox<String> stream;
    
    @FXML
    private JFXButton cancelBook;
    
    ObservableList stream_options = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
                String query = "select STREAM_NAME from STREAMS";
                pst = db.DbConnector().prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    stream_options.add(rs.getString("STREAM_NAME"));
                }
                pst.close();
                rs.close();  
            
            } catch (SQLException ex) {
                Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
            }
            stream.setValue("Select Stream");
            stream.setItems(stream_options);    }    

    @FXML
    public void addBook(ActionEvent event) throws SQLException, ClassNotFoundException {
        
        boolean result;
 
        String Book_ID = id.getText();
        String Book_Name = name.getText();
        String Book_Author = author.getText();
        String Book_Publisher = publisher.getText();
        String Book_Stream = stream.getValue();
        
        
        if (validateFields())
        {
        result = db.addBook(Book_ID,Book_Name,Book_Author,Book_Publisher,Book_Stream);
         if (result == true){
                Alert alertinfo = new Alert(Alert.AlertType.INFORMATION);
                alertinfo.setHeaderText(null);
                alertinfo.setContentText("Book Added Successfully.");
                alertinfo.showAndWait();
                clearFields();
               }
             else {
            
            Alert alerterr = new Alert(Alert.AlertType.ERROR);
            alerterr.setHeaderText(null);
            alerterr.setContentText("There was an Error while Adding A New Book !! ");
            alerterr.showAndWait();
            clearFields();
        }
        }  
}
        
    @FXML
    private void cancelAddBook(ActionEvent event) {
        
        Alert alertcnfrm = new Alert(Alert.AlertType.CONFIRMATION);
        alertcnfrm.setTitle("Warning");
        alertcnfrm.setHeaderText(null);
        alertcnfrm.setContentText("Are You Sure ? You Want To Cancel ?");

        Optional<ButtonType> result = alertcnfrm.showAndWait();
        if (result.get() == ButtonType.OK){
            alertcnfrm.close();
            clearFields();
        } else {
            alertcnfrm.close();
        }
        
    }    
    
public void clearFields(){
    
     id.clear();
     name.clear();
     author.clear();
     publisher.clear();
     stream.setValue("Select Stream");
     
}
    
    
  public boolean validateFields(){
     
     if(id.getText().isEmpty() | name.getText().isEmpty() | author.getText().isEmpty() | publisher.getText().isEmpty() | stream.getValue().isEmpty())
     {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter All Fields.");
            alert.showAndWait();
            return false;
     }
     return true; 
 }  
    
    
    
}



