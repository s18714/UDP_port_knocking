package client;

import client.TCP.TCP_client;
import client.UDP.UDP_scanner;

import java.io.IOException;
import java.util.Arrays;

public class ClientMain {
    public static final String LOCAL_IP = "127.0.0.1";

    public static void main(String[] args) {

        int[] ports = new int[args.length];
        for (int i = 0; i < args.length; i++)
            ports[i] = Integer.parseInt(args[i]);
        System.out.println(Arrays.toString(ports));

        try {
            new UDP_scanner(ClientMain::onKnock).start(LOCAL_IP, ports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void onKnock(byte[] response, int length) {
        int port = Integer.parseInt(new String(response, 0, length));
        new TCP_client(LOCAL_IP, port).start();
    }
}