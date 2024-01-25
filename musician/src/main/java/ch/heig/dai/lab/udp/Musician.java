package ch.heig.dai.lab.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

/*
Bibliothèque : Jackson, mieux : Gson {"uuid":Ox..., "sound":"ti-ta-ti", "timestamp":1500000000000}
*/

import static java.nio.charset.StandardCharsets.*;

enum Instrument {
    PIANO("ti-ta-ti"), TRUMPET("pouet"), FLUTE("trulu"), VIOLIN("gzi-gzi"), DRUM("boum-boum");
    private String sound;
    Instrument(String sound) { this.sound = sound; } // constructeur appelé par PIANO("ti-ta-ti"), etc...
    public String toString() { return sound; }
}

class Client {
    private static final String IPADDRESS = "239.255.22.5";
    private static final int PORT = 9904;

    public static void sendData(String jsonData) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] payload = jsonData.getBytes(UTF_8);
            InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);
            var packet = new DatagramPacket(payload, payload.length, dest_address);
            socket.send(packet);
            System.out.println(jsonData);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

class Musician {
    final private UUID uuid;
    final private Instrument instrument;

    Musician(Instrument instrument) {
        this.instrument = instrument;
        this.uuid = UUID.randomUUID();
    }

    public void run() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        int timerPeriod = 1;
        var a = executorService.scheduleAtFixedRate(this::sendData, 0, timerPeriod, TimeUnit.SECONDS);
        /*
        try {
            a.wait();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        */
    }

    private void sendData() {
        Client.sendData(this.toJsonString());
    }

    public String toJsonString() {
        return "{\"uuid\":\"" + this.uuid + "\", \"sound\":\"" + this.instrument + "\"}";
    }
}