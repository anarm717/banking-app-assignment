package az.company.app.mapper;
import az.company.app.entity.CustomerNumber;
import az.company.app.model.CustomerBaseDto;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CustomerNumberMapper {

    @Mapping(target = "id", source = "customer.id")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "surname", source = "customer.surname")
    @Mapping(target = "birthDate", source = "customer.birthDate")
    public abstract CustomerBaseDto entityToDto(CustomerNumber entity);

    public abstract  CustomerNumber dtoToEntity(CustomerBaseDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract  void updateEntity(@MappingTarget CustomerNumber customer, CustomerBaseDto customerBaseDto);

    public abstract List<CustomerBaseDto> entityToDtos(List<CustomerNumber> listEntity);

    public abstract  List<CustomerNumber> dtosToentities(List<CustomerBaseDto> listDto);


}
