package np.com.avocado.sixamcity.service.mapper;

import np.com.avocado.sixamcity.domain.OceanFacet;
import np.com.avocado.sixamcity.domain.OceanModel;
import np.com.avocado.sixamcity.service.dto.OceanFacetDTO;
import np.com.avocado.sixamcity.service.dto.OceanModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OceanFacet} and its DTO {@link OceanFacetDTO}.
 */
@Mapper(componentModel = "spring")
public interface OceanFacetMapper extends EntityMapper<OceanFacetDTO, OceanFacet> {
    @Mapping(target = "oceanModel", source = "oceanModel", qualifiedByName = "oceanModelId")
    OceanFacetDTO toDto(OceanFacet s);

    @Named("oceanModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OceanModelDTO toDtoOceanModelId(OceanModel oceanModel);
}
