package data.base;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public abstract class BaseTool extends BaseNameVisibleData {

    private Integer count = 1;
    private Integer inUse = 0;

    public BaseTool(String id) {
        super(id);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        Integer old = this.count;
        this.count = count;
        firePropertyChange("count", old, count);
    }

    public Integer getInUse() {
        return inUse;
    }

    public void setInUse(Integer inUse) {
        Integer old = this.inUse;
        this.inUse = inUse;
        firePropertyChange("inUse", old, inUse);
    }

    @Override
    public void update(BaseBean baseBean) {
        super.update(baseBean);
        BaseTool baseTool = (BaseTool) baseBean;
        this.setCount(baseTool.getCount());
        this.setInUse(baseTool.getInUse());
    }

    @Override
    public String toString() {
        return getName() + " (" + inUse + "/" + count + ")";
    }

}
