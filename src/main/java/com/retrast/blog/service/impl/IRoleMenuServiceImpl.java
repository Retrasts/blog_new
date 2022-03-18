package com.retrast.blog.service.impl;

import com.retrast.blog.domain.IRoleMenu;
import com.retrast.blog.repository.IRoleMenuRepository;
import com.retrast.blog.service.IRoleMenuService;
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
 * Service Implementation for managing {@link IRoleMenu}.
 */
@Service
@Transactional
public class IRoleMenuServiceImpl implements IRoleMenuService {

    private final Logger log = LoggerFactory.getLogger(IRoleMenuServiceImpl.class);

    private final IRoleMenuRepository iRoleMenuRepository;

    public IRoleMenuServiceImpl(IRoleMenuRepository iRoleMenuRepository) {
        this.iRoleMenuRepository = iRoleMenuRepository;
    }

    @Override
    public Mono<IRoleMenu> save(IRoleMenu iRoleMenu) {
        log.debug("Request to save IRoleMenu : {}", iRoleMenu);
        return iRoleMenuRepository.save(iRoleMenu);
    }

    @Override
    public Mono<IRoleMenu> partialUpdate(IRoleMenu iRoleMenu) {
        log.debug("Request to partially update IRoleMenu : {}", iRoleMenu);

        return iRoleMenuRepository
            .findById(iRoleMenu.getId())
            .map(existingIRoleMenu -> {
                return existingIRoleMenu;
            })
            .flatMap(iRoleMenuRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IRoleMenu> findAll() {
        log.debug("Request to get all IRoleMenus");
        return iRoleMenuRepository.findAll();
    }

    /**
     *  Get all the iRoleMenus where Role is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IRoleMenu> findAllWhereRoleIsNull() {
        log.debug("Request to get all iRoleMenus where Role is null");
        return iRoleMenuRepository.findAllWhereRoleIsNull();
    }

    public Mono<Long> countAll() {
        return iRoleMenuRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IRoleMenu> findOne(Long id) {
        log.debug("Request to get IRoleMenu : {}", id);
        return iRoleMenuRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IRoleMenu : {}", id);
        return iRoleMenuRepository.deleteById(id);
    }
}
