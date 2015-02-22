package client;

import data.system.Login;
import data.system.Logout;
import ini.ClientProperty;
import ini.VersionProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Client {
    private static int PORT = 1235;     // server details
    private static String HOST = "localhost";
    private Socket sock;
    private ObjectOutputStream out;
    private static boolean connected = false;
    private Timer timer;
    List<ClientListener> clientListeners;

    public Client() {
        clientListeners = new ArrayList<ClientListener>();
        PORT = Integer.parseInt(ClientProperty.getProperty(ClientProperty.SERVER_PORT));
        HOST = ClientProperty.getProperty(ClientProperty.SERVER_ADDR);
    }

    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isConnected()) {
                    connect();
                    login();
                }
            }
        }, 5000, 1000);
        connect();
        login();
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            if (isConnected()) {
                sendData(new Logout());
                disconnect();
            }

        }
    }

    protected void connect() {
        if (isConnected()) {
            return;
        }
        try {
            sock = new Socket(HOST, PORT);
            setConnected(true);
            System.out.println("Spojení se server navázáno");
            out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream ino = new ObjectInputStream(sock.getInputStream());
            ChatWatcher chatWatcher = new ChatWatcher(ino) {
                @Override
                protected void doRequest(Object data) {
                    for (ClientListener clientListener : clientListeners) {
                        clientListener.onDataReceived(data);
                    }
                }

            };
            chatWatcher.start();
        } catch (Exception e) {
            System.out.println(e);
            disconnect();
        }
    }

    protected void login() {
        Login login = new Login();
        login.setDatum(new Date(System.currentTimeMillis()));
        login.setClientName(ClientProperty.getClientType());
        login.setVersion(VersionProperty.getVersion());
        sendData(login);
    }

    protected void disconnect() {
        if (isConnected()) {
            try {
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            setConnected(false);
            System.out.println("Spojení se ztraceno");
        }
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean connected) {
        Client.connected = connected;
    }


    public void sendData(Object data) {
        if (isConnected()) {
            try {
                if (sock.isConnected()) {
                    out.writeObject(data);
                    out.reset();
                }
            } catch (IOException e) {
                System.out.println(e);
                setConnected(false);
            }
        }
    }


    public void addClientListener(ClientListener clientListener) {
        clientListeners.add(clientListener);
    }
}
