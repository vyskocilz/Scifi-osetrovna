package data.system;

import action.base.ClientAction;

public class ChatData implements ClientAction {
    private String text;

    public ChatData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
