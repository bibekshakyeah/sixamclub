package np.com.avocado.sixamcity.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OceanModelMapperTest {

    private OceanModelMapper oceanModelMapper;

    @BeforeEach
    public void setUp() {
        oceanModelMapper = new OceanModelMapperImpl();
    }
}
