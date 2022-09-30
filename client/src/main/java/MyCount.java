public class MyCount {
    private int count;
    public MyCount(){
        this.count = 0;
    }
    public synchronized void inc(){
        this.count += 1;
    }

    public int getVal(){
        return this.count;
    }
}
