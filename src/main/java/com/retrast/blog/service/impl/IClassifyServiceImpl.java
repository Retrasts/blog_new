package com.retrast.blog.service.impl;

import com.retrast.blog.domain.IClassify;
import com.retrast.blog.repository.IClassifyRepository;
import com.retrast.blog.service.IClassifyService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link IClassify}.
 */
@Service
@Transactional
public class IClassifyServiceImpl implements IClassifyService {

    private final Logger log = LoggerFactory.getLogger(IClassifyServiceImpl.class);

    private final IClassifyRepository iClassifyRepository;

    public IClassifyServiceImpl(IClassifyRepository iClassifyRepository) {
        this.iClassifyRepository = iClassifyRepository;
    }

    @Override
    public Mono<IClassify> save(IClassify iClassify) {
        log.debug("Request to save IClassify : {}", iClassify);
        return iClassifyRepository.save(iClassify);
    }

    @Override
    public Mono<IClassify> partialUpdate(IClassify iClassify) {
        log.debug("Request to partially update IClassify : {}", iClassify);

        return iClassifyRepository
            .findById(iClassify.getId())
            .map(existingIClassify -> {
                if (iClassify.getName() != null) {
                    existingIClassify.setName(iClassify.getName());
                }
                if (iClassify.getAlias() != null) {
                    existingIClassify.setAlias(iClassify.getAlias());
                }
                if (iClassify.getDescription() != null) {
                    existingIClassify.setDescription(iClassify.getDescription());
                }
                if (iClassify.getParentId() != null) {
                    existingIClassify.setParentId(iClassify.getParentId());
                }

                return existingIClassify;
            })
            .flatMap(iClassifyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IClassify> findAll() {
        log.debug("Request to get all IClassifies");
        return iClassifyRepository.findAll();
    }

    public Mono<Long> countAll() {
        return iClassifyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IClassify> findOne(Long id) {
        log.debug("Request to get IClassify : {}", id);
        return iClassifyRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IClassify : {}", id);
        return iClassifyRepository.deleteById(id);
    }
}
