package data;

import action.base.ServerAction;
import data.base.BaseBean;
import data.base.BaseNameVisibleData;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class Patient extends BaseNameVisibleData {

    private CrewMember crewMember;
    private MedicTool tool1;
    private MedicTool tool2;
    private MedicTool tool3;
    private DiagnosticTool diagnosticTool;
    private Sickness sickness;
    private PatientStatus status;
    private Long time;
    private Long deadTime;
    private boolean timeAble = false;

    public Patient() {
        super("Pacient");
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }

    public void setCrewMember(CrewMember crewMember) {
        CrewMember old = this.crewMember;
        this.crewMember = crewMember;
        firePropertyChange("crewMember", old, crewMember);
    }

    public MedicTool getTool1() {
        return tool1;
    }

    public void setTool1(MedicTool tool1) {
        MedicTool old = this.tool1;
        this.tool1 = tool1;
        firePropertyChange("tool1", old, tool1);
    }

    public MedicTool getTool2() {
        return tool2;
    }

    public void setTool2(MedicTool tool2) {
        MedicTool old = this.tool2;
        this.tool2 = tool2;
        firePropertyChange("tool2", old, tool2);
    }

    public MedicTool getTool3() {
        return tool3;
    }

    public void setTool3(MedicTool tool3) {
        MedicTool old = this.tool3;
        this.tool3 = tool3;
        firePropertyChange("tool3", old, tool3);
    }

    public DiagnosticTool getDiagnosticTool() {
        return diagnosticTool;
    }

    public void setDiagnosticTool(DiagnosticTool diagnosticTool) {
        DiagnosticTool old = this.diagnosticTool;
        this.diagnosticTool = diagnosticTool;
        firePropertyChange("diagnosticTool", old, diagnosticTool);
    }

    public Sickness getSickness() {
        return sickness;
    }

    public void setSickness(Sickness sickness) {
        Sickness old = this.sickness;
        this.sickness = sickness;
        firePropertyChange("sickness", old, sickness);
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        PatientStatus old = this.status;
        this.status = status;
        firePropertyChange("status", old, status);
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        Long old = this.time;
        this.time = time;
        firePropertyChange("time", old, time);
    }

    public Long getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Long deadTime) {
        Long old = this.deadTime;
        this.deadTime = deadTime;
        firePropertyChange("deadTime", old, deadTime);
    }

    public boolean isTimeAble() {
        return timeAble;
    }

    public void setTimeAble(boolean timeAble) {
        boolean old = this.timeAble;
        this.timeAble = timeAble;
        firePropertyChange("timeAble", old, deadTime);
    }

    @Override
    public void update(BaseBean baseBean) {
        super.update(baseBean);
        Patient patient = (Patient) baseBean;
        setCrewMember(patient.getCrewMember());
        setTool1(patient.getTool1());
        setTool2(patient.getTool2());
        setTool3(patient.getTool3());
        setDiagnosticTool(patient.getDiagnosticTool());
        setSickness(patient.getSickness());
        setStatus(patient.getStatus());
        setTime(patient.getTime());
        setDeadTime(patient.getDeadTime());
    }

    @Override
    public ServerAction.DataType getDataType() {
        return ServerAction.DataType.Patient;
    }
}
