package server;

import server.TCP.TCP_server;
import server.UDP.UDP_listener;

import java.util.*;

public class ServerMain {

    static List<String> allowedAddresses = new ArrayList<>();
    static Map<String, Integer> authAddresses = new HashMap<>();
    static UDP_listener udpListener;
    static TCP_server tcpServer;

    private static int[] ports;

    static public void main(String[] args) {

        int portNum = Integer.parseInt(args[0]);
        int[] ports = new int[portNum];
        for (int i = 0; i < portNum; i++) {
            ports[i] = getRandomPort(ports);
        }

        System.out.println("UDP ports: " + Arrays.toString(ports));

        ServerMain.ports = ports;
        udpListener = new UDP_listener(ServerMain::onKnock);
        tcpServer = new TCP_server(ServerMain::isAllowedIp);
        tcpServer.setFile("file/demovideo.mp4", "demovideo.mp4");

        tcpServer.start();
        udpListener.start(ports);
    }

    static synchronized int onKnock(String inputIp, int inputPort, int port, List<Object> out) {
        int itemNum = authAddresses.getOrDefault(inputIp, 0);
        if (port != ServerMain.ports[itemNum]) {
            authAddresses.remove(inputIp);
            return -1;
        }
        if (itemNum == ServerMain.ports.length - 1) {
            authAddresses.remove(inputIp);
            allowedAddresses.add(inputIp);
            out.add(String.valueOf(ServerMain.tcpServer.getPort()).getBytes());
            return 1;
        }
        authAddresses.put(inputIp, ++itemNum);
        return 0;
    }

    static synchronized boolean isAllowedIp(String address) {
        for (String allowedIP : allowedAddresses)
            if (allowedIP.equals(address))
                return true;
        return false;
    }

    static int getRandomPort(int[] portsArr) {
        int random = 1025;
        boolean isNotFound = true;
        while (isNotFound) {
            random = 1024 + (int) (Math.random() * ((65535 - 1024) + 1));
            isNotFound = false;
            for (int value : portsArr) {
                if (value == random) {
                    isNotFound = true;
                    break;
                }
            }
        }
        return random;
    }
}