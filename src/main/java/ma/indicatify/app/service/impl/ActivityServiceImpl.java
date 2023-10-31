package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.Activity;
import ma.indicatify.app.repository.ActivityRepository;
import ma.indicatify.app.service.ActivityService;
import ma.indicatify.app.service.dto.ActivityDTO;
import ma.indicatify.app.service.mapper.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Activity}.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Override
    public Mono<ActivityDTO> save(ActivityDTO activityDTO) {
        log.debug("Request to save Activity : {}", activityDTO);
        return activityRepository.save(activityMapper.toEntity(activityDTO)).map(activityMapper::toDto);
    }

    @Override
    public Mono<ActivityDTO> update(ActivityDTO activityDTO) {
        log.debug("Request to update Activity : {}", activityDTO);
        return activityRepository.save(activityMapper.toEntity(activityDTO)).map(activityMapper::toDto);
    }

    @Override
    public Mono<ActivityDTO> partialUpdate(ActivityDTO activityDTO) {
        log.debug("Request to partially update Activity : {}", activityDTO);

        return activityRepository
            .findById(activityDTO.getId())
            .map(existingActivity -> {
                activityMapper.partialUpdate(existingActivity, activityDTO);

                return existingActivity;
            })
            .flatMap(activityRepository::save)
            .map(activityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Activities");
        return activityRepository.findAllBy(pageable).map(activityMapper::toDto);
    }

    public Mono<Long> countAll() {
        return activityRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ActivityDTO> findOne(Long id) {
        log.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Activity : {}", id);
        return activityRepository.deleteById(id);
    }
}
