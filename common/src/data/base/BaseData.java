package data.base;

import action.base.ServerAction;

import java.io.Serializable;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public abstract class BaseData extends BaseBean implements Serializable {

    private String id;

    protected BaseData(String id) {
        this.id = id + "-" + System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract ServerAction.DataType getDataType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseData)) return false;

        BaseData data = (BaseData) o;

        if (id != null ? !id.equals(data.id) : data.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
