package test.clbs.android.example.com.clbsandroidtest.model;

import android.support.annotation.NonNull;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class LocationListViewModel implements Comparable<LocationListViewModel>{

    String locationName;
    double locationLatitude;
    double locationLongitude;
    String locationDistance;
    double radius;

    public LocationListViewModel(String locationName, double locationLatitude, double locationLongitude, String locationDistance, double radius) {
        this.locationName = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationDistance = locationDistance;
        this.radius = radius;
    }

    public LocationListViewModel() {

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationDistance() {
        return locationDistance;
    }

    public void setLocationDistance(String locationDistance) {
        this.locationDistance = locationDistance;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public int compareTo(@NonNull LocationListViewModel o) {

            return getLocationName().compareTo(o.getLocationName());

    }
}
