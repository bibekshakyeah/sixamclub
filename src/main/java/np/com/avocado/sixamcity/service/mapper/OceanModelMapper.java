package np.com.avocado.sixamcity.service.mapper;

import np.com.avocado.sixamcity.domain.OceanModel;
import np.com.avocado.sixamcity.service.dto.OceanModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OceanModel} and its DTO {@link OceanModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface OceanModelMapper extends EntityMapper<OceanModelDTO, OceanModel> {}
