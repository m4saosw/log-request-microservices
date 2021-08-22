package br.com.massao.logrequest.infrastructure.repository.query;

import br.com.massao.logrequest.application.resource.LogRequestParams;
import br.com.massao.logrequest.domain.DomainLogRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LogRequestSpecifications {

    public Specification<DomainLogRequest> fromParams(LogRequestParams params) {
        return (Root<DomainLogRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addPredicateDate(params, root, cb, predicates);
            addPredicateIp(params, root, cb, predicates);
            addPredicateRequest(params, root, cb, predicates);
            addPredicateStatus(params, root, cb, predicates);
            addPredicateUserAgent(params, root, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    private void addPredicateDate(LogRequestParams params, Root<DomainLogRequest> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (params.haveStartDate() && params.haveEndDate()) {
            predicates.add(cb.and(
                    cb.between(root.get("date"), params.getStartDate(), params.getEndDate())));
        } else if (params.haveStartDate()) {
            predicates.add(cb.and(
                    cb.greaterThanOrEqualTo(root.get("date"), params.getStartDate())));

        } else if (params.haveEndDate()) {
            predicates.add(cb.and(
                    cb.lessThanOrEqualTo(root.get("date"), params.getEndDate())));
        }
    }


    private void addPredicateIp(LogRequestParams params, Root<DomainLogRequest> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (params.haveIp())
            predicates.add(cb.and(
                    cb.equal(root.get("ip"), params.getIp())));
    }


    private void addPredicateRequest(LogRequestParams params, Root<DomainLogRequest> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (params.haveRequests())
            predicates.add(cb.and(
                    cb.like(cb.lower(root.get("request")), "%" + params.getRequest().toLowerCase() + "%")));
    }


    private void addPredicateStatus(LogRequestParams params, Root<DomainLogRequest> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (params.haveStatus())
            predicates.add(cb.and(
                    cb.equal(root.get("status"), params.getStatus())));
    }


    private void addPredicateUserAgent(LogRequestParams params, Root<DomainLogRequest> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (params.haveUserAgent())
            predicates.add(cb.and(
                    cb.like(cb.lower(root.get("userAgent")), "%" + params.getUserAgent().toLowerCase() + "%")));
    }
}
