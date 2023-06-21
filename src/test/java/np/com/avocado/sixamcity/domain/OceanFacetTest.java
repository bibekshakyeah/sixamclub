package np.com.avocado.sixamcity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OceanFacetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OceanFacet.class);
        OceanFacet oceanFacet1 = new OceanFacet();
        oceanFacet1.setId(1L);
        OceanFacet oceanFacet2 = new OceanFacet();
        oceanFacet2.setId(oceanFacet1.getId());
        assertThat(oceanFacet1).isEqualTo(oceanFacet2);
        oceanFacet2.setId(2L);
        assertThat(oceanFacet1).isNotEqualTo(oceanFacet2);
        oceanFacet1.setId(null);
        assertThat(oceanFacet1).isNotEqualTo(oceanFacet2);
    }
}
