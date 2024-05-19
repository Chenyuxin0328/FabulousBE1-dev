package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author minskiter
 * @date 24/8/2023 18:32
 * @description 分区更新传输对象
 */
@Schema(description = "分区更新传输对象")
public class PartitionUpdateDTO {

    @JsonProperty
    @Pattern(regexp = RegexPattern.GUID, message = "分区的Id格式错误")
    @NotNull(message = "分区的Id不能为空")
    private String id;


    /**
     * 分区的名称
     */
    @JsonProperty
    private String name;

    /**
     * 分区的图标
     */
    @JsonProperty
    private String emoji;

    public String getName() {
        return name;
    }

    public PartitionUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public PartitionUpdateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getId() {
        return id;
    }

    public PartitionUpdateDTO setId(String id) {
        this.id = id;
        return this;
    }
}
