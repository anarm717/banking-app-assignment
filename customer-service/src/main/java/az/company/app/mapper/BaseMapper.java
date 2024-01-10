package az.company.app.mapper;

import java.util.List;


public interface BaseMapper<E, D> {

    D entityToDto(E entity);
    E dtoToEntity(D dto);

    List<D> entitiesToDtos(List<E> entities);
    List<E> dtosToEntities(List<D> dtos);

}
