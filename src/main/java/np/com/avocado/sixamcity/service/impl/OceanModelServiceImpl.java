package np.com.avocado.sixamcity.service.impl;

import java.util.Optional;
import np.com.avocado.sixamcity.domain.OceanModel;
import np.com.avocado.sixamcity.repository.OceanModelRepository;
import np.com.avocado.sixamcity.service.OceanModelService;
import np.com.avocado.sixamcity.service.dto.OceanModelDTO;
import np.com.avocado.sixamcity.service.mapper.OceanModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OceanModel}.
 */
@Service
@Transactional
public class OceanModelServiceImpl implements OceanModelService {

    private final Logger log = LoggerFactory.getLogger(OceanModelServiceImpl.class);

    private final OceanModelRepository oceanModelRepository;

    private final OceanModelMapper oceanModelMapper;

    public OceanModelServiceImpl(OceanModelRepository oceanModelRepository, OceanModelMapper oceanModelMapper) {
        this.oceanModelRepository = oceanModelRepository;
        this.oceanModelMapper = oceanModelMapper;
    }

    @Override
    public OceanModelDTO save(OceanModelDTO oceanModelDTO) {
        log.debug("Request to save OceanModel : {}", oceanModelDTO);
        OceanModel oceanModel = oceanModelMapper.toEntity(oceanModelDTO);
        oceanModel = oceanModelRepository.save(oceanModel);
        return oceanModelMapper.toDto(oceanModel);
    }

    @Override
    public OceanModelDTO update(OceanModelDTO oceanModelDTO) {
        log.debug("Request to update OceanModel : {}", oceanModelDTO);
        OceanModel oceanModel = oceanModelMapper.toEntity(oceanModelDTO);
        oceanModel = oceanModelRepository.save(oceanModel);
        return oceanModelMapper.toDto(oceanModel);
    }

    @Override
    public Optional<OceanModelDTO> partialUpdate(OceanModelDTO oceanModelDTO) {
        log.debug("Request to partially update OceanModel : {}", oceanModelDTO);

        return oceanModelRepository
            .findById(oceanModelDTO.getId())
            .map(existingOceanModel -> {
                oceanModelMapper.partialUpdate(existingOceanModel, oceanModelDTO);

                return existingOceanModel;
            })
            .map(oceanModelRepository::save)
            .map(oceanModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OceanModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OceanModels");
        return oceanModelRepository.findAll(pageable).map(oceanModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OceanModelDTO> findOne(Long id) {
        log.debug("Request to get OceanModel : {}", id);
        return oceanModelRepository.findById(id).map(oceanModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OceanModel : {}", id);
        oceanModelRepository.deleteById(id);
    }
}
