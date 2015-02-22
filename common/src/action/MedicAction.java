package action;

import action.base.ClientAction;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class MedicAction implements ClientAction {

    String idTool1;
    String idTool2;
    String idTool3;
    String idPatient;

    public MedicAction() {
    }

    public MedicAction(String idTool1, String idTool2, String idTool3, String idPatient) {
        this.idTool1 = idTool1;
        this.idTool2 = idTool2;
        this.idTool3 = idTool3;
        this.idPatient = idPatient;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdTool1() {
        return idTool1;
    }

    public void setIdTool1(String idTool1) {
        this.idTool1 = idTool1;
    }

    public String getIdTool2() {
        return idTool2;
    }

    public void setIdTool2(String idTool2) {
        this.idTool2 = idTool2;
    }

    public String getIdTool3() {
        return idTool3;
    }

    public void setIdTool3(String idTool3) {
        this.idTool3 = idTool3;
    }
}
