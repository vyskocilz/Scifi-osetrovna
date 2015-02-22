package data;

import action.base.ServerAction;
import data.base.BaseBean;
import data.base.BaseTool;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class DiagnosticTool extends BaseTool {

    private Long diagnosticTime = 0L;

    public DiagnosticTool() {
        super("Diagnostika");
    }

    public Long getDiagnosticTime() {
        return diagnosticTime;
    }

    public void setDiagnosticTime(Long diagnosticTime) {
        Long old = this.diagnosticTime;
        this.diagnosticTime = diagnosticTime;
        firePropertyChange("diagnosticTime", old, diagnosticTime);
    }

    @Override
    public void update(BaseBean baseBean) {
        super.update(baseBean);
        DiagnosticTool diagnosticTool = (DiagnosticTool) baseBean;
        this.setDiagnosticTime(diagnosticTool.getDiagnosticTime());
    }

    @Override
    public ServerAction.DataType getDataType() {
        return ServerAction.DataType.DiagnosticTool;
    }
}
