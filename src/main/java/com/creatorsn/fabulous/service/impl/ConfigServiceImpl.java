package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.DataStruct;
import com.creatorsn.fabulous.entity.WikiConfig;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.ConfigMapper;
import com.creatorsn.fabulous.mapper.DataGroupMapper;
import com.creatorsn.fabulous.mapper.DataStructMapper;
import com.creatorsn.fabulous.service.ConfigService;
import com.creatorsn.fabulous.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 配置服务实现类
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    final private ConfigMapper configMapper;

    final private DataGroupMapper dataGroupMapper;

    final private DataStructMapper dataStructMapper;

    final private Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    public ConfigServiceImpl(
            ConfigMapper configMapper,
            DataGroupMapper dataGroupMapper,
            DataStructMapper dataStructMapper
    ) {
        this.configMapper = configMapper;
        this.dataGroupMapper = dataGroupMapper;
        this.dataStructMapper = dataStructMapper;
    }

    /**
     * @param config 用户的配置文件
     * @return 返回用户创建的配置文件
     * @throws UserException 用户异常
     */
    @Override
    public WikiConfig createOrUpdateConfig(WikiConfig config) throws UserException {
        if (configMapper.existsByUserId(config.getUserId())) {
            return updateConfig(config);
        }
        config.setConfigId(RandomUtil.DateTimeUUID());
        return configMapper.create(config);
    }

    /**
     * @param config 用户配置文件
     * @return 返回更新后的用户配置文件
     * @throws UserException 用户异常
     */
    @Override
    public WikiConfig updateConfig(WikiConfig config) throws UserException {
        var old = configMapper.getConfigByUserId(config.getUserId());
        if (old == null) {
            throw new UserException(StatusCode.ConfigNotExists);
        }
        if (!Objects.equals(old.getConfigId(), config.getConfigId())) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        return configMapper.update(config);
    }

    /**
     * @param userId 用户的Id
     * @return 返回用户的配置文件
     * @throws UserException 用户异常
     */
    @Override
    public WikiConfig getConfig(String userId) throws UserException {
        // 获取所有的笔记本分组
        var config = configMapper.getConfigByUserId(userId);
        if (config != null) {
            config.setDataPath(dataStructMapper.getByUserId(userId));
        }
        return config;
    }

    /**
     * @param id 数据源的Id
     * @return 返回指定的数据源
     * @throws UserException 用户异常
     */
    @Override
    public DataStruct getDataStruct(String id) throws UserException {
        var dataStruct = dataStructMapper.getById(id);
        dataStruct.setPath(dataStruct.getId());
        return dataStruct;
    }

    /**
     * @param dataStruct 数据源
     * @return 返回创建后的数据源
     * @throws UserException 用户异常
     */
    @Override
    public DataStruct createDataStruct(DataStruct dataStruct) throws UserException {
        if (dataStructMapper.existsByName(dataStruct.getName(), dataStruct.getUserId())) {
            throw new UserException(StatusCode.DataStructNameRepeated);
        }
        // 判断名称是否重复
        dataStruct.setId(RandomUtil.DateTimeUUID());
        dataStruct.setPath(dataStruct.getId());
        return dataStructMapper.create(dataStruct);
    }

    /**
     * 判断数据是否存在
     *
     * @param id     数据源的Id
     * @param userId 用户的Id
     * @throws UserException 用户异常
     */
    private void existsDataStruct(String id, String userId) throws UserException {
        var source = getDataStruct(id);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        if (userId != null && !Objects.equals(source.getUserId(), userId))
            throw new UserException(StatusCode.PermissionDeny);
    }

    /**
     * @param dataStruct 数据源
     * @return 返回更新后的数据源
     * @throws UserException 用户异常
     */
    @Override
    public DataStruct updateDataStruct(DataStruct dataStruct) throws UserException {
        existsDataStruct(dataStruct.getId(), dataStruct.getUserId());
        if (StringUtils.hasText(dataStruct.getName())) {
            if (dataStructMapper.existsByName(dataStruct.getName(), dataStruct.getUserId())) {
                throw new UserException(StatusCode.DataStructNameRepeated);
            }
        }
        dataStruct = dataStructMapper.update(dataStruct);
        dataStruct.setPath(dataStruct.getId());
        return dataStruct;
    }

    /**
     * @param id     数据源的Id
     * @param userId 用户的Id
     * @return 如果删除成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    @Override
    public boolean deleteDataStruct(String id, String userId) throws UserException {
        existsDataStruct(id, userId);
        return dataStructMapper.delete(id);
    }
}
