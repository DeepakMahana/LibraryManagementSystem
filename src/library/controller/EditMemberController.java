
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import library.model.DBOperation;

public class EditMemberController implements Initializable {
        
    @FXML
    private TextField mem_ID;

    @FXML
    private TextField mem_Name;

    @FXML
    private TextField mem_Contact1;

    @FXML
    private TextField mem_Contact2;

    @FXML
    private TextArea mem_Address;

    @FXML
    private ChoiceBox<String> mem_Stream;

    @FXML
    private ChoiceBox<String> mem_Status;

    @FXML
    private ChoiceBox<String> mem_Gender;

    @FXML
    private JFXDatePicker mem_DOB;
    
    @FXML
    private ImageView mem_Image;

    @FXML
    private JFXButton browseImage;
    
    @FXML
    private JFXButton updateMember;
    
    @FXML
    private JFXButton updateCancel;
    
    private Image img;

    File file1;
    InputStream fis;
    String data;
    PreparedStatement pst;
    ResultSet rs;
    DBOperation db = new DBOperation();
    
    
    public void setData1(String data){
        this.data=data;
    }
    
    ObservableList stream_options = FXCollections.observableArrayList();
    ObservableList status_options = FXCollections.observableArrayList("Select Status","Active","Inactive");
    ObservableList gender_options = FXCollections.observableArrayList("Select Gender","Male","Female");
    
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
                Image image1 = SwingFXUtils.toFXImage(bufferedImage, null);
                mem_Image.setImage(image1);
                
            } catch (IOException ex) {
            }
            
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mem_ID.setEditable(false);
        mem_ID.setDisable(true);
        
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
            
        mem_Stream.setItems(stream_options);
        
        String query = "Select * from MEMBER where MEMBER_ID='" + data + "'";
       
        try {
            pst = db.DbConnector().prepareStatement(query);
            rs = pst.executeQuery();
            rs.next();
            ObservableList status_options1 = FXCollections.observableArrayList("Active","Inactive");
            ObservableList gender_options1 = FXCollections.observableArrayList("Male","Female");
            mem_Status.setItems(status_options1);
            mem_Gender.setItems(gender_options1);
            mem_ID.setText(rs.getString("MEMBER_ID"));
            mem_Name.setText(rs.getString("MEMBER_NAME"));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String Current_Date = rs.getString("MEMBER_DOB").substring(0, 10);
            LocalDate CurrentDate = LocalDate.parse(Current_Date, formatter);
            mem_DOB.setValue(CurrentDate);
            
            mem_Address.setText(rs.getString("MEMBER_ADDRESS"));
            mem_Contact1.setText(rs.getString("MEMBER_CONTACT1"));
            mem_Contact2.setText(rs.getString("MEMBER_CONTACT2"));
            mem_Stream.setValue(rs.getString("MEMBER_STREAM"));
            mem_Gender.setValue(rs.getString("MEMBER_GENDER"));
            mem_Status.setValue(rs.getString("MEMBER_STATUS"));
            
            InputStream is = rs.getBinaryStream("MEMBER_IMAGE");
            OutputStream os = new FileOutputStream(file1 = new File("photo.jpg"));
            fis = new FileInputStream(file1);
            byte[] content= new byte[1024];
            int size = 0;
            while ((size = is.read(content)) != -1 ) {
                os.write(content,0,size);
            }
            
            os.close();
            is.close();
            img = new Image("file:photo.jpg");
            mem_Image.setImage(img);
            
            pst.close();
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(EditMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EditMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 }

    @FXML
    private void updateMember(ActionEvent event) throws SQLException {
        
        boolean result;
       
        if (validateContactNo() & validateContactNo2() & validateFields())
        {
          // Taking System Current Date
        String Current_Date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        
        String Member_ID = mem_ID.getText();
        String Member_Name = mem_Name.getText();
        
        //Date of Birth:
        LocalDate localDate = mem_DOB.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String Member_DOB = localDate.format(formatter);
        
        String Member_Address = mem_Address.getText();
        String Member_Contact1 = mem_Contact1.getText();
        String Member_Contact2 = mem_Contact2.getText();
        String Member_Stream = mem_Stream.getValue();
        String Member_Status = mem_Status.getValue();
        String Member_Gender = mem_Gender.getValue();   
             
        result = db.updateMember(Member_ID,Member_Name,Member_DOB,Member_Address,Member_Contact1,Member_Contact2,Member_Stream,Member_Status,Member_Gender, (FileInputStream) fis,file1);
            if (result == true){
                Alert alertinfo = new Alert(Alert.AlertType.INFORMATION);
                alertinfo.setHeaderText(null);
                alertinfo.setContentText("Member Updated Successfully.");
                alertinfo.showAndWait();
                //clearFields();
               }
             else {
            
            Alert alerterr = new Alert(Alert.AlertType.ERROR);
            alerterr.setHeaderText(null);
            alerterr.setContentText("There was an Error while Updating Member Details!! ");
            alerterr.showAndWait();
        }
        
        }
        
        
        
        
        
    }

    @FXML
    private void updateCancel(ActionEvent event) {
    }
    
    
    @FXML
    private void cancelRegist(ActionEvent event) {
        
        Alert alertcnfrm = new Alert(Alert.AlertType.CONFIRMATION);
        alertcnfrm.setTitle("Warning");
        alertcnfrm.setHeaderText(null);
        alertcnfrm.setContentText("Are You Sure ? You Want To Cancel ?");

        Optional<ButtonType> result = alertcnfrm.showAndWait();
        if (result.get() == ButtonType.OK){
            alertcnfrm.close();
            alertcnfrm.close();
            Stage stage = (Stage) updateCancel.getScene().getWindow();
            stage.close();
        } else {
            alertcnfrm.close();
        }
        
    }
     
public boolean validateContactNo() {
 
            Pattern p = Pattern.compile("^[789]\\d{9}$");
            Matcher m = p.matcher(mem_Contact1.getText());
            if(m.find() && m.group().equals(mem_Contact1.getText())){
                return true;
            }
            else {
                Alert er = new Alert(Alert.AlertType.WARNING);
                er.setTitle("Wrong Contact No");
                er.setHeaderText(null);
                er.setContentText("Please Enter Valid 10 Numbers");
                er.showAndWait();
                return false;
            }
 }          
            
 public boolean validateContactNo2() {
 
            Pattern p = Pattern.compile("^[789]\\d{9}$");
            Matcher m = p.matcher(mem_Contact2.getText());
            if(m.find() && m.group().equals(mem_Contact2.getText())){
                return true;
            }
            else {
                Alert er = new Alert(Alert.AlertType.WARNING);
                er.setTitle("Wrong Contact No");
                er.setHeaderText(null);
                er.setContentText("Please Enter Valid 10 Numbers");
                er.showAndWait();
                return false;
            }            
 }
 
 public boolean validateFields(){
     
     if (mem_ID.getText().isEmpty() | mem_Name.getText().isEmpty() | mem_Address.getText().isEmpty() | mem_Contact1.getText().isEmpty() | mem_Contact2.getText().isEmpty()
              | mem_Stream.getValue().isEmpty() | mem_Status.getValue().isEmpty() | mem_Gender.getValue().isEmpty())
     {
         
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter All Fields.");
            alert.showAndWait();
            return false;
     }
     
     if (mem_DOB.getValue() == null){
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
