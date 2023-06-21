import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentType from './content-type';
import Category from './category';
import Content from './content';
import OceanModel from './ocean-model';
import OceanFacet from './ocean-facet';
import ContentMedia from './content-media';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="content-type/*" element={<ContentType />} />
        <Route path="category/*" element={<Category />} />
        <Route path="content/*" element={<Content />} />
        <Route path="ocean-model/*" element={<OceanModel />} />
        <Route path="ocean-facet/*" element={<OceanFacet />} />
        <Route path="content-media/*" element={<ContentMedia />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
