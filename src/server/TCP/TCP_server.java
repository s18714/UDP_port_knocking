package server.TCP;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCP_server extends Thread {
    private ServerSocket serverSocket;
    private TCP_callback callback;

    private File fileObj;
    private String fileName;

    public TCP_server(TCP_callback callback) {
        this.callback = callback;
        try {
            serverSocket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFile(String path, String name) {
        fileObj = new File(path);
        fileName = name;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                String address = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
                System.out.println("IP: " + address);
                if (this.callback.run(address)) {
                    int status = 0, offset = 0;
                    switch (status) {
                        case 0:
                            byte[] data = (fileName + ">" + fileObj.length()).getBytes(StandardCharsets.UTF_8);
                            genPacketAndSend(socket, data);
                            status = 1;
                        case 1:
                            OutputStream oos = socket.getOutputStream();
                            InputStream ios = new FileInputStream(fileObj);
                            while (true) {
                                int size = 4096;
                                byte[] buffer1 = new byte[size];
                                System.out.println(offset);
                                int read = ios.read(buffer1, 0, size);
                                offset += read;
                                System.out.println(Arrays.toString(buffer1) + " " + read);
                                oos.write(buffer1, 0, read);
                                oos.flush();
                                System.out.println(offset + "==" + fileObj.length());
                                if (offset >= fileObj.length()) {
                                    socket.close();
                                    break;
                                }
                            }
                    }
                } else {
                    System.out.println("Client which should connect and real client's addresses are different");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void genPacketAndSend(Socket socket, byte[] buffer) throws IOException {
        OutputStream oos = socket.getOutputStream();

        byte[] size = longToBytes((long) buffer.length & 0xffffffffL);
        ByteBuffer bb = ByteBuffer.allocate(size.length + buffer.length);
        bb.put(size);
        bb.put(buffer);

        oos.write(bb.array());
        oos.flush();
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
