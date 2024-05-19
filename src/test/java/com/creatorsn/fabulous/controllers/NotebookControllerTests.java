package com.creatorsn.fabulous.controllers;

import com.creatorsn.fabulous.controller.ConfigController;
import com.creatorsn.fabulous.controller.NotebookController;
import com.creatorsn.fabulous.dto.NotebookDTO;
import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.DataGroup;
import com.creatorsn.fabulous.entity.DataGroupItem;
import com.creatorsn.fabulous.entity.NoteBookContent;
import com.creatorsn.fabulous.entity.WikiConfig;
import com.creatorsn.fabulous.exception.UserException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockRestServiceServer
public class NotebookControllerTests {

    private final Logger logger = LoggerFactory.getLogger(NotebookControllerTests.class);

    @Autowired
    private NotebookController notebookController;

    @Autowired
    private ConfigController configController;

    private void info(Object obj) {
        var objectMapper = new ObjectMapper();
        logger.info(() -> {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private WikiConfig getConfig() throws UserException {
        var res = configController.getConfig();
        info(res);
        Assertions.assertNotNull(res);
        var config = res.getData(WikiConfig.class);
        Assertions.assertFalse(config.getDataPath().isEmpty());
        return config;
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void copyDirectoryTest() throws UserException {
        var config = getConfig();
        var source = config.getDataPath().get(0);
        var sourceId = source.getId();
        info(sourceId);
//        var res = notebookController.getDirectoryChildren("20230902-1203-0389-7F9F-B374C88FB4CA");
//        var groups = (List<DataGroupItem>) res.getData();
//        var firstGroups = groups.stream().findFirst();
//        if (firstGroups.isPresent()) {
//            var item = firstGroups.get();
//            info(item.getType());
        var oldPath = sourceId + "/" + "20230902-1635-1431-3148-9E8AF5CB0411";
        var newPath = sourceId + "/" + "20230902-1203-0389-7F9F-B374C88FB4CA";
        var res = notebookController.copyDirectory(sourceId, oldPath, newPath);
        Assertions.assertSame(res.getStatus(), StatusCode.success);
//        }
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void createDirectoryTest() throws UserException, ExecutionException, InterruptedException {
        var config = getConfig();
        var source = config.getDataPath().get(0);
        var sourceId = source.getId();
        var res = notebookController.createDirectory(sourceId, sourceId + "/newDirectory2");
        var dataGroup = res.getData(DataGroup.class);
        Assertions.assertNotNull(dataGroup);
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void listDirectoryTest() throws UserException {
        var config = getConfig();
        var source = config.getDataPath().stream().findFirst();
        if (source.isPresent()) {
            var sourceId = source.get().getId();
            var res = notebookController.getDirectoryChildren("20230902-1203-0389-7F9F-B374C88FB4CA");
            var dataGroupItems = (List<DataGroupItem>) res.getData();
            info(dataGroupItems);
            Assertions.assertFalse(dataGroupItems.isEmpty());
        }
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void moveDirectoryTest() throws UserException {
        var config = getConfig();
        var source = config.getDataPath().stream().findFirst();
        if (source.isPresent()) {
            var sourceId = source.get().getId();
            var res = notebookController.getDirectoryChildren("20230902-1203-0389-7F9F-B374C88FB4CA");
            var dataGroupItems = (List<DataGroupItem>) res.getData();
            info(dataGroupItems);
            if (dataGroupItems.size() >= 2) {
                var firstGroup = dataGroupItems.get(0);
                var secondGroup = dataGroupItems.get(1);
                var oldPath = sourceId + "/" + "20230902-1203-0389-7F9F-B374C88FB4CA" + "/" + firstGroup.getId();
                var newPath = sourceId + "/" + "20230902-1203-0389-7F9F-B374C88FB4CA" + "/" + secondGroup.getId();
                res = notebookController.moveDirectory(sourceId, oldPath, newPath);
                Assertions.assertEquals(StatusCode.success, res.getStatus());
                res = notebookController.getDirectoryChildren(secondGroup.getId());
                Assertions.assertEquals(StatusCode.success, res.getStatus());
                dataGroupItems = (List<DataGroupItem>) res.getData();
                dataGroupItems = dataGroupItems.stream().filter(e -> e.getId().equals(firstGroup.getId())).toList();
                Assertions.assertFalse(dataGroupItems.isEmpty());
            }
        }
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void createNoteBookTest() throws UserException {
        var config = getConfig();
        var source = config.getDataPath().stream().findFirst();
        if (source.isPresent()) {
            var sourceId = source.get().getId();
            var res = notebookController.createDocument(
                    sourceId,
                    sourceId + "/newNotebook.fbn",
                    "{\"doc\":{}}"
            );
            Assertions.assertNotNull(res);
            var notebook = res.getData(NotebookDTO.class);
            res = notebookController.getDocumentContentHistoryVersionIds(notebook.getId());
            Assertions.assertNotNull(res);
            var versions = (List<NoteBookContent>) res.getData();
            info(versions);
            Assertions.assertFalse(versions.isEmpty());
        }
    }

    @Test
    @WithMockUser(username = "0BD1CAEA-D4C5-44C0-8579-D6586C4A91BF", roles = {"user", "admin"})
    public void transferPathTest() throws UserException {
        var config = getConfig();
        var source = config.getDataPath().stream().findFirst();
        if (source.isPresent()) {
            var sourceId = source.get().getId();
            var res = notebookController.getDirectoryChildren(sourceId);
            Assertions.assertNotNull(res);
            var items = (List<DataGroupItem>) res.getData();
            Assertions.assertFalse(items.isEmpty());
            res = notebookController.transferIdsToNames(sourceId + "/" + items.get(0).getId());
            Assertions.assertNotNull(res);
            var path = res.getData(String.class);
            info(path);
            Assertions.assertNotNull(path);
        }
    }


}
