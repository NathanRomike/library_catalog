import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Logbook {
  private int mBookId;
  private int mPatronId;
  private String mCheckoutdate;
  private String mDueDate;
  private boolean mIsOverDue;
  private static final int DURATION = 7;

  public int getBookId() {
    return mBookId;
  }

  public int getPatronId() {
    return mPatronId;
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

  public Logbook(int bookId, int patronId, String checkoutDate, boolean isOverDue) {
    this.mBookId = bookId;
    this.mPatronId = patronId;
    this.mCheckoutdate = checkoutDate;
    this.mIsOverDue = isOverDue;
  }

  public static List<Logbook> getHistory(Book book) {
    String sql = "SELECT book_id AS mBookId, patron_id AS mPatronId, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
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
    String sql = "SELECT book_id AS mBookId, patron_id AS mPatronId, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
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
    String sql = "SELECT book_id AS mBookId, patron_id AS mPatronId, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue FROM books " +
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
    String sql = "SELECT book_id AS mBookId, patron_id AS mPatronId, checkout_date AS mCheckoutdate, is_overdue AS mIsOverDue, (checkout_date + :duration) AS mDueDate FROM books " +
                 "INNER JOIN log_records ON books.id = log_records.book_id " +
                 "INNER JOIN patrons ON patrons.id = log_records.patron_id WHERE is_overdue = true";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("duration", DURATION)
        .executeAndFetch(Logbook.class);

    }
  }
}
