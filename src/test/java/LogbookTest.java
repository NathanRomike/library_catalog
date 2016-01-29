import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;

public class LogbookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Logbook.all().size(), 0);
  }

  @Test
  public void logbook_intiateCorrectly() {
    Book book = new Book("Of Mice and Men", "paperback", 10);
    book.save();
    Patron patron = new Patron("Michael Jordan", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron.save();
    book.checkout(patron, "2016-01-01");
    Logbook.updateOverdueStatus();
    assertEquals("Of Mice and Men", Book.find(Logbook.getHistory(book).get(0).getBookId()).getTitle());
    assertEquals("Michael Jordan", Patron.find(Logbook.getHistory(book).get(0).getPatronId()).getName());
    assertEquals("2016-01-01", Logbook.getHistory(book).get(0).getCheckoutDate());
    assertEquals(true, Logbook.getHistory(book).get(0).isOverDue());
    assertEquals("2016-01-01", Logbook.getHistory(patron).get(0).getCheckoutDate());
    assertEquals("2016-01-08", Logbook.getHistory(patron).get(0).getDueDate());
    assertEquals("2016-01-08", Logbook.getHistory(book).get(0).getDueDate());
  }

  @Test
  public void getBookAvailable_9() {
    Book book = new Book("Of Mice and Men", "paperback", 10);
    book.save();
    Patron patron = new Patron("Michael Jordan", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron.save();
    book.checkout(patron, "2016-01-01");
    assertEquals(9, book.getAvailability());
  }

  @Test
  public void getListOfOverdueBooks() {
    Book book = new Book("Of Mice and Men", "paperback", 10);
    book.save();
    Patron patron = new Patron("Michael Jordan", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron.save();
    book.checkout(patron, "2016-01-01");
    Logbook.updateOverdueStatus();
    assertEquals(Logbook.getOverdueBooks().size(), 1);
  }
}
