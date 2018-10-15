package com.carlossamartin.realstatevaluation.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "homes")
public class HomeTableWrapper {

    private String size;
    private String formattedAddress;
    private String distance;
    private List<HomeTable> homes;

    @XmlElement(name = "home")
    public List<HomeTable> getHomes() {
        return homes;
    }

    public void setHomes(List<HomeTable> homes) {
        this.homes = homes;
    }

    @XmlElement(name = "address")
    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @XmlElement(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @XmlElement(name = "distance")
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
