package dialogs;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public interface DialogCallback<T> {

    public void onSelected(T... items);

}
