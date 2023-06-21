package np.com.avocado.sixamcity.repository;

import np.com.avocado.sixamcity.domain.ContentMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContentMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentMediaRepository extends JpaRepository<ContentMedia, Long> {}
