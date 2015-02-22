package data;

import java.io.Serializable;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public enum PatientStatus implements Serializable {
    Sick("Nemocen"),
    Healing("Léčí se"),
    Healthy("Vyléčen"),
    Dead("Mrtev"),
    Diagnostic("Diagnostikuje se"),
    Smazán("Smazán");

    private String description;

    private PatientStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
