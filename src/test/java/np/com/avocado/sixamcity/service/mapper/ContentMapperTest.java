package np.com.avocado.sixamcity.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContentMapperTest {

    private ContentMapper contentMapper;

    @BeforeEach
    public void setUp() {
        contentMapper = new ContentMapperImpl();
    }
}
