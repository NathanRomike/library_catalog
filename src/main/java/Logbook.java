import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Logbook {
  private String mBookTitle;
  private String mPatronName;
  private String mCheckoutdate;
  private String mDueDate;
  private boolean mIsOverDue;
  private static final int DURATION = 7;

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

  public String getDueDate() {
    return mDueDate;
  }

  public Logbook(String bookTitle, String patronName, String checkoutDate, boolean isOverDue) {
    this.mBookTitle = bookTitle;
    this.mPatronName = patronName;
    this.mCheckoutdate = checkoutDate;
    this.mIsOverDue = isOverDue;
  }

  public static List<Logbook> getHistory(Book book) {
    String sql = "SELECT title AS mBookTitle, name AS mPatronName, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id WHERE books.id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", book.getId())
        .addParameter("duration", DURATION)
        .executeAndFetch(Logbook.class);
    }
  }

  public static List<Logbook> getHistory(Patron patron) {
    String sql = "SELECT title AS mBookTitle, name AS mPatronName, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id WHERE patrons.id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", patron.getId())
        .addParameter("duration", DURATION)
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

  public static void updateOverdueStatus() {
    String sql = "UPDATE log_records SET is_overdue=true WHERE (CURRENT_DATE - checkout_date) > :DURATION";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
      .addParameter("DURATION", DURATION)
      .executeUpdate();
    }
  }

  public static List<Logbook> getOverdueBooks() {
    String sql = "SELECT title AS mBookTitle, name AS mPatronName, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id WHERE is_overdue = true";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("duration", DURATION)
        .executeAndFetch(Logbook.class);

    }
  }
}
