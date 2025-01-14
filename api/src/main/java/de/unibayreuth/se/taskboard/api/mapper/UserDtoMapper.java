package de.unibayreuth.se.taskboard.api.mapper;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.domain.User;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
@NoArgsConstructor
public abstract class UserDtoMapper {

    private boolean utcNowUpdated = false;
    private LocalDateTime utcNow;

    public abstract UserDto fromBusiness(User user);

    @Mapping(target = "id", ignore = true) // ID is auto-generated
    @Mapping(target = "createdAt", expression = "java(mapTimestamp(source.getCreatedAt()))")
    public abstract User toBusiness(UserDto userDto);

    protected LocalDateTime mapTimestamp(LocalDateTime timestamp) {
        return timestamp != null ? timestamp : LocalDateTime.now(ZoneId.of("UTC"));
    }
    public UserDto optionalToDto(Optional<User> source) { return source.map(this::fromBusiness).orElse(null);}
}
