import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private static Connection connection;
    static {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setHost("44.232.7.90");//elasticIP
//        factory.setHost("54.185.71.60");
//        factory.setUsername("admin");
//        factory.setPassword("password");
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.out.println("error while trying to new a connection");
            e.printStackTrace();
        }
    }

    @Override
    public Channel create() throws IOException {
        return connection.createChannel();
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }
}
