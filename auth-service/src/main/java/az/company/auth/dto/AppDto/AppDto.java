package az.company.auth.dto.AppDto;

import lombok.Data;

import java.time.LocalDateTime;

import az.company.auth.entity.Base;

@Data
public class AppDto extends Base<Long> {
    private String appName;
    private String appDesc;
    private LocalDateTime createdAt;
    private Long createdBy;
}
