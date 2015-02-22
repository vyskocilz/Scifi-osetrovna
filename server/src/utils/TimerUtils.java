package utils;

import data.base.BaseData;
import main.ServerDataController;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public abstract class TimerUtils<T extends BaseData> {

    private Timer timer = new Timer();
    private HashMap<String, TimerTask> patients = new HashMap<String, TimerTask>();
    protected ServerDataController controller;

    public TimerUtils(ServerDataController controller) {
        this.controller = controller;
        timer = new Timer();
        patients = new HashMap<String, TimerTask>();
    }

    public void add(final T subject) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                process(subject);
            }
        };
        patients.put(subject.getId(), task);
        timer.schedule(task, 1000, 1000);
    }

    protected abstract void process(T subject);

    protected abstract void onTimeOver(T subject);

    public void remove(T subject, boolean onTimeEvent) {
        if (patients.containsKey(subject.getId())) {
            patients.get(subject.getId()).cancel();
            patients.remove(subject.getId());
            if (onTimeEvent) {
                onTimeOver(subject);
            }
            if (patients.isEmpty()) {
                timer.purge();
            }
        }
    }

    public void clear() {
        patients.clear();
        timer.purge();
    }
}
