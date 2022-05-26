package co.com.ies.iesgaming.service.impl;

import co.com.ies.iesgaming.domain.UserClient;
import co.com.ies.iesgaming.repository.UserClientRepository;
import co.com.ies.iesgaming.service.UserClientService;
import co.com.ies.iesgaming.service.dto.UserClientDTO;
import co.com.ies.iesgaming.service.mapper.UserClientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserClient}.
 */
@Service
@Transactional
public class UserClientServiceImpl implements UserClientService {

    private final Logger log = LoggerFactory.getLogger(UserClientServiceImpl.class);

    private final UserClientRepository userClientRepository;

    private final UserClientMapper userClientMapper;

    public UserClientServiceImpl(UserClientRepository userClientRepository, UserClientMapper userClientMapper) {
        this.userClientRepository = userClientRepository;
        this.userClientMapper = userClientMapper;
    }

    @Override
    public UserClientDTO save(UserClientDTO userClientDTO) {
        log.debug("Request to save UserClient : {}", userClientDTO);
        UserClient userClient = userClientMapper.toEntity(userClientDTO);
        userClient = userClientRepository.save(userClient);
        return userClientMapper.toDto(userClient);
    }

    @Override
    public UserClientDTO update(UserClientDTO userClientDTO) {
        log.debug("Request to save UserClient : {}", userClientDTO);
        UserClient userClient = userClientMapper.toEntity(userClientDTO);
        userClient = userClientRepository.save(userClient);
        return userClientMapper.toDto(userClient);
    }

    @Override
    public Optional<UserClientDTO> partialUpdate(UserClientDTO userClientDTO) {
        log.debug("Request to partially update UserClient : {}", userClientDTO);

        return userClientRepository
            .findById(userClientDTO.getId())
            .map(existingUserClient -> {
                userClientMapper.partialUpdate(existingUserClient, userClientDTO);

                return existingUserClient;
            })
            .map(userClientRepository::save)
            .map(userClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserClients");
        return userClientRepository.findAll(pageable).map(userClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserClientDTO> findOne(Long id) {
        log.debug("Request to get UserClient : {}", id);
        return userClientRepository.findById(id).map(userClientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserClient : {}", id);
        userClientRepository.deleteById(id);
    }
}
