import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Author {
  private String mName;
  private int mId;

  public Author(String name) {
    this.mName = name;
  }

  public String getName() {
    return mName;
  }
  public int getId() {
    return mId;
  }

  @Override
  public boolean equals(Object otherAuthor) {
    if (!(otherAuthor instanceof Author)) {
      return false;
    } else {
      Author newAuthor = (Author) otherAuthor;
      return (newAuthor.getName().equals(this.getName())) &&
             (newAuthor.getId() == this.getId());
    }
  }

  public void save() {
    String sql = "INSERT INTO authors (name) VALUES (:name)";
    try(Connection con = DB.sql2o.open()) {
      this.mId = (int) con.createQuery(sql, true)
        .addParameter("name", this.mName)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Author> all() {
    String sql = "SELECT id AS mId, name AS mName FROM authors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .executeAndFetch(Author.class);
    }
  }

  public static Author find(int id) {
    String sql = "SELECT id AS mId, name AS mName FROM authors WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Author.class);
    }
  }

  public void update(String name) {
    String sql = "UPDATE authors SET name = :name WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", mId)
        .executeUpdate();
      this.mName = name;
    }
  }

  public void delete() {
    String sql = "DELETE FROM authors WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("id", mId)
        .executeUpdate();
    }
  }

  public List<Book> getBooks() {
    String sql = "SELECT books.id AS mId, books.title AS mTitle, books.format AS mFormat FROM authors " +
                   "INNER JOIN authors_books AS a_b ON authors.id = a_b.author_id INNER JOIN " +
                   "books ON books.id = a_b.book_id WHERE authors.id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", this.mId)
        .executeAndFetch(Book.class);
    }
  }

  public void assign(Book book) {
    String sql = "INSERT INTO authors_books (author_id, book_id) VALUES (:authorId, :bookId)";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("authorId", mId)
        .addParameter("bookId", book.getId())
        .executeUpdate();
    }
  }
}
