package co.com.ies.iesgaming.service;

import co.com.ies.iesgaming.service.dto.UserClientDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.iesgaming.domain.UserClient}.
 */
public interface UserClientService {
    /**
     * Save a userClient.
     *
     * @param userClientDTO the entity to save.
     * @return the persisted entity.
     */
    UserClientDTO save(UserClientDTO userClientDTO);

    /**
     * Updates a userClient.
     *
     * @param userClientDTO the entity to update.
     * @return the persisted entity.
     */
    UserClientDTO update(UserClientDTO userClientDTO);

    /**
     * Partially updates a userClient.
     *
     * @param userClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserClientDTO> partialUpdate(UserClientDTO userClientDTO);

    /**
     * Get all the userClients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserClientDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserClientDTO> findOne(Long id);

    /**
     * Delete the "id" userClient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
