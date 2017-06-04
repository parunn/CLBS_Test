package test.clbs.android.example.com.clbsandroidtest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class LocationModel implements Serializable {

    @SerializedName("geometry")
    Geometry geometry;

    @SerializedName("icon")
    String icon;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("photos")
    List<Photo> photos;

    @SerializedName("place_id")
    String placeId;

    @SerializedName("reference")
    String reference;

    @SerializedName("scope")
    String scope;

    @SerializedName("vicinity")
    String vicinity;

    public LocationModel() {
    }

    public LocationModel(String name) {
        this.name = name;
    }

    //Getter & Setter
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
