package client2;

public class Main {
    //local = "http://localhost:8080/newServer_war_exploded/skiers/12";
    //EC2: "http://34.220.181.128:8080/newServer_war/skiers/12/seasons/2019/days/365/skiers/123";
    private static final int TOTAL_REQUEST = 200000;
    private static final int MAX_THREADS = 168;
    private static String basePath = "http://localhost:8080/newServer_war_exploded/skiers/12/seasons/2019/days/365/skiers/123";

    public static void main(String[] args) throws Exception {
        MultiThreadClient2 multiThreadClient2 = new MultiThreadClient2(basePath, TOTAL_REQUEST, MAX_THREADS);
        multiThreadClient2.open();
        multiThreadClient2.analyze();
    }
}
