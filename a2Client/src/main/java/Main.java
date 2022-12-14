public class Main {
    //local = "http://localhost:8080/newServer_war_exploded/";
    //EC2: "http://34.220.181.128:8080/newServer_war/";
//    private static final int TOTAL_REQUEST = 200000;
//    private static final int MAX_THREADS = 168;
    private static final int TOTAL_REQUEST = 32000;
    private static final int MAX_THREADS = 32;
//    private static String basePath = "http://localhost:8080/a2Server_war_exploded/skiers/12/seasons/2019/days/365/skiers/123";
    //private static String basePath = "http://localhost:8080/a2Server_war_exploded/";

//    private static String basePath = "http://localhost:8080/a3server_war_exploded/";

    //single server ec2 elasticIP
    private static String basePath = "http://44.224.249.77:8080/a3server_war/";

    //load balancer
    //private static String basePath = "http://serveralb-1053142013.us-west-2.elb.amazonaws.com:8080/a2Server_war/";

    public static void main(String[] args) throws Exception {
        MultiThreadClient2 multiThreadClient2 = new MultiThreadClient2(basePath, TOTAL_REQUEST, MAX_THREADS);
        multiThreadClient2.open();
        multiThreadClient2.analyze();
    }
}
