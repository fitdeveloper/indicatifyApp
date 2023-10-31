package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.Level;
import ma.indicatify.app.repository.LevelRepository;
import ma.indicatify.app.service.LevelService;
import ma.indicatify.app.service.dto.LevelDTO;
import ma.indicatify.app.service.mapper.LevelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Level}.
 */
@Service
@Transactional
public class LevelServiceImpl implements LevelService {

    private final Logger log = LoggerFactory.getLogger(LevelServiceImpl.class);

    private final LevelRepository levelRepository;

    private final LevelMapper levelMapper;

    public LevelServiceImpl(LevelRepository levelRepository, LevelMapper levelMapper) {
        this.levelRepository = levelRepository;
        this.levelMapper = levelMapper;
    }

    @Override
    public Mono<LevelDTO> save(LevelDTO levelDTO) {
        log.debug("Request to save Level : {}", levelDTO);
        return levelRepository.save(levelMapper.toEntity(levelDTO)).map(levelMapper::toDto);
    }

    @Override
    public Mono<LevelDTO> update(LevelDTO levelDTO) {
        log.debug("Request to update Level : {}", levelDTO);
        return levelRepository.save(levelMapper.toEntity(levelDTO)).map(levelMapper::toDto);
    }

    @Override
    public Mono<LevelDTO> partialUpdate(LevelDTO levelDTO) {
        log.debug("Request to partially update Level : {}", levelDTO);

        return levelRepository
            .findById(levelDTO.getId())
            .map(existingLevel -> {
                levelMapper.partialUpdate(existingLevel, levelDTO);

                return existingLevel;
            })
            .flatMap(levelRepository::save)
            .map(levelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Levels");
        return levelRepository.findAllBy(pageable).map(levelMapper::toDto);
    }

    public Mono<Long> countAll() {
        return levelRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LevelDTO> findOne(Long id) {
        log.debug("Request to get Level : {}", id);
        return levelRepository.findById(id).map(levelMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Level : {}", id);
        return levelRepository.deleteById(id);
    }
}
