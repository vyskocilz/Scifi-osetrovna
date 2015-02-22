package server;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientGroup {
    ObservableCollections.ObservableListHelper<ServerClient> serverClientListHelper;
    ObservableList<ServerClient> serverClientList;

    public ClientGroup() {
        serverClientListHelper = ObservableCollections.observableListHelper(new ArrayList<ServerClient>());
        serverClientList = serverClientListHelper.getObservableList();
    }

    synchronized public ServerClient addClient(String cliAddr, int port,
                                               ObjectOutputStream out) {
        ServerClient client = new ServerClient(cliAddr, port, out);
        serverClientList.add(client);
        return client;
//        broadcast("Welcome a new chatter (" + cliAddr + ", " + port + ")");
    }

    synchronized public void delClient(String cliAddr, int port) {
        for (ServerClient serverClient : serverClientList) {
            if (serverClient.matches(cliAddr, port)) {
                serverClientList.remove(serverClient);
//                broadcast("(" + cliAddr + ", " + port + ") has departed");
                break;
            }
        }
    }  // end of delPerson()

    synchronized public void removeClient(ServerClient client) {
        serverClientList.remove(client);
    }

    synchronized public void broadcast(Object msg) {
        for (ServerClient serverClient : serverClientList) {
            serverClient.sendMessage(msg);
        }
    }

    synchronized public void broadcast(Object msg, String client) {
        for (ServerClient serverClient : serverClientList) {
            if (client.equals(serverClient.getClientType())) {
                serverClient.sendMessage(msg);
            }
        }
    }

    synchronized public void broadcast(Object msg, ServerClient client) {
        int i = serverClientList.indexOf(client);

        if (i > -1) {
            serverClientList.get(i).sendMessage(msg);
        }
    }  // end of broadcast()

    public ObservableList<ServerClient> getServerClientList() {
        return serverClientList;
    }

    public void fireUpdateChange(ServerClient client) {
        serverClientListHelper.fireElementChanged(serverClientList.indexOf(client));
    }

}