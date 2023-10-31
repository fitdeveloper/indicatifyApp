import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee.reducer';

export const EmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeDetailsHeading">
          <Translate contentKey="indicatifyApp.employee.detail.title">Employee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.id}</dd>
          <dt>
            <span id="firstnameEmployee">
              <Translate contentKey="indicatifyApp.employee.firstnameEmployee">Firstname Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.firstnameEmployee}</dd>
          <dt>
            <span id="lastnameEmployee">
              <Translate contentKey="indicatifyApp.employee.lastnameEmployee">Lastname Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.lastnameEmployee}</dd>
          <dt>
            <span id="matriculationNumberEmployee">
              <Translate contentKey="indicatifyApp.employee.matriculationNumberEmployee">Matriculation Number Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.matriculationNumberEmployee}</dd>
          <dt>
            <span id="dateOfBirthEmployee">
              <Translate contentKey="indicatifyApp.employee.dateOfBirthEmployee">Date Of Birth Employee</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.dateOfBirthEmployee ? (
              <TextFormat value={employeeEntity.dateOfBirthEmployee} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="emailEmployee">
              <Translate contentKey="indicatifyApp.employee.emailEmployee">Email Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.emailEmployee}</dd>
          <dt>
            <span id="phoneEmployee">
              <Translate contentKey="indicatifyApp.employee.phoneEmployee">Phone Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.phoneEmployee}</dd>
          <dt>
            <span id="hireDateEmployee">
              <Translate contentKey="indicatifyApp.employee.hireDateEmployee">Hire Date Employee</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.hireDateEmployee ? (
              <TextFormat value={employeeEntity.hireDateEmployee} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="genderEmployee">
              <Translate contentKey="indicatifyApp.employee.genderEmployee">Gender Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.genderEmployee}</dd>
          <dt>
            <span id="descEmployee">
              <Translate contentKey="indicatifyApp.employee.descEmployee">Desc Employee</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.descEmployee}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.user">User</Translate>
          </dt>
          <dd>{employeeEntity.user ? employeeEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.superiorEmployee">Superior Employee</Translate>
          </dt>
          <dd>{employeeEntity.superiorEmployee ? employeeEntity.superiorEmployee.id : ''}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.jobTitle">Job Title</Translate>
          </dt>
          <dd>{employeeEntity.jobTitle ? employeeEntity.jobTitle.id : ''}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.level">Level</Translate>
          </dt>
          <dd>{employeeEntity.level ? employeeEntity.level.id : ''}</dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.knowledgeDomain">Knowledge Domain</Translate>
          </dt>
          <dd>
            {employeeEntity.knowledgeDomains
              ? employeeEntity.knowledgeDomains.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {employeeEntity.knowledgeDomains && i === employeeEntity.knowledgeDomains.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.perimeter">Perimeter</Translate>
          </dt>
          <dd>
            {employeeEntity.perimeters
              ? employeeEntity.perimeters.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {employeeEntity.perimeters && i === employeeEntity.perimeters.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="indicatifyApp.employee.site">Site</Translate>
          </dt>
          <dd>{employeeEntity.site ? employeeEntity.site.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeDetail;
