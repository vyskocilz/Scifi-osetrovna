package action;

import action.base.ClientAction;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class AddPatientAkce implements ClientAction {

    String idPatient;
    String idSickness;

    public AddPatientAkce() {
    }


    public AddPatientAkce(String idPatient, String idSickness) {
        this.idPatient = idPatient;
        this.idSickness = idSickness;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdSickness() {
        return idSickness;
    }

    public void setIdSickness(String idSickness) {
        this.idSickness = idSickness;
    }
}
