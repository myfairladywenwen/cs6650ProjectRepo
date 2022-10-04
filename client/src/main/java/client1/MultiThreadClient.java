package client1;

import client1.Barrier;
import client1.Generator;

import java.util.concurrent.*;

public class MultiThreadClient {
    private static final int TOTAL_REQUEST = 64000;
    private static final int MAX_THREADS = 32;
    public static void main(String[] arg) throws Exception {
        LinkedBlockingQueue<MyLiftRide> eventQueue = new LinkedBlockingQueue<>(TOTAL_REQUEST);
        Generator generator = new Generator(eventQueue);
        Barrier successCounter = new Barrier();
        Barrier failCounter = new Barrier();
        long start = System.currentTimeMillis();
        Thread thread = new Thread(generator);
        thread.start();

        ExecutorService producerPool = Executors.newFixedThreadPool(MAX_THREADS);
        CountDownLatch firstCountDown = new CountDownLatch(1);
        CountDownLatch endCountDown = new CountDownLatch(TOTAL_REQUEST);
        for (int i = 0; i < 32; i++)
        {
            Poster poster = new Poster(eventQueue, firstCountDown, endCountDown, successCounter, failCounter) ;
            producerPool.execute(poster);
        }
        firstCountDown.await();

        for(int i = 0; i < MAX_THREADS; i++){
            Poster poster = new Poster(eventQueue, firstCountDown, endCountDown, successCounter, failCounter) ;
            producerPool.execute(poster);
        }

        endCountDown.await();
        producerPool.shutdown();
        producerPool.awaitTermination(10, TimeUnit.SECONDS);

        long end = System.currentTimeMillis();
        long time = end-start;
        System.out.println("max threads count: " + MAX_THREADS);
        System.out.println("total success: " + successCounter.getVal());
        System.out.println("total failure: " + failCounter.getVal());
        System.out.println("total time used in millisecond: " + time);
        System.out.println("total throughput in requests per second: " + (double)TOTAL_REQUEST/time * 1000);
    }
}
