package np.com.avocado.sixamcity.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import np.com.avocado.sixamcity.repository.ContentMediaRepository;
import np.com.avocado.sixamcity.service.ContentMediaService;
import np.com.avocado.sixamcity.service.dto.ContentMediaDTO;
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
 * REST controller for managing {@link np.com.avocado.sixamcity.domain.ContentMedia}.
 */
@RestController
@RequestMapping("/api")
public class ContentMediaResource {

    private final Logger log = LoggerFactory.getLogger(ContentMediaResource.class);

    private static final String ENTITY_NAME = "contentMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentMediaService contentMediaService;

    private final ContentMediaRepository contentMediaRepository;

    public ContentMediaResource(ContentMediaService contentMediaService, ContentMediaRepository contentMediaRepository) {
        this.contentMediaService = contentMediaService;
        this.contentMediaRepository = contentMediaRepository;
    }

    /**
     * {@code POST  /content-medias} : Create a new contentMedia.
     *
     * @param contentMediaDTO the contentMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentMediaDTO, or with status {@code 400 (Bad Request)} if the contentMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-medias")
    public ResponseEntity<ContentMediaDTO> createContentMedia(@Valid @RequestBody ContentMediaDTO contentMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContentMedia : {}", contentMediaDTO);
        if (contentMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentMediaDTO result = contentMediaService.save(contentMediaDTO);
        return ResponseEntity
            .created(new URI("/api/content-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-medias/:id} : Updates an existing contentMedia.
     *
     * @param id the id of the contentMediaDTO to save.
     * @param contentMediaDTO the contentMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentMediaDTO,
     * or with status {@code 400 (Bad Request)} if the contentMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-medias/{id}")
    public ResponseEntity<ContentMediaDTO> updateContentMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentMediaDTO contentMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContentMedia : {}, {}", id, contentMediaDTO);
        if (contentMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentMediaDTO result = contentMediaService.update(contentMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-medias/:id} : Partial updates given fields of an existing contentMedia, field will ignore if it is null
     *
     * @param id the id of the contentMediaDTO to save.
     * @param contentMediaDTO the contentMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentMediaDTO,
     * or with status {@code 400 (Bad Request)} if the contentMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contentMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/content-medias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentMediaDTO> partialUpdateContentMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentMediaDTO contentMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentMedia partially : {}, {}", id, contentMediaDTO);
        if (contentMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentMediaDTO> result = contentMediaService.partialUpdate(contentMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /content-medias} : get all the contentMedias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentMedias in body.
     */
    @GetMapping("/content-medias")
    public ResponseEntity<List<ContentMediaDTO>> getAllContentMedias(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContentMedias");
        Page<ContentMediaDTO> page = contentMediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /content-medias/:id} : get the "id" contentMedia.
     *
     * @param id the id of the contentMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-medias/{id}")
    public ResponseEntity<ContentMediaDTO> getContentMedia(@PathVariable Long id) {
        log.debug("REST request to get ContentMedia : {}", id);
        Optional<ContentMediaDTO> contentMediaDTO = contentMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentMediaDTO);
    }

    /**
     * {@code DELETE  /content-medias/:id} : delete the "id" contentMedia.
     *
     * @param id the id of the contentMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-medias/{id}")
    public ResponseEntity<Void> deleteContentMedia(@PathVariable Long id) {
        log.debug("REST request to delete ContentMedia : {}", id);
        contentMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
