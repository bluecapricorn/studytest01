package biz.mybaidumap.mybaidumap.model;

/**
 * Created by Ice Wu on 2017/9/16.
 */

public class AddressModel {

    private static final String LOCAL_CITY = "上海市";

    private String city = LOCAL_CITY;
    private String description;
    private String streetName;
    private String streetNumber;

    public AddressModel(String description, String streetName, String streetNumber) {
        this.description = description;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }
}
