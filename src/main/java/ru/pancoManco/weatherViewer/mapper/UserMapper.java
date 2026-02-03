package ru.pancoManco.weatherViewer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(source="username", target = "login")
    User toEntity(UserRegisterDto userRegisterDto);
    UserRegisterDto toUserRegisterDto(User user);
}
