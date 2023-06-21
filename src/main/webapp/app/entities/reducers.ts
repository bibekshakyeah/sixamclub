import contentType from 'app/entities/content-type/content-type.reducer';
import category from 'app/entities/category/category.reducer';
import content from 'app/entities/content/content.reducer';
import oceanModel from 'app/entities/ocean-model/ocean-model.reducer';
import oceanFacet from 'app/entities/ocean-facet/ocean-facet.reducer';
import contentMedia from 'app/entities/content-media/content-media.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  contentType,
  category,
  content,
  oceanModel,
  oceanFacet,
  contentMedia,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
