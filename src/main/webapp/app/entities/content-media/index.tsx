import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentMedia from './content-media';
import ContentMediaDetail from './content-media-detail';
import ContentMediaUpdate from './content-media-update';
import ContentMediaDeleteDialog from './content-media-delete-dialog';

const ContentMediaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContentMedia />} />
    <Route path="new" element={<ContentMediaUpdate />} />
    <Route path=":id">
      <Route index element={<ContentMediaDetail />} />
      <Route path="edit" element={<ContentMediaUpdate />} />
      <Route path="delete" element={<ContentMediaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentMediaRoutes;
