import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './site.reducer';

export const SiteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const siteEntity = useAppSelector(state => state.site.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="siteDetailsHeading">
          <Translate contentKey="indicatifyApp.site.detail.title">Site</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{siteEntity.id}</dd>
          <dt>
            <span id="nameSite">
              <Translate contentKey="indicatifyApp.site.nameSite">Name Site</Translate>
            </span>
          </dt>
          <dd>{siteEntity.nameSite}</dd>
          <dt>
            <span id="addressSite">
              <Translate contentKey="indicatifyApp.site.addressSite">Address Site</Translate>
            </span>
          </dt>
          <dd>{siteEntity.addressSite}</dd>
          <dt>
            <span id="descSite">
              <Translate contentKey="indicatifyApp.site.descSite">Desc Site</Translate>
            </span>
          </dt>
          <dd>{siteEntity.descSite}</dd>
        </dl>
        <Button tag={Link} to="/site" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/site/${siteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SiteDetail;
