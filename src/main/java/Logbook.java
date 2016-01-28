import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Logbook {
  private String mBookTitle;
  private String mPatronName;
  private String mCheckoutdate;
  private boolean mIsOverDue;

  public String getBookTitle() {
    return mBookTitle;
  }

  public String getPatronName() {
    return mPatronName;
  }

  public String getCheckoutDate() {
    return mCheckoutdate;
  }

  public boolean isOverDue() {
    return mIsOverDue;
  }

  public Logbook(String bookTitle, String patronName, String checkoutDate, boolean isOverDue) {
    this.mBookTitle = bookTitle;
    this.mPatronName = patronName;
    this.mCheckoutdate = checkoutDate;
    this.mIsOverDue = isOverDue;
  }

  public static List<Logbook> getHistory(Book book) {
    String sql = "SELECT title AS mBookTitle, name AS mPatronName, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id WHERE books.id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", book.getId())
        .executeAndFetch(Logbook.class);
    }
  }

  public static List<Logbook> all() {
    String sql = "SELECT title AS mBookTitle, name AS mPatronName, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .executeAndFetch(Logbook.class);
    }
  }
}
