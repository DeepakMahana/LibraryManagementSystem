
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
import javafx.stage.Stage;
import library.model.DBOperation;


public class EditBookController implements Initializable {
    
    ObservableList stream_options = FXCollections.observableArrayList();
    
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
    private JFXButton cancelupdatebook;
    
    @FXML
    private JFXButton updateBook;

    String data;
    
    public void setData1(String data){
        this.data=data;
    }
    
    @FXML
    private void updateBook(ActionEvent event) throws SQLException {
        
        boolean result;
        String Book_ID = id.getText();
        String Book_Name = name.getText();
        String Book_Author = author.getText();
        String Book_Publisher = publisher.getText();
        String Book_Stream = stream.getValue();
        
        if (validateFields())
        {
        result = db.updateBook(Book_ID,Book_Name,Book_Author,Book_Publisher,Book_Stream);
         if (result == true){
                Alert alertinfo = new Alert(Alert.AlertType.INFORMATION);
                alertinfo.setHeaderText(null);
                alertinfo.setContentText("Book Updated Successfully.");
                alertinfo.showAndWait();
                Stage stage = (Stage) updateBook.getScene().getWindow();
                stage.close();
               }
             else {
            
            Alert alerterr = new Alert(Alert.AlertType.ERROR);
            alerterr.setHeaderText(null);
            alerterr.setContentText("There was an Error while Updating Book Details !! ");
            alerterr.showAndWait();
            Stage stage = (Stage) updateBook.getScene().getWindow();
            stage.close();
        }
        }  
    } 

    @FXML
    private void cancelUpdateBook(ActionEvent event) {
        
        Alert alertcnfrm = new Alert(Alert.AlertType.CONFIRMATION);
        alertcnfrm.setTitle("Warning");
        alertcnfrm.setHeaderText(null);
        alertcnfrm.setContentText("Are You Sure ? You Want To Cancel ?");

        Optional<ButtonType> result = alertcnfrm.showAndWait();
        if (result.get() == ButtonType.OK){
            alertcnfrm.close();
            Stage stage = (Stage) cancelupdatebook.getScene().getWindow();
            stage.close();
        } else {
            alertcnfrm.close();
        }
        
    }
    
  public boolean validateFields(){
     
     if(name.getText().isEmpty() | author.getText().isEmpty() | publisher.getText().isEmpty() | stream.getValue().isEmpty())
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        id.setEditable(false);
        id.setDisable(true);
       
         try {
            String query = "select STREAM_NAME from STREAMS";
            pst = db.DbConnector().prepareStatement(query);
                try (ResultSet rs = pst.executeQuery()) {
                    while(rs.next()){
                        stream_options.add(rs.getString("STREAM_NAME"));
                    }
                    rs.close();
                    pst.close();   
                }  
        } catch (SQLException ex) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        stream.setItems(stream_options);
        
        String query = "Select * from BOOKS where BOOK_ID='" + data + "'";
        try {
            pst = db.DbConnector().prepareStatement(query);
            rs = pst.executeQuery();
            rs.next();
            id.setText(rs.getString("BOOK_ID"));
            name.setText(rs.getString("BOOK_NAME"));
            author.setText(rs.getString("BOOK_AUTHOR"));
            publisher.setText(rs.getString("BOOK_PUBLISHER"));
            stream.setValue(rs.getString("BOOK_STREAM"));
            
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }  
}
