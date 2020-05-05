package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.Operation;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Hibernate Panache repository for the Operation entity.
 */
@ApplicationScoped
public class OperationRepository implements PanacheRepository<Operation> {

    public Operation update(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("operation can't be null");
        }
        var entity = Operation.<Operation>findById(operation.id);
        if (entity != null) {
            entity.date = operation.date;
            entity.description = operation.description;
            entity.amount = operation.amount;
            entity.bankAccount = operation.bankAccount;
            entity.labels = operation.labels;
        }
        return entity;
    }

    public Operation persistOrUpdate(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("operation can't be null");
        }
        if (operation.id == null) {
            persist(operation);
            return operation;
        } else {
            return update(operation);
        }
    }

    public PanacheQuery<Operation> findAllWithEagerRelationships() {
        return find("select distinct operation from Operation operation left join fetch operation.labels");
    }

    public Optional<Operation> findOneWithEagerRelationships(Long id) {
        return find("select operation from Operation operation left join fetch operation.labels where operation.id =?1", id).firstResultOptional();
    }

}
