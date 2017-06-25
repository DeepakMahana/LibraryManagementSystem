
package library.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IssuedBooks {
    
        private final Integer SrNo;
        private final StringProperty BookID;
        private final StringProperty BookName;
        private final StringProperty BookAuthor;
        private final StringProperty BookPublisher;
        private final StringProperty Issue_Date;
        private final StringProperty Submission_Date;
        
public IssuedBooks(int Sr_No,String BookID, String BookName, String BookAuthor, String BookPublisher, String Issue_Date, String Submission_Date) {
            
            this.SrNo = new Integer(Sr_No);
            this.BookID = new SimpleStringProperty(BookID);
            this.BookName = new SimpleStringProperty(BookName);
            this.BookAuthor = new SimpleStringProperty(BookAuthor);
            this.BookPublisher = new SimpleStringProperty(BookPublisher);
            this.Issue_Date = new SimpleStringProperty(Issue_Date);
            this.Submission_Date = new SimpleStringProperty(Submission_Date);
            
        }

// Getter Methods

    public int getSrNo(){
     return SrNo;
    }

    public String getBookID() {
        return BookID.get();
    }

    public String getBookName() {
        return BookName.get();
    }

    public String getBookAuthor() {
        return BookAuthor.get();
    }

    public String getBookPublisher() {
        return BookPublisher.get();
    }

    public String getIssue_Date() {
        return Issue_Date.get();
    }

    public String getSubmission_Date() {
        return Submission_Date.get();
    }

}
