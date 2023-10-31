import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Level from './level';
import LevelDetail from './level-detail';
import LevelUpdate from './level-update';
import LevelDeleteDialog from './level-delete-dialog';

const LevelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Level />} />
    <Route path="new" element={<LevelUpdate />} />
    <Route path=":id">
      <Route index element={<LevelDetail />} />
      <Route path="edit" element={<LevelUpdate />} />
      <Route path="delete" element={<LevelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LevelRoutes;
