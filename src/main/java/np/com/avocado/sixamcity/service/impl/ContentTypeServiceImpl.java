package np.com.avocado.sixamcity.service.impl;

import java.util.Optional;
import np.com.avocado.sixamcity.domain.ContentType;
import np.com.avocado.sixamcity.repository.ContentTypeRepository;
import np.com.avocado.sixamcity.service.ContentTypeService;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
import np.com.avocado.sixamcity.service.mapper.ContentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContentType}.
 */
@Service
@Transactional
public class ContentTypeServiceImpl implements ContentTypeService {

    private final Logger log = LoggerFactory.getLogger(ContentTypeServiceImpl.class);

    private final ContentTypeRepository contentTypeRepository;

    private final ContentTypeMapper contentTypeMapper;

    public ContentTypeServiceImpl(ContentTypeRepository contentTypeRepository, ContentTypeMapper contentTypeMapper) {
        this.contentTypeRepository = contentTypeRepository;
        this.contentTypeMapper = contentTypeMapper;
    }

    @Override
    public ContentTypeDTO save(ContentTypeDTO contentTypeDTO) {
        log.debug("Request to save ContentType : {}", contentTypeDTO);
        ContentType contentType = contentTypeMapper.toEntity(contentTypeDTO);
        contentType = contentTypeRepository.save(contentType);
        return contentTypeMapper.toDto(contentType);
    }

    @Override
    public ContentTypeDTO update(ContentTypeDTO contentTypeDTO) {
        log.debug("Request to update ContentType : {}", contentTypeDTO);
        ContentType contentType = contentTypeMapper.toEntity(contentTypeDTO);
        contentType = contentTypeRepository.save(contentType);
        return contentTypeMapper.toDto(contentType);
    }

    @Override
    public Optional<ContentTypeDTO> partialUpdate(ContentTypeDTO contentTypeDTO) {
        log.debug("Request to partially update ContentType : {}", contentTypeDTO);

        return contentTypeRepository
            .findById(contentTypeDTO.getId())
            .map(existingContentType -> {
                contentTypeMapper.partialUpdate(existingContentType, contentTypeDTO);

                return existingContentType;
            })
            .map(contentTypeRepository::save)
            .map(contentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentTypes");
        return contentTypeRepository.findAll(pageable).map(contentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentTypeDTO> findOne(Long id) {
        log.debug("Request to get ContentType : {}", id);
        return contentTypeRepository.findById(id).map(contentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContentType : {}", id);
        contentTypeRepository.deleteById(id);
    }
}
