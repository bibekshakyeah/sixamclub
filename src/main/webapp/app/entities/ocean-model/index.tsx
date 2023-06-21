import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OceanModel from './ocean-model';
import OceanModelDetail from './ocean-model-detail';
import OceanModelUpdate from './ocean-model-update';
import OceanModelDeleteDialog from './ocean-model-delete-dialog';

const OceanModelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OceanModel />} />
    <Route path="new" element={<OceanModelUpdate />} />
    <Route path=":id">
      <Route index element={<OceanModelDetail />} />
      <Route path="edit" element={<OceanModelUpdate />} />
      <Route path="delete" element={<OceanModelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OceanModelRoutes;
