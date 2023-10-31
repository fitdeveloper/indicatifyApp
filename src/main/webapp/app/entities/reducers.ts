import site from 'app/entities/site/site.reducer';
import jobTitle from 'app/entities/job-title/job-title.reducer';
import knowledgeDomain from 'app/entities/knowledge-domain/knowledge-domain.reducer';
import level from 'app/entities/level/level.reducer';
import perimeter from 'app/entities/perimeter/perimeter.reducer';
import activity from 'app/entities/activity/activity.reducer';
import employee from 'app/entities/employee/employee.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  site,
  jobTitle,
  knowledgeDomain,
  level,
  perimeter,
  activity,
  employee,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
