package np.com.avocado.sixamcity.service;

import java.util.Optional;
import np.com.avocado.sixamcity.service.dto.OceanModelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link np.com.avocado.sixamcity.domain.OceanModel}.
 */
public interface OceanModelService {
    /**
     * Save a oceanModel.
     *
     * @param oceanModelDTO the entity to save.
     * @return the persisted entity.
     */
    OceanModelDTO save(OceanModelDTO oceanModelDTO);

    /**
     * Updates a oceanModel.
     *
     * @param oceanModelDTO the entity to update.
     * @return the persisted entity.
     */
    OceanModelDTO update(OceanModelDTO oceanModelDTO);

    /**
     * Partially updates a oceanModel.
     *
     * @param oceanModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OceanModelDTO> partialUpdate(OceanModelDTO oceanModelDTO);

    /**
     * Get all the oceanModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OceanModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" oceanModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OceanModelDTO> findOne(Long id);

    /**
     * Delete the "id" oceanModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
