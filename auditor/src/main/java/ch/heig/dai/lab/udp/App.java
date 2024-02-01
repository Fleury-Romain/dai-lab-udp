package ch.heig.dai.lab.udp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

//import org.json.JSONArray;
//import org.json.JSONObject;

public class App 
{
    static List<String> musicians = new ArrayList<>();
    String jsonInput = "[\n" +
            "  {\n" +
            "    \"uuid\": \"aa7d8cb3-a15f-4f06-a0eb-b8feb6244a60\",\n" +
            "    \"instrument\": \"piano\",\n" +
            "    \"lastActivity\": 1643467200000\n" + // Replace with your timestamp
            "  },\n" +
            "  {\n" +
            "    \"uuid\": \"06dbcbeb-c4c8-49ed-ac2a-cd8716cbf2d3\",\n" +
            "    \"instrument\": \"flute\",\n" +
            "    \"lastActivity\": 1643467200000\n" + // Replace with your timestamp
            "  }\n" +
            "]";

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        UdpRoutine udpr = new UdpRoutine("239.255.22.5", 9904);
        TcpClientHandler tcps = new TcpClientHandler("localhost", 2205);

        // Test
        String jsonInput = "[\n" +
                "  {\n" +
                "    \"uuid\": \"aa7d8cb3-a15f-4f06-a0eb-b8feb6244a60\",\n" +
                "    \"instrument\": \"piano\",\n" +
                "    \"lastActivity\": 1643467200000\n" + // Replace with your timestamp
                "  },\n" +
                "  {\n" +
                "    \"uuid\": \"06dbcbeb-c4c8-49ed-ac2a-cd8716cbf2d3\",\n" +
                "    \"instrument\": \"flute\",\n" +
                "    \"lastActivity\": 1643467200000\n" + // Replace with your timestamp
                "  }\n" +
                "]";

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(udpr::run);
        executor.submit(tcps::run).get();
    }
}
