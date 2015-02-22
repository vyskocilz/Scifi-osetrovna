package utils;

import data.Patient;
import main.ServerDataController;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class DeadTimerUtils extends TimerUtils<Patient> {
    public DeadTimerUtils(ServerDataController controller) {
        super(controller);
    }

    @Override
    protected void process(Patient patient) {
        if (patient.isTimeAble()) {
            if (patient.getDeadTime() <= 0) {
                remove(patient, true);
            } else {
                patient.setDeadTime(patient.getDeadTime() - 1);
                controller.onPatientDataChange(patient);
            }
        }
    }

    @Override
    protected void onTimeOver(Patient patient) {
        controller.onPatientDeadTimeOver(patient);
    }
}
