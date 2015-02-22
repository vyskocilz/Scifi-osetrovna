package data;

import action.base.ServerAction;
import data.base.BaseTool;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class MedicTool extends BaseTool {

    public MedicTool() {
        super("Přístroj");
    }

    @Override
    public ServerAction.DataType getDataType() {
        return ServerAction.DataType.MedicTool;
    }
}
