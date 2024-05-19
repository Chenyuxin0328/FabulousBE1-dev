package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.*;
import com.creatorsn.fabulous.entity.*;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.service.AcademicService;
import com.creatorsn.fabulous.service.ConfigService;
import com.creatorsn.fabulous.service.NoteBookService;
import com.creatorsn.fabulous.util.RegexPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author minskiter
 * @date 22/8/2023 21:32
 * @description
 */
@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "AcademicController", description = "文献接口")
@Validated
public class AcademicController extends ControllerBase {

    private final AcademicService academicService;

    private final NoteBookService noteBookService;

    private final ConfigService configService;
    private final Logger logger = LoggerFactory.getLogger(AcademicController.class);

    public AcademicController(
            ModelMapper modelMapper,
            AcademicService academicService,
            NoteBookService noteBookService,
            ConfigService configService) {
        super(modelMapper);
        this.academicService = academicService;
        this.noteBookService = noteBookService;
        this.configService = configService;
    }

    /**
     * 获取数据源的信息
     *
     * @param uri 数据源的Id
     * @return 返回数据源的信息
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取数据源的信息")
    @GetMapping("/sources/{uri}/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult getDataSourceInfo(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri)
            throws UserException {
        var source = configService.getDataStruct(uri);
        if (source == null)
            return badRequest();
        return ok(source);
    }

    /**
     * 获取根目录下的所有区组信息
     *
     * @param uri sourceId
     * @return 返回区间信息
     */
    @Operation(summary = "获取根目录下的所有区组信息")
    @GetMapping("/sources/{uri}/groups")
    @PreAuthorize("isAuthenticated()")
    public StdResult getRootGroups(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri)
            throws UserException {
        var groups = academicService.getDataGroupBySourceId(uri);
        return ok(groups);
    }

    /**
     * 获取指定区组下的所有区组信息
     *
     * @param uri sourceId
     * @param id  groupId
     * @return 返回所有的区组信息
     */
    @Operation(summary = "获取指定区组下的所有区组信息")
    @GetMapping("/sources/{uri}/groups/{id}/subgroups")
    @PreAuthorize("isAuthenticated()")
    public StdResult getGroups(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分区的Id格式错误") @Valid String id)
            throws UserException {
        var groups = academicService.getDataGroupByParentId(id);
        var filters = groups.stream().filter(e -> Objects.equals(e.getSourceId(), uri)).toList();
        return ok(filters);
    }

    /**
     * 创建区组
     *
     * @param uri   sourceId
     * @param id    父groupId
     * @param group 创建的区组传输对象
     * @return 返回创建后的区组对象
     */
    @Operation(summary = "创建区组")
    @PostMapping("/sources/{uri}/groups/{id}/subgroups")
    @PreAuthorize("isAuthenticated()")
    public StdResult createGroup(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分组的Id格式错误") @Valid String id,
            @RequestBody @Valid GroupCreateDTO group) throws UserException {
        var groupModel = modelMapper.map(group, DataGroup.class)
                .setSourceId(uri)
                .setParent(id)
                .setOwner(getUserId());
        groupModel = academicService.createGroup(groupModel);
        if (groupModel == null)
            return badRequest();
        return ok(groupModel);
    }

    /**
     * 更新区组
     *
     * @param uri   sourceId
     * @param group 区组更新对象
     * @return 返回区组的更新对象
     */
    @Operation(summary = "更新区组")
    @PutMapping("/sources/{uri}/groups")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateGroup(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody @Valid GroupUpdateDTO group) throws UserException {
        var groupModel = modelMapper.map(group, DataGroup.class)
                .setSourceId(uri)
                .setOwner(getUserId());
        groupModel = academicService.updateGroup(groupModel);
        if (groupModel == null)
            return badRequest();
        return ok(groupModel);
    }

    /**
     * 删除区组
     *
     * @param uri sourceId
     * @param id  区组的Id
     * @return 如果成功返回Ok
     */
    @Operation(summary = "删除区组")
    @DeleteMapping("/sources/{uri}/groups/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult deleteGroup(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分组的Id格式错误") @Valid String id)
            throws UserException {
        if (!academicService.deleteGroup(uri, id, getUserId()))
            return badRequest();
        return ok();
    }

    /**
     * 获取根目录下的所有分区信息
     *
     * @param uri sourceId
     * @return 返回对应的分区信息
     */
    @Operation(summary = "获取根目录下的所有分区信息")
    @GetMapping("/sources/{uri}/partitions")
    @PreAuthorize("isAuthenticated()")
    public StdResult getRootPartitions(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri)
            throws UserException {
        var partitions = academicService.getPartitionsBySourceId(uri);
        return ok(partitions);
    }

    /**
     * 获取指定区组下的所有分区信息
     *
     * @param uri sourceId
     * @param id  父分区的Id
     * @return 返回指定分组下的所有分区信息
     */
    @Operation(summary = "获取指定区组下的所有分区信息")
    @GetMapping("/sources/{uri}/groups/{id}/subpartitions")
    @PreAuthorize("isAuthenticated()")
    public StdResult getPartitions(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分组的Id格式错误") @Valid String id)
            throws UserException {
        var partitions = academicService.getPartitionsByParentId(id);
        var filters = partitions.stream().filter(e -> e.getSourceId().equals(uri)).toList();
        return ok(filters);
    }

    /**
     * 获取指定分区信息
     *
     * @param uri sourceId
     * @param id  指定分区的Id
     * @return 返回指定分区的信息
     */
    @Operation(summary = "获取指定分区信息")
    @GetMapping("/sources/{uri}/partitions/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getPartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分区的Id格式错误") @Valid String id)
            throws UserException {
        var partition = academicService.getPartitionById(uri, id);
        return ok(partition);
    }

    /**
     * 创建分区
     *
     * @param uri       sourceId
     * @param groupId   分组的Id
     * @param partition 要创建的分区
     * @return 返回要创建分区
     */
    @Operation(summary = "创建分区")
    @PostMapping("/sources/{uri}/groups/{groupId}/partitions")
    @PreAuthorize("isAuthenticated()")
    public StdResult createPartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分组的Id格式错误") @Valid String groupId,
            @RequestBody @Valid PartitionCreateDTO partition) throws UserException {
        var partitionModel = modelMapper.map(partition, DataPartition.class)
                .setSourceId(uri)
                .setParent(groupId)
                .setOwner(getUserId());
        partitionModel = academicService.createPartition(partitionModel);
        if (partitionModel == null)
            return badRequest();
        return ok(partitionModel);
    }

    /**
     * 更新分区
     *
     * @param uri       sourceId
     * @param groupId   分组的Id
     * @param partition 要更新的分区
     * @return 返回更新的分区
     */
    @Operation(summary = "更新分区")
    @PutMapping("/sources/{uri}/groups/{groupId}/partitions")
    @PreAuthorize("isAuthenticated()")
    public StdResult updatePartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分组的Id格式错误") @Valid String groupId,
            @RequestBody @Valid PartitionUpdateDTO partition) throws UserException {
        var partitionModel = modelMapper.map(partition, DataPartition.class)
                .setOwner(getUserId())
                .setParent(groupId)
                .setSourceId(uri);
        partitionModel = academicService.updatePartition(partitionModel);
        if (partitionModel == null)
            return badRequest();
        return ok(partitionModel);
    }

    /**
     * 删除分区
     *
     * @param uri     sourceId
     * @param groupId 分组的Id
     * @param id      分区的Id
     * @return 如果删除成功则返回Ok
     */
    @Operation(summary = "删除分区")
    @DeleteMapping("/sources/{uri}/groups/{groupId}/partitions/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult deletePartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分组的Id格式错误") @Valid String groupId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据分区的Id格式错误") @Valid String id)
            throws UserException {
        // TODO： 用户权限检测
        if (!academicService.deletePartition(uri, groupId, id)) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 获取指定分区下的所有数据项信息
     *
     * @param uri         sourceId
     * @param partitionId 分区的Id
     * @param length      长度
     * @param offset      偏移量
     * @param sort        排序
     * @param desc        是否降序
     * @return 返回指定分区下的所有数据项
     */
    @Operation(summary = "获取指定分区下的所有数据项信息")
    @GetMapping("/sources/{uri}/partitions/{partitionId}/items")
    @PreAuthorize("isAuthenticated()")
    public StdResult getItems(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分区id格式错误") @Valid String partitionId,
            @RequestParam(defaultValue = "20") @Max(value = 10000, message = "长度不能大于10000") @Valid int length,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "数据偏移量不能小于1") @Valid long offset,
            @RequestParam(defaultValue = "id") @Pattern(regexp = RegexPattern.ItemSortKey, message = "排序关键字不正确") @Valid String sort,
            @RequestParam(defaultValue = "false") @Valid boolean desc) throws UserException {
        length = (length <= 0 ? 10000 : length);
        if (partitionId.equals(uri)) {
            var items = academicService.listItemsBySource(
                    uri,
                    offset,
                    length,
                    sort,
                    desc);
            return ok(items);
        }
        var items = academicService.listItemsByParent(
                partitionId,
                offset,
                length,
                sort,
                desc);
        return ok(items);
    }

    /**
     * 获取指定分区下的所有数据项数量
     *
     * @param uri         sourceId
     * @param partitionId 分区的Id
     * @return 返回指定分区下的所有数据项的数量
     */
    @Operation(summary = "获取指定分区下的所有数据项数量")
    @GetMapping("/sources/{uri}/partitions/{partitionId}/items/count")
    @PreAuthorize("isAuthenticated()")
    public StdResult getItemsCount(
            @PathVariable String uri,
            @PathVariable String partitionId) throws UserException {
        if (partitionId.equals(uri)) {
            return ok(academicService.getItemsCountBySource(uri));
        }
        return ok(academicService.getItemsCountByParent(partitionId));
    }

    /**
     * 获取当前数据源下的所有数据项信息
     *
     * @param uri    数据源的Id
     * @param length 数据项的长度
     * @param offset 偏移量
     * @param sort   排序的关键字
     * @param desc   是否降序
     * @return 返回当前数据项下的所有数据项的信息
     */
    @Operation(summary = "获取当前数据源下的所有数据项信息")
    @GetMapping("/sources/{uri}/items")
    @PreAuthorize("isAuthenticated()")
    public StdResult getAllItems(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的格式错误") @Valid String uri,
            @RequestParam(defaultValue = "-1") @Max(value = 10000, message = "长度最大不能超过10000") @Valid int length,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "偏移量不能小于0") @Valid long offset,
            @RequestParam(defaultValue = "id") @Pattern(regexp = RegexPattern.ItemSortKey, message = "关键字格式错误") @Valid String sort,
            @RequestParam(defaultValue = "false") boolean desc) throws UserException {
        length = length <= 0 ? 10000 : length;
        var items = academicService.listItemsBySource(uri, offset, length, sort, desc);
        return ok(items);
    }

    /**
     * 获取当前数据源下的所有数据项数量
     *
     * @param uri sourceId
     * @return 返回当前数据源下的所有数据项的数量
     */
    @Operation(summary = "获取当前数据源下的所有数据项数量")
    @GetMapping("/sources/{uri}/items/count")
    @PreAuthorize("isAuthenticated()")
    public StdResult getAllItemsCount(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id错误") @Valid String uri)
            throws UserException {
        return ok(academicService.getDataGroupBySourceId(uri));
    }

    /**
     * 搜索获取数据项信息
     *
     * @param uri         sourceId
     * @param partitionId 分组的Id
     * @param keyword     关键字
     * @param length      长度
     * @param offset      偏移量
     * @param sort        排序的关键字
     * @param desc        是否降序
     * @return 返回符合条件的数据项信息
     */
    @Operation(summary = "搜索获取数据项信息")
    @GetMapping("/sources/{uri}/partitions/items/matches")
    @PreAuthorize("isAuthenticated()")
    public StdResult getSearchItems(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestParam(required = false) @Pattern(regexp = RegexPattern.GUID, message = "数据源的分区格式错误") @Valid String partitionId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") @Max(value = 10000, message = "长度不能超过10000") @Valid int length,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "偏移量不能小于0") @Valid long offset,
            @RequestParam(defaultValue = "id") @Pattern(regexp = RegexPattern.ItemSortKey, message = "排序关键字错误") @Valid String sort,
            @RequestParam(defaultValue = "false") @Valid boolean desc) {
        length = (length <= 0 ? 10000 : length);
        var items = academicService.queryItems(uri, partitionId, keyword, offset, length, sort, desc);
        return ok(items);
    }

    /**
     * 根据数据项id获取指定数据项信息
     *
     * @param uri sourceId
     * @param id  数据项的Id
     * @return 返回指定的数据项信息
     */
    @Operation(summary = "根据数据项id获取指定数据项信息")
    @GetMapping("/sources/{uri}/items/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getItem(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项Id格式错误") @Valid String id) {
        var item = academicService.getItem(id);
        return ok(item);
    }

    /**
     * 创建数据项
     *
     * @param uri  sourceId
     * @param item 数据项
     * @return 返回创建的数据项
     */
    @Operation(summary = "创建数据项")
    @PostMapping("/sources/{uri}/items")
    @PreAuthorize("isAuthenticated()")
    public StdResult createItem(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody @Valid DataItemCreateDTO item) throws UserException {
        var itemEntity = modelMapper.map(item, DataItem.class);
        itemEntity.setSourceId(uri);
        itemEntity.setOwner(getUserId());
        itemEntity = academicService.createItem(itemEntity);
        if (itemEntity == null)
            return badRequest();
        return ok(itemEntity);
    }

    /**
     * 更新数据项
     *
     * @param uri  sourceId
     * @param item 数据项
     * @return 返回更新的数据项
     */
    @Operation(summary = "更新数据项")
    @PutMapping("/sources/{uri}/items")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateItem(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody @Valid DataItemUpdateDTO item) throws UserException {
        var itemEntity = modelMapper.map(item, DataItem.class);
        itemEntity.setSourceId(uri);
        itemEntity.setOwner(getUserId());
        itemEntity = academicService.updateItem(itemEntity);
        if (itemEntity == null)
            return badRequest();
        return ok(itemEntity);
    }

    /**
     * 删除数据项
     *
     * @param uri sourceId
     * @param id  数据项的Id
     * @return 如果成功删除数据项返回Ok
     */
    @Operation(summary = "删除数据项")
    @DeleteMapping("/sources/{uri}/items/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult deleteItem(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String id)
            throws UserException {
        if (!academicService.deleteItem(id)) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 删除多个数据项
     *
     * @param uri   sourceId
     * @param items 数据项的Id，以，间隔
     * @return 返回被删除的数据项的Id
     */
    @Operation(summary = "删除多个数据项")
    @PostMapping("/sources/{uri}/items/batched/deleted")
    @PreAuthorize("isAuthenticated()")
    public StdResult deleteItems(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody List<@Pattern(regexp = RegexPattern.GUID, message = "数据项Id列表格式错误") @Valid String> items) {
        items.stream().filter(Objects::nonNull).toList();
        items = academicService.deleteItemList(uri, items);
        return ok(items);
    }

    /**
     * 将数据项添加到指定分区
     *
     * @param uri         sourceId
     * @param partitionId 分区的Id
     * @param items       数据项Id，以，间隔
     * @return 返回成功添加的分区
     */
    @Operation(summary = "将数据项添加到指定分区")
    @PostMapping(value = "/sources/{uri}/partitions/{partitionId}/items/batched")
    @PreAuthorize("isAuthenticated()")
    public StdResult addItemsToPartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分区的Id格式错误") @Valid String partitionId,
            @RequestBody List<@Pattern(regexp = RegexPattern.GUID, message = "数据项Id列表格式错误") @Valid String> items) {
        items = items.stream().filter(Objects::nonNull).toList();
        items = academicService.addListToPartition(items, partitionId, getUserId());
        return ok(items);
    }

    /**
     * 更新数据项的元信息
     *
     * @param uri      数据源的Id
     * @param itemId   数据项的Id
     * @param metadata 元信息
     * @return 返回更新后的数据项的元信息
     */
    @Operation(summary = "更新数据项的元信息")
    @PutMapping("/sources/{uri}/items/{itemId}/metadata")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateItemMetadata(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id") @Valid String itemId,
            @RequestBody @Valid MetadataUpdateDTO metadata) throws UserException {
        var metadataEntity = modelMapper.map(metadata, ItemMetadata.class);
        metadataEntity = academicService.updateItemMetadata(uri, itemId, metadataEntity);
        if (metadataEntity == null)
            return badRequest();
        return ok(metadataEntity);
    }

    /**
     * 从分区移除数据项
     *
     * @param uri         sourceId
     * @param partitionId 分区的Id
     * @param items       数据项的Id，以，间隔
     * @return 返回成功删除的数据项Id
     */
    @Operation(summary = "从分区移除数据项")
    @PostMapping(value = "/sources/{uri}/partitions/{partitionId}/items/batched/deleted")
    @PreAuthorize("isAuthenticated()")
    public StdResult removeItemsFromPartition(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "分区的Id格式错误") @Valid String partitionId,
            @RequestBody List<@Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String> items) {
        items = items.stream().filter(Objects::nonNull).toList();
        items = academicService.removeListFromPartition(items, partitionId);
        return ok(items);
    }

    /**
     * 创建数据项的笔记
     *
     * @param uri    source的Id
     * @param itemId 数据项的Id
     * @param page   创建数据笔记的传输对象
     * @return 返回被创建的传输对象
     */
    @Operation(summary = "创建数据项的笔记")
    @PostMapping("/sources/{uri}/items/{itemId}/pages")
    @PreAuthorize("isAuthenticated()")
    public StdResult createItemPage(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @RequestBody @Valid DataPageCreateDTO page) throws UserException {
        var pageEntity = modelMapper.map(page, DataPage.class);
        pageEntity.setParent(itemId);
        pageEntity.setOwner(getUserId());
        pageEntity = academicService.createItemPage(uri, pageEntity);
        if (pageEntity == null)
            return badRequest();
        return ok(pageEntity);
    }

    /**
     * 更新数据项笔记信息
     *
     * @param uri    source的Id
     * @param itemId 数据项的Id
     * @param page   要更新的笔记传输对象
     * @return 返回更新后的传输对象
     */
    @Operation(summary = "更新数据项笔记信息")
    @PutMapping("/sources/{uri}/items/{itemId}/pages")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateItemPage(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @RequestBody @Valid DataPageUpdateDTO page) throws UserException {
        var pageEntity = modelMapper.map(page, DataPage.class);
        pageEntity.setParent(itemId);
        pageEntity.setOwner(getUserId());
        pageEntity = academicService.updateItemPage(uri, pageEntity);
        if (pageEntity == null)
            return badRequest();
        return ok(pageEntity);
    }

    /**
     * 删除数据项笔记
     *
     * @param uri    sourceId
     * @param itemId 数据项的Id
     * @param pageId 笔记的Id
     * @return 如果成功则返回Ok
     */
    @Operation(summary = "删除数据项笔记")
    @DeleteMapping("/sources/{uri}/items/{itemId}/pages/{pageId}")
    @PreAuthorize("isAuthenticated()")
    public StdResult deleteItemPage(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据笔记的Id格式错误") @Valid String pageId)
            throws UserException {
        if (!academicService.deleteItemPage(uri, pageId)) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 复制数据项的笔记
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param pageId 数据笔记的Id
     * @return 返回新复制后的数据项
     */
    @Operation(summary = "复制数据项笔记")
    @PostMapping("/sources/{uri}/items/{itemId}/pages/{pageId}/duplicate")
    @PreAuthorize("isAuthenticated()")
    public StdResult duplicateItemPage(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的笔记的Id格式错误") @Valid String pageId)
            throws UserException {
        var page = academicService.duplicateItemPage(uri, pageId);
        if (!page.getParent().equals(itemId))
            return badRequest().setCode(StatusCode.PermissionDeny);
        return ok(page);
    }

    /**
     * 获取对应的笔记数据的最新内容
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param pageId 数据笔记的Id
     * @return 返回对应数据笔记内容
     */
    @Operation(summary = "获取笔记的内容")
    @GetMapping("/sources/{uri}/items/{itemId}/pages/{pageId}/content")
    @PreAuthorize("isAuthenticated()")
    public StdResult getItemPageContent(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的笔记格式错误") @Valid String pageId)
            throws UserException {
        var content = academicService.getItemPageContent(uri, pageId);
        if (content == null)
            return badRequest();
        return ok(content);
    }

    /**
     * 获取笔记指定版本的内容
     *
     * @param uri       数据源的Id
     * @param itemId    数据项的Id
     * @param pageId    数据项笔记的Id
     * @param versionId 数据项笔记版本的Id
     * @return 返回对应的版本内容
     */
    @Operation(summary = "获取笔记的指定版本的内容")
    @GetMapping("/sources/{uri}/items/{itemId}/pages/{pageId}/versions/{versionId}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getItemPageContentByVersionId(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的笔记Id格式错误") @Valid String pageId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项笔记的版本Id格式错误") @Valid String versionId)
            throws UserException {
        var content = academicService.getItemPageContentByVersionId(uri, pageId, versionId);
        if (content == null)
            return badRequest();
        return ok(content);
    }

    /**
     * 获取文献笔记的历史
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param pageId 笔记的Id
     * @return 返回文献笔记的历史版本信息
     */
    @Operation(summary = "获取文献笔记的历史")
    @GetMapping("/sources/{uri}/items/{itemId}/pages/{pageId}/versions")
    @PreAuthorize("isAuthenticated()")
    public StdResult listItemPageVersions(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项笔记的Id格式错误") @Valid String pageId)
            throws UserException {
        var versions = academicService.getItemPageContentVersions(uri, pageId);
        return ok(versions);
    }

    /**
     * 保存对应的数据笔记
     *
     * @param uri       数据源的Id
     * @param itemId    数据项的Id
     * @param pageId    数据笔记的Id
     * @param versionId 数据版本
     * @param content   笔记内容
     * @return 返回保存后的数据笔记内容
     */
    @Operation(summary = "保存笔记的内容")
    @PostMapping(value = "/sources/{uri}/items/{itemId}/pages/{pageId}/content/versions/{versionId}")
    @PreAuthorize("isAuthenticated()")
    public StdResult saveItemPageContent(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项笔记的Id格式错误") @Valid String pageId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项笔记的版本格式错误") @Valid String versionId,
            @RequestBody String content) throws UserException {
        var ctx = new DataPageContent();
        ctx.setAuthor(getUserId());
        ctx.setPageId(pageId);
        ctx.setContent(content);
        ctx.setVersionId(versionId);
        ctx = academicService.updateItemPageContent(uri, ctx);
        if (ctx == null)
            return badRequest();
        return ok(ctx);
    }

    /**
     * 保存数据项的Pdf文件
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param pdfId  PDF的Id
     * @param file   数据项的文件
     * @return 如果成功则返回Pdf的Id
     */
    @Operation(summary = "保存Pdf文件")
    @PostMapping(value = "/sources/{uri}/items/{itemId}/pdfs", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateItemPDF(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @RequestPart(required = false) @Pattern(regexp = RegexPattern.GUID, message = "PDF的Id格式错误") @Valid String pdfId,
            @RequestPart MultipartFile file) throws UserException {
        pdfId = academicService.updateItemPDF(uri, itemId, pdfId, file);
        if (pdfId == null)
            return badRequest();
        return ok(pdfId);
    }

    /**
     * 获取数据项的Pdf文件
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param pdfId  数据项Pdf的Id
     * @return 返回对应的数据项的Pdf文件
     */
    @Operation(summary = "获取数据项的Pdf文件")
    @GetMapping(value = "/sources/{uri}/items/{itemId}/pdfs/{pdfId}", produces = MediaType.APPLICATION_PDF_VALUE)
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> getItemPDF(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUIDPDF, message = "PDF的Id格式错误") @NotNull(message = "pdf的Id不能为空") @Valid String pdfId)
            throws UserException, IOException {
        if (pdfId.endsWith(".pdf"))
            pdfId = pdfId.replace(".pdf", "");
        var file = academicService.getItemPDF(uri, itemId, pdfId);
        if (file == null) {
            return ResponseEntity.badRequest().build();
        }
        HttpHeaders headers = new HttpHeaders();
        var contentDisposition = ContentDisposition
                .inline()
                .filename(file.getName() + ".pdf")
                .build().toString();
        var resource = new InputStreamResource(file.getInputStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .headers(headers)
                .contentType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())))
                .body(resource);
    }

    /**
     * 获取模版的信息
     *
     * @param uri 数据源的Id
     * @return 返回模版的信息
     */
    @Operation(summary = "获取模版的信息")
    @GetMapping("/sources/{uri}/templates")
    @PreAuthorize("isAuthenticated()")
    public StdResult getTemplateInfo(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri)
            throws UserException {
        var templates = academicService.getTemplates(uri);
        return ok(templates);
    }

    /**
     * 获取模版的内容
     *
     * @param uri 数据源的Id
     * @param id  模版的Id
     * @return 返回模版的内容
     */
    @Operation(summary = "获取模版的内容")
    @GetMapping("/sources/{uri}/templates/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getTemplateContent(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "模版的Id格式错误") @Valid String id)
            throws UserException {
        var templateContent = academicService.getLatestTemplateContent(uri, id);
        return ok(templateContent);
    }

    /**
     * 创建模版
     *
     * @param uri      数据源的Id
     * @param template 模版
     * @return 返回创建后的模版
     */
    @Operation(summary = "创建模版")
    @PostMapping("/sources/{uri}/templates")
    @PreAuthorize("isAuthenticated()")
    public StdResult createTemplate(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody @Valid DataTemplateCreateDTO template) throws UserException {
        var templateEntity = modelMapper.map(template, DataTemplate.class);
        templateEntity.setOwner(getUserId());
        templateEntity.setParent(uri);
        templateEntity = academicService.createTemplate(templateEntity);
        if (templateEntity == null)
            return badRequest();
        return ok(templateEntity);
    }

    /**
     * 更新模版
     *
     * @param uri      数据源的Id
     * @param template 模版
     * @return 返回更新后的模版
     */
    @Operation(summary = "更新模版")
    @PutMapping("/sources/{uri}/templates")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateTemplate(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @RequestBody @Valid DataTemplateUpdateDTO template) throws UserException {
        var templateEntity = modelMapper.map(template, DataTemplate.class);
        templateEntity.setParent(uri);
        templateEntity.setOwner(getUserId());
        templateEntity = academicService.updateTemplate(templateEntity);
        if (templateEntity == null)
            return badRequest();
        return ok(templateEntity);
    }

    /**
     * 删除模版
     *
     * @param uri 数据源的Id
     * @param id  模版的Id
     * @return 如果成功则返回Ok，否则返回BadRequest
     */
    @Operation(summary = "删除模版")
    @DeleteMapping("/sources/{uri}/templates/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult deleteTemplate(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "模版的Id") @Valid String id)
            throws UserException {
        if (!academicService.deleteTemplate(uri, id)) {
            return badRequest();
        }
        return ok();
    }

    /**
     * 保存模版的内容
     *
     * @param uri       数据源的Id
     * @param id        模版的Id
     * @param versionId 版本的Id
     * @param content   内容
     * @return 返回新的保存模版的内容，如果失败则返回旧的内容
     */
    @Operation(summary = "保存模版的内容")
    @PostMapping(value = "/sources/{uri}/templates/{id}/versions/{versionId}")
    @PreAuthorize("isAuthenticated()")
    public StdResult saveTemplateContent(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "模版的Id格式错误") @Valid String id,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "版本Id格式错误") @Valid String versionId,
            @RequestBody String content) throws UserException {
        var contentEntity = academicService.updateTemplateContent(
                uri, id, versionId, getUserId(), content);
        if (contentEntity == null)
            return badRequest();
        return ok(contentEntity);
    }

    /**
     * TODO: 获取文件的临时链接
     *
     * @param uri    数据源的Id
     * @param itemId 数据项的Id
     * @param fileId PDF文件的Id
     * @return 返回临时链接
     */
    @Operation(summary = "获取数据源的临时链接")
    @GetMapping("/sources/{uri}/items/{itemId}/pdfs/{fileId}/link")
    @PreAuthorize("isAuthenticated()")
    public StdResult openItemFile(
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据源的Id格式错误") @Valid String uri,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误") @Valid String itemId,
            @PathVariable @Pattern(regexp = RegexPattern.GUID, message = "文件的Id格式错误") @Valid String fileId) {
        return ok("/sources/" + uri + "/items/" + itemId + "/pdfs/" + fileId + ".pdf");
    }
}
