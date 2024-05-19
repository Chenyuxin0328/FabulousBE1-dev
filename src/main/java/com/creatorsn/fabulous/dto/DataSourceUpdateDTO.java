package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * @author minskiter
 * @date 10/8/2023 18:44
 * @description
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceUpdateDTO {

    @JsonProperty("name")
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public DataSourceUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }
}
