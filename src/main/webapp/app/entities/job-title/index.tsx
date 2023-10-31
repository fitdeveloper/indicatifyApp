import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobTitle from './job-title';
import JobTitleDetail from './job-title-detail';
import JobTitleUpdate from './job-title-update';
import JobTitleDeleteDialog from './job-title-delete-dialog';

const JobTitleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JobTitle />} />
    <Route path="new" element={<JobTitleUpdate />} />
    <Route path=":id">
      <Route index element={<JobTitleDetail />} />
      <Route path="edit" element={<JobTitleUpdate />} />
      <Route path="delete" element={<JobTitleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobTitleRoutes;
