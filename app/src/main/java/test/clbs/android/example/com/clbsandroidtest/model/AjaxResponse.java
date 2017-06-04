package test.clbs.android.example.com.clbsandroidtest.model;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public interface AjaxResponse<T> {
    void callback(int responseCode, AjaxResponseModel<T> response);
}
