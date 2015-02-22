package utils;

import data.Patient;
import main.ServerDataController;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class PatientTimerUtils extends TimerUtils<Patient> {
    public PatientTimerUtils(ServerDataController controller) {
        super(controller);
    }

    @Override
    protected void process(Patient patient) {
        if (patient.getTime() == null) {
            remove(patient, false);
        } else if (patient.getTime() <= 0) {
            remove(patient, true);
        } else {
            patient.setTime(patient.getTime() - 1);
            controller.onPatientDataChange(patient);
        }
    }

    @Override
    protected void onTimeOver(Patient patient) {
        controller.onPatientTimeOver(patient, true);
    }
}
