package com.creatorsn.fabulous.utils;

import com.creatorsn.fabulous.util.FileManager;
import com.creatorsn.fabulous.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author minskiter
 * @date 25/8/2023 17:41
 * @description
 */
@DisplayName("Minio文件系统测试")
@SpringBootTest
public class MinioFileManagerTests {

    @Autowired
    FileManager fileManager;

    @Test
    void uploadTest() {
        var file = new FileManager.MultipartFileImpl(
                RandomUtil.DateTimeUUID(),
                "",
                "application/json",
                "{}".getBytes()
        );
        try {
            var fileId = fileManager.create(file);
            Assertions.assertNotNull(fileId);
        } catch (Exception ignored) {
            Assertions.assertNull(ignored);
        }
    }
}
