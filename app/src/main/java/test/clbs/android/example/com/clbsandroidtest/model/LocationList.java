package test.clbs.android.example.com.clbsandroidtest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class LocationList {

    @SerializedName("results")
    List<LocationModel> locations;

    //Getter & Setter
    public List<LocationModel> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationModel> locations) {
        this.locations = locations;
    }
}
