package server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerClient {
    private String clientIp;
    private Integer port;
    private ObjectOutputStream out;
    private String clientType;
    private boolean connected = false;


    public ServerClient(String clientIP, int port, ObjectOutputStream out) {
        this.clientIp = clientIP;
        this.port = port;
        this.out = out;
    }


    public boolean matches(String ca, int p)
    // the address and port of a client are used to uniquely identify it
    {
        if (clientIp.equals(ca) && (port == p)) {
            return true;
        }
        return false;
    } // end of matches()


    public void sendMessage(Object msg) {
        try {
            out.writeObject(msg);
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String toString() {
        return clientIp + " & " + port + " & ";
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int Integer) {
        this.port = port;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
