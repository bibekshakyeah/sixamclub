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
import np.com.avocado.sixamcity.domain.ContentType;
import np.com.avocado.sixamcity.repository.ContentTypeRepository;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
import np.com.avocado.sixamcity.service.mapper.ContentTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/content-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private ContentTypeMapper contentTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentTypeMockMvc;

    private ContentType contentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createEntity(EntityManager em) {
        ContentType contentType = new ContentType().name(DEFAULT_NAME);
        return contentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createUpdatedEntity(EntityManager em) {
        ContentType contentType = new ContentType().name(UPDATED_NAME);
        return contentType;
    }

    @BeforeEach
    public void initTest() {
        contentType = createEntity(em);
    }

    @Test
    @Transactional
    void createContentType() throws Exception {
        int databaseSizeBeforeCreate = contentTypeRepository.findAll().size();
        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);
        restContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createContentTypeWithExistingId() throws Exception {
        // Create the ContentType with an existing ID
        contentType.setId(1L);
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        int databaseSizeBeforeCreate = contentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentTypeRepository.findAll().size();
        // set the field null
        contentType.setName(null);

        // Create the ContentType, which fails.
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        restContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentTypes() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getContentType() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        // Get the contentType
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, contentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingContentType() throws Exception {
        // Get the contentType
        restContentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentType() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();

        // Update the contentType
        ContentType updatedContentType = contentTypeRepository.findById(contentType.getId()).get();
        // Disconnect from session so that the updates on updatedContentType are not directly saved in db
        em.detach(updatedContentType);
        updatedContentType.name(UPDATED_NAME);
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(updatedContentType);

        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentTypeWithPatch() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();

        // Update the contentType using partial update
        ContentType partialUpdatedContentType = new ContentType();
        partialUpdatedContentType.setId(contentType.getId());

        partialUpdatedContentType.name(UPDATED_NAME);

        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentType))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateContentTypeWithPatch() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();

        // Update the contentType using partial update
        ContentType partialUpdatedContentType = new ContentType();
        partialUpdatedContentType.setId(contentType.getId());

        partialUpdatedContentType.name(UPDATED_NAME);

        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentType))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
        ContentType testContentType = contentTypeList.get(contentTypeList.size() - 1);
        assertThat(testContentType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentType() throws Exception {
        int databaseSizeBeforeUpdate = contentTypeRepository.findAll().size();
        contentType.setId(count.incrementAndGet());

        // Create the ContentType
        ContentTypeDTO contentTypeDTO = contentTypeMapper.toDto(contentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentType in the database
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentType() throws Exception {
        // Initialize the database
        contentTypeRepository.saveAndFlush(contentType);

        int databaseSizeBeforeDelete = contentTypeRepository.findAll().size();

        // Delete the contentType
        restContentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentType> contentTypeList = contentTypeRepository.findAll();
        assertThat(contentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
