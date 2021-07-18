package br.com.massao.logrequest.service;

import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.repository.LogRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogRequestService {
    @Autowired
    private LogRequestRepository repository;


    public Page<LogRequestModel> list(Pageable pageable) {
        log.debug("list pageable");

        return repository.findAll(pageable);
    }


}
