package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.DataStruct;
import com.creatorsn.fabulous.entity.WikiConfig;
import com.creatorsn.fabulous.exception.UserException;
import org.springframework.stereotype.Service;

/**
 * 配置服务
 */
@Service
public interface ConfigService {

    /**
     * 创建和更新配置
     *
     * @param config 配置
     * @return 返回Wiki配置
     * @throws UserException 用户异常
     */
    WikiConfig createOrUpdateConfig(WikiConfig config) throws UserException;

    /**
     * 更细腻配置
     *
     * @param config 配置
     * @return 返回Wiki配置
     * @throws UserException 用户异常
     */
    WikiConfig updateConfig(WikiConfig config) throws UserException;

    /**
     * 获取配置
     *
     * @param userId 用户
     * @return 返回指定用户的配置
     * @throws UserException 用户异常
     */
    WikiConfig getConfig(String userId) throws UserException;

    /**
     * 获取数据源
     *
     * @param id 数据源的Id
     * @return 获取指定的数据源
     * @throws UserException 用户异常
     */
    DataStruct getDataStruct(String id) throws UserException;

    /**
     * 创建数据源
     *
     * @param dataStruct 数据源
     * @return 返回创建后的数据源
     * @throws UserException 用户异常
     */
    DataStruct createDataStruct(DataStruct dataStruct) throws UserException;

    /**
     * 更新数据源的信息
     *
     * @param dataStruct 数据源
     * @return 返回更新后的数据源
     * @throws UserException 用户异常
     */
    DataStruct updateDataStruct(DataStruct dataStruct) throws UserException;

    /**
     * 删除数据源
     *
     * @param id     数据源的Id
     * @param userId 用户的Id
     * @return 删除指定的数据源
     * @throws UserException 用户异常
     */
    boolean deleteDataStruct(String id, String userId) throws UserException;
}
