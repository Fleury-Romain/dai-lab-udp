package ch.heig.dai.lab.udp;

import java.io.IOException;
import java.net.*;
import java.time.Instant;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UdpRoutine implements Runnable{
    final private String ip;
    final private int port;
    public UdpRoutine(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void run() {
        // Écouté les instruments
        while(true) {
            try (MulticastSocket socket = new MulticastSocket(port)) {
                InetSocketAddress group_address = new InetSocketAddress(ip, port);
                NetworkInterface netif = NetworkInterface.getByName("eth0");
                socket.joinGroup(group_address, netif);

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);

                Json data = new Json(message);
                long timestamp = System.currentTimeMillis();
                String uuid = data.getValue("uuid");
                String instrument = data.getValue("sound");
                App.musicians.add(data.createJsonString(uuid, instrument, timestamp));


                System.out.println(App.musicians);
                socket.leaveGroup(group_address, netif);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
