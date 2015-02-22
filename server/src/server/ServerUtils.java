package server;

import data.system.ChatData;
import org.jdesktop.observablecollections.ObservableList;

public class ServerUtils {

    private static Server server = new Server();

    public static void startServer() {
        if (!Server.isRunning()) {
            Thread runServer = new Thread() {
                public void run() {
                    server.start();
                }
            };
            runServer.start();
        }
    }

    public static void stopServer() {
        server.stop();
    }

    public static ObservableList<ServerClient> getServerClients() {
        return server.getClientGroup().getServerClientList();
    }

    public static void sendChatMessage(String msg) {
        server.getClientGroup().broadcast(new ChatData(msg));
    }

    public static ClientGroup getClientGroup() {
        return server.getClientGroup();
    }

    public static void addServerClientListener(ServerClientListener serverClientListener) {
        server.addServerClientListener(serverClientListener);
    }
}
