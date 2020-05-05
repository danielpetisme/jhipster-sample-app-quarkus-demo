package io.github.jhipster.sample.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.domain.BankAccount;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.sample.web.util.HeaderUtil;
import io.github.jhipster.sample.web.util.ResponseUtil;
import io.github.jhipster.sample.service.dto.BankAccountDTO;
import io.github.jhipster.sample.service.mapper.BankAccountMapper;

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
 * REST controller for managing {@link io.github.jhipster.sample.domain.BankAccount}.
 */
@Path("/api/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "bankAccount";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    BankAccountMapper bankAccountMapper;
    /**
     * {@code POST  /bank-accounts} : Create a new bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new bankAccountDTO, or with status {@code 400 (Bad Request)} if the bankAccount has already an ID.
     */
    @POST
    @Transactional
    public Response createBankAccount(@Valid BankAccountDTO bankAccountDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.id != null) {
            throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount = BankAccount.persistOrUpdate(bankAccount);
        var result = bankAccountMapper.toDto(bankAccount);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /bank-accounts} : Updates an existing bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated bankAccountDTO,
     * or with status {@code 400 (Bad Request)} if the bankAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankAccountDTO couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateBankAccount(@Valid BankAccountDTO bankAccountDTO) {
        log.debug("REST request to update BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount = BankAccount.persistOrUpdate(bankAccount);
        var result = bankAccountMapper.toDto(bankAccount);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankAccountDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /bank-accounts/:id} : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBankAccount(@PathParam("id") Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        BankAccount.findByIdOptional(id).ifPresent(bankAccount -> {
            bankAccount.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /bank-accounts} : get all the bankAccounts.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and the list of bankAccounts in body.
     */
    @GET
    public List<BankAccountDTO> getAllBankAccounts() {
        log.debug("REST request to get all BankAccounts");
        List<BankAccount> bankAccounts = BankAccount.findAll().list();
        return bankAccountMapper.toDto(bankAccounts);
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the bankAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getBankAccount(@PathParam("id") Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        Optional<BankAccountDTO> bankAccountDTO = BankAccount.findByIdOptional(id)
            .map(bankAccount -> bankAccountMapper.toDto((BankAccount) bankAccount)); 
        return ResponseUtil.wrapOrNotFound(bankAccountDTO);
    }
}
