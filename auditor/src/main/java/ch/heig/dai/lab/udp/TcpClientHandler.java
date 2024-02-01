package ch.heig.dai.lab.udp;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TcpClientHandler implements Runnable{
    final String ip;
    final int port;
    public TcpClientHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try{
                    Socket sock = serverSocket.accept();
                    try(
                            var out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), UTF_8));
                    ){
                        // Envoyer la liste des musiciens actifs au client.
                        // Nettoyer le tableau de musicien
                        if(!App.musicians.isEmpty()) {
                            purgeOldEntries(App.musicians, Instant.now());
                        }

                        out.write(App.musicians + "\n");
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }

    private static void purgeOldEntries(List<String> jsonList, Instant currentTime) {
        Map<String, String> latestEntries = new HashMap<>();
        Map<String, Instant> latestTimestamps = new HashMap<>();

        Iterator<String> iterator = jsonList.iterator();
        while (iterator.hasNext()) {
            String jsonString = iterator.next();
            String uuid = getUuid(jsonString);
            String lastActivity = getLastActivity(jsonString);

            // Check if lastActivity is not empty
            if (!lastActivity.isEmpty()) {
                Instant timestamp = Instant.parse(lastActivity);

                // Remove entries older than 5 seconds
                if (timestamp.isAfter(currentTime.minusSeconds(5))) {
                    // Keep the latest entry for each uuid
                    if (!latestTimestamps.containsKey(uuid) || timestamp.isAfter(latestTimestamps.get(uuid))) {
                        latestEntries.put(uuid, jsonString);
                        latestTimestamps.put(uuid, timestamp);
                    }
                } else {
                    // Remove entries older than 5 seconds
                    iterator.remove();
                }
            }
        }
        // Update the original list with the latest entries
        jsonList.clear();
        jsonList.addAll(latestEntries.values());
    }
    private static String getLastActivity(String jsonString) {
        // Extract the value associated with the "lastActivity" key
        int startIndex = jsonString.indexOf("\"lastActivity\":\"") + 15;
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }

    private static String getUuid(String jsonString) {
        // Extract the value associated with the "uuid" key
        int startIndex = jsonString.indexOf("\"uuid\":\"") + 8;
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }
}
