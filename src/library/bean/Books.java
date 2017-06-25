
package library.bean;

import javafx.beans.property.*;

public class Books{
        
        private final Integer SrNo;
        private final StringProperty BookID;
        private final StringProperty BookName;
        private final StringProperty BookAuthor;
        private final StringProperty BookPublisher;
        private final StringProperty BookStream;
        private final StringProperty BookAvail;

        public Books(int Sr_No,String Book_ID, String Book_Name, String Book_Author, String Book_Publisher, String Book_Stream, String Book_Avail) {
            this.SrNo = new Integer(Sr_No);
            this.BookID = new SimpleStringProperty(Book_ID);
            this.BookAuthor = new SimpleStringProperty(Book_Author);
            this.BookName = new SimpleStringProperty(Book_Name);
            this.BookPublisher = new SimpleStringProperty(Book_Publisher);
            this.BookStream = new SimpleStringProperty(Book_Stream);
            this.BookAvail = new SimpleStringProperty(Book_Avail);
        }
        
    // getter
        
     public int getSrNo() {
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

    public String getBookStream() {
        return BookStream.get();
    }

    public String getBookAvail() {
        return BookAvail.get();
    }
        
}