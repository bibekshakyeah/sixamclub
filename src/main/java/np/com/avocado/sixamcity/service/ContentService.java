package np.com.avocado.sixamcity.service;

import java.util.Optional;
import np.com.avocado.sixamcity.service.dto.ContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link np.com.avocado.sixamcity.domain.Content}.
 */
public interface ContentService {
    /**
     * Save a content.
     *
     * @param contentDTO the entity to save.
     * @return the persisted entity.
     */
    ContentDTO save(ContentDTO contentDTO);

    /**
     * Updates a content.
     *
     * @param contentDTO the entity to update.
     * @return the persisted entity.
     */
    ContentDTO update(ContentDTO contentDTO);

    /**
     * Partially updates a content.
     *
     * @param contentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentDTO> partialUpdate(ContentDTO contentDTO);

    /**
     * Get all the contents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentDTO> findAll(Pageable pageable);

    /**
     * Get all the contents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" content.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentDTO> findOne(Long id);

    /**
     * Delete the "id" content.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
