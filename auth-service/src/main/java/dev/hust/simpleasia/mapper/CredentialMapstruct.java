package dev.hust.simpleasia.mapper;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.SignInResponse;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface CredentialMapstruct {
    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "birthday", qualifiedByName = "convertDate")
    UserCredential toEntity(RegisterInitReq registerInitReq);

    @Mapping(target = "token", source = "token")
    SignInResponse toResponse(UserCredential userCredential, String token);

    @Named("convertDate")
    default Date toDate(String dateStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateStr);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
}
