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
import np.com.avocado.sixamcity.domain.OceanFacet;
import np.com.avocado.sixamcity.repository.OceanFacetRepository;
import np.com.avocado.sixamcity.service.dto.OceanFacetDTO;
import np.com.avocado.sixamcity.service.mapper.OceanFacetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OceanFacetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OceanFacetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/ocean-facets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OceanFacetRepository oceanFacetRepository;

    @Autowired
    private OceanFacetMapper oceanFacetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOceanFacetMockMvc;

    private OceanFacet oceanFacet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OceanFacet createEntity(EntityManager em) {
        OceanFacet oceanFacet = new OceanFacet().name(DEFAULT_NAME).value(DEFAULT_VALUE);
        return oceanFacet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OceanFacet createUpdatedEntity(EntityManager em) {
        OceanFacet oceanFacet = new OceanFacet().name(UPDATED_NAME).value(UPDATED_VALUE);
        return oceanFacet;
    }

    @BeforeEach
    public void initTest() {
        oceanFacet = createEntity(em);
    }

    @Test
    @Transactional
    void createOceanFacet() throws Exception {
        int databaseSizeBeforeCreate = oceanFacetRepository.findAll().size();
        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);
        restOceanFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO)))
            .andExpect(status().isCreated());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeCreate + 1);
        OceanFacet testOceanFacet = oceanFacetList.get(oceanFacetList.size() - 1);
        assertThat(testOceanFacet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOceanFacet.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createOceanFacetWithExistingId() throws Exception {
        // Create the OceanFacet with an existing ID
        oceanFacet.setId(1L);
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        int databaseSizeBeforeCreate = oceanFacetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOceanFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = oceanFacetRepository.findAll().size();
        // set the field null
        oceanFacet.setName(null);

        // Create the OceanFacet, which fails.
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        restOceanFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO)))
            .andExpect(status().isBadRequest());

        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = oceanFacetRepository.findAll().size();
        // set the field null
        oceanFacet.setValue(null);

        // Create the OceanFacet, which fails.
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        restOceanFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO)))
            .andExpect(status().isBadRequest());

        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOceanFacets() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        // Get all the oceanFacetList
        restOceanFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oceanFacet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getOceanFacet() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        // Get the oceanFacet
        restOceanFacetMockMvc
            .perform(get(ENTITY_API_URL_ID, oceanFacet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oceanFacet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOceanFacet() throws Exception {
        // Get the oceanFacet
        restOceanFacetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOceanFacet() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();

        // Update the oceanFacet
        OceanFacet updatedOceanFacet = oceanFacetRepository.findById(oceanFacet.getId()).get();
        // Disconnect from session so that the updates on updatedOceanFacet are not directly saved in db
        em.detach(updatedOceanFacet);
        updatedOceanFacet.name(UPDATED_NAME).value(UPDATED_VALUE);
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(updatedOceanFacet);

        restOceanFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oceanFacetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isOk());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
        OceanFacet testOceanFacet = oceanFacetList.get(oceanFacetList.size() - 1);
        assertThat(testOceanFacet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOceanFacet.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oceanFacetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOceanFacetWithPatch() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();

        // Update the oceanFacet using partial update
        OceanFacet partialUpdatedOceanFacet = new OceanFacet();
        partialUpdatedOceanFacet.setId(oceanFacet.getId());

        partialUpdatedOceanFacet.value(UPDATED_VALUE);

        restOceanFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOceanFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOceanFacet))
            )
            .andExpect(status().isOk());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
        OceanFacet testOceanFacet = oceanFacetList.get(oceanFacetList.size() - 1);
        assertThat(testOceanFacet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOceanFacet.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateOceanFacetWithPatch() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();

        // Update the oceanFacet using partial update
        OceanFacet partialUpdatedOceanFacet = new OceanFacet();
        partialUpdatedOceanFacet.setId(oceanFacet.getId());

        partialUpdatedOceanFacet.name(UPDATED_NAME).value(UPDATED_VALUE);

        restOceanFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOceanFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOceanFacet))
            )
            .andExpect(status().isOk());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
        OceanFacet testOceanFacet = oceanFacetList.get(oceanFacetList.size() - 1);
        assertThat(testOceanFacet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOceanFacet.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oceanFacetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOceanFacet() throws Exception {
        int databaseSizeBeforeUpdate = oceanFacetRepository.findAll().size();
        oceanFacet.setId(count.incrementAndGet());

        // Create the OceanFacet
        OceanFacetDTO oceanFacetDTO = oceanFacetMapper.toDto(oceanFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOceanFacetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oceanFacetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OceanFacet in the database
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOceanFacet() throws Exception {
        // Initialize the database
        oceanFacetRepository.saveAndFlush(oceanFacet);

        int databaseSizeBeforeDelete = oceanFacetRepository.findAll().size();

        // Delete the oceanFacet
        restOceanFacetMockMvc
            .perform(delete(ENTITY_API_URL_ID, oceanFacet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OceanFacet> oceanFacetList = oceanFacetRepository.findAll();
        assertThat(oceanFacetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
