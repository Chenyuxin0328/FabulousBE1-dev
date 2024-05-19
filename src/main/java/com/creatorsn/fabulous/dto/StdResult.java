package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 标准的JSON返回
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StdResult {

    @JsonProperty
    StatusCode status;

    @JsonProperty
    long code;

    @JsonProperty
    Object data;

    @JsonProperty
    List<String> errors;

    @JsonProperty
    String message;

    static public StdResult Ok() {
        return Ok(null);
    }

    static public StdResult Ok(Object data) {
        return new StdResult().setCode(StatusCode.success).setData(data);
    }

    static public StdResult BadRequest() {
        return BadRequest(null);
    }

    static public StdResult BadRequest(Object data) {
        return new StdResult().setCode(StatusCode.Fail).setData(data);
    }

    static public StdResult Status(StatusCode code, Object data) {
        return new StdResult().setCode(code).setData(data);
    }

    static public StdResult Status(StatusCode code) {
        return Status(code, null);
    }


    public StatusCode getStatus() {
        return status;
    }

    public long getCode() {
        return code;
    }

    public StdResult setCode(StatusCode code) {
        this.code = code.getValue();
        this.status = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public StdResult setData(Object data) {
        this.data = data;
        return this;
    }

    public <T> T getData(Class<T> cls) {
        return (T) data;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public StdResult setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public StdResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
