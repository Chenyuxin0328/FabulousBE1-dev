package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.GroupUpdateDTO;
import com.creatorsn.fabulous.dto.NotebookDTO;
import com.creatorsn.fabulous.dto.NotebookUpdateDTO;
import com.creatorsn.fabulous.dto.StdResult;
import com.creatorsn.fabulous.entity.DataGroup;
import com.creatorsn.fabulous.entity.NoteBookContent;
import com.creatorsn.fabulous.entity.Notebook;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.service.NoteBookService;
import com.creatorsn.fabulous.util.RegexPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * 笔记本接口
 */
@RestController
@Tag(name = "NotebookController", description = "文档接口")
@SecurityRequirement(name = "Authorization")
@Validated
public class NotebookController extends ControllerBase {

    final NoteBookService noteBookService;

    public NotebookController(
            ModelMapper modelMapper,
            NoteBookService noteBookService) {
        super(modelMapper);
        this.noteBookService = noteBookService;
    }

    /**
     * 创建笔记本
     *
     * @param uri      sourceId
     * @param content  笔记本的内容
     * @param filePath 笔记本的路径
     * @return 返回创建的笔记本
     * @throws UserException 用户异常
     */
    @Operation(summary = "创建笔记本")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sources/{uri}/documents")
    public StdResult createDocument(
            @Valid @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "uri格式错误，必须为sourceId") String uri,
            @Valid @RequestParam @Pattern(regexp = RegexPattern.PathWithName, message = "路径格式错误") String filePath,
            @Valid @RequestBody String content) throws UserException {
        var groupIds = filePath.split("/");
        if (groupIds.length < 2)
            return badRequest();
        var userId = getUserId();
        var notebook = new Notebook()
                .setOwner(userId)
                .setSourceId(uri)
                .setName(groupIds[groupIds.length - 1])
                .setParent(groupIds[groupIds.length - 2])
                .setContent(new NoteBookContent().setContent(content).setAuthor(userId));
        notebook = this.noteBookService.createNoteBook(notebook);
        if (notebook == null)
            return badRequest();
        var notebookDTO = modelMapper.map(notebook, NotebookDTO.class);
        notebookDTO.setContent(notebook.getContent().getContent());
        notebookDTO.setVersionId(notebook.getContent().getVersionId());
        return ok(notebookDTO);
    }

    /**
     * 更新笔记本的信息
     *
     * @param uri      数据源
     * @param filePath 文件的路径
     * @param info     笔记本的信息
     * @return 返回笔记本更新后的信息
     */
    @Operation(summary = "更新笔记本的信息")
    @PutMapping("/sources/{uri}/documents/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateDocumentInfo(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件的路径") @Valid String filePath,
            @RequestBody @Valid NotebookUpdateDTO info) throws UserException {
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        if (!ids[0].equals(uri))
            return badRequest();
        var notebook = modelMapper.map(info, Notebook.class);
        notebook.setId(ids[ids.length - 1]);
        notebook.setOwner(getUserId());
        notebook = noteBookService.updateNoteBook(notebook);
        if (notebook == null)
            return badRequest();
        return ok(notebook);
    }

    /**
     * 更新笔记本的内容
     *
     * @param uri       sourceId
     * @param filePath  笔记本的路径
     * @param content   笔记本的内容
     * @param versionId 版本的Id
     * @return 更新后的笔记本
     * @throws UserException 用户异常
     */
    @Operation(summary = "更新笔记本的内容")
    @PutMapping("/sources/{uri}/documents/content")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateDocument(
            @Valid @Pattern(regexp = RegexPattern.GUID, message = "id格式错误") @PathVariable String uri,
            @Valid @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "路径格式错误") String filePath,
            @Valid @RequestParam @Pattern(regexp = RegexPattern.GUID, message = "版本的Id格式错误") String versionId,
            @RequestBody String content) throws UserException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        var notebook = new Notebook()
                .setId(ids[ids.length - 1])
                .setSourceId(uri)
                .setContent(new NoteBookContent()
                        .setNotebookId(ids[ids.length - 1])
                        .setContent(content)
                        .setVersionId(versionId)
                        .setAuthor(userId));
        notebook = this.noteBookService.updateNotebookContent(notebook);
        if (notebook == null)
            return badRequest();
        var notebookDTO = modelMapper.map(notebook, NotebookDTO.class);
        notebookDTO.setContent(notebook.getContent().getContent());
        notebookDTO.setVersionId(notebook.getContent().getVersionId());
        return ok(notebookDTO);
    }

    /**
     * 删除笔记本
     *
     * @param uri      笔记本的Id
     * @param filePath 文件路径
     * @return 如果删除成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    @Operation(summary = "删除笔记本")
    @DeleteMapping("/sources/{uri}/documents")
    @PreAuthorize("isAuthenticated()")
    public StdResult removeDocument(
            @Valid @Pattern(regexp = RegexPattern.GUID, message = "URI 格式错误") @PathVariable String uri,
            @Valid @Pattern(regexp = RegexPattern.PATH, message = "路径格式错误") @RequestParam String filePath)
            throws UserException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        if (!this.noteBookService.deleteNotebook(ids[ids.length - 1], userId)) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 获取笔记本
     *
     * @param uri      sourceId
     * @param filePath 笔记本的绝对路径
     * @return 返回获取到的笔记本
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取笔记本")
    @GetMapping("/sources/{uri}/documents")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDocument(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "URI格式必须为GUID格式") String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "绝对路径，最后一个Id必须是笔记本Id") @Valid String filePath)
            throws UserException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        var notebook = noteBookService.getNoteBook(ids[ids.length - 1], userId, true);
        if (notebook == null || !Objects.equals(notebook.getSourceId(), uri)) {
            return badRequest();
        }
        var notebookDTO = modelMapper.map(notebook, NotebookDTO.class);
        notebookDTO.setContent(notebook.getContent().getContent());
        notebookDTO.setVersionId(notebook.getContent().getVersionId());
        return ok(notebookDTO);
    }

    /**
     * 获取笔记本的历史版本列表
     *
     * @param id 笔记本的Id
     * @return 返回笔记本的历史版本号
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取笔记本内容所有历史版本号")
    @GetMapping("/documents/{id}/content/history/ids")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDocumentContentHistoryVersionIds(
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUID, message = "笔记本的Id格式错误") String id)
            throws UserException {
        var notebookVersionIds = noteBookService.listNotebookHistoryVersionIds(id);
        return ok(notebookVersionIds);
    }

    /**
     * 获取笔记本指定版本的内容
     *
     * @param notebookId 笔记本的Id
     * @param ids        版本号列表，用，间隔
     * @return 返回指定的id列表
     */
    @Operation(summary = "获取笔记本指定版本的内容")
    @GetMapping("/documents/{notebookId}/content/{ids}/history/")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDocumentContentHistoryByVersionIds(
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUID, message = "笔记本的Id格式错误") String notebookId,
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUIDList, message = "版本号格式错误，应该为{GUID},{GUID}") String ids) {
        var idsList = ids.split(",");
        return ok(noteBookService.listNotebookHistoryVersionByIds(notebookId, Arrays.stream(idsList).toList()));
    }

    /**
     * 获取笔记本的历史版本列表
     *
     * @param id            笔记本的Id
     * @param pageSize      页数
     * @param lastVersionId 上一页的最后一个版本号
     * @return 返回本页的历史版本列表
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取笔记本内容的历史")
    @GetMapping("/documents/{id}/content/history")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDocumentContentHistory(
            @PathVariable("id") @Valid @Pattern(regexp = RegexPattern.GUID, message = "笔记本的Id格式错误") String id,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") @Min(value = 1, message = "页数的大小不能小于1") @Max(value = 10000, message = "页数大小不能超过10000") @Valid Integer pageSize,
            @Pattern(regexp = RegexPattern.GUID, message = "最后的版本号") @RequestParam(value = "lastVersionId", required = false) @Valid String lastVersionId)
            throws UserException {
        var noteBookContents = noteBookService.listNotebookHistoryVersion(id, pageSize, lastVersionId);
        return ok(noteBookContents);
    }

    /**
     * 获取指定的目录信息
     *
     * @param id 目录的标识
     * @return 返回指定目录的信息
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取指定目录的信息")
    @GetMapping("/directories/{id}/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDirectory(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "id 的格式不正确") @Valid String id)
            throws UserException {
        var group = noteBookService.getGroup(id);
        if (group == null)
            return badRequest();
        return ok(group);
    }

    /**
     * 获取指定目录的子项
     *
     * @param id 目录的Id
     * @return 返回指定目录的子项
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取指定目录的所有子项")
    @GetMapping("/directories/{id}/children")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDirectoryChildren(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "id 的格式不正确") @Valid String id)
            throws UserException {
        var userId = getUserId();
        var items = noteBookService.listChildren(id, userId);
        return ok(items);
    }

    /**
     * 创建指定目录
     *
     * @param uri      SourceId
     * @param filePath 目录的路径
     * @return 返回创建后的目录实体
     * @throws UserException 用户异常
     */
    @Operation(summary = "创建指定的目录")
    @PostMapping("/sources/{uri}/directories")
    @PreAuthorize("isAuthenticated()")
    public StdResult createDirectory(
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUID, message = "URI的格式错误") String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PathWithName, message = "filePath格式错误，最后一个是文件夹的名称，格式：{GUID}/.../{name}") String filePath)
            throws UserException, ExecutionException, InterruptedException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        var group = new DataGroup()
                .setOwner(userId)
                .setParent(ids[ids.length - 2])
                .setSourceId(uri)
                .setName(ids[ids.length - 1]);
        group = noteBookService.createGroup(group);
        if (group == null)
            return badRequest();
        return ok(group);
    }

    /**
     * 更新指定的文件或者目录的信息
     *
     * @param uri      数据源的Id
     * @param filePath 分组的路径
     * @param info     目录更新的传输实体
     * @return 返回更新后的目录信息
     * @throws UserException 用户异常
     */
    @Operation(summary = "更新指定的目录信息")
    @PutMapping("/sources/{uri}/directories/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateDirectoryInfo(
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUID, message = "uri 的格式不正确") String uri,
            @RequestParam @Valid @Pattern(regexp = RegexPattern.PATH, message = "路径 格式不正确") String filePath,
            @RequestBody @Valid GroupUpdateDTO info) throws UserException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        var group = modelMapper.map(info, DataGroup.class)
                .setId(ids[ids.length - 1])
                .setSourceId(uri)
                .setOwner(userId)
                .setParent(ids[ids.length - 2]);
        group = noteBookService.updateGroup(userId, group);
        if (group == null)
            return badRequest();
        return ok(group);
    }

    /**
     * 删除指定的目录或笔记
     *
     * @param uri      source的Id
     * @param filePath 路径
     * @return 返回删除的指定目录
     * @throws UserException 用户异常
     */
    @Operation(summary = "删除指定的目录或笔记")
    @DeleteMapping("/sources/{uri}/directories")
    @PreAuthorize("isAuthenticated()")
    public StdResult removeDirectory(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "uri 的格式不正确") @Valid String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件的路径") @Valid String filePath)
            throws UserException {
        var userId = getUserId();
        var ids = filePath.split("/");
        if (ids.length < 2)
            return badRequest();
        if (!noteBookService.deleteGroup(userId, ids[ids.length - 1])) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 判断指定路径是否存在
     *
     * @param uri      sourceId
     * @param filePath 指定的路径
     * @return 返回指定的路径
     * @throws UserException 用户异常
     */
    @Operation(summary = "判断指定路径是否存在,路径以/间隔,格式例如: {GUID}/{GUID}/{GUID}")
    @GetMapping("/sources/{uri}/path")
    @PreAuthorize("isAuthenticated()")
    public StdResult existsPath(
            @PathVariable @Valid @Pattern(regexp = RegexPattern.GUID, message = "uri 格式错误") String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件路径格式错误") @Valid String filePath)
            throws UserException {
        // 确保以sourceId开始
        if (filePath.startsWith(uri))
            filePath = filePath.toUpperCase();
        else
            filePath = uri.toUpperCase() + "/" + filePath.toUpperCase();
        if (noteBookService.existsPath(filePath)) {
            return ok();
        }
        return badRequest();
    }

    /**
     * 移动目录或者文件
     *
     * @param uri      sourceId
     * @param filePath 文件的路径
     * @param newPath  新路径
     * @return 返回成功移动的目录或文件
     * @throws UserException 用户异常
     */
    @Operation(summary = "移动目录或文件")
    @PostMapping("/sources/{uri}/directories/move")
    @PreAuthorize("isAuthenticated()")
    public StdResult moveDirectory(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件的路径格式错误") @Valid String filePath,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件的路径格式错误") @Valid String newPath)
            throws UserException {
        if (noteBookService.moveDirectory(uri, filePath, newPath)) {
            return ok();
        }
        return badRequest();
    }

    /**
     * 复制目录或文件
     *
     * @param uri      sourceId
     * @param filePath 文件的路径
     * @param newPath  新路径
     * @return 返回成功复制的目录或文件
     * @throws UserException 用户异常
     */
    @Operation(summary = "复制目录或文件")
    @PostMapping("/sources/{uri}/directories/copy")
    @PreAuthorize("isAuthenticated()")
    public StdResult copyDirectory(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "文件的路径格式错误") @Valid String filePath,
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "新文件的路径格式错误") @Valid String newPath)
            throws UserException {
        if (noteBookService.copyDirectory(uri, filePath, newPath)) {
            return ok();
        }
        return badRequest();
    }

    /**
     * 获取路径对应的名称路径
     *
     * @param path 路径
     * @return 返回对应的名称路径
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取路径对应的名称路径")
    @GetMapping("/sources/notebooks/names")
    @PreAuthorize("isAuthenticated()")
    public StdResult transferIdsToNames(
            @RequestParam @Pattern(regexp = RegexPattern.PATH, message = "路径的格式不正确") @Valid String path)
            throws UserException {
        path = noteBookService.idsPathToNamePath(path);
        if (path == null)
            return badRequest();
        return ok(path);
    }


    @Operation(summary = "上传图片信息")
    @PostMapping("/sources/uploadImage")
//    @PreAuthorize("isAuthenticated()")
    public StdResult uploadImage(
            @Valid
            @RequestBody String base64Image) throws UserException {

        // 调用MinIO工具类上传图片
        String imageGuid = noteBookService.uploadBase64Image(base64Image);
        // 返回图片的GUID或路径等
        if (imageGuid == null) {
            return badRequest();
        } else {
            return ok(imageGuid);
        }
    }

}
