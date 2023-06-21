import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentType from './content-type';
import ContentTypeDetail from './content-type-detail';
import ContentTypeUpdate from './content-type-update';
import ContentTypeDeleteDialog from './content-type-delete-dialog';

const ContentTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContentType />} />
    <Route path="new" element={<ContentTypeUpdate />} />
    <Route path=":id">
      <Route index element={<ContentTypeDetail />} />
      <Route path="edit" element={<ContentTypeUpdate />} />
      <Route path="delete" element={<ContentTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentTypeRoutes;
