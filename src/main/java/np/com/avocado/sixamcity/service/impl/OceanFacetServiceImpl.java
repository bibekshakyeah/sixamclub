package np.com.avocado.sixamcity.service.impl;

import java.util.Optional;
import np.com.avocado.sixamcity.domain.OceanFacet;
import np.com.avocado.sixamcity.repository.OceanFacetRepository;
import np.com.avocado.sixamcity.service.OceanFacetService;
import np.com.avocado.sixamcity.service.dto.OceanFacetDTO;
import np.com.avocado.sixamcity.service.mapper.OceanFacetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OceanFacet}.
 */
@Service
@Transactional
public class OceanFacetServiceImpl implements OceanFacetService {

    private final Logger log = LoggerFactory.getLogger(OceanFacetServiceImpl.class);

    private final OceanFacetRepository oceanFacetRepository;

    private final OceanFacetMapper oceanFacetMapper;

    public OceanFacetServiceImpl(OceanFacetRepository oceanFacetRepository, OceanFacetMapper oceanFacetMapper) {
        this.oceanFacetRepository = oceanFacetRepository;
        this.oceanFacetMapper = oceanFacetMapper;
    }

    @Override
    public OceanFacetDTO save(OceanFacetDTO oceanFacetDTO) {
        log.debug("Request to save OceanFacet : {}", oceanFacetDTO);
        OceanFacet oceanFacet = oceanFacetMapper.toEntity(oceanFacetDTO);
        oceanFacet = oceanFacetRepository.save(oceanFacet);
        return oceanFacetMapper.toDto(oceanFacet);
    }

    @Override
    public OceanFacetDTO update(OceanFacetDTO oceanFacetDTO) {
        log.debug("Request to update OceanFacet : {}", oceanFacetDTO);
        OceanFacet oceanFacet = oceanFacetMapper.toEntity(oceanFacetDTO);
        oceanFacet = oceanFacetRepository.save(oceanFacet);
        return oceanFacetMapper.toDto(oceanFacet);
    }

    @Override
    public Optional<OceanFacetDTO> partialUpdate(OceanFacetDTO oceanFacetDTO) {
        log.debug("Request to partially update OceanFacet : {}", oceanFacetDTO);

        return oceanFacetRepository
            .findById(oceanFacetDTO.getId())
            .map(existingOceanFacet -> {
                oceanFacetMapper.partialUpdate(existingOceanFacet, oceanFacetDTO);

                return existingOceanFacet;
            })
            .map(oceanFacetRepository::save)
            .map(oceanFacetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OceanFacetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OceanFacets");
        return oceanFacetRepository.findAll(pageable).map(oceanFacetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OceanFacetDTO> findOne(Long id) {
        log.debug("Request to get OceanFacet : {}", id);
        return oceanFacetRepository.findById(id).map(oceanFacetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OceanFacet : {}", id);
        oceanFacetRepository.deleteById(id);
    }
}
