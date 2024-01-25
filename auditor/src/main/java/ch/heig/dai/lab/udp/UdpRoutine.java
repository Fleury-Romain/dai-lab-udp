package ch.heig.dai.lab.udp;

import java.io.IOException;
import java.net.*;

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
                NetworkInterface netif = NetworkInterface.getByName("lo");
                socket.joinGroup(group_address, netif);

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength(), UTF_8);
                App.musicians.add(message);

                System.out.println("Received message: " + message + " from " + packet.getAddress() + ", port " + packet.getPort());
                socket.leaveGroup(group_address, netif);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
