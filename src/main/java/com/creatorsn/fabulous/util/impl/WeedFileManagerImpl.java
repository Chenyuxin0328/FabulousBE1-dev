package com.creatorsn.fabulous.util.impl;

import com.creatorsn.fabulous.util.FileManager;
import com.creatorsn.fabulous.util.RandomUtil;
import com.creatorsn.fabulous.util.configuration.WeedFileConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import seaweedfs.client.FilerClient;
import seaweedfs.client.SeaweedInputStream;
import seaweedfs.client.SeaweedOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author minskiter
 * @date 24/8/2023 22:14
 * @description sea weed file 文件实现
 */
//@Component
public class WeedFileManagerImpl implements FileManager {

    private final FilerClient filerClient;

    private final Logger logger = LoggerFactory.getLogger(WeedFileManagerImpl.class);

    public WeedFileManagerImpl(WeedFileConfiguration configuration) {
        this.filerClient = new FilerClient(configuration.getHost(), configuration.getPort());
        logger.info("load weed file client");

    }

    /**
     * @param file 文件
     * @return 返回文件的唯一标识
     */
    @Override
    public String create(MultipartFile file) throws IOException {
        var uuid = RandomUtil.DateTimeUUID();
        SeaweedOutputStream seaweedOutputStream = new SeaweedOutputStream(filerClient, uuid);
        try {
            seaweedOutputStream.write(file.getBytes());
            seaweedOutputStream.flush();
        } catch (Exception ignored) {
            return null;
        } finally {
            seaweedOutputStream.close();
        }
        return uuid;
    }

    /**
     * @param fileId 文件名的Id
     * @return 如果成功删除则返回true，否则返回false
     */
    @Override
    public boolean delete(String fileId) {
        if (!filerClient.exists(fileId)) return false;
        return filerClient.rm(fileId, false, true);
    }

    /**
     * @param fileId 文件名的Id
     * @return 返回标准的文件
     */
    @Override
    public MultipartFile get(String fileId) throws IOException {
        if (!filerClient.exists(fileId)) return null;
        SeaweedInputStream seaweedInputStream = new SeaweedInputStream(filerClient, fileId);
        return new MultipartFileImpl(fileId, seaweedInputStream);
    }

    /**
     * @param fileId 文件的Id
     * @param offset 文件的偏移量
     * @param length 文件的长度
     * @return 返回文件对应的局部部分
     */
    @Override
    public MultipartFile get(String fileId, long offset, long length) throws IOException {
        if (!filerClient.exists(fileId)) return null;
        var inputStream = new SeaweedInputStream(filerClient, fileId);
        inputStream.skip(offset);
        byte[] buffer = new byte[1024]; // 缓冲区大小
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((bytesRead = inputStream.read(buffer, 0, (int) Math.min(length, buffer.length))) != -1) {
            outputStream.write(buffer, 0, (int) Math.min(bytesRead, length));
            length -= bytesRead;
            if (length <= 0) {
                break; // 已经读取足够的字节数
            }
        }
        return new MultipartFileImpl(fileId, new ByteArrayInputStream(outputStream.toByteArray()));
    }

    /**
     * @param fileId 文件的Id
     * @param file   文件
     * @return 返回文件的Id
     */
    @Override
    public String replace(String fileId, MultipartFile file) throws IOException {
        if (!filerClient.exists(fileId)) return null;
        SeaweedOutputStream seaweedOutputStream = new SeaweedOutputStream(filerClient, fileId);
        try {
            seaweedOutputStream.write(file.getBytes());
            seaweedOutputStream.flush();
        } catch (Exception ignored) {
            return null;
        } finally {
            seaweedOutputStream.close();
        }
        return fileId;
    }
}
