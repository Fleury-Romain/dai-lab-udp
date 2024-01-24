package ch.heig.dai.lab.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;

import static java.nio.charset.StandardCharsets.*;

enum Instrument {
    PIANO, TRUMPER, FLUTE, VIOLIN, DRUM
}

public class JsonExample {

    public static void main(String[] args) {
        // Créer un objet à sérialiser en JSON
        DataObject dataObject = new Musician("123456", "pouet");

        // Utiliser Gson pour générer la chaîne JSON
        Gson gson = new Gson();
        String json = gson.toJson(dataObject);

        // Afficher la chaîne JSON résultante
        System.out.println(json);
    }
}


// Une classe simple pour l'exemple
class Personne {
    private String nom;
    private String prenom;
    private int age;

    // Constructeur, getters et setters (peuvent être générés automatiquement par votre IDE)

    public Personne(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }
}



class Musician {
    final static String IPADDRESS = "239.255.22.5";
    final static int PORT = 9904;
    final static int TIMER = 1; // timer in seconds
    final static Map<String, String> INSTRUMENT_SOUND = new HashMap<String, String>() {{
        put("piano",   "ti-ta-ti");
        put("trumpet", "pouet");
        put("flute",   "trulu");
        put("violin",  "gzi-gzi");
        put("drum",    "boum-boum");
    }};
    final private UUID uuid;
    final private Instrument instrument;

    Musician(Instrument instrument) {
        this.instrument = instrument;
        this.uuid = UUID.randomUUID();
    }
    /*
    As long as it is running, every second it will emit a sound
    (well... simulate the emission of a sound: we are talking about a communication protocol).
    Bibliothèque : Jackson, mieux : Gson {"uuid":Ox..., "sound":"ti-ta-ti", "timestamp":1500000000000}
    */
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            while (true) {
                String sound = "ti-ta-ti";                String uuid = "Ox...";
                String message = "{\"uuid\":\"" + uuid + "\", \"sound\":\"" + sound + "\", \"timestamp\":1500000000000}";
                byte[] payload = message.getBytes(UTF_8);
                InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, PORT);
                var packet = new DatagramPacket(payload, payload.length, dest_address);
                socket.send(packet);
                try {
                    Thread.sleep(TIMER * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}