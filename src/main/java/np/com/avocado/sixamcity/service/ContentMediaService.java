package np.com.avocado.sixamcity.service;

import java.util.Optional;
import np.com.avocado.sixamcity.service.dto.ContentMediaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link np.com.avocado.sixamcity.domain.ContentMedia}.
 */
public interface ContentMediaService {
    /**
     * Save a contentMedia.
     *
     * @param contentMediaDTO the entity to save.
     * @return the persisted entity.
     */
    ContentMediaDTO save(ContentMediaDTO contentMediaDTO);

    /**
     * Updates a contentMedia.
     *
     * @param contentMediaDTO the entity to update.
     * @return the persisted entity.
     */
    ContentMediaDTO update(ContentMediaDTO contentMediaDTO);

    /**
     * Partially updates a contentMedia.
     *
     * @param contentMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentMediaDTO> partialUpdate(ContentMediaDTO contentMediaDTO);

    /**
     * Get all the contentMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentMediaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contentMedia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentMediaDTO> findOne(Long id);

    /**
     * Delete the "id" contentMedia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
