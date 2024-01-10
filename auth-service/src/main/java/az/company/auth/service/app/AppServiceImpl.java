package az.company.auth.service.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import az.company.auth.converter.AppMapper;
import az.company.auth.dto.AppDto.AppDto;
import az.company.auth.repository.AppRepository;

import java.util.List;

/**
 * @author fuad
 */

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService{

    private final AppMapper mapper;
    private final AppRepository repository;

    @Override
    public List<AppDto> getAll() {
       return mapper.toDtos(repository.findAll());
    }
}
