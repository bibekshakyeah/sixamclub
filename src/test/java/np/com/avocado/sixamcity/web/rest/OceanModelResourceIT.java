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
import np.com.avocado.sixamcity.domain.OceanModel;
import np.com.avocado.sixamcity.repository.OceanModelRepository;
import np.com.avocado.sixamcity.service.dto.OceanModelDTO;
import np.com.avocado.sixamcity.service.mapper.OceanModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OceanModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OceanModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ocean-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OceanModelRepository oceanModelRepository;

    @Autowired
    private OceanModelMapper oceanModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOceanModelMockMvc;

    private OceanModel oceanModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OceanModel createEntity(EntityManager em) {
        OceanModel oceanModel = new OceanModel().name(DEFAULT_NAME);
        return oceanModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OceanModel createUpdatedEntity(EntityManager em) {
        OceanModel oceanModel = new OceanModel().name(UPDATED_NAME);
        return oceanModel;
    }

    @BeforeEach
    public void initTest() {
        oceanModel = createEntity(em);
    }

    @Test
    @Transactional
    void createOceanModel() throws Exception {
        int databaseSizeBeforeCreate = oceanModelRepository.findAll().size();
        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);
        restOceanModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanModelDTO)))
            .andExpect(status().isCreated());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeCreate + 1);
        OceanModel testOceanModel = oceanModelList.get(oceanModelList.size() - 1);
        assertThat(testOceanModel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOceanModelWithExistingId() throws Exception {
        // Create the OceanModel with an existing ID
        oceanModel.setId(1L);
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        int databaseSizeBeforeCreate = oceanModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOceanModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = oceanModelRepository.findAll().size();
        // set the field null
        oceanModel.setName(null);

        // Create the OceanModel, which fails.
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        restOceanModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanModelDTO)))
            .andExpect(status().isBadRequest());

        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOceanModels() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        // Get all the oceanModelList
        restOceanModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oceanModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOceanModel() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        // Get the oceanModel
        restOceanModelMockMvc
            .perform(get(ENTITY_API_URL_ID, oceanModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oceanModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOceanModel() throws Exception {
        // Get the oceanModel
        restOceanModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOceanModel() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();

        // Update the oceanModel
        OceanModel updatedOceanModel = oceanModelRepository.findById(oceanModel.getId()).get();
        // Disconnect from session so that the updates on updatedOceanModel are not directly saved in db
        em.detach(updatedOceanModel);
        updatedOceanModel.name(UPDATED_NAME);
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(updatedOceanModel);

        restOceanModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oceanModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
        OceanModel testOceanModel = oceanModelList.get(oceanModelList.size() - 1);
        assertThat(testOceanModel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oceanModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOceanModelWithPatch() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();

        // Update the oceanModel using partial update
        OceanModel partialUpdatedOceanModel = new OceanModel();
        partialUpdatedOceanModel.setId(oceanModel.getId());

        restOceanModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOceanModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOceanModel))
            )
            .andExpect(status().isOk());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
        OceanModel testOceanModel = oceanModelList.get(oceanModelList.size() - 1);
        assertThat(testOceanModel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOceanModelWithPatch() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();

        // Update the oceanModel using partial update
        OceanModel partialUpdatedOceanModel = new OceanModel();
        partialUpdatedOceanModel.setId(oceanModel.getId());

        partialUpdatedOceanModel.name(UPDATED_NAME);

        restOceanModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOceanModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOceanModel))
            )
            .andExpect(status().isOk());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
        OceanModel testOceanModel = oceanModelList.get(oceanModelList.size() - 1);
        assertThat(testOceanModel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oceanModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOceanModel() throws Exception {
        int databaseSizeBeforeUpdate = oceanModelRepository.findAll().size();
        oceanModel.setId(count.incrementAndGet());

        // Create the OceanModel
        OceanModelDTO oceanModelDTO = oceanModelMapper.toDto(oceanModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oceanModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OceanModel in the database
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOceanModel() throws Exception {
        // Initialize the database
        oceanModelRepository.saveAndFlush(oceanModel);

        int databaseSizeBeforeDelete = oceanModelRepository.findAll().size();

        // Delete the oceanModel
        restOceanModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, oceanModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OceanModel> oceanModelList = oceanModelRepository.findAll();
        assertThat(oceanModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
