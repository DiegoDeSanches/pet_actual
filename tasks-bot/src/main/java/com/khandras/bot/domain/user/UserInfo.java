package com.khandras.bot.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UserInfo", schema = "tasks-bot")
public class UserInfo {
    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    @NotNull
    private String telegramId;
    private BigInteger savedInfoCountBytes;
}
