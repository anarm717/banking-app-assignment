package az.company.app.mapper;
import az.company.app.entity.Transaction;
import az.company.app.model.TransactionBaseDto;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    @Mapping(target = "transactionType", source = "transactionType.name")
    public abstract TransactionBaseDto entityToDto(Transaction entity);

    @Mapping(target = "transactionType.name", source = "transactionType")
    public abstract  Transaction dtoToEntity(TransactionBaseDto dto);

    public abstract List<TransactionBaseDto> entityToDtos(List<Transaction> listEntity);

    public abstract  List<Transaction> dtosToentities(List<TransactionBaseDto> listDto);


}
