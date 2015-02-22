package action.base;

import java.io.Serializable;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public interface ServerAction extends Serializable {

    public enum Operation {
        Update,
        Remove,
        Add,
        List
    }

    public enum DataType {
        Crew,
        Sickness,
        Patient,
        MedicTool,
        DiagnosticTool
    }
}
