import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './knowledge-domain.reducer';

export const KnowledgeDomainDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const knowledgeDomainEntity = useAppSelector(state => state.knowledgeDomain.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="knowledgeDomainDetailsHeading">
          <Translate contentKey="indicatifyApp.knowledgeDomain.detail.title">KnowledgeDomain</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{knowledgeDomainEntity.id}</dd>
          <dt>
            <span id="nameKnowledgeDomain">
              <Translate contentKey="indicatifyApp.knowledgeDomain.nameKnowledgeDomain">Name Knowledge Domain</Translate>
            </span>
          </dt>
          <dd>{knowledgeDomainEntity.nameKnowledgeDomain}</dd>
          <dt>
            <span id="descKnowledgeDomain">
              <Translate contentKey="indicatifyApp.knowledgeDomain.descKnowledgeDomain">Desc Knowledge Domain</Translate>
            </span>
          </dt>
          <dd>{knowledgeDomainEntity.descKnowledgeDomain}</dd>
        </dl>
        <Button tag={Link} to="/knowledge-domain" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/knowledge-domain/${knowledgeDomainEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default KnowledgeDomainDetail;
