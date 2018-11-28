/*
 * Real Estate Valuation Project
 *
 * Copyright © 2018 Carlos Sánchez Martín (carlos.samartin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carlossamartin.realestatevaluation.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "homes")
public class HomeTableWrapper {

    private String size;
    private String formattedAddress;
    private String distance;
    private List<HomeTable> homes;

    private String currentDate;
    private String beforeDate;

    private Double factorBeforeDate;

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

    @XmlElement(name = "currentDate")
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    @XmlElement(name = "beforeDate")
    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }

    @XmlElement(name = "factorBeforeDate")
    public Double getFactorBeforeDate() {
        return factorBeforeDate;
    }

    public void setFactorBeforeDate(Double factorBeforeDate) {
        this.factorBeforeDate = factorBeforeDate;
    }
}
