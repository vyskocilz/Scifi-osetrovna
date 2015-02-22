package data;

import action.base.ServerAction;
import data.base.BaseBean;
import data.base.BaseNameVisibleData;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class Sickness extends BaseNameVisibleData {

    private MedicTool medicTool1;
    private MedicTool medicTool2;
    private MedicTool medicTool3;
    private DiagnosticTool diagnosticTool1;
    private DiagnosticTool diagnosticTool2;
    private DiagnosticTool diagnosticTool3;
    private Long healthTime = 0L;
    private Long deadTime = 0L;

    public Sickness() {
        super("Nemoc");
    }

    public MedicTool getMedicTool1() {
        return medicTool1;
    }

    public void setMedicTool1(MedicTool medicTool1) {
        MedicTool old = this.medicTool1;
        this.medicTool1 = medicTool1;
        firePropertyChange("medicTool1", old, medicTool1);
    }

    public MedicTool getMedicTool2() {
        return medicTool2;
    }

    public void setMedicTool2(MedicTool medicTool2) {
        MedicTool old = this.medicTool1;
        this.medicTool2 = medicTool2;
        firePropertyChange("medicTool2", old, medicTool2);
    }

    public MedicTool getMedicTool3() {
        return medicTool3;
    }

    public void setMedicTool3(MedicTool medicTool3) {
        MedicTool old = this.medicTool3;
        this.medicTool3 = medicTool3;
        firePropertyChange("medicTool3", old, medicTool3);
    }

    public Long getHealthTime() {
        return healthTime;
    }

    public void setHealthTime(Long healthTime) {
        Long old = this.healthTime;
        this.healthTime = healthTime;
        firePropertyChange("healthTime", old, healthTime);
    }

    public Long getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Long deadTime) {
        Long old = this.deadTime;
        this.deadTime = deadTime;
        firePropertyChange("deadTime", old, deadTime);
    }

    public DiagnosticTool getDiagnosticTool1() {
        return diagnosticTool1;
    }

    public void setDiagnosticTool1(DiagnosticTool diagnosticTool1) {
        DiagnosticTool old = this.diagnosticTool1;
        this.diagnosticTool1 = diagnosticTool1;
        firePropertyChange("diagnosticTool1", old, diagnosticTool1);
    }

    public DiagnosticTool getDiagnosticTool2() {
        return diagnosticTool2;
    }

    public void setDiagnosticTool2(DiagnosticTool diagnosticTool2) {
        DiagnosticTool old = this.diagnosticTool2;
        this.diagnosticTool2 = diagnosticTool2;
        firePropertyChange("diagnosticTool2", old, diagnosticTool2);
    }

    public DiagnosticTool getDiagnosticTool3() {
        return diagnosticTool3;
    }

    public void setDiagnosticTool3(DiagnosticTool diagnosticTool3) {
        DiagnosticTool old = this.diagnosticTool3;
        this.diagnosticTool3 = diagnosticTool3;
        firePropertyChange("diagnosticTool3", old, diagnosticTool3);
    }

    @Override
    public void update(BaseBean baseBean) {
        super.update(baseBean);
        Sickness sickness = (Sickness) baseBean;
        setMedicTool1(sickness.getMedicTool1());
        setMedicTool2(sickness.getMedicTool2());
        setMedicTool3(sickness.getMedicTool3());
        setDiagnosticTool1(sickness.getDiagnosticTool1());
        setDiagnosticTool2(sickness.getDiagnosticTool2());
        setDiagnosticTool3(sickness.getDiagnosticTool3());
        setHealthTime(sickness.getHealthTime());
        setDeadTime(sickness.getDeadTime());
    }

    @Override
    public ServerAction.DataType getDataType() {
        return ServerAction.DataType.Sickness;
    }
}
