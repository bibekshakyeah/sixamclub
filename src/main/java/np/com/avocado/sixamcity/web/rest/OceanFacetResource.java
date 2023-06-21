package np.com.avocado.sixamcity.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import np.com.avocado.sixamcity.repository.OceanFacetRepository;
import np.com.avocado.sixamcity.service.OceanFacetService;
import np.com.avocado.sixamcity.service.dto.OceanFacetDTO;
import np.com.avocado.sixamcity.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link np.com.avocado.sixamcity.domain.OceanFacet}.
 */
@RestController
@RequestMapping("/api")
public class OceanFacetResource {

    private final Logger log = LoggerFactory.getLogger(OceanFacetResource.class);

    private static final String ENTITY_NAME = "oceanFacet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OceanFacetService oceanFacetService;

    private final OceanFacetRepository oceanFacetRepository;

    public OceanFacetResource(OceanFacetService oceanFacetService, OceanFacetRepository oceanFacetRepository) {
        this.oceanFacetService = oceanFacetService;
        this.oceanFacetRepository = oceanFacetRepository;
    }

    /**
     * {@code POST  /ocean-facets} : Create a new oceanFacet.
     *
     * @param oceanFacetDTO the oceanFacetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oceanFacetDTO, or with status {@code 400 (Bad Request)} if the oceanFacet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ocean-facets")
    public ResponseEntity<OceanFacetDTO> createOceanFacet(@Valid @RequestBody OceanFacetDTO oceanFacetDTO) throws URISyntaxException {
        log.debug("REST request to save OceanFacet : {}", oceanFacetDTO);
        if (oceanFacetDTO.getId() != null) {
            throw new BadRequestAlertException("A new oceanFacet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OceanFacetDTO result = oceanFacetService.save(oceanFacetDTO);
        return ResponseEntity
            .created(new URI("/api/ocean-facets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ocean-facets/:id} : Updates an existing oceanFacet.
     *
     * @param id the id of the oceanFacetDTO to save.
     * @param oceanFacetDTO the oceanFacetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oceanFacetDTO,
     * or with status {@code 400 (Bad Request)} if the oceanFacetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oceanFacetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ocean-facets/{id}")
    public ResponseEntity<OceanFacetDTO> updateOceanFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OceanFacetDTO oceanFacetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OceanFacet : {}, {}", id, oceanFacetDTO);
        if (oceanFacetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oceanFacetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oceanFacetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OceanFacetDTO result = oceanFacetService.update(oceanFacetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oceanFacetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ocean-facets/:id} : Partial updates given fields of an existing oceanFacet, field will ignore if it is null
     *
     * @param id the id of the oceanFacetDTO to save.
     * @param oceanFacetDTO the oceanFacetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oceanFacetDTO,
     * or with status {@code 400 (Bad Request)} if the oceanFacetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the oceanFacetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the oceanFacetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ocean-facets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OceanFacetDTO> partialUpdateOceanFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OceanFacetDTO oceanFacetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OceanFacet partially : {}, {}", id, oceanFacetDTO);
        if (oceanFacetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oceanFacetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oceanFacetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OceanFacetDTO> result = oceanFacetService.partialUpdate(oceanFacetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oceanFacetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocean-facets} : get all the oceanFacets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oceanFacets in body.
     */
    @GetMapping("/ocean-facets")
    public ResponseEntity<List<OceanFacetDTO>> getAllOceanFacets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OceanFacets");
        Page<OceanFacetDTO> page = oceanFacetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocean-facets/:id} : get the "id" oceanFacet.
     *
     * @param id the id of the oceanFacetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oceanFacetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ocean-facets/{id}")
    public ResponseEntity<OceanFacetDTO> getOceanFacet(@PathVariable Long id) {
        log.debug("REST request to get OceanFacet : {}", id);
        Optional<OceanFacetDTO> oceanFacetDTO = oceanFacetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oceanFacetDTO);
    }

    /**
     * {@code DELETE  /ocean-facets/:id} : delete the "id" oceanFacet.
     *
     * @param id the id of the oceanFacetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ocean-facets/{id}")
    public ResponseEntity<Void> deleteOceanFacet(@PathVariable Long id) {
        log.debug("REST request to delete OceanFacet : {}", id);
        oceanFacetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
