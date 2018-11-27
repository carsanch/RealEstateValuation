/*
 * Real State Valuation Project
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

package com.carlossamartin.realstatevaluation.restclient.idealista;

import com.carlossamartin.realstatevaluation.model.idealista.Home;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdealistaResponse {
    private List<Home> elementList;

    private Integer total;
    private Integer totalPages;
    private Integer actualPage;
    private Integer itemsPerPage;

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

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
