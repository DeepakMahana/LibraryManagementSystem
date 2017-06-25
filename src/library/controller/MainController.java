/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.model.DBOperation;

/**
 * FXML Controller class
 *
 * @author Deep Makwana
 */
public class MainController extends DBOperation implements Initializable {

    @FXML
    private JFXButton viewMemberBtn;
    
    @FXML
    private JFXButton home;
    
    @FXML
    private BorderPane BorderPane;
    
    @FXML
    private ImageView addMember;
    
    @FXML
    private JFXButton logout;
    
    @FXML
    private JFXButton addMemberBtn;

    @FXML
    private ImageView viewBooks;

    @FXML
    private ImageView viewMembers;
    
    @FXML
    private JFXButton addbook;

    @FXML
    private JFXButton viewBooksBtn;
    
    @FXML
    private JFXTextField searchbook;

    @FXML
    private Label bookname;

    @FXML
    private Label authorname;
    
    @FXML
    private JFXTextField searchmember;
     
    @FXML
    private Label bookstatus;
    
    @FXML
    private JFXButton IRBookBtn;

    DBOperation db = new DBOperation();

    @FXML
    void loadBookDetails(ActionEvent event) throws SQLException {
        String search=searchbook.getText();
        Boolean flag=false;
        db.DbConnector();
            String query = "select * "
                    + "from BOOKS "
                    + "where BOOK_NAME='"+search+"'"
                    + "OR BOOK_ID='"+search+"'";
            
        try {
            PreparedStatement pst = db.DbConnector().prepareStatement(query);
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
              /* String status = (bstatus)?"Available":"Not Available";
               bookstatus.setText(status);*/
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
    void openMemberDetails(ActionEvent event) throws IOException {
            //Parent MemberDetailsPage = FXMLLoader.load(getClass().getResource("/library/view/MemberDetails.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/library/view/MemberDetails.fxml"));

            fxmlLoader.setControllerFactory((Class<?> controllerClass) -> {
               if (controllerClass == MemberDetailsController.class) {
                   MemberDetailsController controller = new MemberDetailsController();
                   String data = searchmember.getText();
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
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    void openAddBook(ActionEvent event) throws IOException {
        Node AddBookNode = FXMLLoader.load(getClass().getResource("/library/view/AddBook.fxml"));
        BorderPane.setCenter(AddBookNode);
        addbook.setDisable(true);
        addMemberBtn.setDisable(false);
        home.setDisable(false);
        viewMemberBtn.setDisable(false);
        viewBooksBtn.setDisable(false);
        IRBookBtn.setDisable(false);
    }
    
    @FXML
    void openAddMember(ActionEvent event) throws IOException {
        Node AddMemberNode = FXMLLoader.load(getClass().getResource("/library/view/AddMember.fxml"));
        BorderPane.setCenter(AddMemberNode);
        addMemberBtn.setDisable(true);
        home.setDisable(false);
        addbook.setDisable(false);
        viewMemberBtn.setDisable(false);
        viewBooksBtn.setDisable(false);
        IRBookBtn.setDisable(false);
    }
    
    @FXML
    void openHome(ActionEvent event) throws IOException {
        Node HomeNode = FXMLLoader.load(getClass().getResource("/library/view/home.fxml"));
        BorderPane.setCenter(HomeNode);
        home.setDisable(true);
        addbook.setDisable(false);
        addMemberBtn.setDisable(false);
        viewMemberBtn.setDisable(false);
        viewBooksBtn.setDisable(false);
        IRBookBtn.setDisable(false);
    }
    
    @FXML
    void openViewMember(ActionEvent event) throws IOException {
        Node ViewMemberNode = FXMLLoader.load(getClass().getResource("/library/view/ViewMembers.fxml"));
        BorderPane.setCenter(ViewMemberNode);
        viewMemberBtn.setDisable(true);
        addbook.setDisable(false);
        addMemberBtn.setDisable(false);
        home.setDisable(false);
        viewBooksBtn.setDisable(false);
        IRBookBtn.setDisable(false);
    }
    
    @FXML
    private void openViewBooks(ActionEvent event) throws IOException {
        Node ViewBookNode = FXMLLoader.load(getClass().getResource("/library/view/ViewBooks.fxml"));
        BorderPane.setCenter(ViewBookNode);
        viewBooksBtn.setDisable(true);
        viewMemberBtn.setDisable(false);
        addbook.setDisable(false);
        addMemberBtn.setDisable(false);
        home.setDisable(false);
        IRBookBtn.setDisable(false);
    }

    @FXML
    void openLogin(ActionEvent event) throws IOException {
            Parent LoginPage = FXMLLoader.load(getClass().getResource("/library/view/login.fxml"));
            Scene LoginScene = new Scene(LoginPage);
            Stage LoginStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
            LoginStage.hide();
            LoginStage.setScene(LoginScene);
            LoginStage.setTitle("Login");
            LoginStage.show();
    }
    
    @FXML
    private void openIRBook(ActionEvent event) throws IOException {
        Node IRNode = FXMLLoader.load(getClass().getResource("/library/view/IRBook.fxml"));
        BorderPane.setCenter(IRNode);
        viewMemberBtn.setDisable(false);
        addbook.setDisable(false);
        addMemberBtn.setDisable(false);
        home.setDisable(false);
        viewBooksBtn.setDisable(false);
        IRBookBtn.setDisable(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }       
}
