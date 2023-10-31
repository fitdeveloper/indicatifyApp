package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.repository.PerimeterRepository;
import ma.indicatify.app.service.PerimeterService;
import ma.indicatify.app.service.dto.PerimeterDTO;
import ma.indicatify.app.service.mapper.PerimeterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Perimeter}.
 */
@Service
@Transactional
public class PerimeterServiceImpl implements PerimeterService {

    private final Logger log = LoggerFactory.getLogger(PerimeterServiceImpl.class);

    private final PerimeterRepository perimeterRepository;

    private final PerimeterMapper perimeterMapper;

    public PerimeterServiceImpl(PerimeterRepository perimeterRepository, PerimeterMapper perimeterMapper) {
        this.perimeterRepository = perimeterRepository;
        this.perimeterMapper = perimeterMapper;
    }

    @Override
    public Mono<PerimeterDTO> save(PerimeterDTO perimeterDTO) {
        log.debug("Request to save Perimeter : {}", perimeterDTO);
        return perimeterRepository.save(perimeterMapper.toEntity(perimeterDTO)).map(perimeterMapper::toDto);
    }

    @Override
    public Mono<PerimeterDTO> update(PerimeterDTO perimeterDTO) {
        log.debug("Request to update Perimeter : {}", perimeterDTO);
        return perimeterRepository.save(perimeterMapper.toEntity(perimeterDTO)).map(perimeterMapper::toDto);
    }

    @Override
    public Mono<PerimeterDTO> partialUpdate(PerimeterDTO perimeterDTO) {
        log.debug("Request to partially update Perimeter : {}", perimeterDTO);

        return perimeterRepository
            .findById(perimeterDTO.getId())
            .map(existingPerimeter -> {
                perimeterMapper.partialUpdate(existingPerimeter, perimeterDTO);

                return existingPerimeter;
            })
            .flatMap(perimeterRepository::save)
            .map(perimeterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PerimeterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Perimeters");
        return perimeterRepository.findAllBy(pageable).map(perimeterMapper::toDto);
    }

    public Mono<Long> countAll() {
        return perimeterRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PerimeterDTO> findOne(Long id) {
        log.debug("Request to get Perimeter : {}", id);
        return perimeterRepository.findById(id).map(perimeterMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Perimeter : {}", id);
        return perimeterRepository.deleteById(id);
    }
}
