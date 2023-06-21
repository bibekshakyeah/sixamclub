package np.com.avocado.sixamcity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import np.com.avocado.sixamcity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentMedia.class);
        ContentMedia contentMedia1 = new ContentMedia();
        contentMedia1.setId(1L);
        ContentMedia contentMedia2 = new ContentMedia();
        contentMedia2.setId(contentMedia1.getId());
        assertThat(contentMedia1).isEqualTo(contentMedia2);
        contentMedia2.setId(2L);
        assertThat(contentMedia1).isNotEqualTo(contentMedia2);
        contentMedia1.setId(null);
        assertThat(contentMedia1).isNotEqualTo(contentMedia2);
    }
}
