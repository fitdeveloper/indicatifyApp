import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Perimeter from './perimeter';
import PerimeterDetail from './perimeter-detail';
import PerimeterUpdate from './perimeter-update';
import PerimeterDeleteDialog from './perimeter-delete-dialog';

const PerimeterRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Perimeter />} />
    <Route path="new" element={<PerimeterUpdate />} />
    <Route path=":id">
      <Route index element={<PerimeterDetail />} />
      <Route path="edit" element={<PerimeterUpdate />} />
      <Route path="delete" element={<PerimeterDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PerimeterRoutes;
