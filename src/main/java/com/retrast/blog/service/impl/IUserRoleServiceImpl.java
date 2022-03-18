package com.retrast.blog.service.impl;

import com.retrast.blog.domain.IUserRole;
import com.retrast.blog.repository.IUserRoleRepository;
import com.retrast.blog.service.IUserRoleService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link IUserRole}.
 */
@Service
@Transactional
public class IUserRoleServiceImpl implements IUserRoleService {

    private final Logger log = LoggerFactory.getLogger(IUserRoleServiceImpl.class);

    private final IUserRoleRepository iUserRoleRepository;

    public IUserRoleServiceImpl(IUserRoleRepository iUserRoleRepository) {
        this.iUserRoleRepository = iUserRoleRepository;
    }

    @Override
    public Mono<IUserRole> save(IUserRole iUserRole) {
        log.debug("Request to save IUserRole : {}", iUserRole);
        return iUserRoleRepository.save(iUserRole);
    }

    @Override
    public Mono<IUserRole> partialUpdate(IUserRole iUserRole) {
        log.debug("Request to partially update IUserRole : {}", iUserRole);

        return iUserRoleRepository
            .findById(iUserRole.getId())
            .map(existingIUserRole -> {
                return existingIUserRole;
            })
            .flatMap(iUserRoleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IUserRole> findAll() {
        log.debug("Request to get all IUserRoles");
        return iUserRoleRepository.findAll();
    }

    /**
     *  Get all the iUserRoles where Role is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IUserRole> findAllWhereRoleIsNull() {
        log.debug("Request to get all iUserRoles where Role is null");
        return iUserRoleRepository.findAllWhereRoleIsNull();
    }

    public Mono<Long> countAll() {
        return iUserRoleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IUserRole> findOne(Long id) {
        log.debug("Request to get IUserRole : {}", id);
        return iUserRoleRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IUserRole : {}", id);
        return iUserRoleRepository.deleteById(id);
    }
}
