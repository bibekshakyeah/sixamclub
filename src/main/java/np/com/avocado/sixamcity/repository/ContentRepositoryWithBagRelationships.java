package np.com.avocado.sixamcity.repository;

import java.util.List;
import java.util.Optional;
import np.com.avocado.sixamcity.domain.Content;
import org.springframework.data.domain.Page;

public interface ContentRepositoryWithBagRelationships {
    Optional<Content> fetchBagRelationships(Optional<Content> content);

    List<Content> fetchBagRelationships(List<Content> contents);

    Page<Content> fetchBagRelationships(Page<Content> contents);
}
