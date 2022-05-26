package co.com.ies.iesgaming.web.rest;

import co.com.ies.iesgaming.repository.UserClientRepository;
import co.com.ies.iesgaming.service.UserClientQueryService;
import co.com.ies.iesgaming.service.UserClientService;
import co.com.ies.iesgaming.service.criteria.UserClientCriteria;
import co.com.ies.iesgaming.service.dto.UserClientDTO;
import co.com.ies.iesgaming.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.ies.iesgaming.domain.UserClient}.
 */
@RestController
@RequestMapping("/api")
public class UserClientResource {

    private final Logger log = LoggerFactory.getLogger(UserClientResource.class);

    private static final String ENTITY_NAME = "userClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserClientService userClientService;

    private final UserClientRepository userClientRepository;

    private final UserClientQueryService userClientQueryService;

    public UserClientResource(
        UserClientService userClientService,
        UserClientRepository userClientRepository,
        UserClientQueryService userClientQueryService
    ) {
        this.userClientService = userClientService;
        this.userClientRepository = userClientRepository;
        this.userClientQueryService = userClientQueryService;
    }

    /**
     * {@code POST  /user-clients} : Create a new userClient.
     *
     * @param userClientDTO the userClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userClientDTO, or with status {@code 400 (Bad Request)} if the userClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-clients")
    public ResponseEntity<UserClientDTO> createUserClient(@Valid @RequestBody UserClientDTO userClientDTO) throws URISyntaxException {
        log.debug("REST request to save UserClient : {}", userClientDTO);
        if (userClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new userClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserClientDTO result = userClientService.save(userClientDTO);
        return ResponseEntity
            .created(new URI("/api/user-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-clients/:id} : Updates an existing userClient.
     *
     * @param id the id of the userClientDTO to save.
     * @param userClientDTO the userClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userClientDTO,
     * or with status {@code 400 (Bad Request)} if the userClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-clients/{id}")
    public ResponseEntity<UserClientDTO> updateUserClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserClientDTO userClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserClient : {}, {}", id, userClientDTO);
        if (userClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserClientDTO result = userClientService.update(userClientDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-clients/:id} : Partial updates given fields of an existing userClient, field will ignore if it is null
     *
     * @param id the id of the userClientDTO to save.
     * @param userClientDTO the userClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userClientDTO,
     * or with status {@code 400 (Bad Request)} if the userClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserClientDTO> partialUpdateUserClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserClientDTO userClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserClient partially : {}, {}", id, userClientDTO);
        if (userClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserClientDTO> result = userClientService.partialUpdate(userClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-clients} : get all the userClients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userClients in body.
     */
    @GetMapping("/user-clients")
    public ResponseEntity<List<UserClientDTO>> getAllUserClients(
        UserClientCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserClients by criteria: {}", criteria);
        Page<UserClientDTO> page = userClientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-clients/count} : count all the userClients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-clients/count")
    public ResponseEntity<Long> countUserClients(UserClientCriteria criteria) {
        log.debug("REST request to count UserClients by criteria: {}", criteria);
        return ResponseEntity.ok().body(userClientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-clients/:id} : get the "id" userClient.
     *
     * @param id the id of the userClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-clients/{id}")
    public ResponseEntity<UserClientDTO> getUserClient(@PathVariable Long id) {
        log.debug("REST request to get UserClient : {}", id);
        Optional<UserClientDTO> userClientDTO = userClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userClientDTO);
    }

    /**
     * {@code DELETE  /user-clients/:id} : delete the "id" userClient.
     *
     * @param id the id of the userClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-clients/{id}")
    public ResponseEntity<Void> deleteUserClient(@PathVariable Long id) {
        log.debug("REST request to delete UserClient : {}", id);
        userClientService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
