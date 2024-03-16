package com.khandras.bot.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khandras.bot.domain.CommonFields;
import com.khandras.bot.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_log", schema = "tasks-bot")
@Setter
@Getter
@Accessors(chain = true)
public class MessageLog extends CommonFields {
    @Id
    @GeneratedValue
    private UUID messageId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_telegram_id")
    private User userInfo;
    private String messageText;
    @CreationTimestamp
    private LocalDateTime saveDateTime;

    @JdbcTypeCode(SqlTypes.JSON)
    private Update messageFull;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    @Column(columnDefinition="bytea")
    private byte[] savedInfo;
}