package br.com.massao.logrequest.domain.repository;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de dominio de log
 * Tambem chamado de Port na hexagonal architecture. Responsavel pela interface entre dominio e infraestrutura
 */
public interface DomainLogRequestRepositoryPort {
    Page<DomainLogRequest> listAll(Pageable page);

    Optional<DomainLogRequest> findById(Long id);

    DomainLogRequest update(Long id, DomainLogRequest log) throws NotFoundException;

    DomainLogRequest create(DomainLogRequest log);

    Page<DomainLogRequest> listAll(Specification<DomainLogRequest> spec, Pageable page);
}
