package com.khandras.adapter.api.mapper;

import com.khandras.adapter.api.dto.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import resources.proto.SavedInfo;

import java.util.List;

@Mapper
public interface SavedInfoToDtoMapper {
    SavedInfoToDtoMapper INSTANCE = Mappers.getMapper(SavedInfoToDtoMapper.class);

    @Named("savedInfoToDto")
    List<UserInfoDto> savedInfoToDto(List<SavedInfo> userInfo);

}
