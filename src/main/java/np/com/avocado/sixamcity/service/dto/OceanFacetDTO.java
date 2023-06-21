package np.com.avocado.sixamcity.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link np.com.avocado.sixamcity.domain.OceanFacet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OceanFacetDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double value;

    private OceanModelDTO oceanModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public OceanModelDTO getOceanModel() {
        return oceanModel;
    }

    public void setOceanModel(OceanModelDTO oceanModel) {
        this.oceanModel = oceanModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OceanFacetDTO)) {
            return false;
        }

        OceanFacetDTO oceanFacetDTO = (OceanFacetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, oceanFacetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OceanFacetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", oceanModel=" + getOceanModel() +
            "}";
    }
}
