package test.clbs.android.example.com.clbsandroidtest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class Geometry implements Serializable {

    @SerializedName("location")
    GeoLocation geoLocation;

    //Getter & Setter
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }
}
