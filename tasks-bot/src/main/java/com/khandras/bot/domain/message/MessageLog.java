package com.khandras.bot.domain.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

@Entity
@Table(name = "message_log", schema = "tasks-bot")
@Setter
@Getter
@Accessors(chain = true)
public class MessageLog {
    @Id
    @GeneratedValue
    private UUID messageId;
    private Long telegramId;
    private String messageText;

    @JdbcTypeCode(SqlTypes.JSON)
    private Update messageFull;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    @Column(columnDefinition="bytea")
    private byte[] savedInfo;
}