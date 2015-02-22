package converter;

import data.Patient;
import data.PatientStatus;
import org.jdesktop.beansbinding.Converter;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class PatientStatusDiagnosticConverter extends Converter<Patient, Boolean> {
    @Override
    public Boolean convertForward(Patient value) {
        return value != null && value.getStatus() == PatientStatus.Sick;
    }

    @Override
    public Patient convertReverse(Boolean value) {
        return null;
    }
}
