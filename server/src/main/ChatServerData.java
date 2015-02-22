package main;

import data.system.ChatEnableData;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import server.ServerUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * User: Zdenec
 */
public class ChatServerData {


    private boolean chatEnabled = false;
    private static final String CHAT_ENABLE_TITLE = "Zapnout chat";
    private static final String CHAT_DISABLE_TITLE = "Vypnout chat";
    private String chatButtonTitle = CHAT_ENABLE_TITLE;

    public String getChatButtonTitle() {
        return chatButtonTitle;
    }

    public void setChatButtonTitle(String chatButtonTitle) {
        String old = this.chatButtonTitle;
        this.chatButtonTitle = chatButtonTitle;
        changeSupport.firePropertyChange("chatButtonTitle", old, chatButtonTitle);
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        boolean old = this.chatEnabled;
        this.chatEnabled = chatEnabled;
        changeSupport.firePropertyChange("chatEnabled", old, chatEnabled);
        if (chatEnabled) {
            setChatButtonTitle(CHAT_DISABLE_TITLE);
        } else {
            setChatButtonTitle(CHAT_ENABLE_TITLE);
        }
        ServerUtils.getClientGroup().broadcast(new ChatEnableData(chatEnabled));
    }

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    private ObservableCollections.ObservableListHelper<String> chatModel = ObservableCollections.observableListHelper(new ArrayList<String>());
    private ObservableList<String> chatListModel = chatModel.getObservableList();

    public ObservableList<String> getChatListModel() {
        return chatListModel;
    }

    public void writeText(String client, String text) {
        String msg = String.format("%1$15s %2$s", client + ">", text);
        while (chatListModel.size() > 100) {
            chatListModel.remove(100);
        }
        chatListModel.add(0, msg);
        ServerUtils.sendChatMessage(msg);
    }
}
