package kz.bitlab.G118security.mapper;

import kz.bitlab.G118security.dto.UserCreate;
import kz.bitlab.G118security.dto.UserUpdate;
import kz.bitlab.G118security.dto.UserView;
import kz.bitlab.G118security.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "fullName", target = "name")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "phoneNumber")
    UserView toView(User entity);

    @Named("phoneNumber")
    default String extractPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\-+\\s]", "");
    }

    List<UserView> toViewList(List<User> entityList);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "roles", ignore = true)
    User toEntity(UserCreate userCreate);

    User toEntity(UserUpdate userUpdate);
}
