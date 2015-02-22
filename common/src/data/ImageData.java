package data;

import javax.swing.*;
import java.io.Serializable;

/**
 * User: Zdenec
 */
public class ImageData implements Serializable {
    private ImageIcon map;
    private ImageIcon cross;

    public ImageData() {
    }

    public ImageData(ImageIcon cross, ImageIcon map) {
        this.cross = cross;
        this.map = map;
    }

    public ImageIcon getMap() {
        return map;
    }

    public void setMap(ImageIcon map) {
        this.map = map;
    }

    public ImageIcon getCross() {
        return cross;
    }

    public void setCross(ImageIcon cross) {
        this.cross = cross;
    }
}
