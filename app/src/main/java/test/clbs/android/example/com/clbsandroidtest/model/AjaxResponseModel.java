package test.clbs.android.example.com.clbsandroidtest.model;

import java.util.List;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class AjaxResponseModel<T> {

    public List<T> getList() {
        return list;
    }

    public T getItem() {
        return item;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private List<T> list;
    private T item;

    public void setItem(T item) {
        this.item = item;
    }
}
