package main;

import action.*;
import action.base.ClientAction;
import action.base.ServerAction;
import data.*;
import data.base.BaseData;
import data.base.ItemListener;
import data.base.ListAdapter;
import data.system.ChatData;
import data.system.ChatEnableData;
import org.jdesktop.observablecollections.ObservableList;
import server.ServerClient;
import server.ServerClientListener;
import server.ServerUtils;
import utils.ControllerException;
import utils.DeadTimerUtils;
import utils.PatientTimerUtils;
import utils.ToolUtil;

import javax.swing.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;

import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class ServerDataController implements ServerClientListener, ItemListener {

    private ServerData serverData;
    private ChatServerData chatServerData;
    private PatientTimerUtils patientTimerUtils;
    private DeadTimerUtils deadTimerUtils;

    ListAdapter<CrewMember> crewGridAdapter;
    ListAdapter<MedicTool> toolGridAdapter;
    ListAdapter<DiagnosticTool> diagnosticToolGridAdapter;
    ListAdapter<Sickness> sicknessListAdapter;
    ListAdapter<Patient> patientListAdapter;

    public ServerDataController() {
        serverData = new ServerData();
        patientTimerUtils = new PatientTimerUtils(this);
        deadTimerUtils = new DeadTimerUtils(this);
        chatServerData = new ChatServerData();
        crewGridAdapter = new ListAdapter<CrewMember>(serverData.getCrew());
        crewGridAdapter.setItemListener(this);
        toolGridAdapter = new ListAdapter<MedicTool>(serverData.getTools());
        toolGridAdapter.setItemListener(this);
        diagnosticToolGridAdapter = new ListAdapter<DiagnosticTool>(serverData.getDiagnosticTools());
        diagnosticToolGridAdapter.setItemListener(this);
        sicknessListAdapter = new ListAdapter<Sickness>(serverData.getSicknesses());
        sicknessListAdapter.setItemListener(this);
        patientListAdapter = new ListAdapter<Patient>(serverData.getPatients());
        patientListAdapter.setItemListener(this);
    }


    public void onLogin(ServerClient client) {
        ServerUtils.getClientGroup().broadcast(new ChatEnableData(chatServerData.isChatEnabled()));
        sendServerData();
    }

    public void onDataReceived(ServerClient client, Object data) {
        if (data instanceof ChatData) {
            chatServerData.writeText(client.getClientType(), ((ChatData) data).getText());
        } else if (data instanceof ClientAction) {
            onAction((ClientAction) data);
        }
    }


    private void onAction(ClientAction akce) {
        if (akce instanceof AddPatientAkce) {
            CrewMember member = crewGridAdapter.getItem(((AddPatientAkce) akce).getIdPatient());
            Sickness sickness = sicknessListAdapter.getItem(((AddPatientAkce) akce).getIdSickness());
            if (member != null && sickness != null) {
                addPatient(member, sickness, true);
            }
        } else if (akce instanceof MedicAction) {
            Patient patient = patientListAdapter.getItem(((MedicAction) akce).getIdPatient());
            MedicTool tool1 = toolGridAdapter.getItem(((MedicAction) akce).getIdTool1());
            MedicTool tool2 = toolGridAdapter.getItem(((MedicAction) akce).getIdTool2());
            MedicTool tool3 = toolGridAdapter.getItem(((MedicAction) akce).getIdTool3());
            if (patient != null && (tool1 != null || tool2 != null || tool3 != null)) {
                try {
                    doMedic(patient, tool1, tool2, tool3);
                } catch (ControllerException e) {
                    ServerUtils.getClientGroup().broadcast(new ServerMessage(e.getMessage()));
                }
            }
        } else if (akce instanceof DiagnosticAction) {
            Patient patient = patientListAdapter.getItem(((DiagnosticAction) akce).getIdPatient());
            DiagnosticTool tool = diagnosticToolGridAdapter.getItem(((DiagnosticAction) akce).getIdTool());
            if (patient != null && tool != null) {
                try {
                    doDiagnostic(patient, tool);
                } catch (ControllerException e) {
                    ServerUtils.getClientGroup().broadcast(new ServerMessage(e.getMessage()));
                }
            }
        }
    }

    public ChatServerData getChatServerData() {
        return chatServerData;
    }

    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<Sickness> getSicknessListModel() {
        return sicknessListAdapter.getListModel();
    }

    public Sickness getSickness(Integer index) {
        if (index < 0 || sicknessListAdapter.getListModel().size() < index) {
            return null;
        }
        return sicknessListAdapter.getListModel().get(index);
    }

    public ObservableList<Patient> getPatientListModel() {
        return patientListAdapter.getListModel();
    }

    public Patient getPatient(Integer index) {
        if (index < 0 || patientListAdapter.getListModel().size() < index) {
            return null;
        }
        return patientListAdapter.getListModel().get(index);
    }

    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<CrewMember> getCrewListModel() {
        return crewGridAdapter.getListModel();
    }

    public CrewMember getCrewMember(Integer index) {
        if (index < 0 || crewGridAdapter.getListModel().size() < index) {
            return null;
        }
        return crewGridAdapter.getListModel().get(index);
    }


    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<MedicTool> getToolListModel() {
        return toolGridAdapter.getListModel();
    }

    public MedicTool getMedicTool(Integer index) {
        if (index < 0 || toolGridAdapter.getListModel().size() < index) {
            return null;
        }
        return toolGridAdapter.getListModel().get(index);
    }


    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<DiagnosticTool> getDiagnosticToolListModel() {
        return diagnosticToolGridAdapter.getListModel();
    }

    public DiagnosticTool getDiagnosticTool(Integer index) {
        if (index < 0 || diagnosticToolGridAdapter.getListModel().size() < index) {
            return null;
        }
        return diagnosticToolGridAdapter.getListModel().get(index);
    }


    public void addCrewMember() {
        CrewMember crewMember = new CrewMember();
        crewGridAdapter.addStart(crewMember);
        sendDataAdded(crewMember);
    }

    public void addTool() {
        MedicTool medicTool = new MedicTool();
        toolGridAdapter.addStart(medicTool);
        sendDataAdded(medicTool);
    }

    public void addDiagnosticTool() {
        DiagnosticTool diagnosticTool = new DiagnosticTool();
        diagnosticToolGridAdapter.addStart(diagnosticTool);
        sendDataAdded(diagnosticTool);
    }

    public void addSickness() {
        Sickness sickness = new Sickness();
        sicknessListAdapter.addStart(sickness);
        sendDataAdded(sickness);
    }

    public void addPatient(CrewMember member, Sickness sickness, boolean visible) {
        Patient patient = new Patient();
        patient.setCrewMember(member);
        patient.setSickness(sickness);
        patient.setStatus(PatientStatus.Sick);
        patient.setDeadTime(sickness.getDeadTime());
        patient.setVisible(visible);
        patient.setTimeAble(visible);
        sendDataAdded(patient);
        patientListAdapter.add(patient);
        if (patient.getDeadTime() != 0) {
            deadTimerUtils.add(patient);
            onPatientDataChange(patient);
        } else {
            onPatientDeadTimeOver(patient);
        }
    }

    public void removeCrewMember(CrewMember member) {
        if (member == null) {
            return;
        }
        member.setEditing(true);
        member.setVisible(false);
        member.setEditing(false);
        crewGridAdapter.remove(member);
        sendDataRemoved(member);
    }

    public void removeSickness(Sickness sickness) {
        if (sickness == null) {
            return;
        }
        sickness.setEditing(true);
        sickness.setVisible(false);
        sickness.setEditing(false);
        sicknessListAdapter.remove(sickness);
        sendDataRemoved(sickness);
    }

    public void removeMedicTool(MedicTool medicTool) {
        if (medicTool == null) {
            return;
        }
        for (Sickness sickness : sicknessListAdapter.getListModel()) {
            if (ToolUtil.isUse(medicTool, sickness.getMedicTool1())) {
                sickness.setMedicTool1(null);
            }
            if (ToolUtil.isUse(medicTool, sickness.getMedicTool2())) {
                sickness.setMedicTool2(null);
            }
            if (ToolUtil.isUse(medicTool, sickness.getMedicTool3())) {
                sickness.setMedicTool3(null);
            }
            sicknessListAdapter.onItemPropertiesChange(sickness);
        }
        medicTool.setEditing(true);
        medicTool.setVisible(false);
        medicTool.setEditing(false);
        toolGridAdapter.remove(medicTool);
        sendDataRemoved(medicTool);
    }

    public void removeDiagnosticTool(DiagnosticTool diagnosticTool) {
        if (diagnosticTool == null) {
            return;
        }
        for (Sickness sickness : sicknessListAdapter.getListModel()) {
            if (ToolUtil.isUse(diagnosticTool, sickness.getDiagnosticTool1())) {
                sickness.setDiagnosticTool1(null);
            }
            if (ToolUtil.isUse(diagnosticTool, sickness.getDiagnosticTool2())) {
                sickness.setDiagnosticTool2(null);
            }
            if (ToolUtil.isUse(diagnosticTool, sickness.getDiagnosticTool3())) {
                sickness.setDiagnosticTool3(null);
            }
            sicknessListAdapter.onItemPropertiesChange(sickness);
        }
        diagnosticTool.setEditing(true);
        diagnosticTool.setVisible(false);
        diagnosticTool.setEditing(false);
        diagnosticToolGridAdapter.remove(diagnosticTool);
        sendDataRemoved(diagnosticTool);
    }

    private void workWithPacient(Patient patient, WorkPacient workPacient) throws ControllerException {
        workWithPacient(patient, workPacient, null, null, null, null, null);
    }

    private void workWithPacient(Patient patient, WorkPacient workPacient, Boolean sendData) throws ControllerException {
        workWithPacient(patient, workPacient, sendData, null, null, null, null);
    }

    private void workWithPacient(Patient patient, WorkPacient workPacient, DiagnosticTool diagnosticTool) throws ControllerException {
        workWithPacient(patient, workPacient, null, diagnosticTool, null, null, null);
    }

    private void workWithPacient(Patient patient, WorkPacient workPacient, MedicTool medicTool1, MedicTool medicTool2, MedicTool medicTool3) throws ControllerException {
        workWithPacient(patient, workPacient, null, null, medicTool1, medicTool2, medicTool3);
    }

    private void workWithPacient(Patient patient, WorkPacient workPacient, Boolean sendData,
                                 DiagnosticTool diagnosticTool,
                                 MedicTool medicTool1, MedicTool medicTool2, MedicTool medicTool3) throws ControllerException {
        synchronized (patient) {
            switch (workPacient) {
                case REMOVE:
                    workRemovePatient(patient);
                    break;
                case ON_TIME_OVER:
                    workOnPatientTimeOver(patient, sendData);
                    break;
                case STOP:
                    workStopPacient(patient);
                    break;
                case DIAGNOSTIC:
                    workDiagnostic(patient, diagnosticTool);
                    break;
                case MEDIC:
                    workMedic(patient, medicTool1, medicTool2, medicTool3);
                    break;
                case DEAD:
                    workPatientDead(patient);
                    break;
            }
        }
    }

    private void workRemovePatient(Patient patient) {
        if (patient == null) {
            return;
        }
        patient.setEditing(true);
        deadTimerUtils.remove(patient, false);
        patientTimerUtils.remove(patient, false);
        if (patient.getDiagnosticTool() != null) {
            ToolUtil.decreaseUsability(patient.getDiagnosticTool());
            patient.setDiagnosticTool(null);
        }
        if (patient.getTool1() != null) {
            ToolUtil.decreaseUsability(patient.getTool1());
            patient.setTool1(null);
        }
        if (patient.getTool2() != null) {
            ToolUtil.decreaseUsability(patient.getTool2());
            patient.setTool2(null);
        }
        if (patient.getTool3() != null) {
            ToolUtil.decreaseUsability(patient.getTool3());
            patient.setTool3(null);
        }
        patient.setCrewMember(null);
        patient.setSickness(null);
        patient.setStatus(PatientStatus.Smazán);
        patient.setVisible(false);
        patient.setEditing(false);
        patientListAdapter.remove(patient);
        sendDataRemoved(patient);
    }

    public void removePatient(Patient patient) {
        try {
            workWithPacient(patient, WorkPacient.REMOVE);
        } catch (ControllerException e) {
            // skip
        }
    }

    public void onPatientDataChange(Patient patient) {
        patientListAdapter.onItemPropertiesChange(patient);
    }

    private void workOnPatientTimeOver(Patient patient, Boolean sendData) {
        if (sendData) {
            patient.setEditing(true);
        }
        switch (patient.getStatus()) {
            case Diagnostic:
                ToolUtil.decreaseUsability(patient.getDiagnosticTool());
                if (ToolUtil.isUse(patient.getDiagnosticTool(),
                        patient.getSickness().getDiagnosticTool1(),
                        patient.getSickness().getDiagnosticTool2(),
                        patient.getSickness().getDiagnosticTool3())) {
                    if (patient.isVisible()) {
                        String msg = "Diagnóza u pacienta '" + patient + "' s nemocí '" + patient.getSickness() + "' byla úspěšná. Je potřeba použit přístroje:";
                        if (patient.getSickness().getMedicTool1() != null) {
                            msg += "\n- " + patient.getSickness().getMedicTool1();
                        }
                        if (patient.getSickness().getMedicTool2() != null) {
                            msg += "\n- " + patient.getSickness().getMedicTool2();
                        }
                        if (patient.getSickness().getMedicTool3() != null) {
                            msg += "\n- " + patient.getSickness().getMedicTool3();
                        }
                        ServerUtils.getClientGroup().broadcast(new ServerMessage(msg));
                    }
                } else {
                    String msg = "Diagnóza u pacienta '" + patient + "' s nemocí '" + patient.getSickness() + "' nebyla úspěšná.";
                    ServerUtils.getClientGroup().broadcast(new ServerMessage(msg));
                }
                patient.setDiagnosticTool(null);
                patient.setStatus(PatientStatus.Sick);
                break;
            case Healthy:
                removePatient(patient);
                return;
            case Healing:
                if (patient.getTool1() != null) {
                    ToolUtil.decreaseUsability(patient.getTool1());
                }
                if (patient.getTool2() != null) {
                    ToolUtil.decreaseUsability(patient.getTool2());
                }
                if (patient.getTool3() != null) {
                    ToolUtil.decreaseUsability(patient.getTool3());
                }
                if (ToolUtil.isUse(patient.getSickness().getMedicTool1(), patient.getTool1(), patient.getTool2(), patient.getTool3())
                        && ToolUtil.isUse(patient.getSickness().getMedicTool2(), patient.getTool1(), patient.getTool2(), patient.getTool3())
                        && ToolUtil.isUse(patient.getSickness().getMedicTool3(), patient.getTool1(), patient.getTool2(), patient.getTool3())) {
                    patient.setStatus(PatientStatus.Healthy);
                    deadTimerUtils.remove(patient, false);
                    patient.setTime(10L);
                    patientTimerUtils.add(patient);
                } else {
                    patient.setStatus(PatientStatus.Sick);
                    patientTimerUtils.remove(patient, false);
                }
                patient.setTool1(null);
                patient.setTool2(null);
                patient.setTool3(null);
                break;
        }
        if (sendData) {
            patient.setEditing(false);
            sendDataChanged(patient);
        }
    }

    public void onPatientTimeOver(Patient patient, Boolean sendData) {
        try {
            workWithPacient(patient, WorkPacient.ON_TIME_OVER, sendData);
        } catch (ControllerException e) {
            // skip
        }
    }

    private void workStopPacient(Patient patient) throws ControllerException {
        if (patient == null) {
            return;
        }
        if (patient.getStatus() != PatientStatus.Dead &&
                patient.getStatus() != PatientStatus.Healthy) {

            patientTimerUtils.remove(patient, false);
            if (patient.getDiagnosticTool() != null) {
                ToolUtil.decreaseUsability(patient.getDiagnosticTool());
                patient.setDiagnosticTool(null);
            }
            if (patient.getTool1() != null) {
                ToolUtil.decreaseUsability(patient.getTool1());
                patient.setTool1(null);
            }
            if (patient.getTool2() != null) {
                ToolUtil.decreaseUsability(patient.getTool2());
                patient.setTool2(null);
            }
            if (patient.getTool3() != null) {
                ToolUtil.decreaseUsability(patient.getTool3());
                patient.setTool3(null);
            }
            patient.setStatus(PatientStatus.Sick);
            patient.setTime(null);
            onPatientDataChange(patient);
        } else {
            throw new ControllerException("Pacientovi se nedá přerušit diagnóza/léčení.");
        }
    }

    public void doStopPatient(Patient patient) throws ControllerException {
        workWithPacient(patient, WorkPacient.STOP);
    }

    private void workPatientDead(Patient patient) {
        patient.setEditing(true);
        patient.setStatus(PatientStatus.Dead);
        patientTimerUtils.remove(patient, false);
        if (patient.getDiagnosticTool() != null) {
            ToolUtil.decreaseUsability(patient.getDiagnosticTool());
            patient.setDiagnosticTool(null);
        }
        if (patient.getTool1() != null) {
            ToolUtil.decreaseUsability(patient.getTool1());
            patient.setTool1(null);
        }
        if (patient.getTool2() != null) {
            ToolUtil.decreaseUsability(patient.getTool2());
            patient.setTool2(null);
        }
        if (patient.getTool3() != null) {
            ToolUtil.decreaseUsability(patient.getTool3());
            patient.setTool3(null);
        }
        patient.setTime(null);
        patient.setEditing(false);
        sendDataChanged(patient);
        onPatientDataChange(patient);
    }

    public void onPatientDeadTimeOver(Patient patient) {
        try {
            workWithPacient(patient, WorkPacient.DEAD);
        } catch (ControllerException e) {
            // skip
        }
    }

    private void workDiagnostic(Patient patient, DiagnosticTool diagnosticTool) throws ControllerException {
        if (patient == null) {
            return;
        }
        if (patient.getStatus() != PatientStatus.Sick) {
            throw new ControllerException("Pacient se nedá diagnostikovat.");
        }
        ToolUtil.hasFreeThrow(diagnosticTool);
        ToolUtil.increaseUsability(diagnosticTool);
        patient.setEditing(true);
        patient.setDiagnosticTool(diagnosticTool);
        patient.setStatus(PatientStatus.Diagnostic);
        patient.setTime(diagnosticTool.getDiagnosticTime());
        if (diagnosticTool.getDiagnosticTime() == 0) {
            onPatientTimeOver(patient, false);
        } else {
            patientTimerUtils.add(patient);
        }
        patient.setEditing(false);
        sendDataChanged(patient);
        onPatientDataChange(patient);
    }

    public void doDiagnostic(Patient patient, DiagnosticTool diagnosticTool) throws ControllerException {
        workWithPacient(patient, WorkPacient.DIAGNOSTIC, diagnosticTool);
    }

    private void workMedic(Patient patient, MedicTool medicTool1, MedicTool medicTool2, MedicTool medicTool3) throws ControllerException {
        if (patient == null) {
            return;
        }
        if (patient.getStatus() != PatientStatus.Sick) {
            throw new ControllerException("Pacient se nedá léčit.");
        }
        if (medicTool1 != null) {
            ToolUtil.hasFreeThrow(medicTool1);
            ToolUtil.increaseUsability(medicTool1);
        }
        try {
            if (medicTool2 != null) {
                ToolUtil.hasFreeThrow(medicTool2);
                ToolUtil.increaseUsability(medicTool2);
            }
        } catch (ControllerException e) {
            if (medicTool1 != null) {
                ToolUtil.decreaseUsability(medicTool1);
            }
            throw e;
        }
        try {
            if (medicTool3 != null) {
                ToolUtil.hasFreeThrow(medicTool3);
                ToolUtil.increaseUsability(medicTool3);
            }
        } catch (ControllerException e) {
            if (medicTool1 != null) {
                ToolUtil.decreaseUsability(medicTool1);
            }
            if (medicTool2 != null) {
                ToolUtil.decreaseUsability(medicTool2);
            }
            throw e;
        }
        patient.setEditing(true);
        patient.setTool1(medicTool1);
        patient.setTool2(medicTool2);
        patient.setTool3(medicTool3);
        patient.setStatus(PatientStatus.Healing);
        patient.setTime(patient.getSickness().getHealthTime());
        if (patient.getTime() == null || patient.getTime() == 0) {
            onPatientTimeOver(patient, false);
        } else {
            patientTimerUtils.add(patient);
        }
        patient.setEditing(false);
        sendDataChanged(patient);
        onPatientDataChange(patient);
    }

    public void doMedic(Patient patient, MedicTool medicTool1, MedicTool medicTool2, MedicTool medicTool3) throws ControllerException {
        workWithPacient(patient, WorkPacient.MEDIC, medicTool1, medicTool2, medicTool3);
    }

    public void save(String file) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu uložit data? data", "Uložení dat", JOptionPane.YES_NO_OPTION)) {
            XMLEncoder e;
            try {
                e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
                e.writeObject(serverData);
                e.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void load(String file) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu nahrát data?", "Nahrání dat", JOptionPane.YES_NO_OPTION)) {
            try {
                if (new File(file).exists()) {
                    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
                    Object o = decoder.readObject();
                    if (o instanceof ServerData) {
                        loadServerData((ServerData) o);
                    }
                    decoder.close();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void loadServerData(ServerData serverData) {
        deadTimerUtils.clear();
        patientTimerUtils.clear();
        crewGridAdapter.loadData(serverData.getCrew());
        toolGridAdapter.loadData(serverData.getTools());
        diagnosticToolGridAdapter.loadData(serverData.getDiagnosticTools());
        sicknessListAdapter.loadData(serverData.getSicknesses());
        if (serverData.getPatients() != null) {
            for (Patient patient : serverData.getPatients()) {
                if (patient.getDeadTime() != 0) {
                    deadTimerUtils.add(patient);
                }
                if (patient.getTime() != null && patient.getTime() != 0) {
                    patientTimerUtils.add(patient);
                }
            }
        }
        patientListAdapter.loadData(serverData.getPatients());
    }

    public void onItemPropertyChange(BaseData item) {
        sendDataChanged(item);
    }

    private void sendDataChanged(BaseData item) {
        DataChangeAction dataChangeAction = new DataChangeAction();
        dataChangeAction.setOperation(ServerAction.Operation.Update);
        dataChangeAction.setItem(item);
        dataChangeAction.setDataType(item.getDataType());
        ServerUtils.getClientGroup().broadcast(dataChangeAction);
        System.out.println("Posílám změnu - " + item.getId());
    }

    private void sendDataRemoved(BaseData item) {
        DataChangeAction dataChangeAction = new DataChangeAction();
        dataChangeAction.setOperation(ServerAction.Operation.Remove);
        dataChangeAction.setItem(item);
        dataChangeAction.setDataType(item.getDataType());
        ServerUtils.getClientGroup().broadcast(dataChangeAction);
        System.out.println("Posílám odebrání - " + item.getId());
    }

    private void sendDataAdded(BaseData item) {
        DataChangeAction dataChangeAction = new DataChangeAction();
        dataChangeAction.setOperation(ServerAction.Operation.Add);
        dataChangeAction.setItem(item);
        dataChangeAction.setDataType(item.getDataType());
        ServerUtils.getClientGroup().broadcast(dataChangeAction);
        System.out.println("Posílám přídání - " + item.getId());
    }

    private void sendServerData() {
        sendDataListAdded(serverData.getCrew(), ServerAction.DataType.Crew);
        sendDataListAdded(serverData.getPatients(), ServerAction.DataType.Patient);
        sendDataListAdded(serverData.getTools(), ServerAction.DataType.MedicTool);
        sendDataListAdded(serverData.getDiagnosticTools(), ServerAction.DataType.DiagnosticTool);
        sendDataListAdded(serverData.getSicknesses(), ServerAction.DataType.Sickness);
    }

    private void sendDataListAdded(List<? extends BaseData> items, ServerAction.DataType dataType) {
        DataChangeAction dataChangeAction = new DataChangeAction();
        dataChangeAction.setOperation(ServerAction.Operation.List);
        dataChangeAction.setItems(items);
        dataChangeAction.setDataType(dataType);
        ServerUtils.getClientGroup().broadcast(dataChangeAction);
        System.out.println("Posílám vse");
    }

    private enum WorkPacient {
        REMOVE,
        ON_TIME_OVER,
        STOP,
        DIAGNOSTIC,
        MEDIC,
        DEAD
    }
}
