package com.creatorsn.fabulous.exception;

import com.creatorsn.fabulous.dto.StatusCode;

public class UserException extends Exception{

    final private StatusCode code;

    final private Object data;

    public StatusCode getCode() {
        return code;
    }

    public UserException(StatusCode code, Object data){
        super(code.name());
        this.code = code;
        this.data = data;
    }

    public UserException(StatusCode code) {
        this(code,null);
    }

    public Object getData() {
        return data;
    }
}
