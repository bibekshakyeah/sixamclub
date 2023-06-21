package np.com.avocado.sixamcity.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link np.com.avocado.sixamcity.domain.ContentMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String path;

    @NotNull
    private String type;

    private ContentDTO content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentMediaDTO)) {
            return false;
        }

        ContentMediaDTO contentMediaDTO = (ContentMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contentMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentMediaDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", content=" + getContent() +
            "}";
    }
}
