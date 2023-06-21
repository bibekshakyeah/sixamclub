package np.com.avocado.sixamcity.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import np.com.avocado.sixamcity.repository.ContentTypeRepository;
import np.com.avocado.sixamcity.service.ContentTypeService;
import np.com.avocado.sixamcity.service.dto.ContentTypeDTO;
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
 * REST controller for managing {@link np.com.avocado.sixamcity.domain.ContentType}.
 */
@RestController
@RequestMapping("/api")
public class ContentTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContentTypeResource.class);

    private static final String ENTITY_NAME = "contentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentTypeService contentTypeService;

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeResource(ContentTypeService contentTypeService, ContentTypeRepository contentTypeRepository) {
        this.contentTypeService = contentTypeService;
        this.contentTypeRepository = contentTypeRepository;
    }

    /**
     * {@code POST  /content-types} : Create a new contentType.
     *
     * @param contentTypeDTO the contentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentTypeDTO, or with status {@code 400 (Bad Request)} if the contentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-types")
    public ResponseEntity<ContentTypeDTO> createContentType(@Valid @RequestBody ContentTypeDTO contentTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ContentType : {}", contentTypeDTO);
        if (contentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentTypeDTO result = contentTypeService.save(contentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/content-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-types/:id} : Updates an existing contentType.
     *
     * @param id the id of the contentTypeDTO to save.
     * @param contentTypeDTO the contentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the contentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-types/{id}")
    public ResponseEntity<ContentTypeDTO> updateContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentTypeDTO contentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContentType : {}, {}", id, contentTypeDTO);
        if (contentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentTypeDTO result = contentTypeService.update(contentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-types/:id} : Partial updates given fields of an existing contentType, field will ignore if it is null
     *
     * @param id the id of the contentTypeDTO to save.
     * @param contentTypeDTO the contentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the contentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/content-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentTypeDTO> partialUpdateContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentTypeDTO contentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentType partially : {}, {}", id, contentTypeDTO);
        if (contentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentTypeDTO> result = contentTypeService.partialUpdate(contentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /content-types} : get all the contentTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentTypes in body.
     */
    @GetMapping("/content-types")
    public ResponseEntity<List<ContentTypeDTO>> getAllContentTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContentTypes");
        Page<ContentTypeDTO> page = contentTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /content-types/:id} : get the "id" contentType.
     *
     * @param id the id of the contentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-types/{id}")
    public ResponseEntity<ContentTypeDTO> getContentType(@PathVariable Long id) {
        log.debug("REST request to get ContentType : {}", id);
        Optional<ContentTypeDTO> contentTypeDTO = contentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentTypeDTO);
    }

    /**
     * {@code DELETE  /content-types/:id} : delete the "id" contentType.
     *
     * @param id the id of the contentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-types/{id}")
    public ResponseEntity<Void> deleteContentType(@PathVariable Long id) {
        log.debug("REST request to delete ContentType : {}", id);
        contentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
