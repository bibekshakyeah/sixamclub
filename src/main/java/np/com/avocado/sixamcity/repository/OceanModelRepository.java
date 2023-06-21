package np.com.avocado.sixamcity.repository;

import np.com.avocado.sixamcity.domain.OceanModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OceanModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OceanModelRepository extends JpaRepository<OceanModel, Long> {}
