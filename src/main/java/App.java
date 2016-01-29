
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("patrons", Patron.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patron/main", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/patron.vtl");
      Patron patron = Patron.find(Integer.parseInt(request.queryParams("patron")));
      request.session().attribute("patron", patron);
      model.put("patron", patron);
      model.put("logbook", Logbook.class);
      model.put("book", Book.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patron/main/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/patron.vtl");
      Patron patron = Patron.find(Integer.parseInt(request.params("id")));
      model.put("patron", patron);
      model.put("logbook", Logbook.class);
      model.put("book", Book.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/librarian/main", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/patrons.vtl");
      model.put("patron", Patron.class);
      model.put("logbook", Logbook.class);
      model.put("book", Book.class);
      //model.put("patron", Patron.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bookmanager", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/bookmanager.vtl");
      model.put("book", Book.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/deletebook/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      book.delete();
      response.redirect("/bookmanager");
      return null;
    });

    get("/book/change/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/bookmanager.vtl");

      model.put("book", Book.class);
      model.put("updateBook", Book.find(Integer.parseInt(request.params("id"))));

      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    get("/authors", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/authors.vtl");
      model.put("authors", Author.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/deleteauthor/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params("id")));
      author.delete();
      response.redirect("/authors");
      return null;
    });

    get("/author/change/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/authors.vtl");

      model.put("authors", Author.class);
      model.put("updateAuthors", Author.find(Integer.parseInt(request.params("id"))));

      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    get("/authors/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/authors.vtl");
      Book book = Book.find(Integer.parseInt(request.params("id")));
      model.put("authors", Author.class);
      model.put("book", book);

      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    get("/search", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/books.vtl");
      //Book book = Book.find(Integer.parseInt(request.params("id")));
      //model.put("authors", Author.class);

      model.put("books", Book.search(request.queryParams("search")));

      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());


    post("/book/add", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = new Book(request.queryParams("newTitle"), request.queryParams("newFormat"), Integer.parseInt(request.queryParams("newCopies")));
      book.save();
      model.put("bookObject", book);
      response.redirect("/bookmanager");
      return null;
    });

    post("/book/update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      book.update(request.queryParams("updateTitle"), request.queryParams("updateFormat"), Integer.parseInt(request.queryParams("updateCopies")));
      response.redirect("/bookmanager");
      return null;
    });

    post("/author/add", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = new Author(request.queryParams("newName"));
      author.save();
      model.put("authorObject", author);
      response.redirect("/authors");
      return null;
    });

    post("/author/update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params("id")));
      author.update(request.queryParams("updateName"));
      response.redirect("/authors");
      return null;
    });

    post("/checkbookout/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      book.checkout(request.session().attribute("patron"), request.queryParams("checkoutdate"));
      Patron patron = request.session().attribute("patron");
      response.redirect("/patron/main/" + patron.getId());
      return null;
    });

    post("/addauthors/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      for (Author author: Author.all()){
        if (request.queryParams(Integer.toString(author.getId())) != null) {
          book.assign(author);
        }
      }
      response.redirect("/bookmanager");
      return null;
    });
  }
}
