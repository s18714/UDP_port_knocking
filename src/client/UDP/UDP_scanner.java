package client.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDP_scanner {
    private UDP_callback callback;

    private final static int SEND_DELAY = 50;
    private final static int WAIT_DELAY = 2000;
    private final static String KNOCK_MESSAGE = "KNOCK";

    public UDP_scanner(UDP_callback callback) {
        this.callback = callback;
    }

    public void start(String ip, int[] ports) throws IOException {
        InetAddress serverIp = InetAddress.getByName(ip);
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = KNOCK_MESSAGE.getBytes();
            for (int port : ports) {
                System.out.println(port);
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, serverIp, port);
                socket.send(request);
                Thread.sleep(SEND_DELAY);
            }

            buffer = new byte[256];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(WAIT_DELAY);
            socket.receive(response);
            callback.run(response.getData(), response.getLength());
        } catch (SocketTimeoutException e) {
            System.out.println("No reply.");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
