package client2;

import client1.Barrier;
import client1.MyLiftRide;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Poster2 implements Runnable{

    private MultiThreadClient2 client;
    private LinkedBlockingQueue<MyLiftRide> eventQueue;
    private CountDownLatch firstCountDown;
    private CountDownLatch endCountDown;
    private Barrier successCounter;
    private Barrier failCounter;
    private Logger errLogger;
    public static String basePathLocal = "http://localhost:8080/newServer_war_exploded/skiers/12";
    //        public static String basePathEC2 = "http://35.167.243.15:8080/newServer_war/skiers/12";

    public Poster2(MultiThreadClient2 client, LinkedBlockingQueue<MyLiftRide> eventQueue, CountDownLatch firstCountDown, CountDownLatch endCountDown,
                  Barrier successCounter, Barrier failCounter, Logger errLogger){
        this.client = client;
        this.eventQueue = eventQueue;
        this.firstCountDown = firstCountDown;
        this.endCountDown = endCountDown;
        this.successCounter = successCounter;
        this.failCounter = failCounter;

    }

    @Override
    public void run() {
        sendPostRequest();
    }

    private void sendPostRequest(){
        List<Long> timeInThread = new ArrayList<>();
        StringBuilder threadLogWriter = new StringBuilder();
        int success = 0;
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePathLocal);
//        apiClient.setBasePath(basePathEC2);
        SkiersApi skiersApi = new SkiersApi(apiClient);
        for(int i = 0; i < 1000; i++){
            MyLiftRide curr = eventQueue.poll();
            long start = System.currentTimeMillis();
            ApiResponse<Void> res = null;
            try {
                res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
                int j = 0;
                while(j < 5 && res.getStatusCode() != 201){
                    res = skiersApi.writeNewLiftRideWithHttpInfo(curr.getBody(), curr.getResortID(), curr.getSeasonID(), curr.getDayID(),curr.getSkierID());
                    j++;
                }
                if(res.getStatusCode() == 201){
                    success++;
                    endCountDown.countDown();
                    successCounter.inc();
                }else{
                    failCounter.inc();
                    this.client.errLogger.log(Level.SEVERE, "Error: Http Code " + res.getStatusCode());
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            long end = System.currentTimeMillis();
            timeInThread.add(end - start);
            this.addLog(threadLogWriter, start, "POST", end - start, res);
        }
        firstCountDown.countDown();
        this.client.getAnalyzer().addSuccess(success);
        this.client.getAnalyzer().addNonSuccess(1000 - success);
        this.client.getAnalyzer().addTime(timeInThread);
        threadLogWriter.deleteCharAt(threadLogWriter.length()-1);
        this.client.getAnalyzer().addLog(threadLogWriter.toString());

    }

    private void addLog(StringBuilder builder, long startTime, String requestType, long latency, ApiResponse response) {
        builder.append(startTime).append(",")
                .append(requestType).append(",")
                .append(latency).append(",");
        if (response == null) builder.append(-1);
        else builder.append(response.getStatusCode());
        builder.append("\n");
    }
}
