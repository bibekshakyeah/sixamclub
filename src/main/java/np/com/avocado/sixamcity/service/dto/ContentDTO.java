package np.com.avocado.sixamcity.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link np.com.avocado.sixamcity.domain.Content} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String sourceUrl;

    private String videoUrl;

    private String contentText;

    private ZonedDateTime publishedDate;

    private ContentTypeDTO contentType;

    private Set<CategoryDTO> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public ZonedDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ContentTypeDTO getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeDTO contentType) {
        this.contentType = contentType;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentDTO)) {
            return false;
        }

        ContentDTO contentDTO = (ContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", contentText='" + getContentText() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", contentType=" + getContentType() +
            ", categories=" + getCategories() +
            "}";
    }
}
