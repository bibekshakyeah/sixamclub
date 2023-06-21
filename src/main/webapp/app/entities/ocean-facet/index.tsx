import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OceanFacet from './ocean-facet';
import OceanFacetDetail from './ocean-facet-detail';
import OceanFacetUpdate from './ocean-facet-update';
import OceanFacetDeleteDialog from './ocean-facet-delete-dialog';

const OceanFacetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OceanFacet />} />
    <Route path="new" element={<OceanFacetUpdate />} />
    <Route path=":id">
      <Route index element={<OceanFacetDetail />} />
      <Route path="edit" element={<OceanFacetUpdate />} />
      <Route path="delete" element={<OceanFacetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OceanFacetRoutes;
