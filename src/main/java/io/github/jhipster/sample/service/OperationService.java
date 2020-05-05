package io.github.jhipster.sample.service;

import io.github.jhipster.sample.service.dto.OperationDTO;
import io.quarkus.panache.common.Page;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.jhipster.sample.domain.Operation}.
 */
public interface OperationService {

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save.
     * @return the persisted entity.
     */
    OperationDTO persistOrUpdate(OperationDTO operationDTO);

    /**
     * Delete the "id" operationDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the operations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OperationDTO> findAll(Page page);

    /**
     * Get the "id" operationDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperationDTO> findOne(Long id);


    /**
     * Get all the operations with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
    */
    public Paged<OperationDTO> findAllWithEagerRelationships(Page page);


}
