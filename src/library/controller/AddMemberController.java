
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import library.model.DBOperation;

public class AddMemberController implements Initializable {

    ObservableList stream_options = FXCollections.observableArrayList();
    ObservableList status_options = FXCollections.observableArrayList("Select Status","Active","Inactive");
    ObservableList gender_options = FXCollections.observableArrayList("Select Gender","Male","Female");
    
    PreparedStatement pst;
    ResultSet rs;
    DBOperation db = new DBOperation();
    
    @FXML
    private ImageView MemberImage;

    @FXML
    private JFXButton browseImage;
    
    @FXML
    private ChoiceBox<String> mem_stream;
    
    @FXML
    private ChoiceBox<String> mem_status;
    
    @FXML
    private JFXTextField id;
    
    @FXML
    private JFXTextField name;
    
    @FXML
    private JFXTextArea address;
    
    @FXML
    private JFXTextField contactNumber;
    
    @FXML
    private JFXDatePicker dob;
    
    @FXML
    private JFXTextField contactNumber2;
    
    @FXML
    private ChoiceBox<String> mem_gender;
    
    @FXML
    private JFXButton registCancel;
    
    File file1;
    InputStream fis;
    
    
    @FXML
    void openFileChooser(ActionEvent event) throws FileNotFoundException {
            FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            fis = new FileInputStream(file);
            file1 = file;           
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                MemberImage.setImage(image);
                
            } catch (IOException ex) {
            }
            
    }
    
    
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
        mem_stream.setValue("Select Stream");
        mem_stream.setItems(stream_options);
        mem_status.setValue("Select Status");
        mem_status.setItems(status_options);
        mem_gender.setValue("Select Gender");
        mem_gender.setItems(gender_options);
        
        //Action Listener for Nurmeric Text fields
        contactNumber.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.matches("\\d*")) {
                contactNumber.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }
    });
        contactNumber2.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.matches("\\d*")) {
                contactNumber2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }
    });
        
    }    
    

    @FXML
    private void addMember(ActionEvent event) throws SQLException {
        
        boolean result;
       
        if (validateContactNo() & validateContactNo2() & validateFields())
        {
          // Taking System Current Date
        String Current_Date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        
        String Member_ID = id.getText();
        String Member_Name = name.getText();
        
        //Date of Birth:
        LocalDate localDate = dob.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String Member_DOB = localDate.format(formatter);
        
        String Member_Address = address.getText();
        String Member_Contact1 = contactNumber.getText();
        String Member_Contact2 = contactNumber2.getText();
        String Member_Stream = mem_stream.getValue();
        String Member_Status = mem_status.getValue();
        String Member_Gender = mem_gender.getValue();   
             
        result = db.addMember(Member_ID,Member_Name,Member_DOB,Member_Address,Member_Contact1,Member_Contact2,Member_Stream,Member_Status,Member_Gender, (FileInputStream) fis,file1,Current_Date);
            if (result == true){
                Alert alertinfo = new Alert(Alert.AlertType.INFORMATION);
                alertinfo.setHeaderText(null);
                alertinfo.setContentText("Member Added Successfully.");
                alertinfo.showAndWait();
                clearFields();
               }
             else {
            
            Alert alerterr = new Alert(Alert.AlertType.ERROR);
            alerterr.setHeaderText(null);
            alerterr.setContentText("There was an Error while Adding a New Member !! ");
            alerterr.showAndWait();
        }
        
        }
        
    }
    
    
    @FXML
    private void cancelRegist(ActionEvent event) {
        
        Alert alertcnfrm = new Alert(AlertType.CONFIRMATION);
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
     dob.setValue(null);
     address.clear();
     contactNumber.clear();
     contactNumber2.clear();
     mem_stream.setValue("Select Stream");
     mem_status.setValue("Select Status");
     mem_gender.setValue("Select Gender");
     try{
     BufferedImage buffimage;
     File f;
     f = new File("..\\library\\icons\\IIH.png");
     FileInputStream imgfis = new FileInputStream(f);          
     buffimage = ImageIO.read(imgfis);
     Image img = SwingFXUtils.toFXImage(buffimage, null);
     MemberImage.setImage(img);
     } 
     catch (IOException ex) {
            }
       }
 
 public boolean validateContactNo() {
 
            Pattern p = Pattern.compile("^[789]\\d{9}$");
            Matcher m = p.matcher(contactNumber.getText());
            if(m.find() && m.group().equals(contactNumber.getText())){
                return true;
            }
            else {
                Alert er = new Alert(AlertType.WARNING);
                er.setTitle("Wrong Contact No");
                er.setHeaderText(null);
                er.setContentText("Please Enter Valid 10 Numbers");
                er.showAndWait();
                return false;
            }
 }          
            
 public boolean validateContactNo2() {
 
            Pattern p = Pattern.compile("^[789]\\d{9}$");
            Matcher m = p.matcher(contactNumber2.getText());
            if(m.find() && m.group().equals(contactNumber2.getText())){
                return true;
            }
            else {
                Alert er = new Alert(AlertType.WARNING);
                er.setTitle("Wrong Contact No");
                er.setHeaderText(null);
                er.setContentText("Please Enter Valid 10 Numbers");
                er.showAndWait();
                return false;
            }            
 }
 
 public boolean validateFields(){
     
     if (id.getText().isEmpty() | name.getText().isEmpty() | address.getText().isEmpty() | contactNumber.getText().isEmpty() | contactNumber2.getText().isEmpty()
              | mem_stream.getValue().isEmpty() | mem_status.getValue().isEmpty() | mem_gender.getValue().isEmpty())
     {
         
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter All Fields.");
            alert.showAndWait();
            return false;
     }
     
     if (dob.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter The Date Of Birth.");
            alert.showAndWait();
            return false;
     }
     return true; 
 }
    
}