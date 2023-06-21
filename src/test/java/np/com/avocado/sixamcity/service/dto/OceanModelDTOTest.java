package np.com.avocado.sixamcity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OceanModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OceanModelDTO.class);
        OceanModelDTO oceanModelDTO1 = new OceanModelDTO();
        oceanModelDTO1.setId(1L);
        OceanModelDTO oceanModelDTO2 = new OceanModelDTO();
        assertThat(oceanModelDTO1).isNotEqualTo(oceanModelDTO2);
        oceanModelDTO2.setId(oceanModelDTO1.getId());
        assertThat(oceanModelDTO1).isEqualTo(oceanModelDTO2);
        oceanModelDTO2.setId(2L);
        assertThat(oceanModelDTO1).isNotEqualTo(oceanModelDTO2);
        oceanModelDTO1.setId(null);
        assertThat(oceanModelDTO1).isNotEqualTo(oceanModelDTO2);
    }
}
