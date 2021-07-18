package br.com.massao.logrequest.converter;


import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
}
