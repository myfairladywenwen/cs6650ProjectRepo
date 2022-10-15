import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "A2Servlet", value = "/A2Servlet")
public class A2Servlet extends HttpServlet {
    private static final String DELIMITER = " ";
    private static final String EXCHANGE_NAME = "liftride_records";
    private final static String QUEUE_NAME = "skiersPost";
//    private ObjectPool<Channel> channelPool;

//    @Override
//    public void init(){
        /*
        // Create a channel pool
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(2);
        config.setMaxIdle(5);
        config.setMaxTotal(20);
        channelPool = new GenericObjectPool<>(new ChannelFactory());
        */

//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String urlPath = req.getPathInfo();

        // check we have a URL
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
            /*
            Integer obj = 2;
            String jsonString = new Gson().toJson(obj);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
            */

            int resortID = Integer.parseInt(urlParts[1]);
            int seasonID = Integer.parseInt(urlParts[3]);
            int dayID = Integer.parseInt(urlParts[5]);
            int skierID = Integer.parseInt(urlParts[7]);

            //TODO: how to process requst body?
            // Process request body, convert it into String
            BufferedReader reqBuffer = req.getReader();
            StringBuilder reqStringBuilder = new StringBuilder();
            String line;
            while ((line = reqBuffer.readLine()) != null) {
                reqStringBuilder.append(line.trim());
            }
            String jsonString = reqStringBuilder.toString();
            String message = jsonString + (DELIMITER + resortID + DELIMITER + seasonID + DELIMITER + dayID + DELIMITER + skierID);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }


            // Publish request message to RabbitMQ
//            Channel channel = null;
//            try {
//                channel = channelPool.borrowObject();
//                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
//            } catch (IOException e) {
//                throw e;
//            } catch (Exception e) {
//                throw new RuntimeException("Unable to borrow channel from pool" + e.toString());
//            } finally {
//                try {
//                    if (null != channel) {
//                        channelPool.returnObject(channel);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            res.setStatus(HttpServletResponse.SC_CREATED);
            System.out.println("server side");
        }
    }

    /*
    /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
            int32               2019            1-366           int32
     */
    private boolean isUrlValid(String[] urlPath) {
        /*
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
        */
        return true;
    }
}
