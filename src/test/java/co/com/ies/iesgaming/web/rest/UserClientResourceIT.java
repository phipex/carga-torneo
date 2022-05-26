package co.com.ies.iesgaming.web.rest;

import static co.com.ies.iesgaming.web.rest.TestUtil.sameInstant;
import static co.com.ies.iesgaming.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.iesgaming.IntegrationTest;
import co.com.ies.iesgaming.domain.UserClient;
import co.com.ies.iesgaming.repository.UserClientRepository;
import co.com.ies.iesgaming.service.criteria.UserClientCriteria;
import co.com.ies.iesgaming.service.dto.UserClientDTO;
import co.com.ies.iesgaming.service.mapper.UserClientMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserClientResourceIT {

    private static final Long DEFAULT_ID_OPERATOR = 1L;
    private static final Long UPDATED_ID_OPERATOR = 2L;
    private static final Long SMALLER_ID_OPERATOR = 1L - 1L;

    private static final String DEFAULT_ID_USER_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_ID_USER_OPERATOR = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_TORNEO = 1L;
    private static final Long UPDATED_ID_TORNEO = 2L;
    private static final Long SMALLER_ID_TORNEO = 1L - 1L;

    private static final BigDecimal DEFAULT_ACUMULADO = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACUMULADO = new BigDecimal(1);
    private static final BigDecimal SMALLER_ACUMULADO = new BigDecimal(0 - 1);

    private static final ZonedDateTime DEFAULT_LAST_REPORT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_REPORT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_REPORT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserClientRepository userClientRepository;

    @Autowired
    private UserClientMapper userClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserClientMockMvc;

    private UserClient userClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserClient createEntity(EntityManager em) {
        UserClient userClient = new UserClient()
            .idOperator(DEFAULT_ID_OPERATOR)
            .idUserOperator(DEFAULT_ID_USER_OPERATOR)
            .idTorneo(DEFAULT_ID_TORNEO)
            .acumulado(DEFAULT_ACUMULADO)
            .lastReport(DEFAULT_LAST_REPORT);
        return userClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserClient createUpdatedEntity(EntityManager em) {
        UserClient userClient = new UserClient()
            .idOperator(UPDATED_ID_OPERATOR)
            .idUserOperator(UPDATED_ID_USER_OPERATOR)
            .idTorneo(UPDATED_ID_TORNEO)
            .acumulado(UPDATED_ACUMULADO)
            .lastReport(UPDATED_LAST_REPORT);
        return userClient;
    }

    @BeforeEach
    public void initTest() {
        userClient = createEntity(em);
    }

    @Test
    @Transactional
    void createUserClient() throws Exception {
        int databaseSizeBeforeCreate = userClientRepository.findAll().size();
        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);
        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isCreated());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeCreate + 1);
        UserClient testUserClient = userClientList.get(userClientList.size() - 1);
        assertThat(testUserClient.getIdOperator()).isEqualTo(DEFAULT_ID_OPERATOR);
        assertThat(testUserClient.getIdUserOperator()).isEqualTo(DEFAULT_ID_USER_OPERATOR);
        assertThat(testUserClient.getIdTorneo()).isEqualTo(DEFAULT_ID_TORNEO);
        assertThat(testUserClient.getAcumulado()).isEqualByComparingTo(DEFAULT_ACUMULADO);
        assertThat(testUserClient.getLastReport()).isEqualTo(DEFAULT_LAST_REPORT);
    }

    @Test
    @Transactional
    void createUserClientWithExistingId() throws Exception {
        // Create the UserClient with an existing ID
        userClient.setId(1L);
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        int databaseSizeBeforeCreate = userClientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdOperatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = userClientRepository.findAll().size();
        // set the field null
        userClient.setIdOperator(null);

        // Create the UserClient, which fails.
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdUserOperatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = userClientRepository.findAll().size();
        // set the field null
        userClient.setIdUserOperator(null);

        // Create the UserClient, which fails.
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdTorneoIsRequired() throws Exception {
        int databaseSizeBeforeTest = userClientRepository.findAll().size();
        // set the field null
        userClient.setIdTorneo(null);

        // Create the UserClient, which fails.
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcumuladoIsRequired() throws Exception {
        int databaseSizeBeforeTest = userClientRepository.findAll().size();
        // set the field null
        userClient.setAcumulado(null);

        // Create the UserClient, which fails.
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastReportIsRequired() throws Exception {
        int databaseSizeBeforeTest = userClientRepository.findAll().size();
        // set the field null
        userClient.setLastReport(null);

        // Create the UserClient, which fails.
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        restUserClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isBadRequest());

        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserClients() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].idOperator").value(hasItem(DEFAULT_ID_OPERATOR.intValue())))
            .andExpect(jsonPath("$.[*].idUserOperator").value(hasItem(DEFAULT_ID_USER_OPERATOR)))
            .andExpect(jsonPath("$.[*].idTorneo").value(hasItem(DEFAULT_ID_TORNEO.intValue())))
            .andExpect(jsonPath("$.[*].acumulado").value(hasItem(sameNumber(DEFAULT_ACUMULADO))))
            .andExpect(jsonPath("$.[*].lastReport").value(hasItem(sameInstant(DEFAULT_LAST_REPORT))));
    }

    @Test
    @Transactional
    void getUserClient() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get the userClient
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL_ID, userClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userClient.getId().intValue()))
            .andExpect(jsonPath("$.idOperator").value(DEFAULT_ID_OPERATOR.intValue()))
            .andExpect(jsonPath("$.idUserOperator").value(DEFAULT_ID_USER_OPERATOR))
            .andExpect(jsonPath("$.idTorneo").value(DEFAULT_ID_TORNEO.intValue()))
            .andExpect(jsonPath("$.acumulado").value(sameNumber(DEFAULT_ACUMULADO)))
            .andExpect(jsonPath("$.lastReport").value(sameInstant(DEFAULT_LAST_REPORT)));
    }

    @Test
    @Transactional
    void getUserClientsByIdFiltering() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        Long id = userClient.getId();

        defaultUserClientShouldBeFound("id.equals=" + id);
        defaultUserClientShouldNotBeFound("id.notEquals=" + id);

        defaultUserClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserClientShouldNotBeFound("id.greaterThan=" + id);

        defaultUserClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator equals to DEFAULT_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.equals=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator equals to UPDATED_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.equals=" + UPDATED_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator not equals to DEFAULT_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.notEquals=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator not equals to UPDATED_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.notEquals=" + UPDATED_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsInShouldWork() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator in DEFAULT_ID_OPERATOR or UPDATED_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.in=" + DEFAULT_ID_OPERATOR + "," + UPDATED_ID_OPERATOR);

        // Get all the userClientList where idOperator equals to UPDATED_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.in=" + UPDATED_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator is not null
        defaultUserClientShouldBeFound("idOperator.specified=true");

        // Get all the userClientList where idOperator is null
        defaultUserClientShouldNotBeFound("idOperator.specified=false");
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator is greater than or equal to DEFAULT_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.greaterThanOrEqual=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator is greater than or equal to UPDATED_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.greaterThanOrEqual=" + UPDATED_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator is less than or equal to DEFAULT_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.lessThanOrEqual=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator is less than or equal to SMALLER_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.lessThanOrEqual=" + SMALLER_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsLessThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator is less than DEFAULT_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.lessThan=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator is less than UPDATED_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.lessThan=" + UPDATED_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdOperatorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idOperator is greater than DEFAULT_ID_OPERATOR
        defaultUserClientShouldNotBeFound("idOperator.greaterThan=" + DEFAULT_ID_OPERATOR);

        // Get all the userClientList where idOperator is greater than SMALLER_ID_OPERATOR
        defaultUserClientShouldBeFound("idOperator.greaterThan=" + SMALLER_ID_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorIsEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator equals to DEFAULT_ID_USER_OPERATOR
        defaultUserClientShouldBeFound("idUserOperator.equals=" + DEFAULT_ID_USER_OPERATOR);

        // Get all the userClientList where idUserOperator equals to UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldNotBeFound("idUserOperator.equals=" + UPDATED_ID_USER_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator not equals to DEFAULT_ID_USER_OPERATOR
        defaultUserClientShouldNotBeFound("idUserOperator.notEquals=" + DEFAULT_ID_USER_OPERATOR);

        // Get all the userClientList where idUserOperator not equals to UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldBeFound("idUserOperator.notEquals=" + UPDATED_ID_USER_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorIsInShouldWork() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator in DEFAULT_ID_USER_OPERATOR or UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldBeFound("idUserOperator.in=" + DEFAULT_ID_USER_OPERATOR + "," + UPDATED_ID_USER_OPERATOR);

        // Get all the userClientList where idUserOperator equals to UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldNotBeFound("idUserOperator.in=" + UPDATED_ID_USER_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator is not null
        defaultUserClientShouldBeFound("idUserOperator.specified=true");

        // Get all the userClientList where idUserOperator is null
        defaultUserClientShouldNotBeFound("idUserOperator.specified=false");
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorContainsSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator contains DEFAULT_ID_USER_OPERATOR
        defaultUserClientShouldBeFound("idUserOperator.contains=" + DEFAULT_ID_USER_OPERATOR);

        // Get all the userClientList where idUserOperator contains UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldNotBeFound("idUserOperator.contains=" + UPDATED_ID_USER_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdUserOperatorNotContainsSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idUserOperator does not contain DEFAULT_ID_USER_OPERATOR
        defaultUserClientShouldNotBeFound("idUserOperator.doesNotContain=" + DEFAULT_ID_USER_OPERATOR);

        // Get all the userClientList where idUserOperator does not contain UPDATED_ID_USER_OPERATOR
        defaultUserClientShouldBeFound("idUserOperator.doesNotContain=" + UPDATED_ID_USER_OPERATOR);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo equals to DEFAULT_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.equals=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo equals to UPDATED_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.equals=" + UPDATED_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo not equals to DEFAULT_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.notEquals=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo not equals to UPDATED_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.notEquals=" + UPDATED_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsInShouldWork() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo in DEFAULT_ID_TORNEO or UPDATED_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.in=" + DEFAULT_ID_TORNEO + "," + UPDATED_ID_TORNEO);

        // Get all the userClientList where idTorneo equals to UPDATED_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.in=" + UPDATED_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsNullOrNotNull() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo is not null
        defaultUserClientShouldBeFound("idTorneo.specified=true");

        // Get all the userClientList where idTorneo is null
        defaultUserClientShouldNotBeFound("idTorneo.specified=false");
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo is greater than or equal to DEFAULT_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.greaterThanOrEqual=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo is greater than or equal to UPDATED_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.greaterThanOrEqual=" + UPDATED_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo is less than or equal to DEFAULT_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.lessThanOrEqual=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo is less than or equal to SMALLER_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.lessThanOrEqual=" + SMALLER_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsLessThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo is less than DEFAULT_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.lessThan=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo is less than UPDATED_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.lessThan=" + UPDATED_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByIdTorneoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where idTorneo is greater than DEFAULT_ID_TORNEO
        defaultUserClientShouldNotBeFound("idTorneo.greaterThan=" + DEFAULT_ID_TORNEO);

        // Get all the userClientList where idTorneo is greater than SMALLER_ID_TORNEO
        defaultUserClientShouldBeFound("idTorneo.greaterThan=" + SMALLER_ID_TORNEO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado equals to DEFAULT_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.equals=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado equals to UPDATED_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.equals=" + UPDATED_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado not equals to DEFAULT_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.notEquals=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado not equals to UPDATED_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.notEquals=" + UPDATED_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsInShouldWork() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado in DEFAULT_ACUMULADO or UPDATED_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.in=" + DEFAULT_ACUMULADO + "," + UPDATED_ACUMULADO);

        // Get all the userClientList where acumulado equals to UPDATED_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.in=" + UPDATED_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado is not null
        defaultUserClientShouldBeFound("acumulado.specified=true");

        // Get all the userClientList where acumulado is null
        defaultUserClientShouldNotBeFound("acumulado.specified=false");
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado is greater than or equal to DEFAULT_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.greaterThanOrEqual=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado is greater than or equal to UPDATED_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.greaterThanOrEqual=" + UPDATED_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado is less than or equal to DEFAULT_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.lessThanOrEqual=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado is less than or equal to SMALLER_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.lessThanOrEqual=" + SMALLER_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsLessThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado is less than DEFAULT_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.lessThan=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado is less than UPDATED_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.lessThan=" + UPDATED_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByAcumuladoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where acumulado is greater than DEFAULT_ACUMULADO
        defaultUserClientShouldNotBeFound("acumulado.greaterThan=" + DEFAULT_ACUMULADO);

        // Get all the userClientList where acumulado is greater than SMALLER_ACUMULADO
        defaultUserClientShouldBeFound("acumulado.greaterThan=" + SMALLER_ACUMULADO);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport equals to DEFAULT_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.equals=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport equals to UPDATED_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.equals=" + UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport not equals to DEFAULT_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.notEquals=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport not equals to UPDATED_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.notEquals=" + UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsInShouldWork() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport in DEFAULT_LAST_REPORT or UPDATED_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.in=" + DEFAULT_LAST_REPORT + "," + UPDATED_LAST_REPORT);

        // Get all the userClientList where lastReport equals to UPDATED_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.in=" + UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsNullOrNotNull() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport is not null
        defaultUserClientShouldBeFound("lastReport.specified=true");

        // Get all the userClientList where lastReport is null
        defaultUserClientShouldNotBeFound("lastReport.specified=false");
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport is greater than or equal to DEFAULT_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.greaterThanOrEqual=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport is greater than or equal to UPDATED_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.greaterThanOrEqual=" + UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport is less than or equal to DEFAULT_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.lessThanOrEqual=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport is less than or equal to SMALLER_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.lessThanOrEqual=" + SMALLER_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsLessThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport is less than DEFAULT_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.lessThan=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport is less than UPDATED_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.lessThan=" + UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void getAllUserClientsByLastReportIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        // Get all the userClientList where lastReport is greater than DEFAULT_LAST_REPORT
        defaultUserClientShouldNotBeFound("lastReport.greaterThan=" + DEFAULT_LAST_REPORT);

        // Get all the userClientList where lastReport is greater than SMALLER_LAST_REPORT
        defaultUserClientShouldBeFound("lastReport.greaterThan=" + SMALLER_LAST_REPORT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserClientShouldBeFound(String filter) throws Exception {
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].idOperator").value(hasItem(DEFAULT_ID_OPERATOR.intValue())))
            .andExpect(jsonPath("$.[*].idUserOperator").value(hasItem(DEFAULT_ID_USER_OPERATOR)))
            .andExpect(jsonPath("$.[*].idTorneo").value(hasItem(DEFAULT_ID_TORNEO.intValue())))
            .andExpect(jsonPath("$.[*].acumulado").value(hasItem(sameNumber(DEFAULT_ACUMULADO))))
            .andExpect(jsonPath("$.[*].lastReport").value(hasItem(sameInstant(DEFAULT_LAST_REPORT))));

        // Check, that the count call also returns 1
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserClientShouldNotBeFound(String filter) throws Exception {
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserClient() throws Exception {
        // Get the userClient
        restUserClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserClient() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();

        // Update the userClient
        UserClient updatedUserClient = userClientRepository.findById(userClient.getId()).get();
        // Disconnect from session so that the updates on updatedUserClient are not directly saved in db
        em.detach(updatedUserClient);
        updatedUserClient
            .idOperator(UPDATED_ID_OPERATOR)
            .idUserOperator(UPDATED_ID_USER_OPERATOR)
            .idTorneo(UPDATED_ID_TORNEO)
            .acumulado(UPDATED_ACUMULADO)
            .lastReport(UPDATED_LAST_REPORT);
        UserClientDTO userClientDTO = userClientMapper.toDto(updatedUserClient);

        restUserClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
        UserClient testUserClient = userClientList.get(userClientList.size() - 1);
        assertThat(testUserClient.getIdOperator()).isEqualTo(UPDATED_ID_OPERATOR);
        assertThat(testUserClient.getIdUserOperator()).isEqualTo(UPDATED_ID_USER_OPERATOR);
        assertThat(testUserClient.getIdTorneo()).isEqualTo(UPDATED_ID_TORNEO);
        assertThat(testUserClient.getAcumulado()).isEqualByComparingTo(UPDATED_ACUMULADO);
        assertThat(testUserClient.getLastReport()).isEqualTo(UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void putNonExistingUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserClientWithPatch() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();

        // Update the userClient using partial update
        UserClient partialUpdatedUserClient = new UserClient();
        partialUpdatedUserClient.setId(userClient.getId());

        partialUpdatedUserClient.idOperator(UPDATED_ID_OPERATOR).acumulado(UPDATED_ACUMULADO);

        restUserClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserClient))
            )
            .andExpect(status().isOk());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
        UserClient testUserClient = userClientList.get(userClientList.size() - 1);
        assertThat(testUserClient.getIdOperator()).isEqualTo(UPDATED_ID_OPERATOR);
        assertThat(testUserClient.getIdUserOperator()).isEqualTo(DEFAULT_ID_USER_OPERATOR);
        assertThat(testUserClient.getIdTorneo()).isEqualTo(DEFAULT_ID_TORNEO);
        assertThat(testUserClient.getAcumulado()).isEqualByComparingTo(UPDATED_ACUMULADO);
        assertThat(testUserClient.getLastReport()).isEqualTo(DEFAULT_LAST_REPORT);
    }

    @Test
    @Transactional
    void fullUpdateUserClientWithPatch() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();

        // Update the userClient using partial update
        UserClient partialUpdatedUserClient = new UserClient();
        partialUpdatedUserClient.setId(userClient.getId());

        partialUpdatedUserClient
            .idOperator(UPDATED_ID_OPERATOR)
            .idUserOperator(UPDATED_ID_USER_OPERATOR)
            .idTorneo(UPDATED_ID_TORNEO)
            .acumulado(UPDATED_ACUMULADO)
            .lastReport(UPDATED_LAST_REPORT);

        restUserClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserClient))
            )
            .andExpect(status().isOk());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
        UserClient testUserClient = userClientList.get(userClientList.size() - 1);
        assertThat(testUserClient.getIdOperator()).isEqualTo(UPDATED_ID_OPERATOR);
        assertThat(testUserClient.getIdUserOperator()).isEqualTo(UPDATED_ID_USER_OPERATOR);
        assertThat(testUserClient.getIdTorneo()).isEqualTo(UPDATED_ID_TORNEO);
        assertThat(testUserClient.getAcumulado()).isEqualByComparingTo(UPDATED_ACUMULADO);
        assertThat(testUserClient.getLastReport()).isEqualTo(UPDATED_LAST_REPORT);
    }

    @Test
    @Transactional
    void patchNonExistingUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserClient() throws Exception {
        int databaseSizeBeforeUpdate = userClientRepository.findAll().size();
        userClient.setId(count.incrementAndGet());

        // Create the UserClient
        UserClientDTO userClientDTO = userClientMapper.toDto(userClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserClientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserClient in the database
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserClient() throws Exception {
        // Initialize the database
        userClientRepository.saveAndFlush(userClient);

        int databaseSizeBeforeDelete = userClientRepository.findAll().size();

        // Delete the userClient
        restUserClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, userClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserClient> userClientList = userClientRepository.findAll();
        assertThat(userClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
