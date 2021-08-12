package br.com.massao.logrequest.domain.repository;

import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRequestRepository extends JpaRepository<LogRequestModel, Long>, JpaSpecificationExecutor<LogRequestModel> {

}
