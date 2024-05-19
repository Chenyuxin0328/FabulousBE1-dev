package com.creatorsn.fabulous.util.impl;

import com.creatorsn.fabulous.util.FileManager;
import com.creatorsn.fabulous.util.RandomUtil;
import com.creatorsn.fabulous.util.configuration.MinioConfiguration;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author minskiter
 * @date 27/8/2023 17:42
 * @description
 */
@Configuration
public class MinioFileManagerImpl implements FileManager {


    private final MinioConfiguration configuration;

    private MinioClient minioClient;

    private Logger logger = LoggerFactory.getLogger(MinioFileManagerImpl.class);

    public MinioFileManagerImpl(MinioConfiguration minioConfiguration) {
        this.configuration = minioConfiguration;
    }

    private MinioClient getClientInstance() {
        if (this.minioClient == null) {
            this.minioClient = MinioClient.builder()
                    .endpoint(configuration.getHost(), configuration.getPort(), false)
                    .credentials(configuration.getAccessKey(), configuration.getSecretKey())
                    .build();
        }
        return this.minioClient;
    }

    /**
     * 判断Bucket是否存在
     *
     * @return 如果存在则返回true，否则返回false
     */
    private boolean checkBucket() {
        var client = getClientInstance();
        try {
            if (!client.bucketExists(BucketExistsArgs.builder()
                    .bucket(configuration.getBucket()).build())) {
                client.makeBucket(MakeBucketArgs.builder().bucket(configuration.getBucket()).build());
            }
            return true;
        } catch (
                Exception ex
        ) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }


    /**
     * @param file 文件
     * @return 返回文件的Guid
     * @throws IOException 文件读入异常
     */
    @Override
    public String create(MultipartFile file) throws IOException {
        if (!checkBucket()) return null;
        var client = getClientInstance();
        var builder = PutObjectArgs.builder();
        var fileId = RandomUtil.DateTimeUUID();
        builder.bucket(configuration.getBucket())
                .object(fileId)
                .contentType(file.getContentType())
                .stream(
                        file.getInputStream(),
                        file.getSize(),
                        -1
                );
        try {
            if (client.putObject(builder.build()) != null) {
                return fileId;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * @param fileId 文件的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    @Override
    public boolean delete(String fileId) {
        if (!checkBucket()) return false;
        var client = getClientInstance();
        var builder = RemoveObjectArgs.builder();
        builder.bucket(configuration.getBucket())
                .object(fileId);
        try {
            client.removeObject(builder.build());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    /**
     * @param fileId 文件的Id
     * @return 返回文件的类型
     * @throws IOException 文件读入异常
     */
    @Override
    public MultipartFile get(String fileId) throws IOException {
        if (!checkBucket()) return null;
        var client = getClientInstance();
        var builder = GetObjectArgs.builder();
        builder.bucket(configuration.getBucket())
                .object(fileId);
        try {
            var res = client.getObject(builder.build());
            var stat = client.statObject(StatObjectArgs.builder().bucket(configuration.getBucket()).object(fileId).build());
            return new MultipartFileImpl(
                    res.object(),
                    "",
                    stat.contentType(),
                    res
            );
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return null;
    }

    /**
     * @param fileId 文件的Id
     * @param offset 偏移量
     * @param length 长度
     * @return 返回的文件
     * @throws IOException 文件读入异常
     */
    @Override
    public MultipartFile get(String fileId, long offset, long length) throws IOException {
        if (!checkBucket()) return null;
        var client = getClientInstance();
        var builder = GetObjectArgs.builder();
        builder.bucket(configuration.getBucket())
                .object(fileId)
                .offset(offset)
                .length(length);
        try {
            var res = client.getObject(builder.build());
            var stat = client.statObject(StatObjectArgs.builder().bucket(configuration.getBucket()).object(fileId).build());
            return new MultipartFileImpl(
                    res.object(),
                    "",
                    stat.contentType(),
                    res
            );
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return null;
    }

    /**
     * @param fileId 文件的Id
     * @param file   文件
     * @return 返回文件的Id
     * @throws IOException 异常
     */
    @Override
    public String replace(String fileId, MultipartFile file) throws IOException {
        if (!checkBucket()) return null;
        var client = getClientInstance();
        var statBuilder = StatObjectArgs.builder()
                .bucket(configuration.getBucket())
                .object(fileId);
        try {
            var res = client.statObject(statBuilder.build());
            if (res == null) return null;
            var putRes = client.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucket())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileId).build());
            if (putRes != null) {
                return fileId;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
