package data.system;

import action.base.ClientAction;
import data.base.BaseBean;

public class ChatEnableData extends BaseBean implements ClientAction {
    private boolean enabled;

    public ChatEnableData(boolean enabled) {
        this.enabled = enabled;
    }

    public ChatEnableData() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        boolean old = this.enabled;
        this.enabled = enabled;
        firePropertyChange("enabled", old, enabled);
    }
}
