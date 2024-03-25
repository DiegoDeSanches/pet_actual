package com.khandras.adapter.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class UserDto {
    private String telegramId;
    private String savedInfoCountBytes;
    private List<UserInfoDto> userInfo;
}
