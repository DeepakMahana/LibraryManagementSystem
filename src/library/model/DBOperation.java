
package library.model;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

public class DBOperation {
    
    Statement stmt = null;
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
 
public Connection DbConnector(){
          
         String username = "library";
	 String password= "oracle";
	 String dbname="library";
	 String url= "jdbc:oracle:thin:@localhost:1521:xe";
	 String driver = "oracle.jdbc.driver.OracleDriver";
           
         try{
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
                return conn;
            
            }catch(ClassNotFoundException | SQLException e){
                System.out.println(e);
            }
            
            return null;
        }        
    
    
    public boolean loginCheck(String userid, String password) throws SQLException  {
        
        int count = 0;
        String logQuery = "select * from LIBRARY.LOGIN where userid=? and password=?";
        pst = conn.prepareStatement(logQuery);
        pst.setString(1, userid);
        pst.setString(2, password);
        rs = pst.executeQuery();
        while(rs.next())
        {
	count=1;
        }
	pst.close();
        rs.close();
	if(count == 1)
	return true;
        
      return false;
    }  
        
    
    
    public boolean addMember(String mid,String mname,String mdob,String maddr,String mcon1,String mcon2,String mstream,String mstatus,String mgender,FileInputStream fis,File imageFile,String cdate) throws SQLException{
        
        String memaddQuery = "insert into LIBRARY.MEMBER values (?,?,?,?,?,?,?,?,?,?,?)"; 
        pst = conn.prepareStatement(memaddQuery);
        pst.setString(1,mid);
        pst.setString(2,mname);
        pst.setString(3,mdob);
        pst.setString(4,maddr);
        pst.setString(5,mcon1);
        pst.setString(6,mcon2);
        pst.setString(7,mstream);
        pst.setString(8,mstatus);
        pst.setString(9,mgender);
        pst.setBinaryStream(10,fis,(int)imageFile.length());
        pst.setString(11,cdate);
        
        if(execAction(pst)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            pst.close();
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst.close();
            return false;
        }
    }
    
     public boolean updateMember(String mid,String mname,String mdob,String maddr,String mcon1,String mcon2,String mstream,String mstatus,String mgender,FileInputStream fis,File imageFile) throws SQLException{
        
        String updateQuery = "Update MEMBER set MEMBER_NAME=?,MEMBER_DOB=?,MEMBER_ADDRESS=?,MEMBER_CONTACT1=?,MEMBER_CONTACT2=?,MEMBER_STREAM=?,MEMBER_STATUS=?,MEMBER_GENDER=?,MEMBER_IMAGE=? where MEMBER_ID='"+ mid +"'";
        pst = conn.prepareStatement(updateQuery);
        pst.setString(1,mname);
        pst.setString(2,mdob);
        pst.setString(3,maddr);
        pst.setString(4,mcon1);
        pst.setString(5,mcon2);
        pst.setString(6,mstream);
        pst.setString(7,mstatus);
        pst.setString(8,mgender);
        pst.setBinaryStream(9,fis,(int)imageFile.length());
        
        if(execAction(pst)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            pst.close();
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst.close();
            return false;
        }  
     }
    
    
    
    public boolean addBook(String bId, String bName, String bAuthor, String bPublisher, String bStream) throws SQLException {
        
        String addBookQ = "insert into LIBRARY.BOOKS values (?,?,?,?,?,?)";
        pst = conn.prepareStatement(addBookQ);
        pst.setString(1,bId);
        pst.setString(2,bName);
        pst.setString(3,bAuthor);
        pst.setString(4,bPublisher);
        pst.setString(5,bStream);
        pst.setString(6,"Available");
        
        if(execAction(pst)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            pst.close();
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst.close();
            return false;
        }
    }
    
   public boolean updateBook(String bID,String bName, String bAuthor, String bPublisher, String bStream) throws SQLException {
        
        String upQuery = "Update BOOKS set BOOK_NAME=?,BOOK_AUTHOR=?,BOOK_PUBLISHER=?,BOOK_STREAM=? where BOOK_ID='"+ bID +"'";
        pst = conn.prepareStatement(upQuery);
        pst.setString(1,bName);
        pst.setString(2,bAuthor);
        pst.setString(3,bPublisher);
        pst.setString(4,bStream);
    
        if(execAction(pst)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            pst.close();
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst.close();
            return false;
        }
    } 
     
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    
    public boolean execAction(PreparedStatement pst) {
        try {
            pst.executeQuery();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }
       
}       
        
      













