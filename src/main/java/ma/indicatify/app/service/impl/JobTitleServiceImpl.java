package ma.indicatify.app.service.impl;

import ma.indicatify.app.domain.JobTitle;
import ma.indicatify.app.repository.JobTitleRepository;
import ma.indicatify.app.service.JobTitleService;
import ma.indicatify.app.service.dto.JobTitleDTO;
import ma.indicatify.app.service.mapper.JobTitleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link JobTitle}.
 */
@Service
@Transactional
public class JobTitleServiceImpl implements JobTitleService {

    private final Logger log = LoggerFactory.getLogger(JobTitleServiceImpl.class);

    private final JobTitleRepository jobTitleRepository;

    private final JobTitleMapper jobTitleMapper;

    public JobTitleServiceImpl(JobTitleRepository jobTitleRepository, JobTitleMapper jobTitleMapper) {
        this.jobTitleRepository = jobTitleRepository;
        this.jobTitleMapper = jobTitleMapper;
    }

    @Override
    public Mono<JobTitleDTO> save(JobTitleDTO jobTitleDTO) {
        log.debug("Request to save JobTitle : {}", jobTitleDTO);
        return jobTitleRepository.save(jobTitleMapper.toEntity(jobTitleDTO)).map(jobTitleMapper::toDto);
    }

    @Override
    public Mono<JobTitleDTO> update(JobTitleDTO jobTitleDTO) {
        log.debug("Request to update JobTitle : {}", jobTitleDTO);
        return jobTitleRepository.save(jobTitleMapper.toEntity(jobTitleDTO)).map(jobTitleMapper::toDto);
    }

    @Override
    public Mono<JobTitleDTO> partialUpdate(JobTitleDTO jobTitleDTO) {
        log.debug("Request to partially update JobTitle : {}", jobTitleDTO);

        return jobTitleRepository
            .findById(jobTitleDTO.getId())
            .map(existingJobTitle -> {
                jobTitleMapper.partialUpdate(existingJobTitle, jobTitleDTO);

                return existingJobTitle;
            })
            .flatMap(jobTitleRepository::save)
            .map(jobTitleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<JobTitleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTitles");
        return jobTitleRepository.findAllBy(pageable).map(jobTitleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return jobTitleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<JobTitleDTO> findOne(Long id) {
        log.debug("Request to get JobTitle : {}", id);
        return jobTitleRepository.findById(id).map(jobTitleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete JobTitle : {}", id);
        return jobTitleRepository.deleteById(id);
    }
}
