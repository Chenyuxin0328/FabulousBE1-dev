package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 邮箱使用计数
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailUsedCounter {

    /**
     * 开始的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime from;
    /**
     * 结束的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime to;

    /**
     * 邮箱的使用次数
     */
    private List<EmailUsed> emailUseds;

    public OffsetDateTime getFrom() {
        return from;
    }

    public EmailUsedCounter setFrom(OffsetDateTime from) {
        this.from = from;
        return this;
    }

    public OffsetDateTime getTo() {
        return to;
    }

    public EmailUsedCounter setTo(OffsetDateTime to) {
        this.to = to;
        return this;
    }

    public List<EmailUsed> getEmailUseds() {
        return emailUseds;
    }

    public EmailUsedCounter setEmailUseds(List<EmailUsed> emailUseds) {
        this.emailUseds = emailUseds;
        return this;
    }
}
