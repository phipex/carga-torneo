package co.com.ies.iesgaming.repository;

import co.com.ies.iesgaming.domain.UserClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long>, JpaSpecificationExecutor<UserClient> {}
