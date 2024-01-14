package az.company.app.mapper;
import az.company.app.entity.CustomerNumber;
import az.company.app.model.CustomerBaseDto;
import az.company.app.model.CustomerNumberBaseDto;
import az.company.app.model.CustomerNumberDto;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CustomerNumberMapper {

    @Mapping(target = "id", source = "customer.id")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "surname", source = "customer.surname")
    @Mapping(target = "birthDate", source = "customer.birthDate")
    public abstract CustomerBaseDto entityToDto(CustomerNumber entity);

    @Mapping(target = "customerId", source = "customer.id")
    public abstract  CustomerNumberBaseDto entityToNumberBaseDto(CustomerNumber entitiy);

    public abstract  CustomerNumber numberDtoToEntity(CustomerNumberBaseDto dto);

    public abstract  CustomerNumber dtoToEntity(CustomerBaseDto dto);
    
    public abstract CustomerNumberDto entityToNumberDto(CustomerNumber entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract  void updateEntity(@MappingTarget CustomerNumber customer, CustomerBaseDto customerBaseDto);

    public abstract List<CustomerBaseDto> entityToDtos(List<CustomerNumber> listEntity);

    public abstract List<CustomerNumberDto> entityToNumberDtos(List<CustomerNumber> listEntity);

    public abstract  List<CustomerNumber> dtosToentities(List<CustomerBaseDto> listDto);


}
