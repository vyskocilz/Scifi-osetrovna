package base;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.YES_OPTION;

public abstract class EntityList<T extends Entity> {

    private List<EntityListChangeHandler<T>> entityListChangeHandlers = new ArrayList<EntityListChangeHandler<T>>();

    private ObservableCollections.ObservableListHelper<T> listHelper = ObservableCollections.observableListHelper(new ArrayList<T>());
    private ObservableList<T> list = listHelper.getObservableList();

    public void init() {

    }

    public ObservableList<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
        for (T element : list) {
            element.addDataChangeHandler(new EntityChangeHandler<T>() {
                public void onDataChange(T object) {
                    updateElement(object);
                }
            });
        }
    }

    public void updateElement(T element) {
        listHelper.fireElementChanged(list.indexOf(element));
        onDataChangeElement(element);
    }


    public void addElement(T element) {
        element.addDataChangeHandler(new EntityChangeHandler<T>() {
            public void onDataChange(T object) {
                updateElement(object);
            }
        });
        list.add(element);
        onAddElement(element);
    }

    public void createNewElement() {
        T element = createElements();
        addElement(element);
    }

    public boolean removeElement(int index) {

        if (index < 0 || index >= getList().size()) {
            return false;
        }
        String msg = "Opravdu smazat " + list.get(index).getNazev() + "?";
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, msg, "Smazaní", JOptionPane.YES_NO_OPTION)) {
            T element = list.get(index);
            list.remove(index);
            onRemoveElement(element);
            return true;
        }
        return false;
    }

    public void onAddElement(T element) {
        for (EntityListChangeHandler<T> entityListChangeHandler : entityListChangeHandlers) {
            entityListChangeHandler.onAdd(element);
        }
    }

    public void onRemoveElement(T element) {
        sendElements();
        for (EntityListChangeHandler<T> entityListChangeHandler : entityListChangeHandlers) {
            entityListChangeHandler.onRemove(element);
        }
    }

    public void onDataChangeElement(T element) {
        sendElement(element);
        for (EntityListChangeHandler<T> entityListChangeHandler : entityListChangeHandlers) {
            entityListChangeHandler.onDataChange(element);
        }
    }

    public void addEntityListChangeHandler(EntityListChangeHandler<T> entityListChangeHandler) {
        entityListChangeHandlers.add(entityListChangeHandler);
    }

    public abstract void sendElement(T element);

    public abstract void sendElements();

    protected abstract T createElements();

}
