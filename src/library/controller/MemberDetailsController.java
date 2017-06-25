
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import library.bean.IssuedBooks;
import library.model.DBOperation;

public class MemberDetailsController extends DBOperation implements Initializable {
    
    DBOperation db = new DBOperation();
    ObservableList<IssuedBooks> IRBooks = FXCollections.observableArrayList();
    
    private Image img;
    
    @FXML
    private TableView<IssuedBooks> tableView;
    
    @FXML
    private JFXTextField mem_id;

    @FXML
    private JFXTextField mem_name;

    @FXML
    private JFXTextField mem_stream;

    @FXML
    private JFXTextField mem_status;

    @FXML
    private ImageView MemberImage;

    @FXML
    private JFXButton cancel;

    PreparedStatement pst,pst1,pst2;
    ResultSet rs,rs1,rs2;
    
    String data;     
    public void setData1(String data){
        this.data=data;
    }
    
     @FXML
    void openHome(ActionEvent event) throws IOException {
       Stage stage = (Stage) cancel.getScene().getWindow();
       stage.close();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(true);
        
        TableColumn SerialNo = new TableColumn("Sr.No");
        SerialNo.setPrefWidth(80);
        SerialNo.setCellValueFactory(new PropertyValueFactory<>("SrNo"));
        
        TableColumn Book_ID = new TableColumn("Book ID");
        Book_ID.setPrefWidth(100);
        Book_ID.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        
        TableColumn Book_Name = new TableColumn("Book Name");
        Book_Name.setPrefWidth(200);
        Book_Name.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        
        TableColumn Book_Author = new TableColumn("Author");
        Book_Author.setPrefWidth(100);
        Book_Author.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        
        TableColumn Book_Publisher = new TableColumn("Publisher");
        Book_Publisher.setPrefWidth(100);
        Book_Publisher.setCellValueFactory(new PropertyValueFactory<>("BookPublisher"));
        
        TableColumn Issue_Date = new TableColumn("Issue Date");
        Issue_Date.setPrefWidth(150);
        Issue_Date.setCellValueFactory(new PropertyValueFactory<>("Issue_Date"));
        
        TableColumn Submission_Date = new TableColumn("Submission Date");
        Submission_Date.setPrefWidth(150);
        Submission_Date.setCellValueFactory(new PropertyValueFactory<>("Submission_Date"));
         
        tableView.getColumns().addAll(SerialNo,Book_ID,Book_Name,Book_Author,Book_Publisher,Issue_Date,Submission_Date);
        
        try {
            showDetails();
        } catch (IOException ex) {
            Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        showIssuedBooks();
        
    }    
    
    
  
     public void showDetails() throws FileNotFoundException, IOException{
           
            String query = "select * "
                    + "from MEMBER "
                    + "where MEMBER_ID='"+data+"'"
                    + "OR MEMBER_NAME='"+data+"'";
            
        try {
            pst2 = DbConnector().prepareStatement(query);
            rs2 = pst2.executeQuery();
            while(rs2.next()){
                mem_id.setText(rs2.getString("MEMBER_ID"));
                mem_name.setText(rs2.getString("MEMBER_NAME"));
                mem_stream.setText(rs2.getString("MEMBER_STREAM"));
                mem_status.setText(rs2.getString("MEMBER_STATUS"));
                
            InputStream is = rs2.getBinaryStream("MEMBER_IMAGE");
            OutputStream os = new FileOutputStream(new File("photo.jpg"));
            byte[] content= new byte[1024];
            int size = 0;
            while ((size = is.read(content)) != -1 ) {
                os.write(content,0,size);
            }
            os.close();
            is.close();
            img = new Image("file:photo.jpg");
            MemberImage.setImage(img);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }


public void showIssuedBooks() {
        IRBooks.clear();
        try{
            String query = "select * from ISSUEBOOK where MEMBER_ID='"+data+"'";
            pst = db.DbConnector().prepareStatement(query);
            rs = pst.executeQuery();
            int i=1;
            while(rs.next()){
                if(rs.getString("RETURNED_DATE")==null)
                {   
                String query1 = "select BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER from BOOKS where BOOK_ID='"+rs.getString("BOOK_ID")+"'";
                pst1 = db.DbConnector().prepareStatement(query1);
                rs1 = pst1.executeQuery();
                rs1.next();
                IRBooks.add(new IssuedBooks(
                        i++,
                        rs.getString("BOOK_ID"),
                        rs1.getString("BOOK_NAME"),
                        rs1.getString("BOOK_AUTHOR"),
                        rs1.getString("BOOK_PUBLISHER"),
                        rs.getString("ISSUE_DATE").substring(0, 10),
                        rs.getString("SUBMISSION_DATE").substring(0, 10)
                        ));
                tableView.setItems(IRBooks);
                pst1.close();
                rs1.close();
                }
            }
            pst.close();
            rs.close();
          }catch(SQLException e){
            System.err.println();
        }
    }




}


