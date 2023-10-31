import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/site">
        <Translate contentKey="global.menu.entities.site" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/job-title">
        <Translate contentKey="global.menu.entities.jobTitle" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/knowledge-domain">
        <Translate contentKey="global.menu.entities.knowledgeDomain" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/level">
        <Translate contentKey="global.menu.entities.level" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/perimeter">
        <Translate contentKey="global.menu.entities.perimeter" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/activity">
        <Translate contentKey="global.menu.entities.activity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
