package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity implements Serializable {

    private List<EntityChangeHandler> entityChangeHandlers = new ArrayList<EntityChangeHandler>();

    private static Integer counter = 0;

    private String sysName;
    protected String nazev;

    public Entity(String sysName) {
        this.sysName = sysName + (++counter);
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
        onDataChange();
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public void addDataChangeHandler(EntityChangeHandler entityChangeHandler) {
        entityChangeHandlers.add(entityChangeHandler);
    }

    protected void onDataChange() {
        for (EntityChangeHandler entityChangeHandler : entityChangeHandlers) {
            entityChangeHandler.onDataChange(this);
        }
    }

    protected Integer notNull(Integer cislo) {
        return notNull(cislo, 0);
    }

    protected Integer notNull(Integer cislo, Integer defaultValue) {
        return (cislo == null) ? defaultValue : cislo;
    }
}
