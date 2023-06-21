import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/content-type">
        <Translate contentKey="global.menu.entities.contentType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/content">
        <Translate contentKey="global.menu.entities.content" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ocean-model">
        <Translate contentKey="global.menu.entities.oceanModel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ocean-facet">
        <Translate contentKey="global.menu.entities.oceanFacet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/content-media">
        <Translate contentKey="global.menu.entities.contentMedia" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
