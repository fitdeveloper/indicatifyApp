import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './perimeter.reducer';

export const PerimeterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const perimeterEntity = useAppSelector(state => state.perimeter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="perimeterDetailsHeading">
          <Translate contentKey="indicatifyApp.perimeter.detail.title">Perimeter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{perimeterEntity.id}</dd>
          <dt>
            <span id="namePerimeter">
              <Translate contentKey="indicatifyApp.perimeter.namePerimeter">Name Perimeter</Translate>
            </span>
          </dt>
          <dd>{perimeterEntity.namePerimeter}</dd>
          <dt>
            <span id="descPerimeter">
              <Translate contentKey="indicatifyApp.perimeter.descPerimeter">Desc Perimeter</Translate>
            </span>
          </dt>
          <dd>{perimeterEntity.descPerimeter}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.perimeter.activity">Activity</Translate>
          </dt>
          <dd>{perimeterEntity.activity ? perimeterEntity.activity.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/perimeter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/perimeter/${perimeterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PerimeterDetail;
