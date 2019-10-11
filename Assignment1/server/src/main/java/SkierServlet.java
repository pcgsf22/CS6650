import Model.*;
import Model.Message.InvalidInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * The type Skier servlet.
 */
@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  private static final int VERTICAL = -200;
  private static final int SKIER = -300;

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    String urlPath = req.getPathInfo();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonString = null;
    int response = 0;
    if (urlPath == null || urlPath.isEmpty()) {
      response = 400;
    } else {
      response = valPostUrl(urlPath);
    }
    if (response == SKIER) {
      String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
      if (!valLiftRide(json)) {
        response = 400;
      } else {
        response = 201;
      }
    }
    if (response == 400) {
      jsonString = gson.toJson(new InvalidInput());
    }
    res.setStatus(response);
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    String urlPath = req.getPathInfo();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String jsonString = null;
    int response = 0;
    if (urlPath == null || urlPath.isEmpty()) {
      response = 400;
    } else {
      response = valGetUrl(urlPath);
    }
    if (response == SKIER) {
      response = 200;
      jsonString = String.valueOf(34507);
    } else if (response == VERTICAL) {
      response = 200;
      jsonString = "{}";
    } else if (response == 400) {
      jsonString = gson.toJson(new InvalidInput());
    }
    res.setStatus(response);
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();

  }

  /**
   * Validate the get request path and return corresponding response code.
   *
   * @param urlPath the url path
   * @return the integer
   */
  protected Integer valGetUrl(String urlPath) {
    String[] urlParts = urlPath.split("/");
    if (urlParts.length == 3) {
      return valVerticalUrl(urlParts);
    } else if (urlParts.length == 8) {
      return valSkierUrl(urlParts);
    }
    return 400;
  }


  /**
   * Validate the vertical url path and return corresponding response code.
   *
   * @param urlParts the url parts
   * @return the integer
   */
  protected Integer valVerticalUrl(String[] urlParts) {
    if (!urlParts[1].equals("vertical")) {
      return 400;
    }
    return VERTICAL;
  }

  /**
   * Validate the skier url path and return corresponding response code.
   *
   * @param urlParts the url parts
   * @return the integer
   */
  protected Integer valSkierUrl(String[] urlParts) {
    if (!urlParts[2].equals("seasons") || !urlParts[4].equals("days") || !urlParts[6]
        .equals("skiers")) {
      return 400;
    }
    return SKIER;
  }

  /**
   * Validate the post request path and return corresponding response code.
   *
   * @param urlPath the url path
   * @return the integer
   */
  protected Integer valPostUrl(String urlPath) {
    String[] urlParts = urlPath.split("/");
    if (urlParts.length == 8) {
      return valSkierUrl(urlParts);
    }
    return 400;
  }

  /**
   * Validate the received json text is correct or not.
   *
   * @param json the json
   * @return the boolean
   */
  protected boolean valLiftRide(String json) {
    LiftRide convertedObject = new Gson().fromJson(json, LiftRide.class);
    return convertedObject.getLiftID() != null && convertedObject.getTime() != null;
  }

}
