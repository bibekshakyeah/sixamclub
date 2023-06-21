package np.com.avocado.sixamcity.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link np.com.avocado.sixamcity.domain.ContentType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentTypeDTO implements Serializable {

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
        if (!(o instanceof ContentTypeDTO)) {
            return false;
        }

        ContentTypeDTO contentTypeDTO = (ContentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
