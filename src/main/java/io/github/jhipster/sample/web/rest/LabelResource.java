package io.github.jhipster.sample.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.domain.Label;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.sample.web.util.HeaderUtil;
import io.github.jhipster.sample.web.util.ResponseUtil;
import io.github.jhipster.sample.service.dto.LabelDTO;
import io.github.jhipster.sample.service.mapper.LabelMapper;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.Label}.
 */
@Path("/api/labels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LabelResource {

    private final Logger log = LoggerFactory.getLogger(LabelResource.class);

    private static final String ENTITY_NAME = "label";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    LabelMapper labelMapper;
    /**
     * {@code POST  /labels} : Create a new label.
     *
     * @param labelDTO the labelDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new labelDTO, or with status {@code 400 (Bad Request)} if the label has already an ID.
     */
    @POST
    @Transactional
    public Response createLabel(@Valid LabelDTO labelDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Label : {}", labelDTO);
        if (labelDTO.id != null) {
            throw new BadRequestAlertException("A new label cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var label = labelMapper.toEntity(labelDTO);
        label = Label.persistOrUpdate(label);
        var result = labelMapper.toDto(label);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /labels} : Updates an existing label.
     *
     * @param labelDTO the labelDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated labelDTO,
     * or with status {@code 400 (Bad Request)} if the labelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the labelDTO couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateLabel(@Valid LabelDTO labelDTO) {
        log.debug("REST request to update Label : {}", labelDTO);
        if (labelDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var label = labelMapper.toEntity(labelDTO);
        label = Label.persistOrUpdate(label);
        var result = labelMapper.toDto(label);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, labelDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /labels/:id} : delete the "id" label.
     *
     * @param id the id of the labelDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteLabel(@PathParam("id") Long id) {
        log.debug("REST request to delete Label : {}", id);
        Label.findByIdOptional(id).ifPresent(label -> {
            label.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /labels} : get all the labels.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and the list of labels in body.
     */
    @GET
    public List<LabelDTO> getAllLabels() {
        log.debug("REST request to get all Labels");
        List<Label> labels = Label.findAll().list();
        return labelMapper.toDto(labels);
    }

    /**
     * {@code GET  /labels/:id} : get the "id" label.
     *
     * @param id the id of the labelDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the labelDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getLabel(@PathParam("id") Long id) {
        log.debug("REST request to get Label : {}", id);
        Optional<LabelDTO> labelDTO = Label.findByIdOptional(id)
            .map(label -> labelMapper.toDto((Label) label)); 
        return ResponseUtil.wrapOrNotFound(labelDTO);
    }
}
