public class Main {
    //local = "http://localhost:8080/newServer_war_exploded/";
    //EC2: "http://34.220.181.128:8080/newServer_war/";
    private static final int TOTAL_REQUEST = 100;
    private static final int MAX_THREADS = 10;
//    private static String basePath = "http://localhost:8080/a2Server_war_exploded/skiers/12/seasons/2019/days/365/skiers/123";
    //private static String basePath = "http://localhost:8080/a2Server_war_exploded/";
    private static String basePath = "http://52.42.9.41:8080/a2Server_war/";
    public static void main(String[] args) throws Exception {
        MultiThreadClient2 multiThreadClient2 = new MultiThreadClient2(basePath, TOTAL_REQUEST, MAX_THREADS);
        multiThreadClient2.open();
        multiThreadClient2.analyze();
    }
}
