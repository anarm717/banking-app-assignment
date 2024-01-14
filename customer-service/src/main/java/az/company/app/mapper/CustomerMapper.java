package az.company.app.mapper;
import az.company.app.entity.Customer;
import az.company.app.model.CustomerBaseDto;
import az.company.app.model.CustomerCriterieDto;
import az.company.app.model.CustomerUpdateDto;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper {

    public abstract CustomerBaseDto entityToDto(Customer entity);

    public abstract CustomerCriterieDto entityCriterieToDto(Customer entity);

    public abstract CustomerUpdateDto entityToUpdateDto(Customer entity);

    public abstract  Customer dtoToEntity(CustomerBaseDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract  void updateEntity(@MappingTarget Customer customer, CustomerUpdateDto customerBaseDto);

    public abstract List<CustomerBaseDto> entityToDtos(List<Customer> listEntity);

    public abstract List<CustomerCriterieDto> entityToCriterieDtos(List<Customer> listEntity);
    
    public abstract  List<Customer> dtosToentities(List<CustomerBaseDto> listDto);


}
