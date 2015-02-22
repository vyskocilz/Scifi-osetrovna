package base;

public interface EntityListChangeHandler<T> {

    public void onAdd(T element);

    public void onRemove(T element);

    public void onDataChange(T element);
}
