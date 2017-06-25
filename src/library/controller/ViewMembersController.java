
package library.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
import library.bean.Members;
import library.model.DBOperation;

public class ViewMembersController implements Initializable {
    DBOperation db = new DBOperation();
    PreparedStatement pst;
    ResultSet rs;
    ObservableList<Members> members = FXCollections.observableArrayList();             
    private Stage popupStage;
    
    //TableView
    @FXML
    private TableView<Members> tableView;
    
    @FXML
    private JFXTextField searchField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(true);
        
        TableColumn SerialNo = new TableColumn("Sr.No");
        SerialNo.setPrefWidth(80);
        SerialNo.setCellValueFactory(new PropertyValueFactory<>("SrNo"));
        
        TableColumn Mem_ID = new TableColumn("Member ID");
        Mem_ID.setPrefWidth(100);
        Mem_ID.setCellValueFactory(new PropertyValueFactory<>("MemberID"));
        
        TableColumn Mem_Name = new TableColumn("Member Name");
        Mem_Name.setPrefWidth(200);
        Mem_Name.setCellValueFactory(new PropertyValueFactory<>("MemberName"));
        
        TableColumn Mem_Age = new TableColumn("Age");
        Mem_Age.setPrefWidth(50);
        Mem_Age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        
        TableColumn Mem_Contact = new TableColumn("Contact");
        Mem_Contact.setPrefWidth(100);
        Mem_Contact.setCellValueFactory(new PropertyValueFactory<>("ContactNumber"));
        
        TableColumn Mem_Gender = new TableColumn("Gender");
        Mem_Gender.setPrefWidth(100);
        Mem_Gender.setCellValueFactory(new PropertyValueFactory<>("MemberGender"));
        
        TableColumn Mem_Stream = new TableColumn("Stream");
        Mem_Stream.setPrefWidth(150);
        Mem_Stream.setCellValueFactory(new PropertyValueFactory<>("MemberStream"));
        
        TableColumn Mem_Status = new TableColumn("Status");
        Mem_Status.setPrefWidth(100);
        Mem_Status.setCellValueFactory(new PropertyValueFactory<>("MemberStatus"));
        
        tableView.getColumns().addAll(SerialNo,Mem_ID,Mem_Name,Mem_Age,Mem_Contact,Mem_Gender,Mem_Stream,Mem_Status);
                
        try{
            String query = "select * from MEMBER";
            pst = db.DbConnector().prepareStatement(query);
            rs = pst.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String Current_Date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            LocalDate CurrentDate = LocalDate.parse(Current_Date, formatter);
            while(rs.next()){
                LocalDate DateOfBirth = LocalDate.parse(rs.getString("MEMBER_DOB").substring(0, 10), formatter);
                Period p = Period.between(DateOfBirth, CurrentDate);
                members.add(new Members(
                        rs.getRow(),
                        rs.getString("MEMBER_ID"),
                        rs.getString("MEMBER_NAME"),
                        Integer.toString(p.getYears()),
                        rs.getString("MEMBER_CONTACT1"),
                        rs.getString("MEMBER_GENDER"),
                        rs.getString("MEMBER_STREAM")
                        ,rs.getString("MEMBER_STATUS")));
                tableView.setItems(members);
                  }
            pst.close();
            rs.close();
          }catch(SQLException e){
            System.err.println();
        }
        
        //Double Click:
        tableView.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText().equals("Member ID"))){
           
           //Get position of mouse click and get row index
           TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
           int row = pos.getRow();
           //Set all items of that row in Members Object
           Members item = tableView.getItems().get(row);
           TableColumn col = pos.getTableColumn();
           System.out.println(col.getText());
           // this gives the value in the selected cell:
           String data = (String) col.getCellObservableValue(item).getValue();
           
           //Passing Clicked Data to other Controller:
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/library/view/EditMember.fxml"));
           fxmlLoader.setControllerFactory((Class<?> controllerClass) -> {
               if (controllerClass == EditMemberController.class) {
                   EditMemberController controller = new EditMemberController();
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
                stage.setTitle("EDIT DETAILS");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ViewMembersController.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
        });   
 
        //Filter:
        FilteredList<Members> filteredData = new FilteredList<>(members, e-> true);
        searchField.setOnKeyReleased(e ->{
           searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
               filteredData.setPredicate((Predicate<? super Members>) mem->{
                   if(newValue == null || newValue.isEmpty()){
                       return true;
                   }
                   String lowerCaseFilter = newValue.toLowerCase();
                   if(mem.getMemberID().contains(newValue)
                           ||mem.getMemberName().contains(newValue)
                           ||mem.getMemberStream().contains(newValue)
                           ||mem.getAge().contains(newValue)
                           ||mem.getContactNumber().contains(newValue)
                           ||mem.getMemberGender().contains(newValue)){
                       return true; 
                   }else if(mem.getMemberName().toLowerCase().contains(lowerCaseFilter)
                           ||mem.getMemberStream().toLowerCase().contains(lowerCaseFilter)
                           ||mem.getMemberGender().toLowerCase().contains(lowerCaseFilter)){
                       return true;
                   }
                   return false;
               });
           });
           SortedList<Members> sortedData = new SortedList<>(filteredData);
           sortedData.comparatorProperty().bind(tableView.comparatorProperty());
           tableView.setItems(sortedData);
       });
    }
}
        
