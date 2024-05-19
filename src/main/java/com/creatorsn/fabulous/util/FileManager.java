package com.creatorsn.fabulous.util;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author minskiter
 * @date 24/8/2023 21:34
 * @description 文件管理，负责文件的上传
 */
@Component
public interface FileManager {

    public static class MultipartFileImpl implements MultipartFile {

        /**
         * 文件名
         */
        private final String name;
        /**
         * 原始名称
         */
        private final String originalFilename;
        /**
         * 文件类型，MIME
         */
        @Nullable
        private String contentType;
        /**
         * 文件的内容
         */
        private final byte[] content;

        public MultipartFileImpl(String name, @Nullable byte[] content) {
            this(name, "", null, content);
        }

        public MultipartFileImpl(String name, InputStream stream) throws IOException {
            this(name, "", null, FileCopyUtils.copyToByteArray(stream));
        }

        public MultipartFileImpl(String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
            Assert.hasLength(name, "Name must not be empty");
            this.name = name;
            this.originalFilename = originalFilename != null ? originalFilename : "";
            this.contentType = contentType;
            this.content = content != null ? content : new byte[0];
        }

        public MultipartFileImpl(String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable InputStream stream) throws IOException {
            this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(stream));
        }

        /**
         * @return
         */
        @Override
        public String getName() {
            return this.name;
        }

        /**
         * @return
         */
        @Override
        public String getOriginalFilename() {
            return this.originalFilename;
        }

        public MultipartFileImpl setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * @return
         */
        @Override
        public String getContentType() {
            return this.contentType;
        }

        /**
         * @return
         */
        @Override
        public boolean isEmpty() {
            return this.content.length == 0;
        }

        /**
         * @return
         */
        @Override
        public long getSize() {
            return (long) this.content.length;
        }

        /**
         * @return
         * @throws IOException
         */
        @Override
        public byte[] getBytes() throws IOException {
            return this.content;
        }

        /**
         * @return
         * @throws IOException
         */
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.content);
        }

        /**
         * @param dest
         * @throws IOException
         * @throws IllegalStateException
         */
        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            FileCopyUtils.copy(this.content, dest);
        }
    }

    String create(MultipartFile file) throws IOException;

    boolean delete(String fileId);

    MultipartFile get(String fileId) throws IOException;

    MultipartFile get(String fileId, long offset, long length) throws IOException;

    String replace(String fileId, MultipartFile file) throws IOException;

}
