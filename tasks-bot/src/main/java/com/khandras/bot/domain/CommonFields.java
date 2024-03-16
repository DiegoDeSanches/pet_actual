package com.khandras.bot.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Setter
@Getter
@MappedSuperclass
@Accessors(chain = true)
public class CommonFields {
    @CreationTimestamp
    private Timestamp createDateTime;
    @UpdateTimestamp
    private Timestamp updateDateTime;
}
