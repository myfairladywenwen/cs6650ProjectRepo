import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class A2Recv {

    private final static String QUEUE_NAME = "skiersPost";
    private static final String EXCHANGE_NAME = "liftride_records";
    private static final String DELIMITER = " ";
    private static final int THREAD_POOL_SIZE = 10;
    private static ConcurrentHashMap<Integer, List<Integer>> map = new ConcurrentHashMap();

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        Runnable liftRideConsumer = () -> {
            for(int i = 0; i < 10; i++){
                try {
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
                    //not to give more than one message to a worker at a time
                    channel.basicQos(1);
                    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println(" [x] Received '" + message + "'");
                        try {
                            storeToMap(message);
                        } finally {
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        }
                    };
                    channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService consumerPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            consumerPool.execute(liftRideConsumer);
        }
    }

    private static void storeToMap(String message) {
        //TODO: get info from json instead of url
        // Deserialize message
        String[] messageParts = message.split(DELIMITER);
        String jsonString = messageParts[0];
        int resortID = Integer.parseInt(messageParts[1]);
        int seasonID = Integer.parseInt(messageParts[3]);
        int dayID = Integer.parseInt(messageParts[5]);
        int skierID = Integer.parseInt(messageParts[7]);
        map.putIfAbsent(skierID, new ArrayList<>());
        //map.get(skierID).add(liftID);
    }
}
