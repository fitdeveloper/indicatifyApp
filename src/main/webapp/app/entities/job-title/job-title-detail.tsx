import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-title.reducer';

export const JobTitleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobTitleEntity = useAppSelector(state => state.jobTitle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobTitleDetailsHeading">
          <Translate contentKey="indicatifyApp.jobTitle.detail.title">JobTitle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobTitleEntity.id}</dd>
          <dt>
            <span id="nameJobTitle">
              <Translate contentKey="indicatifyApp.jobTitle.nameJobTitle">Name Job Title</Translate>
            </span>
          </dt>
          <dd>{jobTitleEntity.nameJobTitle}</dd>
          <dt>
            <span id="descJobTitle">
              <Translate contentKey="indicatifyApp.jobTitle.descJobTitle">Desc Job Title</Translate>
            </span>
          </dt>
          <dd>{jobTitleEntity.descJobTitle}</dd>
        </dl>
        <Button tag={Link} to="/job-title" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-title/${jobTitleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobTitleDetail;
