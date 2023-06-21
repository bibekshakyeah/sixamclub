package np.com.avocado.sixamcity.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import np.com.avocado.sixamcity.IntegrationTest;
import np.com.avocado.sixamcity.domain.ContentMedia;
import np.com.avocado.sixamcity.repository.ContentMediaRepository;
import np.com.avocado.sixamcity.service.dto.ContentMediaDTO;
import np.com.avocado.sixamcity.service.mapper.ContentMediaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContentMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentMediaResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/content-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentMediaRepository contentMediaRepository;

    @Autowired
    private ContentMediaMapper contentMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentMediaMockMvc;

    private ContentMedia contentMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentMedia createEntity(EntityManager em) {
        ContentMedia contentMedia = new ContentMedia().path(DEFAULT_PATH).type(DEFAULT_TYPE);
        return contentMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentMedia createUpdatedEntity(EntityManager em) {
        ContentMedia contentMedia = new ContentMedia().path(UPDATED_PATH).type(UPDATED_TYPE);
        return contentMedia;
    }

    @BeforeEach
    public void initTest() {
        contentMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createContentMedia() throws Exception {
        int databaseSizeBeforeCreate = contentMediaRepository.findAll().size();
        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);
        restContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeCreate + 1);
        ContentMedia testContentMedia = contentMediaList.get(contentMediaList.size() - 1);
        assertThat(testContentMedia.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testContentMedia.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createContentMediaWithExistingId() throws Exception {
        // Create the ContentMedia with an existing ID
        contentMedia.setId(1L);
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        int databaseSizeBeforeCreate = contentMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentMediaRepository.findAll().size();
        // set the field null
        contentMedia.setPath(null);

        // Create the ContentMedia, which fails.
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        restContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentMediaRepository.findAll().size();
        // set the field null
        contentMedia.setType(null);

        // Create the ContentMedia, which fails.
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        restContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentMedias() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        // Get all the contentMediaList
        restContentMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getContentMedia() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        // Get the contentMedia
        restContentMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, contentMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentMedia.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingContentMedia() throws Exception {
        // Get the contentMedia
        restContentMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentMedia() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();

        // Update the contentMedia
        ContentMedia updatedContentMedia = contentMediaRepository.findById(contentMedia.getId()).get();
        // Disconnect from session so that the updates on updatedContentMedia are not directly saved in db
        em.detach(updatedContentMedia);
        updatedContentMedia.path(UPDATED_PATH).type(UPDATED_TYPE);
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(updatedContentMedia);

        restContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
        ContentMedia testContentMedia = contentMediaList.get(contentMediaList.size() - 1);
        assertThat(testContentMedia.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testContentMedia.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentMediaWithPatch() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();

        // Update the contentMedia using partial update
        ContentMedia partialUpdatedContentMedia = new ContentMedia();
        partialUpdatedContentMedia.setId(contentMedia.getId());

        restContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentMedia))
            )
            .andExpect(status().isOk());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
        ContentMedia testContentMedia = contentMediaList.get(contentMediaList.size() - 1);
        assertThat(testContentMedia.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testContentMedia.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateContentMediaWithPatch() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();

        // Update the contentMedia using partial update
        ContentMedia partialUpdatedContentMedia = new ContentMedia();
        partialUpdatedContentMedia.setId(contentMedia.getId());

        partialUpdatedContentMedia.path(UPDATED_PATH).type(UPDATED_TYPE);

        restContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentMedia))
            )
            .andExpect(status().isOk());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
        ContentMedia testContentMedia = contentMediaList.get(contentMediaList.size() - 1);
        assertThat(testContentMedia.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testContentMedia.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = contentMediaRepository.findAll().size();
        contentMedia.setId(count.incrementAndGet());

        // Create the ContentMedia
        ContentMediaDTO contentMediaDTO = contentMediaMapper.toDto(contentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentMedia in the database
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentMedia() throws Exception {
        // Initialize the database
        contentMediaRepository.saveAndFlush(contentMedia);

        int databaseSizeBeforeDelete = contentMediaRepository.findAll().size();

        // Delete the contentMedia
        restContentMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentMedia> contentMediaList = contentMediaRepository.findAll();
        assertThat(contentMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
