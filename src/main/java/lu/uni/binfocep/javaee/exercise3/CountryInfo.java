package lu.uni.binfocep.javaee.exercise3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryInfo {

    // properties
    @JsonProperty("_id")
    private int id;

    @JsonProperty("iso2")
    private String iso2;

    @JsonProperty("iso3")
    private String iso3;

    @JsonProperty("lat")
    private float lat;

    @JsonProperty("long")
    private float longg;

    @JsonProperty("flag")
    private String flag;

    // Getters and Setters
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public float getLongg() {
        return longg;
    }
    
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
