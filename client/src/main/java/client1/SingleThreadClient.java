package client1;

import client1.Barrier;
import client1.Generator;
import client1.MyLiftRide;
import client1.Poster;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SingleThreadClient {
    private static final int TOTAL_REQUEST = 10000;
    public static void main(String[] arg) throws Exception {
        LinkedBlockingQueue<MyLiftRide> eventQueue = new LinkedBlockingQueue<>(TOTAL_REQUEST);
        Generator generator = new Generator(eventQueue);
        Barrier successCounter = new Barrier();
        Barrier failCounter = new Barrier();
        long start = System.currentTimeMillis();
        Thread thread = new Thread(generator);
        thread.start();

        CountDownLatch endCountDown = new CountDownLatch(TOTAL_REQUEST);
        Poster poster = new Poster(eventQueue, endCountDown, successCounter, failCounter);
        poster.sendPostRequestSingleThread();

        endCountDown.await();
        long end = System.currentTimeMillis();
        long time = end-start;
        System.out.println("max threads count: 1");
        System.out.println("total success: " + successCounter.getVal());
        System.out.println("total failure: " + failCounter.getVal());
        System.out.println("total time used in millisecond: " + time);
        System.out.println("total throughput in requests per second: " + (double)TOTAL_REQUEST/time * 1000);
    }
}

