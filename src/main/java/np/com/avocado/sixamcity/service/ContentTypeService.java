package np.com.avocado.sixamcity.service;

import java.util.Optional;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link np.com.avocado.sixamcity.domain.ContentType}.
 */
public interface ContentTypeService {
    /**
     * Save a contentType.
     *
     * @param contentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ContentTypeDTO save(ContentTypeDTO contentTypeDTO);

    /**
     * Updates a contentType.
     *
     * @param contentTypeDTO the entity to update.
     * @return the persisted entity.
     */
    ContentTypeDTO update(ContentTypeDTO contentTypeDTO);

    /**
     * Partially updates a contentType.
     *
     * @param contentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentTypeDTO> partialUpdate(ContentTypeDTO contentTypeDTO);

    /**
     * Get all the contentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" contentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
