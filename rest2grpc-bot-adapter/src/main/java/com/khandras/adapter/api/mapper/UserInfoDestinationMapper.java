package com.khandras.adapter.api.mapper;

import com.khandras.adapter.api.dto.UserDto;
import com.khandras.adapter.api.dto.UserInfoDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import resources.proto.SavedInfo;
import resources.proto.UserInfo;

import java.util.List;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = SavedInfoToDtoMapper.class)
@Named("UserInfoDestinationMapper")
public interface UserInfoDestinationMapper {
    UserInfoDestinationMapper INSTANCE = Mappers.getMapper(UserInfoDestinationMapper.class);

    @IterableMapping(qualifiedByName = "mapUser")
    @Named("ToRestDto")
    List<UserDto> toRestDto(List<UserInfo> response);

    @Named("mapUser")
    default UserDto map(UserInfo userInfo) {
        return new UserDto()
                .setTelegramId(userInfo.getTelegramId())
                .setSavedInfoCountBytes(String.valueOf(userInfo.getSavedInfoCountBytes()))
                .setUserInfo(mapInfo(userInfo.getUserInfoList()));
    }

    @Named("mapInfo")
    default List<UserInfoDto> mapInfo(List<SavedInfo> savedInfos) {
        return SavedInfoToDtoMapper.INSTANCE.savedInfoToDto(savedInfos);
    }
}
