
package library.bean;

import javafx.beans.property.*;

public class Members {
        
        private final Integer SrNo;
        private final SimpleStringProperty MemberID;
        private final SimpleStringProperty MemberName;
        private final SimpleStringProperty Age;
        private final SimpleStringProperty ContactNumber;
        private final SimpleStringProperty MemberGender;
        private final SimpleStringProperty MemberStream;
        private final SimpleStringProperty MemberStatus;   
        
        
    public Members(int Sr_No,String Member_ID, String Member_Name, String Member_Age, String Contact_Number,String Member_Gender, String Member_Stream, String Member_Status) {
           
            this.SrNo = new Integer(Sr_No);
            this.MemberID = new SimpleStringProperty(Member_ID);
            this.MemberName = new SimpleStringProperty(Member_Name);
            this.Age = new SimpleStringProperty(Member_Age);
            this.ContactNumber = new SimpleStringProperty(Contact_Number);
            this.MemberGender = new SimpleStringProperty(Member_Gender);
            this.MemberStream = new SimpleStringProperty(Member_Stream);
            this.MemberStatus = new SimpleStringProperty(Member_Status);
        
}

   //Getter Methods 
    
   public int getSrNo() {
        return SrNo;
    }
 
   public String getMemberID() {
        return MemberID.get();
    }

    public String getMemberName() {
        return MemberName.get();
    }
    
    public String getAge() {
        return Age.get();
    }

    public String getContactNumber() {
        return ContactNumber.get();
    }
    

    public String getMemberGender() {
        return MemberGender.get();
    }

    public String getMemberStream() {
        return MemberStream.get();
    }

    public String getMemberStatus() {
        return MemberStatus.get();
    } 
    
}