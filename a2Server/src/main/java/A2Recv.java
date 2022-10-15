import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class A2Recv {

    private final static String QUEUE_NAME = "skiersPost";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /*
        Since it will push us messages asynchronously,
        we provide a callback in the form of an object that will buffer the messages until we're ready to use them.
        That is what a DeliverCallback subclass does.
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            //System.out.println("consumer tag is: " + consumerTag);
            //consumer tag is: amq.ctag-k83EFqVVrmcOeIhM_7DFDw

            System.out.println(" [x] Received '" + message + "'");
        };

        /*
        queue name
        autoAck: auto-ack接收消息后是否应答服务器
        deliverCallBack:当一个消息发送过来后的回调接口
        cancelCallback: 当一个消费者取消订阅时的回调接口
        同一个会话， consumerTag是固定的，可以做此会话的名字，deliveryTag每次接收消息+1，可以做此消息处理通道的名字。
        因此 deliveryTag 可以用来回传告诉 rabbitmq 这个消息处理成功 清除此消息（basicAck方法）。

        A consumer tag is a consumer identifier which can be either client- or server-generated.
        To let RabbitMQ generate a node-wide unique tag, use a Channel#basicConsume override that doesn't take a consumer tag argument
        or pass an empty string for consumer tag and use the value returned by Channel#basicConsume.
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
