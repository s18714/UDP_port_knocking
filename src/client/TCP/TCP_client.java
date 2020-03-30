package client.TCP;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TCP_client {
    private String address;
    private int port;

    private SocketInfo socketInfo = new SocketInfo();

    public TCP_client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(address, port)) {
            while (!socket.isClosed()) {
                handlePacket(socket);
            }
            socketInfo.bos.close();
            System.out.println("Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePacket(Socket socket) throws IOException {
        InputStream ios = socket.getInputStream();

        switch (socketInfo.status) {
            case 0:
                int size = ios.available();
                byte[] data;
                if (size <= 0)
                    return;
                data = new byte[8];
                if (ios.read(data, 0, 8) <= 0)
                    throw new IOException();
                long packetSize = bytesToLong(data);
                byte[] payload = new byte[(int) packetSize];
                if (ios.read(payload, 0, (int) packetSize) <= 0)
                    throw new IOException();
                String fileName = new String(payload, StandardCharsets.UTF_8);
                String fileSize = fileName.split(">")[1];
                fileName = fileName.split(">")[0];

                socketInfo.fileSize = fileSize;
                socketInfo.fos = new FileOutputStream(fileName);
                socketInfo.bos = new BufferedOutputStream(socketInfo.fos);
                socketInfo.status = 1;
                return;
            case 1:
                byte[] fileData = new byte[Integer.parseInt(socketInfo.fileSize)];
                int bytesRead;
                while ((bytesRead = ios.read(fileData)) != -1) {
                    socketInfo.bos.write(fileData, 0, bytesRead);
                }
                socketInfo.bos.close();
                socket.close();
        }

    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    static class SocketInfo {
        private FileOutputStream fos;
        private BufferedOutputStream bos;
        private String fileSize;

        public int status = 0;
    }
}