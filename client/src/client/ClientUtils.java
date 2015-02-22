package client;

public class ClientUtils {
    static Client client = new Client();

    public static void start() {
        client.start();
    }

    public static void stop() {
        client.stop();

    }

    public static void sendData(Object data) {
        client.sendData(data);
    }

    public static void addClientListener(ClientListener clientListener) {
        client.addClientListener(clientListener);
    }
}
