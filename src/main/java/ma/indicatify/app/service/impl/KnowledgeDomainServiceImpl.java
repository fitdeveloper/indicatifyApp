package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.KnowledgeDomain;
import ma.indicatify.app.repository.KnowledgeDomainRepository;
import ma.indicatify.app.service.KnowledgeDomainService;
import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
import ma.indicatify.app.service.mapper.KnowledgeDomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link KnowledgeDomain}.
 */
@Service
@Transactional
public class KnowledgeDomainServiceImpl implements KnowledgeDomainService {

    private final Logger log = LoggerFactory.getLogger(KnowledgeDomainServiceImpl.class);

    private final KnowledgeDomainRepository knowledgeDomainRepository;

    private final KnowledgeDomainMapper knowledgeDomainMapper;

    public KnowledgeDomainServiceImpl(KnowledgeDomainRepository knowledgeDomainRepository, KnowledgeDomainMapper knowledgeDomainMapper) {
        this.knowledgeDomainRepository = knowledgeDomainRepository;
        this.knowledgeDomainMapper = knowledgeDomainMapper;
    }

    @Override
    public Mono<KnowledgeDomainDTO> save(KnowledgeDomainDTO knowledgeDomainDTO) {
        log.debug("Request to save KnowledgeDomain : {}", knowledgeDomainDTO);
        return knowledgeDomainRepository.save(knowledgeDomainMapper.toEntity(knowledgeDomainDTO)).map(knowledgeDomainMapper::toDto);
    }

    @Override
    public Mono<KnowledgeDomainDTO> update(KnowledgeDomainDTO knowledgeDomainDTO) {
        log.debug("Request to update KnowledgeDomain : {}", knowledgeDomainDTO);
        return knowledgeDomainRepository.save(knowledgeDomainMapper.toEntity(knowledgeDomainDTO)).map(knowledgeDomainMapper::toDto);
    }

    @Override
    public Mono<KnowledgeDomainDTO> partialUpdate(KnowledgeDomainDTO knowledgeDomainDTO) {
        log.debug("Request to partially update KnowledgeDomain : {}", knowledgeDomainDTO);

        return knowledgeDomainRepository
            .findById(knowledgeDomainDTO.getId())
            .map(existingKnowledgeDomain -> {
                knowledgeDomainMapper.partialUpdate(existingKnowledgeDomain, knowledgeDomainDTO);

                return existingKnowledgeDomain;
            })
            .flatMap(knowledgeDomainRepository::save)
            .map(knowledgeDomainMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<KnowledgeDomainDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KnowledgeDomains");
        return knowledgeDomainRepository.findAllBy(pageable).map(knowledgeDomainMapper::toDto);
    }

    public Mono<Long> countAll() {
        return knowledgeDomainRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<KnowledgeDomainDTO> findOne(Long id) {
        log.debug("Request to get KnowledgeDomain : {}", id);
        return knowledgeDomainRepository.findById(id).map(knowledgeDomainMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete KnowledgeDomain : {}", id);
        return knowledgeDomainRepository.deleteById(id);
    }
}
