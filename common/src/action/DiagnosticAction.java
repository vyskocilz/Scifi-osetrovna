package action;

import action.base.ClientAction;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class DiagnosticAction implements ClientAction {

    String idTool;
    String idPatient;

    public DiagnosticAction() {
    }

    public DiagnosticAction(String idPatient, String idTool) {
        this.idPatient = idPatient;
        this.idTool = idTool;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdTool() {
        return idTool;
    }

    public void setIdTool(String idTool) {
        this.idTool = idTool;
    }
}
