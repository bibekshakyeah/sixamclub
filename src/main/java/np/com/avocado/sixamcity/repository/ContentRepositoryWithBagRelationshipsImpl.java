package np.com.avocado.sixamcity.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import np.com.avocado.sixamcity.domain.Content;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ContentRepositoryWithBagRelationshipsImpl implements ContentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Content> fetchBagRelationships(Optional<Content> content) {
        return content.map(this::fetchCategories);
    }

    @Override
    public Page<Content> fetchBagRelationships(Page<Content> contents) {
        return new PageImpl<>(fetchBagRelationships(contents.getContent()), contents.getPageable(), contents.getTotalElements());
    }

    @Override
    public List<Content> fetchBagRelationships(List<Content> contents) {
        return Optional.of(contents).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    Content fetchCategories(Content result) {
        return entityManager
            .createQuery("select content from Content content left join fetch content.categories where content is :content", Content.class)
            .setParameter("content", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Content> fetchCategories(List<Content> contents) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, contents.size()).forEach(index -> order.put(contents.get(index).getId(), index));
        List<Content> result = entityManager
            .createQuery(
                "select distinct content from Content content left join fetch content.categories where content in :contents",
                Content.class
            )
            .setParameter("contents", contents)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
