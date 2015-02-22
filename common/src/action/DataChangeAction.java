package action;

import action.base.ServerAction;
import data.base.BaseData;

import java.util.List;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class DataChangeAction implements ServerAction {
    private Operation operation;
    private DataType dataType;
    private BaseData item;
    private List<? extends BaseData> items;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BaseData getItem() {
        return item;
    }

    public void setItem(BaseData item) {
        this.item = item;
    }

    public List<? extends BaseData> getItems() {
        return items;
    }

    public void setItems(List<? extends BaseData> items) {
        this.items = items;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
