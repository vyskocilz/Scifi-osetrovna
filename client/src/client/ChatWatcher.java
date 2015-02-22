package client;

import java.io.ObjectInputStream;


public abstract class ChatWatcher extends Thread {
    private ObjectInputStream ino;

    public ChatWatcher(ObjectInputStream ino) {
        this.ino = ino;
    }


    public void run() {
        Object line;
        try {
            while ((line = ino.readObject()) != null) {
                doRequest(line);
            }
        } catch (Exception e) {
            System.out.println("Server odpojen");
            Client.setConnected(false);
        }
    }

    protected abstract void doRequest(Object data);

}