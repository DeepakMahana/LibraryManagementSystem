
package library.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.model.DBOperation;
import library.bean.Books;

public class ViewBooksController implements Initializable {

    DBOperation db = new DBOperation();
    PreparedStatement pst;
    ResultSet rs;
    
    ObservableList<Books> books = FXCollections.observableArrayList();
       
    @FXML
    private JFXTextField searchField;
    
    @FXML
    private TableView<Books> tableView;
    
    
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
        
        TableColumn Book_Name = new TableColumn("Name");
        Book_Name.setPrefWidth(200);
        Book_Name.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        
        TableColumn Book_Author = new TableColumn("Author");
        Book_Author.setPrefWidth(100);
        Book_Author.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        
        TableColumn Book_Publisher = new TableColumn("Publisher");
        Book_Publisher.setPrefWidth(100);
        Book_Publisher.setCellValueFactory(new PropertyValueFactory<>("BookPublisher"));
        
        TableColumn Book_Stream = new TableColumn("Stream");
        Book_Stream.setPrefWidth(150);
        Book_Stream.setCellValueFactory(new PropertyValueFactory<>("BookStream"));
        
        TableColumn Book_Avail = new TableColumn("Availability");
        Book_Avail.setPrefWidth(100);
        Book_Avail.setCellValueFactory(new PropertyValueFactory<>("BookAvail"));
        
        tableView.getColumns().addAll(SerialNo,Book_ID,Book_Name,Book_Author,Book_Publisher,Book_Stream,Book_Avail);
                
        try{
            
            String query = "select * from BOOKS";
            pst = db.DbConnector().prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                books.add(new Books(
                        rs.getRow(),
                        rs.getString("BOOK_ID"),
                        rs.getString("BOOK_NAME"),
                        rs.getString("BOOK_AUTHOR"),
                        rs.getString("BOOK_PUBLISHER"),
                        rs.getString("BOOK_STREAM"),
                        rs.getString("BOOK_ISAVAILABLE")));
                tableView.setItems(books);
                  }
            pst.close();
            rs.close();
          }catch(SQLException e){
            System.err.println();
        }
        
        //Double Click
        tableView.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText().equals("Book ID"))){
           
           //Get position of mouse click and get row index
           TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
           int row = pos.getRow();
           //Set all items of that row in Members Object
           Books item = tableView.getItems().get(row);
           TableColumn col = pos.getTableColumn();
           // this gives the value in the selected cell:
           String data = (String) col.getCellObservableValue(item).getValue();
           
           //Passing Clicked Data to other Controller:
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/library/view/EditBook.fxml"));
           fxmlLoader.setControllerFactory((Class<?> controllerClass) -> {
               if (controllerClass == EditBookController.class) {
                   EditBookController controller = new EditBookController();
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
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("EDIT DETAILS");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ViewMembersController.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
        });   

        //Filter:
        FilteredList<Books> filteredData = new FilteredList<>(books, e-> true);
        searchField.setOnKeyReleased(e ->{
           searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
               filteredData.setPredicate((Predicate<? super Books>) book->{
                   if(newValue == null || newValue.isEmpty()){
                       return true;
                   }
                   String lowerCaseFilter = newValue.toLowerCase();
                   if(book.getBookID().contains(newValue)
                           ||book.getBookName().contains(newValue)
                           ||book.getBookAuthor().contains(newValue)
                           ||book.getBookStream().contains(newValue)
                           ||book.getBookPublisher().contains(newValue)){
                       return true; 
                   }else if(book.getBookName().toLowerCase().contains(lowerCaseFilter)
                           ||book.getBookAuthor().toLowerCase().contains(lowerCaseFilter)
                           ||book.getBookPublisher().toLowerCase().contains(lowerCaseFilter)){
                       return true;
                   }
                   return false;
               });
           });
           SortedList<Books> sortedData = new SortedList<>(filteredData);
           sortedData.comparatorProperty().bind(tableView.comparatorProperty());
           tableView.setItems(sortedData);
       });
    }
}
