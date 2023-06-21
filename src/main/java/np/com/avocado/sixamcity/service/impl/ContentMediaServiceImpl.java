package np.com.avocado.sixamcity.service.impl;

import java.util.Optional;
import np.com.avocado.sixamcity.domain.ContentMedia;
import np.com.avocado.sixamcity.repository.ContentMediaRepository;
import np.com.avocado.sixamcity.service.ContentMediaService;
import np.com.avocado.sixamcity.service.dto.ContentMediaDTO;
import np.com.avocado.sixamcity.service.mapper.ContentMediaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContentMedia}.
 */
@Service
@Transactional
public class ContentMediaServiceImpl implements ContentMediaService {

    private final Logger log = LoggerFactory.getLogger(ContentMediaServiceImpl.class);

    private final ContentMediaRepository contentMediaRepository;

    private final ContentMediaMapper contentMediaMapper;

    public ContentMediaServiceImpl(ContentMediaRepository contentMediaRepository, ContentMediaMapper contentMediaMapper) {
        this.contentMediaRepository = contentMediaRepository;
        this.contentMediaMapper = contentMediaMapper;
    }

    @Override
    public ContentMediaDTO save(ContentMediaDTO contentMediaDTO) {
        log.debug("Request to save ContentMedia : {}", contentMediaDTO);
        ContentMedia contentMedia = contentMediaMapper.toEntity(contentMediaDTO);
        contentMedia = contentMediaRepository.save(contentMedia);
        return contentMediaMapper.toDto(contentMedia);
    }

    @Override
    public ContentMediaDTO update(ContentMediaDTO contentMediaDTO) {
        log.debug("Request to update ContentMedia : {}", contentMediaDTO);
        ContentMedia contentMedia = contentMediaMapper.toEntity(contentMediaDTO);
        contentMedia = contentMediaRepository.save(contentMedia);
        return contentMediaMapper.toDto(contentMedia);
    }

    @Override
    public Optional<ContentMediaDTO> partialUpdate(ContentMediaDTO contentMediaDTO) {
        log.debug("Request to partially update ContentMedia : {}", contentMediaDTO);

        return contentMediaRepository
            .findById(contentMediaDTO.getId())
            .map(existingContentMedia -> {
                contentMediaMapper.partialUpdate(existingContentMedia, contentMediaDTO);

                return existingContentMedia;
            })
            .map(contentMediaRepository::save)
            .map(contentMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentMedias");
        return contentMediaRepository.findAll(pageable).map(contentMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentMediaDTO> findOne(Long id) {
        log.debug("Request to get ContentMedia : {}", id);
        return contentMediaRepository.findById(id).map(contentMediaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContentMedia : {}", id);
        contentMediaRepository.deleteById(id);
    }
}
