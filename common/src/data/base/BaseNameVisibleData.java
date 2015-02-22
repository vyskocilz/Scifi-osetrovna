package data.base;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public abstract class BaseNameVisibleData extends BaseData {
    private String name;
    private boolean visible = false;


    public BaseNameVisibleData(String id) {
        super(id);
        setName(id);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange("name", old, name);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = this.visible;
        this.visible = visible;
        firePropertyChange("visible", old, visible);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void update(BaseBean baseBean) {
        BaseNameVisibleData data = (BaseNameVisibleData) baseBean;
        this.setName(data.getName());
        this.setVisible(data.isVisible());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseNameVisibleData)) return false;
        if (!super.equals(o)) return false;

        BaseNameVisibleData that = (BaseNameVisibleData) o;

        if (visible != that.visible) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (visible ? 1 : 0);
        return result;
    }
}
