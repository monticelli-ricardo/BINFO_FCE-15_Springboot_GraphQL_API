package lu.uni.binfocep.javaee.exercise3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryInfo {
    // properties
    private int _id;
    private String iso2;
    private String iso3;
    private float lat;
    private float longg;
    private String flag;

    // Getters and Setters
    
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getIso2() {
        return iso2;
    }
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }
    public String getIso3() {
        return iso3;
    }
    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    public float getLat() {
        return lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }
    @JsonProperty("long")
    public float getLongg() {
        return longg;
    }
    
    @JsonProperty("long")
    public void setLongg(float longg) {
        this.longg = longg;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }


}
