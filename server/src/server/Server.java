package server;

import ini.ServerProperty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static int PORT = 1234;  // port for this server

    private ClientGroup clientGroup;
    private static boolean running = false;
    ServerSocket serverSock;
    List<ServerHandler> serverHandlers;
    List<ServerClientListener> serverClientListeners;

    public Server() {
        clientGroup = new ClientGroup();
        serverHandlers = new ArrayList<ServerHandler>();
        serverClientListeners = new ArrayList<ServerClientListener>();
        PORT = Integer.parseInt(ServerProperty.getProperty(ServerProperty.SERVER_PORT));

    }  // end of Server()

    public void start() {
        if (Server.isRunning()) {
            return;
        }
        try {

            serverSock = new ServerSocket(PORT);
            setRunning(true);
            while (isRunning()) {
                System.out.println("Waiting for a client...");
                Socket clientSock = serverSock.accept();
                ServerHandler serverHandler = new ServerHandler(clientSock, clientGroup, serverHandlers) {
                    @Override
                    public void onLogin(ServerClient client) {
                        for (ServerClientListener serverClientListener : serverClientListeners) {
                            serverClientListener.onLogin(client);
                        }
                    }

                    @Override
                    public void onClientDataSend(ServerClient client, Object data) {
                        for (ServerClientListener serverClientListener : serverClientListeners) {
                            serverClientListener.onDataReceived(client, data);
                        }
                    }
                };
                serverHandler.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (isRunning()) {
            setRunning(false);
            try {
                serverSock.close();
                serverSock = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (ServerHandler serverHandler : serverHandlers) {
                serverHandler.disconnect();
                serverHandler.interrupt();
            }
            serverHandlers.clear();
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Server.running = running;
    }

    public ClientGroup getClientGroup() {
        return clientGroup;
    }

    public void addServerClientListener(ServerClientListener serverClientListener) {
        serverClientListeners.add(serverClientListener);
    }


}