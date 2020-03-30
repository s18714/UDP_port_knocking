package server.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UDP_listener {
    private List<SocketThread> threads = new ArrayList<>();
    private UDP_callback callback;

    public UDP_listener(UDP_callback callback) {
        this.callback = callback;
    }

    public void start(int[] ports) {
        for (int port : ports) {
            try {
                SocketThread th = new SocketThread(port);
                threads.add(th);
                th.start();
            } catch (SocketException e) {
                e.printStackTrace();
                for (SocketThread thread : threads)
                    thread.interrupt();
                System.exit(0);
            }
        }
    }

    class SocketThread extends Thread {
        private DatagramSocket socket;

        private final static String KNOCK_MESSAGE = "KNOCK";

        SocketThread(int port) throws SocketException {
            this.socket = new DatagramSocket(port);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    byte[] bufferInput = new byte[256];
                    DatagramPacket udpInput = new DatagramPacket(bufferInput, bufferInput.length);
                    socket.receive(udpInput);

                    if (!(new String(udpInput.getData(), 0, udpInput.getLength()).equals(KNOCK_MESSAGE)))
                        break;
                    List<Object> out = new ArrayList<>();
                    switch (callback.run(udpInput.getAddress().getHostAddress(), udpInput.getPort(), this.socket.getLocalPort(), out)) {
                        case 1:
                            byte[] buffOut = (byte[]) out.get(0);
                            DatagramPacket udpOutput = new DatagramPacket(buffOut, buffOut.length, udpInput.getAddress(), udpInput.getPort());
                            socket.send(udpOutput);
                            break;
                        case -1:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        }
    }
}