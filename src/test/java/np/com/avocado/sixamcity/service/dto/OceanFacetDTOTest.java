package np.com.avocado.sixamcity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OceanFacetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OceanFacetDTO.class);
        OceanFacetDTO oceanFacetDTO1 = new OceanFacetDTO();
        oceanFacetDTO1.setId(1L);
        OceanFacetDTO oceanFacetDTO2 = new OceanFacetDTO();
        assertThat(oceanFacetDTO1).isNotEqualTo(oceanFacetDTO2);
        oceanFacetDTO2.setId(oceanFacetDTO1.getId());
        assertThat(oceanFacetDTO1).isEqualTo(oceanFacetDTO2);
        oceanFacetDTO2.setId(2L);
        assertThat(oceanFacetDTO1).isNotEqualTo(oceanFacetDTO2);
        oceanFacetDTO1.setId(null);
        assertThat(oceanFacetDTO1).isNotEqualTo(oceanFacetDTO2);
    }
}
