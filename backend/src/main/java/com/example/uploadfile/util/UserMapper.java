package com.example.uploadfile.util;

import com.example.uploadfile.domain.AccessUserListEntity;
import com.example.uploadfile.dto.WhiteListUserDto;


final public class UserMapper {

    public static WhiteListUserDto toWhiteListUserDto(AccessUserListEntity accessUserListEntity) {
        WhiteListUserDto whiteListUserDto = new WhiteListUserDto();
        whiteListUserDto.setId(accessUserListEntity.getId());
        whiteListUserDto.setUserName(accessUserListEntity.getParticipantEntity().getUsername());
        whiteListUserDto.setFullName(accessUserListEntity.getParticipantEntity().getFullName());
        whiteListUserDto.setCreateDate(accessUserListEntity.getCreateDate());
        whiteListUserDto.setDateFrom(accessUserListEntity.getDateFrom());
        whiteListUserDto.setDateTo(accessUserListEntity.getDateTo());
        whiteListUserDto.setRole(accessUserListEntity.getRole().replace("ROLE_", ""));
        whiteListUserDto.setAddedBy(accessUserListEntity.getAddedBy());
        whiteListUserDto.setIsNative(accessUserListEntity.getIsNative());
        return whiteListUserDto;
    }
}
