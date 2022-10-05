package client1;

public class Counter {
    private int count = 0;

    synchronized public  void inc() {
        count++;
    }

    synchronized public int getVal() {
        return this.count;
    }
}