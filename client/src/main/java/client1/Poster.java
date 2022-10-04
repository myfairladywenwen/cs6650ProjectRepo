package client1;

import client1.Barrier;
import client1.MyLiftRide;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Poster implements Runnable{
    private LinkedBlockingQueue<MyLiftRide> eventQueue;
    private CountDownLatch firstCountDown;
    private CountDownLatch endCountDown;
    private Barrier successCounter;
    private Barrier failCounter;
    public static String basePathLocal = "http://localhost:8080/newServer_war_exploded/skiers/12";
//        public static String basePathEC2 = "http://35.167.243.15:8080/newServer_war/skiers/12";
    public Poster(LinkedBlockingQueue<MyLiftRide> eventQueue, CountDownLatch firstCountDown, CountDownLatch endCountDown,
                  Barrier successCounter, Barrier failCounter){
        this.eventQueue = eventQueue;
        this.firstCountDown = firstCountDown;
        this.endCountDown = endCountDown;
        this.successCounter = successCounter;
        this.failCounter = failCounter;
    }

    public Poster(LinkedBlockingQueue<MyLiftRide> eventQueue, CountDownLatch endCountDown,
                  Barrier successCounter, Barrier failCounter){
        this.eventQueue = eventQueue;
        this.endCountDown = endCountDown;
        this.successCounter = successCounter;
        this.failCounter = failCounter;
    }

    @Override
    public void run() {
        sendPostRequest();
    }

    private void sendPostRequest(){
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePathLocal);
//        apiClient.setBasePath(basePathEC2);
        SkiersApi skiersApi = new SkiersApi(apiClient);
        for(int i = 0; i < 1000; i++){
            MyLiftRide curr = eventQueue.poll();
            try {
                ApiResponse<Void> res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
                int j = 0;
                while(j < 5 && res.getStatusCode() != 201){
                    res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
                    j++;
                }
                if(res.getStatusCode() == 201){
//                    if(firstCountDown != null) {
//                        firstCountDown.countDown();
//                    }
                    endCountDown.countDown();
                    successCounter.inc();
                }else{
                    failCounter.inc();
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
        firstCountDown.countDown();
    }

    public void sendPostRequestSingleThread(){
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePathLocal);
//        apiClient.setBasePath(basePathEC2);
        SkiersApi skiersApi = new SkiersApi(apiClient);
        for(int i = 0; i < 10000; i++){
            MyLiftRide curr = eventQueue.poll();
            try {
                ApiResponse<Void> res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
//                System.out.println(res.getStatusCode());
                int j = 0;
                while(j < 5 && res.getStatusCode() != 201){
                    res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
                    j++;
                }
                if(res.getStatusCode() == 201){
                    endCountDown.countDown();
                    successCounter.inc();
                }else{
                    failCounter.inc();
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
