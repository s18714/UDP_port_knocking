package server.UDP;

import java.util.List;

public interface UDP_callback {

    int run(String inputIp, int inputPort, int port, List<Object> out);
}