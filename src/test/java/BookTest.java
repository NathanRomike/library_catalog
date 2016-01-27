import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;

public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void book_createsCorrectly() {
    Book book = new Book("The game of throne", "Paperback");
    book.save();
    Book savedBook = Book.find(book.getId());
    assertEquals("The game of throne", book.getTitle());
    assertEquals("Paperback", book.getFormat());
    assertTrue(book.equals(savedBook));
  }

  @Test
  public void book_updatesCorrectly() {
    Book book1 = new Book("The game of throne", "Paperback");
    book1.save();
    book1.update("The game of thrones", "PaperBack");
    Book book2 = new Book("The game of throne", "Paperback");
    book2.delete();
    assertEquals(Book.all().size(), 1);
    assertEquals("The game of thrones", book1.getTitle());

  }

}
