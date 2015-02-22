package ini;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerProperty {

    private static ServerProperty INSTANCE;
    private Properties properties = new Properties();
    public static final String PROPERTY_FILE = "server-osetrovna.properties";
    public static final String SERVER_PORT = "server_port";
    public static final String SERVER_LOG = "server_log";


    private ServerProperty() {
        initPropertyFile();
        load();
        checkProperties();
    }

    private void load() {
        try {
            properties.load(new FileInputStream(PROPERTY_FILE));
        } catch (IOException e) {
        }
    }

    private void checkProperties() {
        String msg = "";
        if (properties.getProperty(SERVER_PORT) == null) {
            msg += (msg.length() > 0) ? "\n" : "" + "Port serveru neni vyplnen";
        }
        if (msg.length() > 0) {
            JOptionPane.showMessageDialog(null,
                    "Spatne hodnoty", msg,
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void save() {
        try {
            properties.store(new FileOutputStream(PROPERTY_FILE), null);
        } catch (IOException ignored) {
        }
    }


    private void initPropertyFile() {
        if (!new File(PROPERTY_FILE).exists()) {
            properties.put(SERVER_PORT, DefaultProperty.APPLICATION_PORT);
            properties.put(SERVER_LOG, DefaultProperty.APPLICATION_DEBUG);
            save();
        }
    }

    public static String getProperty(String name) {
        if (INSTANCE == null) {
            INSTANCE = new ServerProperty();
        }
        return INSTANCE.properties.getProperty(name);
    }

    public static boolean getPropertyAsBoolean(String name) {
        return Boolean.valueOf(getProperty(name));
    }
}

