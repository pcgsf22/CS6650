import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import Model.Body;
import Model.Message.InValidResortId;
import Model.Message.InvalidInput;
import Model.ResortList;
import Model.Message.ResortNotFound;
import Model.SeasonList;
import com.google.gson.*;

/**
 * The type Resort servlet.
 */
@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    {
      res.setContentType("text/plain");
      String urlPath = req.getPathInfo();

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonString = "Write Successful!";

      // check we have a URL!
      if (urlPath == null || urlPath.isEmpty()) {
        res.setStatus(400);
        jsonString = gson.toJson(new InvalidInput());
      } else {
        int response = valUrl(urlPath);
        if (response == 200) {
          String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
          if (!valSeason(json)) {
            response = 400;
          } else {
            response = 201;
          }
        }
        if (response == 400) {
          jsonString = gson.toJson(new InvalidInput());
        } else if (response == 404) {
          jsonString = gson.toJson(new ResortNotFound());
        }
        res.setStatus(response);
      }
      PrintWriter out = res.getWriter();
      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");
      out.print(jsonString);
      out.flush();
    }
  }


  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    String urlPath = req.getPathInfo();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String jsonString = null;

    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(200);
      jsonString = gson.toJson(ResortList.createExample());
    } else {
      int response = valUrl(urlPath);
      res.setStatus(response);
      if (response == 200) {
        jsonString = gson.toJson(SeasonList.getInstance());
      } else if (response == 400) {
        jsonString = gson.toJson(new InValidResortId());
      } else if (response == 404) {
        jsonString = gson.toJson(new ResortNotFound());
      }

    }
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    out.print(jsonString);
    out.flush();
  }

  /**
   * Validate the url and return the response code.
   *
   * @param urlPath the url path
   * @return the integer
   */
  protected Integer valUrl(String urlPath) {
    String[] urlParts = urlPath.split("/");
    if (urlParts.length != 3 || !urlParts[2].equals("seasons")) {
      return 400;
    } else if (!urlParts[1].equals("0") && !urlParts[1].equals("1")) {
      return 404;
    } else {
      return 200;
    }

  }

  /**
   * Validate the json body for season post request..
   *
   * @param json the json
   * @return the boolean
   */
  protected boolean valSeason(String json) {
    Body convertedObject = new Gson().fromJson(json, Body.class);
    return convertedObject.getYear() != null;
  }
}
