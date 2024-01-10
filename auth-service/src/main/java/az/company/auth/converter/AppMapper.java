package az.company.auth.converter;

import org.mapstruct.Mapper;

import az.company.auth.dto.AppDto.AppDto;
import az.company.auth.entity.App;

import java.util.List;

/**
 * @author fuad
 */
@Mapper(componentModel = "spring")
public abstract class AppMapper {

    public abstract App toEntity(AppDto dto);

    public abstract AppDto toDto(App entity);

    public abstract List<AppDto> toDtos(List<App> dto);

}
