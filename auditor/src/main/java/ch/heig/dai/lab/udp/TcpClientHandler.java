package ch.heig.dai.lab.udp;

import java.io.*;
import java.net.*;

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
                            var in = new BufferedReader(new InputStreamReader(sock.getInputStream(), UTF_8));
                            var out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), UTF_8));
                    ){
                        // Envoyer la liste des musiciens actifs au client.
                        String line;
                        while ((line = in.readLine()) != null && !line.equals("exit")) {
                            // Nettoyer le tableau de musicien

                            out.write(App.musicians+line+"\n");
                            out.flush();
                        }
                        break;
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
}
