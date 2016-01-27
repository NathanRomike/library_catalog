import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Book {
  private int mId;
  private String mTitle;
  private String mFormat;

  public String getTitle() {
    return mTitle;
  }

  public String getFormat() {
    return mFormat;
  }

  public int getId() {
    return mId;
  }

  public Book(String title, String format) {
    this.mTitle = title;
    this.mFormat = format;
  }


  @Override
  public boolean equals(Object otherBook) {
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getFormat().equals(newBook.getFormat());
    }
  }

  public static List<Book> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, title AS mTitle, format AS mFormat FROM books";
      return con.createQuery(sql)
        .executeAndFetch(Book.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title, format) VALUES (:title, :format)";
      mId = (int) con.createQuery(sql, true)
        .addParameter("title", mTitle)
        .addParameter("format", mFormat)
        .executeUpdate()
        .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mid, title AS mTitle, format AS mFormat FROM books WHERE id = :id";
      Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
    return book;
    }
  }

  public void update(String newTitle, String newFormat) {
    String sql = "UPDATE books SET title = :title, format = :format WHERE id = :id";
    try (Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("title", newTitle)
        .addParameter("format", newFormat)
        .addParameter("id", this.mId)
        .executeUpdate();
      this.mTitle = newTitle;
      this.mFormat = newFormat;
    }
  }

  public void delete() {
    String sql = "DELETE FROM books WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("id", this.mId)
        .executeUpdate();
    }
  }
}
