package np.com.avocado.sixamcity.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentTypeDTO.class);
        ContentTypeDTO contentTypeDTO1 = new ContentTypeDTO();
        contentTypeDTO1.setId(1L);
        ContentTypeDTO contentTypeDTO2 = new ContentTypeDTO();
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
        contentTypeDTO2.setId(contentTypeDTO1.getId());
        assertThat(contentTypeDTO1).isEqualTo(contentTypeDTO2);
        contentTypeDTO2.setId(2L);
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
        contentTypeDTO1.setId(null);
        assertThat(contentTypeDTO1).isNotEqualTo(contentTypeDTO2);
    }
}
