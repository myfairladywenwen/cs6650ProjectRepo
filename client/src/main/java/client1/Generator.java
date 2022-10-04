package client1;

import io.swagger.client.model.LiftRide;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.concurrent.LinkedBlockingQueue;
public class Generator implements Runnable{

    public SecureRandom random;
    public static final String SEASON_ID = "2022";
    public static final String DAY_ID = "1";
    public LinkedBlockingQueue<MyLiftRide> eventQueue;
    public Generator(LinkedBlockingQueue<MyLiftRide> eventQueue) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.eventQueue = eventQueue;
        this.random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    }


    @Override
    public void run(){
        generateMyLiftRide();
    }
    private void generateMyLiftRide(){
        for(int i = 0; i < 200000; i++){
            Integer skierID = random.nextInt(100000) + 1;
            Integer resortID = random.nextInt(10) + 1;
            Integer liftID = random.nextInt(40) + 1;
            Integer time = random.nextInt(360) + 1;
            LiftRide body = new LiftRide();
            body.setTime(time);
            body.setLiftID(liftID);
            MyLiftRide myLiftRide = new MyLiftRide(body, resortID, SEASON_ID, DAY_ID, skierID);
            this.eventQueue.offer(myLiftRide);
        }
    }
}
