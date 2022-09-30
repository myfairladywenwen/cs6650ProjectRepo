import java.util.concurrent.*;

public class MultiThreadClient {

    public static void main(String[] arg) throws Exception {
        LinkedBlockingQueue<MyLiftRide> eventQueue = new LinkedBlockingQueue<>(200000);
        Generator generator = new Generator(eventQueue);
        Barrier successCounter = new Barrier();
        Barrier failCounter = new Barrier();
        long start = System.currentTimeMillis();
        Thread thread = new Thread(generator);
        thread.start();

        LinkedBlockingQueue<Runnable> posterQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(32, 32, 5, TimeUnit.SECONDS,
                        posterQueue, new ThreadPoolExecutor.AbortPolicy());
        executor.prestartAllCoreThreads();
        for(int i = 0; i < 32; i++){
            posterQueue.offer(new Poster(eventQueue, successCounter, failCounter));
        }
        executor.shutdown();
        executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);

        for(int i = 0; i < 7; i++){
            ThreadPoolExecutor nextExecutor =
                    new ThreadPoolExecutor(24, 24, 5, TimeUnit.SECONDS,
                            posterQueue, new ThreadPoolExecutor.AbortPolicy());
            nextExecutor.prestartAllCoreThreads();
            for(int j = 0; j < 24; j++){
                posterQueue.offer(new Poster(eventQueue, successCounter,failCounter));
            }
            nextExecutor.shutdown();
            nextExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        }

        long end = System.currentTimeMillis();
        long time = end-start;
        System.out.println("total success: " + successCounter.getVal());
        System.out.println("total failure: " + failCounter.getVal());
        System.out.println("total time used in millisecond: " + time);
        System.out.println("total throughput in requests per second: " + (double)200000/time * 1000);
    }

}
