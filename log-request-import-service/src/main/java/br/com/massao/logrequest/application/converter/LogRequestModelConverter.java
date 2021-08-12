package br.com.massao.logrequest.application.converter;


import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class LogRequestModelConverter {

    public LogRequestModel modelFrom(LogRequestForm form) {
        if (Objects.isNull(form)) return null;

        return LogRequestModel.builder()
                .date(form.getDate())
                .ip(form.getIp())
                .request(form.getRequest())
                .status(form.getStatus())
                .userAgent(form.getUserAgent())
                .build();
    }

    public List<LogRequestModel> listOfModelsFrom(List<LogRequestForm> form) {
        if (Objects.isNull(form)) return Collections.emptyList();

        return form.stream().map(this::modelFrom).collect(Collectors.toList());
    }
}
