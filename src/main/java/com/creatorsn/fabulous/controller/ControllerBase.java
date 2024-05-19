package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.dto.StdResult;
import com.creatorsn.fabulous.exception.UserException;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * @author minskiter
 * @date 22/8/2023 21:37
 * @description 控制器基类
 */
public class ControllerBase {

    protected ModelMapper modelMapper;

    public ControllerBase(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * 正确响应
     *
     * @param data 返回正确相应的数据
     * @return 返回正常响应的数据
     */
    protected StdResult ok(Object data) {
        return StdResult.Ok(data);
    }

    /**
     * 空正确响应
     *
     * @return 返回空正确响应的数据
     */
    protected StdResult ok() {
        return StdResult.Ok();
    }

    /**
     * 错误响应
     *
     * @param data 数据
     * @return 返回错误响应的数据
     */
    protected StdResult badRequest(Object data) throws UserException {
        throw new UserException(StatusCode.Fail, data);
    }

    /**
     * 空错误响应
     *
     * @return 返回空的错误响应
     */
    protected StdResult badRequest() throws UserException {
        throw new UserException(StatusCode.Fail);
    }

    /**
     * 获取用户名
     *
     * @return 返回对应的用户名
     */
    protected String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        if (authentication.getPrincipal() instanceof String) {
            return authentication.getPrincipal().toString();
        } else if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getUsername();
        }
        return "";
    }
}
