package br.com.massao.logrequest.batch;

import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Fieldset mapper
 * Mapping of the csv fields to log bean
 */
@Slf4j
public class LogRequestFieldSetMapper implements FieldSetMapper<LogRequestForm> {

    @Override
    public LogRequestForm mapFieldSet(FieldSet f) {
        return LogRequestForm
                .builder()
                .date(
                        DateFormatterUtil.localDateTimeFrom(f.readString(0)))
                .ip(f.readString(1))
                .request(f.readString(2))
                .status(f.readShort(3))
                .userAgent(f.readString(4))
                .build();
    }

}
