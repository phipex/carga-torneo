package co.com.ies.iesgaming.service.mapper;

import co.com.ies.iesgaming.domain.UserClient;
import co.com.ies.iesgaming.service.dto.UserClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserClient} and its DTO {@link UserClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserClientMapper extends EntityMapper<UserClientDTO, UserClient> {}
