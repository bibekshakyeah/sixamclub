package np.com.avocado.sixamcity.service;

import java.util.Optional;
import np.com.avocado.sixamcity.service.dto.OceanFacetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link np.com.avocado.sixamcity.domain.OceanFacet}.
 */
public interface OceanFacetService {
    /**
     * Save a oceanFacet.
     *
     * @param oceanFacetDTO the entity to save.
     * @return the persisted entity.
     */
    OceanFacetDTO save(OceanFacetDTO oceanFacetDTO);

    /**
     * Updates a oceanFacet.
     *
     * @param oceanFacetDTO the entity to update.
     * @return the persisted entity.
     */
    OceanFacetDTO update(OceanFacetDTO oceanFacetDTO);

    /**
     * Partially updates a oceanFacet.
     *
     * @param oceanFacetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OceanFacetDTO> partialUpdate(OceanFacetDTO oceanFacetDTO);

    /**
     * Get all the oceanFacets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OceanFacetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" oceanFacet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OceanFacetDTO> findOne(Long id);

    /**
     * Delete the "id" oceanFacet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
