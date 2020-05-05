package io.github.jhipster.sample.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import io.github.jhipster.sample.TestUtil;
import io.github.jhipster.sample.service.dto.BankAccountDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.math.BigDecimal;
    import java.util.List;

@QuarkusTest
public class BankAccountResourceTest {

    private static final TypeRef<BankAccountDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<BankAccountDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);


    String adminToken;

    BankAccountDTO bankAccountDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccountDTO createEntity() {
        var bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.name = DEFAULT_NAME;
        bankAccountDTO.balance = DEFAULT_BALANCE;
        return bankAccountDTO;
    }

    @BeforeEach
    public void initTest() {
        bankAccountDTO = createEntity();
    }

    @Test
    public void createBankAccount() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the BankAccount
        bankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testBankAccountDTO = bankAccountDTOList.stream().filter(it -> bankAccountDTO.id.equals(it.id)).findFirst().get();
        assertThat(testBankAccountDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testBankAccountDTO.balance).isEqualByComparingTo(DEFAULT_BALANCE);
    }

    @Test
    public void createBankAccountWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the BankAccount with an existing ID
        bankAccountDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        bankAccountDTO.name = null;

        // Create the BankAccount, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkBalanceIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        bankAccountDTO.balance = null;

        // Create the BankAccount, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateBankAccount() {
        // Initialize the database
        bankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the bankAccount
        var updatedBankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts/{id}", bankAccountDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the bankAccount
        updatedBankAccountDTO.name = UPDATED_NAME;
        updatedBankAccountDTO.balance = UPDATED_BALANCE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedBankAccountDTO)
            .when()
            .put("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeUpdate);
        var testBankAccountDTO = bankAccountDTOList.stream().filter(it -> updatedBankAccountDTO.id.equals(it.id)).findFirst().get();
        assertThat(testBankAccountDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testBankAccountDTO.balance).isEqualByComparingTo(UPDATED_BALANCE);
    }

    @Test
    public void updateNonExistingBankAccount() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .put("/api/bank-accounts")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the BankAccount in the database
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBankAccount() {
        // Initialize the database
        bankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the bankAccount
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/bank-accounts/{id}", bankAccountDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var bankAccountDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(bankAccountDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllBankAccounts() {
        // Initialize the database
        bankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the bankAccountList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(bankAccountDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME))            .body("balance", hasItem(DEFAULT_BALANCE.floatValue()));
    }

    @Test
    public void getBankAccount() {
        // Initialize the database
        bankAccountDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(bankAccountDTO)
            .when()
            .post("/api/bank-accounts")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the bankAccount
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/bank-accounts/{id}", bankAccountDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the bankAccount
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts/{id}", bankAccountDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(bankAccountDTO.id.intValue()))
            
                .body("name", is(DEFAULT_NAME))
                .body("balance", comparesEqualTo(DEFAULT_BALANCE.floatValue()));
    }

    @Test
    public void getNonExistingBankAccount() {
        // Get the bankAccount
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/bank-accounts/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
