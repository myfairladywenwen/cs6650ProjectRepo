import java.util.concurrent.*;
import java.util.logging.Logger;

public class MultiThreadClient2 {
    private String basePath;
    private int totalRequest;
    private int maxThreads;
    private Analyzer analyzer;
    Logger errLogger;

    public MultiThreadClient2(String basePath, int totalRequest, int maxThreads){
        this.basePath = basePath;
        this.totalRequest = totalRequest;
        this.maxThreads = maxThreads;
        this.analyzer = new Analyzer();
        this.errLogger = Logger.getLogger(String.valueOf(MultiThreadClient2.class));
    }

    public Analyzer getAnalyzer(){
        return this.analyzer;
    }

    public int getTotalRequest(){return this.totalRequest;}

    public void open() throws Exception {
        LinkedBlockingQueue<MyLiftRide> eventQueue = new LinkedBlockingQueue<>(totalRequest);
        Generator generator = new Generator(eventQueue);
        Counter successCounter = new Counter();
        Counter failCounter = new Counter();
        this.analyzer.start();

        Thread thread = new Thread(generator);
        thread.start();

        ExecutorService producerPool = Executors.newFixedThreadPool(maxThreads);
        CountDownLatch firstCountDown = new CountDownLatch(1);
        CountDownLatch endCountDown = new CountDownLatch(totalRequest);
        for (int i = 0; i < 32; i++)
        {
            Poster2 poster = new Poster2(this, eventQueue, firstCountDown, endCountDown, successCounter, failCounter, basePath) ;
            producerPool.execute(poster);
        }
        for (int i = 0; i < 168; i++)
        {
            Poster2 poster = new Poster2(this, eventQueue, firstCountDown, endCountDown, successCounter, failCounter, basePath) ;
            producerPool.execute(poster);
        }

        endCountDown.await();
        producerPool.shutdown();
        producerPool.awaitTermination(10, TimeUnit.SECONDS);

        this.analyzer.stop();

        System.out.println("max threads count: " + maxThreads);
        System.out.println("total success: " + this.analyzer.numSuccess);
        System.out.println("total failure: " + this.analyzer.numNonSuccess);
        System.out.println("total time used in millisecond: " + (this.analyzer.endTime - this.analyzer.startTime));
    }

    public void analyze() {
        this.analyzer.analyze();
    }
}
