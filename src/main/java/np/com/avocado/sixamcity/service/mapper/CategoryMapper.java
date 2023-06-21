package np.com.avocado.sixamcity.service.mapper;

import np.com.avocado.sixamcity.domain.Category;
import np.com.avocado.sixamcity.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
