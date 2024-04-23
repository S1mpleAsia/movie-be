package dev.hust.simpleasia.mapper;

import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.SignInResponse;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CredentialMapstruct {
    @Mapping(target = "id", source = "orderId")
    UserCredential toEntity(RegisterInitReq registerInitReq);

    @Mapping(target = "token", source = "token")
    SignInResponse toResponse(UserCredential userCredential, String token);
}
