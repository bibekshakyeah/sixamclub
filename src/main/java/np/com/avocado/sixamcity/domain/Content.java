package np.com.avocado.sixamcity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Content.
 */
@Entity
@Table(name = "content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "published_date")
    private ZonedDateTime publishedDate;

    @OneToMany(mappedBy = "content")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "content" }, allowSetters = true)
    private Set<ContentMedia> contentMedias = new HashSet<>();

    @ManyToOne
    private ContentType contentType;

    @ManyToMany
    @JoinTable(
        name = "rel_content__categories",
        joinColumns = @JoinColumn(name = "content_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contents" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Content id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Content title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public Content sourceUrl(String sourceUrl) {
        this.setSourceUrl(sourceUrl);
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Content videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getContentText() {
        return this.contentText;
    }

    public Content contentText(String contentText) {
        this.setContentText(contentText);
        return this;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public ZonedDateTime getPublishedDate() {
        return this.publishedDate;
    }

    public Content publishedDate(ZonedDateTime publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Set<ContentMedia> getContentMedias() {
        return this.contentMedias;
    }

    public void setContentMedias(Set<ContentMedia> contentMedias) {
        if (this.contentMedias != null) {
            this.contentMedias.forEach(i -> i.setContent(null));
        }
        if (contentMedias != null) {
            contentMedias.forEach(i -> i.setContent(this));
        }
        this.contentMedias = contentMedias;
    }

    public Content contentMedias(Set<ContentMedia> contentMedias) {
        this.setContentMedias(contentMedias);
        return this;
    }

    public Content addContentMedias(ContentMedia contentMedia) {
        this.contentMedias.add(contentMedia);
        contentMedia.setContent(this);
        return this;
    }

    public Content removeContentMedias(ContentMedia contentMedia) {
        this.contentMedias.remove(contentMedia);
        contentMedia.setContent(null);
        return this;
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Content contentType(ContentType contentType) {
        this.setContentType(contentType);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Content categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Content addCategories(Category category) {
        this.categories.add(category);
        category.getContents().add(this);
        return this;
    }

    public Content removeCategories(Category category) {
        this.categories.remove(category);
        category.getContents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        return id != null && id.equals(((Content) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Content{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", contentText='" + getContentText() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            "}";
    }
}
