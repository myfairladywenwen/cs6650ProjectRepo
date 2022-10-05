package client2;

public class Main {
    public static void main(String[] args) throws Exception {
        MultiThreadClient2 multiThreadClient2 = new MultiThreadClient2();
        multiThreadClient2.open();
        multiThreadClient2.analyze();
    }
}
