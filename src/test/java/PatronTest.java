import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;

public class PatronTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Patron.all().size(), 0);
  }

  @Test
  public void patron_intiateCorrectly() {
    Patron patron = new Patron("Michael Jordan", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron.save();
    Patron savedPatron = Patron.find(patron.getId());
    assertTrue(patron.equals(savedPatron));
    assertEquals("Michael Jordan", patron.getName());
    assertEquals("111 Broadway, New York, 11777", patron.getAddress());
    assertEquals("(555) 123-2233", patron.getPhoneNumber());
  }

  @Test
  public void patron_updatesCorrectly() {
    Patron patron1 = new Patron("Michael Jordan", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron1.save();
    patron1.update("Michael Jackson", "111 Broadway, New York, 11777", "(555) 123-2233");
    Patron patron2 = new Patron("Bill Gates", "111 Broadway, New York, 11777", "(555) 123-2233");
    patron2.delete();
    assertEquals(Patron.all().size(), 1);
    assertEquals("Michael Jordan", patron1.getName());
  }
}
