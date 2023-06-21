package np.com.avocado.sixamcity.service.impl;

import java.util.Optional;
import np.com.avocado.sixamcity.domain.Content;
import np.com.avocado.sixamcity.repository.ContentRepository;
import np.com.avocado.sixamcity.service.ContentService;
import np.com.avocado.sixamcity.service.dto.ContentDTO;
import np.com.avocado.sixamcity.service.mapper.ContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Content}.
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    private final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final ContentRepository contentRepository;

    private final ContentMapper contentMapper;

    public ContentServiceImpl(ContentRepository contentRepository, ContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
    }

    @Override
    public ContentDTO save(ContentDTO contentDTO) {
        log.debug("Request to save Content : {}", contentDTO);
        Content content = contentMapper.toEntity(contentDTO);
        content = contentRepository.save(content);
        return contentMapper.toDto(content);
    }

    @Override
    public ContentDTO update(ContentDTO contentDTO) {
        log.debug("Request to update Content : {}", contentDTO);
        Content content = contentMapper.toEntity(contentDTO);
        content = contentRepository.save(content);
        return contentMapper.toDto(content);
    }

    @Override
    public Optional<ContentDTO> partialUpdate(ContentDTO contentDTO) {
        log.debug("Request to partially update Content : {}", contentDTO);

        return contentRepository
            .findById(contentDTO.getId())
            .map(existingContent -> {
                contentMapper.partialUpdate(existingContent, contentDTO);

                return existingContent;
            })
            .map(contentRepository::save)
            .map(contentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contents");
        return contentRepository.findAll(pageable).map(contentMapper::toDto);
    }

    public Page<ContentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contentRepository.findAllWithEagerRelationships(pageable).map(contentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentDTO> findOne(Long id) {
        log.debug("Request to get Content : {}", id);
        return contentRepository.findOneWithEagerRelationships(id).map(contentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Content : {}", id);
        contentRepository.deleteById(id);
    }
}
