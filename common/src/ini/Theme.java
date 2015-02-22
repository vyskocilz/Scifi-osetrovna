package ini;

import javax.swing.*;

public class Theme {

    public static void init() {
        try {
            UIManager.setLookAndFeel(DefaultProperty.THEME);
        } catch (Exception ignored) {
        }
    }
}
