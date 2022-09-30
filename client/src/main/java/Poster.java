import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;

import java.util.concurrent.LinkedBlockingQueue;

public class Poster implements Runnable{
    private LinkedBlockingQueue<MyLiftRide> eventQueue;
    private Barrier successCounter;
    private Barrier failCounter;
    public static String basePathLocal = "http://localhost:8080/server_war_exploded/skiers/12";
    public static String basePathEC2 = "http://35.171.26.226:8080/server_war/skiers/12";
    public Poster(LinkedBlockingQueue<MyLiftRide> eventQueue, Barrier successCounter, Barrier failCounter){
        this.eventQueue = eventQueue;
        this.successCounter = successCounter;
        this.failCounter = failCounter;
    }

    @Override
    public void run() {
        sendPostRequest();
    }

    private void sendPostRequest(){
        if(successCounter.getVal() >= 200000){
            System.out.println("done. Total success: " + successCounter.getVal());
            return;
        }
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
