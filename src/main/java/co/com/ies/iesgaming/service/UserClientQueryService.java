package co.com.ies.iesgaming.service;

import co.com.ies.iesgaming.domain.*; // for static metamodels
import co.com.ies.iesgaming.domain.UserClient;
import co.com.ies.iesgaming.repository.UserClientRepository;
import co.com.ies.iesgaming.service.criteria.UserClientCriteria;
import co.com.ies.iesgaming.service.dto.UserClientDTO;
import co.com.ies.iesgaming.service.mapper.UserClientMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserClient} entities in the database.
 * The main input is a {@link UserClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserClientDTO} or a {@link Page} of {@link UserClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserClientQueryService extends QueryService<UserClient> {

    private final Logger log = LoggerFactory.getLogger(UserClientQueryService.class);

    private final UserClientRepository userClientRepository;

    private final UserClientMapper userClientMapper;

    public UserClientQueryService(UserClientRepository userClientRepository, UserClientMapper userClientMapper) {
        this.userClientRepository = userClientRepository;
        this.userClientMapper = userClientMapper;
    }

    /**
     * Return a {@link List} of {@link UserClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserClientDTO> findByCriteria(UserClientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserClient> specification = createSpecification(criteria);
        return userClientMapper.toDto(userClientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserClientDTO> findByCriteria(UserClientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserClient> specification = createSpecification(criteria);
        return userClientRepository.findAll(specification, page).map(userClientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserClientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserClient> specification = createSpecification(criteria);
        return userClientRepository.count(specification);
    }

    /**
     * Function to convert {@link UserClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserClient> createSpecification(UserClientCriteria criteria) {
        Specification<UserClient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserClient_.id));
            }
            if (criteria.getIdOperator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdOperator(), UserClient_.idOperator));
            }
            if (criteria.getIdUserOperator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdUserOperator(), UserClient_.idUserOperator));
            }
            if (criteria.getIdTorneo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdTorneo(), UserClient_.idTorneo));
            }
            if (criteria.getAcumulado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcumulado(), UserClient_.acumulado));
            }
            if (criteria.getLastReport() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastReport(), UserClient_.lastReport));
            }
        }
        return specification;
    }
}
