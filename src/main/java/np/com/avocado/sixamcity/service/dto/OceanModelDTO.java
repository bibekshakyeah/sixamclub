package np.com.avocado.sixamcity.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link np.com.avocado.sixamcity.domain.OceanModel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OceanModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OceanModelDTO)) {
            return false;
        }

        OceanModelDTO oceanModelDTO = (OceanModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, oceanModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OceanModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
