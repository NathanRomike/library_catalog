import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Book {
  private int mId;
  private String mTitle;
  private String mFormat;
  private int mCopies;

  public String getTitle() {
    return mTitle;
  }

  public String getFormat() {
    return mFormat;
  }

  public int getId() {
    return mId;
  }

  public int getAmount() {
    return mCopies;
  }

  public Book(String title, String format, int copies) {
    this.mTitle = title;
    this.mFormat = format;
    this.mCopies = copies;
  }


  @Override
  public boolean equals(Object otherBook) {
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getAmount()==(newBook.getAmount()) &&
             this.getFormat().equals(newBook.getFormat());
    }
  }

  public static List<Book> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, title AS mTitle, format AS mFormat, copies AS mCopies FROM books";
      return con.createQuery(sql)
        .executeAndFetch(Book.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title, format, copies) VALUES (:title, :format, :copies)";
      mId = (int) con.createQuery(sql, true)
        .addParameter("title", mTitle)
        .addParameter("copies", mCopies)
        .addParameter("format", mFormat)
        .executeUpdate()
        .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mid, title AS mTitle, format AS mFormat, copies AS mCopies FROM books WHERE id = :id";
      Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
    return book;
    }
  }

  public void update(String newTitle, String newFormat, int newCopy) {
    String sql = "UPDATE books SET title = :title, format = :format, copies = :copies WHERE id = :id";
    try (Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("title", newTitle)
        .addParameter("format", newFormat)
        .addParameter("copies", newCopy)
        .addParameter("id", this.mId)
        .executeUpdate();
      this.mTitle = newTitle;
      this.mFormat = newFormat;
      this.mCopies = newCopy;
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

  public List<Author> getAuthors() {
    String sql = "SELECT authors.id AS mId, authors.name AS mName FROM books " +
                 "INNER JOIN authors_books AS a_b ON books.id = a_b.book_id INNER JOIN " +
                 "authors ON authors.id = a_b.author_id WHERE books.id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", this.mId)
        .executeAndFetch(Author.class);
    }
  }

  public void assign(Author author) {
    String sql = "INSERT INTO authors_books (author_id, book_id) VALUES (:authorId, :bookId)";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("authorId", author.getId())
        .addParameter("bookId", this.mId)
        .executeUpdate();
    }
  }

  public List<Book> search(String searchInput) {
    String sql = "SELECT books.id AS mId, books.title AS mTitle, books.format AS mFormat, books.copies AS mCopies FROM books " +
                 "INNER JOIN authors_books AS a_b ON books.id = a_b.book_id " +
                 "INNER JOIN authors ON authors.id = a_b.author_id WHERE authors.name LIKE :name OR books.title LIKE :title";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("name", "%"+searchInput+"%")
        .addParameter("title", "%"+searchInput+"%")
        .executeAndFetch(Book.class);
    }
  }

  public void checkout(Patron patron, String date) {
    String sql = "INSERT INTO log_records (patron_id, book_id, checkout_date) VALUES (:patron, :book, TO_DATE(:checkoutDate, 'yyyy-mm-dd'))";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("patron", patron.getId())
        .addParameter("book", this.mId)
        .addParameter("checkoutDate", date)
        .executeUpdate();
    }
  }
}
