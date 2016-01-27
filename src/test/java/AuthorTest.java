import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;

public class AuthorTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Author.all().size(), 0);
  }

  @Test
  public void author_createsNewCorrectly() {
    Author newAuthor = new Author("George R.R. Martin");
    newAuthor.save();
    Author savedAuthor = Author.find(newAuthor.getId());
    assertEquals(newAuthor.getName(), "George R.R. Martin");
    assertTrue(savedAuthor.equals(newAuthor));
  }

  @Test
  public void author_updatesCorrectly() {
    Author firstAuthor = new Author("George R. Martin");
    firstAuthor.save();
    firstAuthor.update("George R.R. Martin");
    Author secondAuthor = new Author("Pushkin");
    secondAuthor.delete();
    assertEquals(Author.all().size(), 1);
    assertEquals("George R.R. Martin", firstAuthor.getName());
  }

  @Test
  public void author_assignBookSuccessfuly() {
    Author author = new Author("Mikhail Bulgakov");
    author.save();
    Book book = new Book("Master and Margarita", "paperback");
    book.save();
    author.assign(book);
    assertEquals(1, author.getBooks().size());
  }

}
