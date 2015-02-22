package main;

import action.*;
import action.base.ServerAction;
import client.ClientListener;
import client.ClientUtils;
import data.*;
import data.base.BaseBean;
import data.base.BaseData;
import data.base.BaseNameVisibleData;
import data.base.ListAdapter;
import data.system.ChatData;
import data.system.ChatEnableData;
import org.jdesktop.observablecollections.ObservableList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class ClientDataController extends BaseBean implements ClientListener {
    private ChatEnableData chatEnableData;
    private ChatClientData chatClientData;
    ClientData clientData;
    ListAdapter<CrewMember> crewGridAdapter;
    ListAdapter<CrewMember> visibleCrewGridAdapter;
    ListAdapter<MedicTool> toolGridAdapter;
    ListAdapter<DiagnosticTool> diagnosticToolGridAdapter;
    ListAdapter<Sickness> sicknessListAdapter;
    ListAdapter<Patient> patientListAdapter;
    ClientApplication clientApplication;

    public ClientDataController(ClientApplication clientApplication) {
        this.clientApplication = clientApplication;
        clientData = new ClientData();
        chatEnableData = new ChatEnableData();
        chatClientData = new ChatClientData();
        crewGridAdapter = new ListAdapter<CrewMember>(clientData.getCrew());
        visibleCrewGridAdapter = new ListAdapter<CrewMember>(new ArrayList<CrewMember>());
        toolGridAdapter = new ListAdapter<MedicTool>(clientData.getTools());
        diagnosticToolGridAdapter = new ListAdapter<DiagnosticTool>(clientData.getDiagnosticTools());
        sicknessListAdapter = new ListAdapter<Sickness>(clientData.getSicknesses());
        patientListAdapter = new ListAdapter<Patient>(clientData.getPatients());
    }

    public void onDataReceived(Object data) {
        if (data instanceof ServerBadLogin) {
            JOptionPane.showMessageDialog(clientApplication, ((ServerBadLogin) data).getMsg());
            ClientUtils.stop();
            System.exit(0);
        } else if (data instanceof ChatData) {
            chatClientData.writeText(((ChatData) data).getText());
        } else if (data instanceof ChatEnableData) {
            chatEnableData.setEnabled(((ChatEnableData) data).isEnabled());
        } else if (data instanceof DataChangeAction) {
            processServerAction((DataChangeAction) data);
        } else if (data instanceof ServerMessage) {
            JOptionPane.showMessageDialog(clientApplication, ((ServerMessage) data).getMsg());
        } else {
            System.out.println("Unknown data [" + data + "]");
        }
    }

    private void processServerAction(DataChangeAction dataChangeAction) {
        BaseData item = dataChangeAction.getItem();
        ListAdapter listAdapter = getListAdapter(dataChangeAction.getDataType());
        switch (dataChangeAction.getOperation()) {
            case List:
                List<BaseData> visibleData = new ArrayList<BaseData>();
                for (BaseData data : dataChangeAction.getItems()) {
                    if (isVisible(dataChangeAction.getDataType(), data)) {
                        visibleData.add(data);
                    }
                }
                listAdapter.loadData(visibleData);
                clientApplication.clearComboBox(dataChangeAction.getDataType());
                break;
            case Add:
                if (isVisible(dataChangeAction.getDataType(), item)) {
                    listAdapter.add(dataChangeAction.getItem());
                } else {
                    listAdapter.remove(item.getId());
                }
                break;
            case Remove:
                listAdapter.remove(item.getId());
                break;
            case Update:
                if (isVisible(dataChangeAction.getDataType(), item)) {
                    listAdapter.update(dataChangeAction.getItem());
                    listAdapter.onItemPropertiesChange(getListAdapter(dataChangeAction.getDataType()).getItem(item.getId()));
                } else {
                    listAdapter.remove(item.getId());
                }
                break;
        }
    }

    private boolean isVisible(ServerAction.DataType type, BaseData item) {
        if (item instanceof BaseNameVisibleData && !((BaseNameVisibleData) item).isVisible()) {
            return false;
        }
        switch (type) {
            case Sickness:
            case DiagnosticTool:
            case Crew:
            case MedicTool:
                return true;
            case Patient:
                return !(item instanceof Patient) || ((Patient) item).getStatus() != PatientStatus.Smazán;
            default:
                return false;
        }
    }

    private ListAdapter getListAdapter(ServerAction.DataType type) {
        switch (type) {
            case Sickness:
                return sicknessListAdapter;
            case DiagnosticTool:
                return diagnosticToolGridAdapter;
            case Crew:
                return crewGridAdapter;
            case Patient:
                return patientListAdapter;
            case MedicTool:
                return toolGridAdapter;
        }
        return null;
    }

    public void addCrewMember(CrewMember member, Sickness sickness) {
        ClientUtils.sendData(new AddPatientAkce(member.getId(), sickness.getId()));
    }

    public void doDiagnostic(Patient patient, DiagnosticTool diagnosticTool) {
        ClientUtils.sendData(new DiagnosticAction(patient.getId(), diagnosticTool.getId()));
    }

    public void doMedic(Patient patient, MedicTool tool1, MedicTool tool2, MedicTool tool3) {
        MedicAction action = new MedicAction();
        action.setIdPatient(patient.getId());
        if (tool1 != null) {
            action.setIdTool1(tool1.getId());
        }
        if (tool2 != null) {
            action.setIdTool2(tool2.getId());
        }
        if (tool3 != null) {
            action.setIdTool3(tool3.getId());
        }
        ClientUtils.sendData(action);
    }

    public ChatEnableData getChatEnableData() {
        return chatEnableData;
    }

    public ChatClientData getChatClientData() {
        return chatClientData;
    }

    public Sickness getSickness(Integer index) {
        if (index < 0 || sicknessListAdapter.getListModel().size() < index) {
            return null;
        }
        return sicknessListAdapter.getListModel().get(index);
    }

    @SuppressWarnings("UnusedDeclaration")
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

    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<CrewMember> getVisibleCrewListModel() {
        return visibleCrewGridAdapter.getListModel();
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

    @SuppressWarnings("UnusedDeclaration")
    public ObservableList<Sickness> getSicknessListModel() {
        return sicknessListAdapter.getListModel();
    }

}
