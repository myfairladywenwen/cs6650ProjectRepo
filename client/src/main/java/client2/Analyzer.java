package client2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Analyzer {
    Long startTime;
    Long endTime;
    AtomicInteger numSuccess;
    AtomicInteger numNonSuccess;
    ConcurrentLinkedQueue<List<Long>> time;
    ConcurrentLinkedQueue<String> log;

    public Analyzer() {
        this.numSuccess = new AtomicInteger();
        this.numNonSuccess = new AtomicInteger();
        this.startTime = null;
        this.endTime = null;
        this.log = new ConcurrentLinkedQueue<>();
        this.time = new ConcurrentLinkedQueue<>();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
    }

    public void addSuccess(int num) {
        this.numSuccess.addAndGet(num);
    }

    public void addNonSuccess(int num) {
        this.numNonSuccess.addAndGet(num);
    }

    public void addTime(List<Long> timeList) {
        this.time.add(timeList);
    }

    public void addLog(String log) {
        this.log.add(log);
    }

    public void analyze () {
        List<Long> array = new ArrayList<>();
        for (List<Long> threadTime : this.time) array.addAll(threadTime);
        int n = array.size();
        array.sort(Comparator.comparingLong(x->x));
        double median = array.get(n / 2);
        if (n % 2 == 0) median = (median + array.get(n / 2 - 1)) / 2;
        double mean = 0.0;
        for (Long aLong : array) mean += aLong;
        mean /= n;
        double throughput = (double)(this.numSuccess.get() + this.numNonSuccess.get()) / (this.endTime - this.startTime)  * 1000;
        long p99 = array.get((n-1) * 99 / 100);
        long min = array.get(0);
        long max = array.get(n-1);
        System.out.println("mean response time: " + mean + " milliseconds. median response time: " + median +
                " milliseconds.\n" +
                "throughput: " + throughput + " requests per second.\n" +
                "p99: " + p99 + " milliseconds.\n" +
                "min response time: " + min + " milliseconds. max response time: " + max + " milliseconds");
        File file = new File("log.csv");
        try {
            // create FileWriter object with file as parameter
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            for (String l : log) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
