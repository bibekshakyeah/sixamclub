package np.com.avocado.sixamcity.repository;

import np.com.avocado.sixamcity.domain.OceanFacet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OceanFacet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OceanFacetRepository extends JpaRepository<OceanFacet, Long> {}
