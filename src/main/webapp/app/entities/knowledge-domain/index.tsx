import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import KnowledgeDomain from './knowledge-domain';
import KnowledgeDomainDetail from './knowledge-domain-detail';
import KnowledgeDomainUpdate from './knowledge-domain-update';
import KnowledgeDomainDeleteDialog from './knowledge-domain-delete-dialog';

const KnowledgeDomainRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<KnowledgeDomain />} />
    <Route path="new" element={<KnowledgeDomainUpdate />} />
    <Route path=":id">
      <Route index element={<KnowledgeDomainDetail />} />
      <Route path="edit" element={<KnowledgeDomainUpdate />} />
      <Route path="delete" element={<KnowledgeDomainDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default KnowledgeDomainRoutes;
