package io.github.jhipster.sample.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.service.OperationService;
import io.github.jhipster.sample.repository.OperationRepository;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.sample.web.util.HeaderUtil;
import io.github.jhipster.sample.web.util.ResponseUtil;
import io.github.jhipster.sample.service.dto.OperationDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.jhipster.sample.service.Paged;
import io.github.jhipster.sample.web.rest.vm.PageRequestVM;
import io.github.jhipster.sample.web.util.PaginationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.Operation}.
 */
@Path("/api/operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private static final String ENTITY_NAME = "operation";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    OperationService operationService;
    /**
     * {@code POST  /operations} : Create a new operation.
     *
     * @param operationDTO the operationDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new operationDTO, or with status {@code 400 (Bad Request)} if the operation has already an ID.
     */
    @POST
    public Response createOperation(@Valid OperationDTO operationDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Operation : {}", operationDTO);
        if (operationDTO.id != null) {
            throw new BadRequestAlertException("A new operation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = operationService.persistOrUpdate(operationDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /operations} : Updates an existing operation.
     *
     * @param operationDTO the operationDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated operationDTO,
     * or with status {@code 400 (Bad Request)} if the operationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operationDTO couldn't be updated.
     */
    @PUT
    public Response updateOperation(@Valid OperationDTO operationDTO) {
        log.debug("REST request to update Operation : {}", operationDTO);
        if (operationDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = operationService.persistOrUpdate(operationDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operationDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /operations/:id} : delete the "id" operation.
     *
     * @param id the id of the operationDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOperation(@PathParam("id") Long id) {
        log.debug("REST request to delete Operation : {}", id);
        operationService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /operations} : get all the operations.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of operations in body.
     */
    @GET
    public Response getAllOperations(@BeanParam PageRequestVM pageRequest, @Context UriInfo uriInfo, @QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get a page of Operations");
        var page = pageRequest.toPage();
        Paged<OperationDTO> result;
        if (eagerload) {
            result = operationService.findAllWithEagerRelationships(page);
        } else {
            result = operationService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /operations/:id} : get the "id" operation.
     *
     * @param id the id of the operationDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the operationDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getOperation(@PathParam("id") Long id) {
        log.debug("REST request to get Operation : {}", id);
        Optional<OperationDTO> operationDTO = operationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operationDTO);
    }
}
