package com.khandras.bot.domain.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khandras.bot.domain.CommonFields;
import com.khandras.bot.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

import java.util.UUID;

@Entity
@Table(name = "saved_info", schema = "tasks-bot")
@Setter
@Getter
@Accessors(chain = true)
public class SavedInfo extends CommonFields {
    @Id
    @GeneratedValue
    private UUID infoId;
    @Enumerated(EnumType.STRING)
    private SavedInfoType infoType;
    private String recordName;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    @Column(columnDefinition="bytea")
    private byte[] savedInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "user_info_telegram_id")
    private User userInfo;
}
