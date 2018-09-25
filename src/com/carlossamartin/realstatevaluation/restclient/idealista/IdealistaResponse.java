package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.carlossamartin.realstatevaluation.model.idealista.Home;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdealistaResponse {
    private List<Home> elementList;
    private Integer total;

    private Integer totalPages;
    private Integer actualPage;

    public IdealistaResponse() {
    }

    public List<Home> getElementList() {
        return elementList;
    }

    public void setElementList(List<Home> elementList) {
        this.elementList = elementList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getActualPage() {
        return actualPage;
    }

    public void setActualPage(Integer actualPage) {
        this.actualPage = actualPage;
    }
}
