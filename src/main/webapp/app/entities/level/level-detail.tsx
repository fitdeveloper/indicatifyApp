import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './level.reducer';

export const LevelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const levelEntity = useAppSelector(state => state.level.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="levelDetailsHeading">
          <Translate contentKey="indicatifyApp.level.detail.title">Level</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{levelEntity.id}</dd>
          <dt>
            <span id="nameLevel">
              <Translate contentKey="indicatifyApp.level.nameLevel">Name Level</Translate>
            </span>
          </dt>
          <dd>{levelEntity.nameLevel}</dd>
          <dt>
            <span id="valueLevel">
              <Translate contentKey="indicatifyApp.level.valueLevel">Value Level</Translate>
            </span>
          </dt>
          <dd>{levelEntity.valueLevel}</dd>
          <dt>
            <span id="descLevel">
              <Translate contentKey="indicatifyApp.level.descLevel">Desc Level</Translate>
            </span>
          </dt>
          <dd>{levelEntity.descLevel}</dd>
        </dl>
        <Button tag={Link} to="/level" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/level/${levelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LevelDetail;
