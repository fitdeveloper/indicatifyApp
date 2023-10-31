package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.Site;
import ma.indicatify.app.repository.SiteRepository;
import ma.indicatify.app.service.SiteService;
import ma.indicatify.app.service.dto.SiteDTO;
import ma.indicatify.app.service.mapper.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    public SiteServiceImpl(SiteRepository siteRepository, SiteMapper siteMapper) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
    }

    @Override
    public Mono<SiteDTO> save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);
        return siteRepository.save(siteMapper.toEntity(siteDTO)).map(siteMapper::toDto);
    }

    @Override
    public Mono<SiteDTO> update(SiteDTO siteDTO) {
        log.debug("Request to update Site : {}", siteDTO);
        return siteRepository.save(siteMapper.toEntity(siteDTO)).map(siteMapper::toDto);
    }

    @Override
    public Mono<SiteDTO> partialUpdate(SiteDTO siteDTO) {
        log.debug("Request to partially update Site : {}", siteDTO);

        return siteRepository
            .findById(siteDTO.getId())
            .map(existingSite -> {
                siteMapper.partialUpdate(existingSite, siteDTO);

                return existingSite;
            })
            .flatMap(siteRepository::save)
            .map(siteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sites");
        return siteRepository.findAllBy(pageable).map(siteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return siteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SiteDTO> findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findById(id).map(siteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        return siteRepository.deleteById(id);
    }
}
