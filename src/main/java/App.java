
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

    // get("/", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    

    // post("/task/complete/delete", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   for (Task task: Task.all(true)){
    //     if (request.queryParams(Integer.toString(task.getId())) != null) {
    //       task.delete();
    //     }
    //   }
    //   // deletedTask.delete();
    //   response.redirect("/complete");
    //   return null;
    // });
  }
}
