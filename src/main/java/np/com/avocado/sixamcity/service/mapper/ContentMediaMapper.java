package np.com.avocado.sixamcity.service.mapper;

import np.com.avocado.sixamcity.domain.Content;
import np.com.avocado.sixamcity.domain.ContentMedia;
import np.com.avocado.sixamcity.service.dto.ContentDTO;
import np.com.avocado.sixamcity.service.dto.ContentMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentMedia} and its DTO {@link ContentMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentMediaMapper extends EntityMapper<ContentMediaDTO, ContentMedia> {
    @Mapping(target = "content", source = "content", qualifiedByName = "contentId")
    ContentMediaDTO toDto(ContentMedia s);

    @Named("contentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentDTO toDtoContentId(Content content);
}
