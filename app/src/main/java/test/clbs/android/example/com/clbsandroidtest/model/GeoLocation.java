package test.clbs.android.example.com.clbsandroidtest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by parunpichaiwong on 5/28/2017 AD.
 */

public class GeoLocation implements Serializable {

    @SerializedName("lat")
    String latitude;

    @SerializedName("lng")
    String longitude;

    //Constructor
    public GeoLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getter & Setter
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
