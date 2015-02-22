package action;

import action.base.ServerAction;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class ServerBadLogin implements ServerAction {

    public ServerBadLogin() {
    }

    public ServerBadLogin(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
