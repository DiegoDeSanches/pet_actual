package com.khandras.bot.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khandras.bot.domain.CommonFields;
import com.khandras.bot.domain.info.SavedInfo;
import com.khandras.bot.domain.message.MessageLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(schema = "tasks-bot")
public class User extends CommonFields {
    @Id
    @NotNull
    @Column(nullable = false, insertable = false, updatable = false)
    private String telegramId;
    private BigInteger savedInfoCountBytes;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @OrderBy
    private List<SavedInfo> savedInfoList;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MessageLog> messageLogList;
}
