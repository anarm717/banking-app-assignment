package az.company.auth.service.app;

import java.util.List;

import az.company.auth.dto.AppDto.AppDto;



public interface AppService {

    List<AppDto> getAll();

}
