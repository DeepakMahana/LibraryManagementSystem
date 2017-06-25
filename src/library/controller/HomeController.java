package library.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.model.DBOperation;


public class HomeController extends DBOperation implements Initializable {
    
    @FXML
    private JFXTextField searchbook;

    @FXML
    private Label bookname;

    @FXML
    private Label authorname;
    
    @FXML
    private Label bookstatus;
    
    DBOperation db = new DBOperation();
    IRBookController irb = new IRBookController();
    

    @FXML
    void loadBookDetails(ActionEvent event) {
        String search=searchbook.getText();
        Boolean flag=false;
        db.DbConnector();
            String query = "select * "
                    + "from BOOKS "
                    + "where BOOK_NAME='"+search+"'"
                    + "OR BOOK_ID='"+search+"'";
            
        try {
            PreparedStatement pst = DbConnector().prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String bname = rs.getString("BOOK_NAME");
                String author = rs.getString("BOOK_AUTHOR");
                String bstatus = rs.getString("BOOK_ISAVAILABLE");
                authorname.setVisible(true);
                bookstatus.setVisible(true);
                bookname.setText(bname);
                authorname.setText(author);
                
                if(bstatus.equals("Available")){
                    bookstatus.setText(bstatus);
                 } else{
                         try {
                         db.DbConnector();
                         String query1 = "select * from ISSUEBOOK where BOOK_ID='"+rs.getString("BOOK_ID")+"'";
                        PreparedStatement pst1 = DbConnector().prepareStatement(query1);
                        ResultSet rs1 = pst1.executeQuery();
                            while(rs1.next()){
                                bookstatus.setText("Will be available by "+rs1.getString("RETURNED_DATE").substring(0, 10));
                         }
                         }catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
                }
               // String status = (bstatus)?"Available":"Not Available";
               // bookstatus.setText(status);
                flag=true;
                
            }
            if(!flag){
                bookname.setText("No such Book is available");
                authorname.setVisible(false);
                bookstatus.setVisible(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   
    
    @FXML
    private JFXTextField searchmember;
     
    @FXML
    void openMemberDetails(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/library/view/MemberDetails.fxml"));

            fxmlLoader.setControllerFactory((Class<?> controllerClass) -> {
               if (controllerClass == MemberDetailsController.class) {
                   MemberDetailsController controller = new MemberDetailsController();
                   String data=searchmember.getText();
                   controller.setData1(data);
                   return controller ;
               } else {
                   try {
                       return controllerClass.newInstance();
                   } catch (IllegalAccessException | InstantiationException exc) {
                       throw new RuntimeException(exc); // just bail
                   }
               }
           });
            try {
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("MEMBER DETAILS");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
