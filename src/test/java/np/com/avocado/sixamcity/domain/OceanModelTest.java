package np.com.avocado.sixamcity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OceanModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OceanModel.class);
        OceanModel oceanModel1 = new OceanModel();
        oceanModel1.setId(1L);
        OceanModel oceanModel2 = new OceanModel();
        oceanModel2.setId(oceanModel1.getId());
        assertThat(oceanModel1).isEqualTo(oceanModel2);
        oceanModel2.setId(2L);
        assertThat(oceanModel1).isNotEqualTo(oceanModel2);
        oceanModel1.setId(null);
        assertThat(oceanModel1).isNotEqualTo(oceanModel2);
    }
}
