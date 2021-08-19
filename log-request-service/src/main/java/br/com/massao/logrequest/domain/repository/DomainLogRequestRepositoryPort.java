package br.com.massao.logrequest.domain.repository;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface DomainLogRequestRepositoryPort {
    List<DomainLogRequest> listAll();

    Optional<DomainLogRequest> findById(Long id);

    DomainLogRequest update(Long id, DomainLogRequest log) throws NotFoundException;

    DomainLogRequest create(DomainLogRequest log);
}
