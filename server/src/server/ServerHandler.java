package server;

import action.ServerBadLogin;
import action.base.ClientAction;
import action.base.ClientType;
import data.system.Login;
import data.system.Logout;
import ini.VersionProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public abstract class ServerHandler extends Thread {
    private Socket clientSock;
    private String cliAddr;
    private int port;
    boolean closeAfterDisconnect = false;

    private ClientGroup cg;    // shared by all threads
    List<ServerHandler> serverHandlers;


    public ServerHandler(Socket s, ClientGroup cg, List<ServerHandler> serverHandlers) {
        this.cg = cg;
        this.serverHandlers = serverHandlers;

        clientSock = s;
        cliAddr = clientSock.getInetAddress().getHostAddress();
        port = clientSock.getPort();
        System.out.println("Client connection from (" + cliAddr + ", " + port + ")");
    }

    public void disconnect() {
        try {
            if (!clientSock.isClosed()) {
                clientSock.close();
                closeAfterDisconnect = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        ServerClient client;
        try {
            serverHandlers.add(this);
            // Get I/O streams from the socket
            ObjectInputStream in = new ObjectInputStream(clientSock.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());
            client = cg.addClient(cliAddr, port, out);
            processClient(client, in);
            cg.removeClient(client);
            clientSock.close();
            if (!closeAfterDisconnect) {
                serverHandlers.remove(this);
            }
            System.out.println("Client (" + cliAddr + ", " + port + ") connection closed\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverHandlers = null;
        clientSock = null;
        cg = null;
    }  // end of run()


    private void processClient(ServerClient client, ObjectInputStream in) {
        Object data;
        try {
            client.setConnected(true);
            while (Server.isRunning() && client.isConnected()) {
                if (!((data = in.readObject()) == null)) {
                    onDataReceived(client, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDataReceived(ServerClient client, Object data) {
        if (data instanceof Login) {
            System.out.println("Uživatel se přihlásil v: " + ((Login) data).getDatum());
            client.setClientType(((Login) data).getClientName());
            if (ClientType.OSETROVNA.equals(client.getClientType())) {
                if (!VersionProperty.getVersion().equals(((Login) data).getVersion())) {
                    ServerUtils.getClientGroup().broadcast(new ServerBadLogin("Server odmítl komunikaci - špatná verze klienta" +
                            "\nClient: " + ((Login) data).getVersion() + ", Server: " + VersionProperty.getVersion()));
                    client.setConnected(false);
                    return;
                }
                cg.fireUpdateChange(client);
                onLogin(client);
            } else {
                client.setConnected(false);
            }
        } else if (data instanceof Logout) {
            System.out.println("Uživatel se dobrovolně odhlásil");
            client.setConnected(false);
        } else if (data instanceof ClientAction) {
            onClientDataSend(client, data);
        } else {
            System.err.println("Client poslal neznámá data a bude ukončen (" + cliAddr + ", " + port + "): " + data);
            client.setConnected(false);
        }
    }

    public abstract void onLogin(ServerClient client);

    public abstract void onClientDataSend(ServerClient client, Object data);
}
