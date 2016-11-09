package tw.openedu.android.model;

/**
* Created by hanning on 5/27/15.
*/
public interface Filter<T>{
    boolean apply(T t);
}
