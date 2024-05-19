package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * @author minskiter
 * @date 10/8/2023 18:40
 * @description
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceCreateDTO {
    @NotNull
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public DataSourceCreateDTO setName(String name) {
        this.name = name;
        return this;
    }
}
