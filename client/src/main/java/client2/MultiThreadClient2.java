package client2;

import client1.Barrier;
import client1.Generator;
import client1.MyLiftRide;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class MultiThreadClient2 {

    private static final int TOTAL_REQUEST = 200000;
    private static final int MAX_THREADS = 168;
    private Analyzer analyzer;
    Logger errLogger;

    public MultiThreadClient2(){
        this.analyzer = new Analyzer();
        errLogger = Logger.getLogger(String.valueOf(MultiThreadClient2.class));
    }

    public Analyzer getAnalyzer(){
        return this.analyzer;
    }

    public void open() throws Exception {
        LinkedBlockingQueue<MyLiftRide> eventQueue = new LinkedBlockingQueue<>(TOTAL_REQUEST);
        Generator generator = new Generator(eventQueue);
        Barrier successCounter = new Barrier();
        Barrier failCounter = new Barrier();
        this.analyzer.start();

        Thread thread = new Thread(generator);
        thread.start();

        ExecutorService producerPool = Executors.newFixedThreadPool(MAX_THREADS);
        CountDownLatch firstCountDown = new CountDownLatch(1);
        CountDownLatch endCountDown = new CountDownLatch(TOTAL_REQUEST);
        for (int i = 0; i < 32; i++)
        {
            Poster2 poster = new Poster2(this, eventQueue, firstCountDown, endCountDown, successCounter, failCounter) ;
            producerPool.execute(poster);
        }
        firstCountDown.await();

        for(int i = 0; i < MAX_THREADS; i++){
            Poster2 poster = new Poster2(this, eventQueue, firstCountDown, endCountDown, successCounter, failCounter) ;
            producerPool.execute(poster);
        }

        endCountDown.await();
        producerPool.shutdown();
        producerPool.awaitTermination(10, TimeUnit.SECONDS);

        this.analyzer.stop();

        System.out.println("max threads count: " + MAX_THREADS);
        System.out.println("total success: " + this.analyzer.numSuccess);
        System.out.println("total failure: " + this.analyzer.numNonSuccess);
        System.out.println("total time used in millisecond: " + (this.analyzer.endTime - this.analyzer.startTime));
    }

    public void analyze() {
        this.analyzer.analyze();
    }
}
