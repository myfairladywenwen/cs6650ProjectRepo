import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {

            res.setStatus(HttpServletResponse.SC_CREATED);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            //res.getWriter().write("It works!");
            Integer obj = 1;
            String jsonString = new Gson().toJson(obj);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
        }
    }

    /*
    /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
            int32               2019            1-366           int32
     */

    private boolean isUrlValid(String[] urlPath) {
        String seasons = urlPath[2];
        String days = urlPath[4];
        String skiers2 = urlPath[6];
        if(!seasons.equals("seasons") || !days.equals("days") || !skiers2.equals("skiers")){
            return false;
        }
        int resortID;
        int seasonID;
        int dayID;
        int skierID;
        try {
            resortID = Integer.parseInt(urlPath[1]);
            seasonID = Integer.parseInt(urlPath[3]);
            dayID = Integer.parseInt(urlPath[5]);
            skierID = Integer.parseInt(urlPath[7]);
        } catch (NumberFormatException e) {
            return false;
        }
        if(seasonID != 2019 || dayID > 366  || dayID < 1){
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)
        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_CREATED);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            Integer obj = 1;
            String jsonString = new Gson().toJson(obj);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
        }
    }
}
