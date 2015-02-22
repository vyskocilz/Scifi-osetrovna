package data.base;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Zdenec
 */
public class ListAdapter<T extends BaseData> implements PropertyChangeListener {
    private ObservableCollections.ObservableListHelper<T> modelHelper;
    private ObservableList<T> listModel;
    private Map<String, T> map;
    private ItemListener itemListener;

    public ListAdapter(List<T> dataList) {
        modelHelper = ObservableCollections.observableListHelper(dataList);
        listModel = modelHelper.getObservableList();
        map = new HashMap<String, T>();
    }


    public ObservableList<T> getListModel() {
        return listModel;
    }

    public void addStart(T item) {
        map.put(item.getId(), item);
        listModel.add(0, item);
        addItemListener(item);
    }

    private void addItemListener(T item) {
        item.addPropertyChangeListener(this);
    }

    public void add(T item) {
        map.put(item.getId(), item);
        listModel.add(item);
        addItemListener(item);
    }

    public void remove(T item) {
        item.removePropertyChangeListener(this);
        listModel.remove(item);
        map.remove(item.getId());
    }


    public void remove(String id) {
        if (map.containsKey(id)) {
            remove(map.get(id));
        }
    }

    public void update(T item) {
        if (map.containsKey(item.getId())) {
            map.get(item.getId()).update(item);
        } else {
            add(item);
        }
    }

    public T getItem(String id) {
        if (id == null) {
            return null;
        }
        return map.get(id);
    }

    public void clear() {
        for (T item : listModel) {
            map.remove(item.getId());
            item.removePropertyChangeListener(this);
        }
        listModel.clear();
    }

    public void loadData(List<T> data) {
        clear();
        for (T item : data) {
            map.put(item.getId(), item);
            addItemListener(item);
        }
        listModel.addAll(data);
    }

    public void onItemPropertiesChange(T item) {
        if (listModel.contains(item)) {
            modelHelper.fireElementChanged(listModel.indexOf(item));
        }
    }

    private void onItemPropertyChange(T item) {
        if (!item.isEditing()) {
            if (itemListener != null) {
                itemListener.onItemPropertyChange(item);
            }
        }
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        onItemPropertyChange((T) evt.getSource());
    }
}
