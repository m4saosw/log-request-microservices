package br.com.massao.logrequest.batch;

import br.com.massao.logrequest.application.converter.LogRequestModelConverter;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import br.com.massao.logrequest.domain.service.LogRequestImportFromFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Item writer
 * Save logs in data repository
 */
@Component
@Slf4j
public class LogRequestItemWriter implements ItemWriter<LogRequestForm> {

    @Autowired
    private LogRequestImportFromFileService service;

    @Autowired
    private LogRequestModelConverter converter;


    @Override
    public void write(List<? extends LogRequestForm> logs) {
        log.debug("Bulk load logs: {}", logs);

        final List<LogRequestModel> models = converter.listOfModelsFrom((List<LogRequestForm>) logs);

        service.saveMany(models);
    }
}
