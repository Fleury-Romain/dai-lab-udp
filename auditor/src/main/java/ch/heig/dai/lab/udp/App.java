package ch.heig.dai.lab.udp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class App 
{
    static List<String> musicians = new ArrayList<>();
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        UdpRoutine udpr = new UdpRoutine("239.1.2.3", 9904);
        TcpClientHandler tcps = new TcpClientHandler("localhost", 2205);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(udpr::run);
        executor.submit(tcps::run).get();
    }
}
