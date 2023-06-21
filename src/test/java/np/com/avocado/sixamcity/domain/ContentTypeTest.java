package np.com.avocado.sixamcity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentType.class);
        ContentType contentType1 = new ContentType();
        contentType1.setId(1L);
        ContentType contentType2 = new ContentType();
        contentType2.setId(contentType1.getId());
        assertThat(contentType1).isEqualTo(contentType2);
        contentType2.setId(2L);
        assertThat(contentType1).isNotEqualTo(contentType2);
        contentType1.setId(null);
        assertThat(contentType1).isNotEqualTo(contentType2);
    }
}
