package np.com.avocado.sixamcity.web.rest;

import static np.com.avocado.sixamcity.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import np.com.avocado.sixamcity.IntegrationTest;
import np.com.avocado.sixamcity.domain.Content;
import np.com.avocado.sixamcity.repository.ContentRepository;
import np.com.avocado.sixamcity.service.ContentService;
import np.com.avocado.sixamcity.service.dto.ContentDTO;
import np.com.avocado.sixamcity.service.mapper.ContentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISHED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentRepository contentRepository;

    @Mock
    private ContentRepository contentRepositoryMock;

    @Autowired
    private ContentMapper contentMapper;

    @Mock
    private ContentService contentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentMockMvc;

    private Content content;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createEntity(EntityManager em) {
        Content content = new Content()
            .title(DEFAULT_TITLE)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .videoUrl(DEFAULT_VIDEO_URL)
            .contentText(DEFAULT_CONTENT_TEXT)
            .publishedDate(DEFAULT_PUBLISHED_DATE);
        return content;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createUpdatedEntity(EntityManager em) {
        Content content = new Content()
            .title(UPDATED_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .videoUrl(UPDATED_VIDEO_URL)
            .contentText(UPDATED_CONTENT_TEXT)
            .publishedDate(UPDATED_PUBLISHED_DATE);
        return content;
    }

    @BeforeEach
    public void initTest() {
        content = createEntity(em);
    }

    @Test
    @Transactional
    void createContent() throws Exception {
        int databaseSizeBeforeCreate = contentRepository.findAll().size();
        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);
        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentDTO)))
            .andExpect(status().isCreated());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate + 1);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContent.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testContent.getVideoUrl()).isEqualTo(DEFAULT_VIDEO_URL);
        assertThat(testContent.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testContent.getPublishedDate()).isEqualTo(DEFAULT_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void createContentWithExistingId() throws Exception {
        // Create the Content with an existing ID
        content.setId(1L);
        ContentDTO contentDTO = contentMapper.toDto(content);

        int databaseSizeBeforeCreate = contentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentRepository.findAll().size();
        // set the field null
        content.setTitle(null);

        // Create the Content, which fails.
        ContentDTO contentDTO = contentMapper.toDto(content);

        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentDTO)))
            .andExpect(status().isBadRequest());

        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContents() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        // Get all the contentList
        restContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(content.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        // Get the content
        restContentMockMvc
            .perform(get(ENTITY_API_URL_ID, content.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(content.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL))
            .andExpect(jsonPath("$.contentText").value(DEFAULT_CONTENT_TEXT))
            .andExpect(jsonPath("$.publishedDate").value(sameInstant(DEFAULT_PUBLISHED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingContent() throws Exception {
        // Get the content
        restContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeUpdate = contentRepository.findAll().size();

        // Update the content
        Content updatedContent = contentRepository.findById(content.getId()).get();
        // Disconnect from session so that the updates on updatedContent are not directly saved in db
        em.detach(updatedContent);
        updatedContent
            .title(UPDATED_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .videoUrl(UPDATED_VIDEO_URL)
            .contentText(UPDATED_CONTENT_TEXT)
            .publishedDate(UPDATED_PUBLISHED_DATE);
        ContentDTO contentDTO = contentMapper.toDto(updatedContent);

        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContent.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testContent.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testContent.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testContent.getPublishedDate()).isEqualTo(UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentWithPatch() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeUpdate = contentRepository.findAll().size();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent.title(UPDATED_TITLE).videoUrl(UPDATED_VIDEO_URL).contentText(UPDATED_CONTENT_TEXT);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContent.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testContent.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testContent.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testContent.getPublishedDate()).isEqualTo(DEFAULT_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateContentWithPatch() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeUpdate = contentRepository.findAll().size();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent
            .title(UPDATED_TITLE)
            .sourceUrl(UPDATED_SOURCE_URL)
            .videoUrl(UPDATED_VIDEO_URL)
            .contentText(UPDATED_CONTENT_TEXT)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContent.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testContent.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testContent.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testContent.getPublishedDate()).isEqualTo(UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();
        content.setId(count.incrementAndGet());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeDelete = contentRepository.findAll().size();

        // Delete the content
        restContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, content.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
