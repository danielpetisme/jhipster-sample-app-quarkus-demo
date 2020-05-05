package io.github.jhipster.sample.service.impl;

import io.github.jhipster.sample.service.OperationService;
import io.quarkus.panache.common.Page;
import io.github.jhipster.sample.service.Paged;
import io.github.jhipster.sample.domain.Operation;
import io.github.jhipster.sample.repository.OperationRepository;
import io.github.jhipster.sample.service.dto.OperationDTO;
import io.github.jhipster.sample.service.mapper.OperationMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class OperationServiceImpl implements OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    @Inject
    OperationRepository operationRepository;

    @Inject
    OperationMapper operationMapper;

    @Override
    @Transactional
    public OperationDTO persistOrUpdate(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        var operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.persistOrUpdate(operation);
        return operationMapper.toDto(operation);
    }

    /**
     * Delete the Operation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.findByIdOptional(id).ifPresent(operation -> {
            operationRepository.delete(operation);
        });
    }

    /**
     * Get one operation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OperationDTO> findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        return operationRepository.findOneWithEagerRelationships(id)
            .map(operation -> operationMapper.toDto((Operation) operation)); 
    }

    /**
     * Get all the operations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OperationDTO> findAll(Page page) {
        log.debug("Request to get all Operations");
        return new Paged<>(operationRepository.findAll().page(page))
            .map(operation -> operationMapper.toDto(operation));
    }


    /**
     * Get all the operations with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OperationDTO> findAllWithEagerRelationships(Page page) {
        var operations = operationRepository.findAllWithEagerRelationships().page(page).list();
        var totalCount = operationRepository.findAll().count();
        var pageCount = operationRepository.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, operations)
            .map(operation -> operationMapper.toDto(operation));
    }


}
