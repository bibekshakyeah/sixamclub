package np.com.avocado.sixamcity.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import np.com.avocado.sixamcity.domain.Category;
import np.com.avocado.sixamcity.domain.Content;
import np.com.avocado.sixamcity.domain.ContentType;
import np.com.avocado.sixamcity.service.dto.CategoryDTO;
import np.com.avocado.sixamcity.service.dto.ContentDTO;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Content} and its DTO {@link ContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentMapper extends EntityMapper<ContentDTO, Content> {
    @Mapping(target = "contentType", source = "contentType", qualifiedByName = "contentTypeId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    ContentDTO toDto(Content s);

    @Mapping(target = "removeCategories", ignore = true)
    Content toEntity(ContentDTO contentDTO);

    @Named("contentTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentTypeDTO toDtoContentTypeId(ContentType contentType);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }
}
