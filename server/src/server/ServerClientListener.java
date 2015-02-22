package server;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public interface ServerClientListener {
    public void onLogin(ServerClient client);

    public void onDataReceived(ServerClient client, Object data);
}
