package np.com.avocado.sixamcity.service.mapper;

import np.com.avocado.sixamcity.domain.ContentType;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentType} and its DTO {@link ContentTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentTypeMapper extends EntityMapper<ContentTypeDTO, ContentType> {}
