package np.com.avocado.sixamcity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentMediaDTO.class);
        ContentMediaDTO contentMediaDTO1 = new ContentMediaDTO();
        contentMediaDTO1.setId(1L);
        ContentMediaDTO contentMediaDTO2 = new ContentMediaDTO();
        assertThat(contentMediaDTO1).isNotEqualTo(contentMediaDTO2);
        contentMediaDTO2.setId(contentMediaDTO1.getId());
        assertThat(contentMediaDTO1).isEqualTo(contentMediaDTO2);
        contentMediaDTO2.setId(2L);
        assertThat(contentMediaDTO1).isNotEqualTo(contentMediaDTO2);
        contentMediaDTO1.setId(null);
        assertThat(contentMediaDTO1).isNotEqualTo(contentMediaDTO2);
    }
}
