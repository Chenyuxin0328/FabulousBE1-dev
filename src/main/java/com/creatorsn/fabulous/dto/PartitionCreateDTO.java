package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author minskiter
 * @date 24/8/2023 18:26
 * @description 分区创建传输对象
 */
@Schema(description = "分区创建传输对象")
public class PartitionCreateDTO {

    /**
     * 分区的名称
     */
    @JsonProperty
    @NotNull(message = "分区的名称不能为空")
    private String name;

    /**
     * 分区的图标
     */
    @JsonProperty
    private String emoji;

    public String getName() {
        return name;
    }

    public PartitionCreateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public PartitionCreateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }
}
