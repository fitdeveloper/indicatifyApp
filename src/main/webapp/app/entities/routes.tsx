import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Site from './site';
import JobTitle from './job-title';
import KnowledgeDomain from './knowledge-domain';
import Level from './level';
import Perimeter from './perimeter';
import Activity from './activity';
import Employee from './employee';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="site/*" element={<Site />} />
        <Route path="job-title/*" element={<JobTitle />} />
        <Route path="knowledge-domain/*" element={<KnowledgeDomain />} />
        <Route path="level/*" element={<Level />} />
        <Route path="perimeter/*" element={<Perimeter />} />
        <Route path="activity/*" element={<Activity />} />
        <Route path="employee/*" element={<Employee />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
