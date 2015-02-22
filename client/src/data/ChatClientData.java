package data;

import client.ClientUtils;
import data.system.ChatData;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

import java.util.ArrayList;

//import server.ServerUtils;

public class ChatClientData {
    private ObservableCollections.ObservableListHelper<String> chatModel = ObservableCollections.observableListHelper(new ArrayList<String>());
    private ObservableList<String> chatListModel = chatModel.getObservableList();

    public ObservableList<String> getChatListModel() {
        return chatListModel;
    }

    public void writeText(String text) {
        while (chatListModel.size() > 100) {
            chatListModel.remove(100);
        }
        chatListModel.add(0, text);
    }

    public void sendText(String text) {
        ClientUtils.sendData(new ChatData(text));
    }
}
