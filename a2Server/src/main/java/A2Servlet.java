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
    private ObjectPool<Channel> channelPool;
    private RMQChannelPool channelPool1;
    private static Connection connection;
    @Override
    public void init(){
        // Create a channel pool

        /*
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(2);
        config.setMaxIdle(5);
        config.setMaxTotal(20);
        channelPool = new GenericObjectPool<>(new ChannelFactory());
        */


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("35.88.135.218");
        factory.setUsername("admin");
        factory.setPassword("password");
        //factory.setHost("localhost");
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.out.println("error while trying to new a connection");
            e.printStackTrace();
        }
        channelPool1 = new RMQChannelPool(20, new RMQChannelFactory(connection));

    }

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
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_CREATED);
            //System.out.println("status code is: " + res.getStatus());
            int resortID = Integer.parseInt(urlParts[1]);
            int seasonID = Integer.parseInt(urlParts[3]);
            int dayID = Integer.parseInt(urlParts[5]);
            int skierID = Integer.parseInt(urlParts[7]);

            BufferedReader reqBuffer = req.getReader();
            StringBuilder reqStringBuilder = new StringBuilder();
            String line;
            while ((line = reqBuffer.readLine()) != null) {
                reqStringBuilder.append(line.trim());
            }
            String jsonString = reqStringBuilder.toString();
            String message = jsonString + (DELIMITER + resortID + DELIMITER + seasonID + DELIMITER + dayID + DELIMITER + skierID);

            // Publish request message to RabbitMQ
            Channel channel = null;

            /*
            try {
                channel = channelPool.borrowObject();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                //System.out.println(" [x] Sent '" + message + "'");
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                //System.out.println("error. can't borrow channel from pool");
                throw new RuntimeException("Unable to borrow channel from pool" + e.toString());
            } finally {
                try {
                    if (null != channel) {
                        channelPool.returnObject(channel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
             */


            try {
                channel = channelPool1.borrowObject();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                //System.out.println(" [x] Sent '" + message + "'");
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                //System.out.println("error. can't borrow channel from pool");
                throw new RuntimeException("Unable to borrow channel from pool" + e.toString());
            } finally {
                try {
                    if (null != channel) {
                        channelPool1.returnObject(channel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            res.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    /*
    /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
            int32               2019            1-366           int32
     */
    private boolean isUrlValid(String[] urlPath){
        return urlPath != null
                && urlPath.length == 8
                && "seasons".equals(urlPath[2])
                && isNum(urlPath[3])
                && "days".equals(urlPath[4])
                && isNum(urlPath[5])
                && "skiers".equals(urlPath[6])
                && isNum(urlPath[7]);
    }

    private boolean isNum(String input) {
        if (input == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
