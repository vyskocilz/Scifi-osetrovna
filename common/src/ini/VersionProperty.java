package ini;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class VersionProperty {

    public static final String PROPERTY_FILE = "version-osetrovna.properties";
    public static final String VERSION = "version";

    public static String getVersion() {
        String version = "";
        try {
            Properties properties = new Properties();
            if (new File(PROPERTY_FILE).exists()) {
                properties.load(new FileInputStream(PROPERTY_FILE));
                if (properties.getProperty(VERSION) != null) {
                    version = properties.getProperty(VERSION);
                }
            }
        } catch (IOException e) {
        }
        return version + ". verze";
    }
}

