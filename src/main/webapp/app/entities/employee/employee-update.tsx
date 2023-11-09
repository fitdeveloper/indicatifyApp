import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IJobTitle } from 'app/shared/model/job-title.model';
import { getEntities as getJobTitles } from 'app/entities/job-title/job-title.reducer';
import { ILevel } from 'app/shared/model/level.model';
import { getEntities as getLevels } from 'app/entities/level/level.reducer';

import { getEntities as getActivities } from 'app/entities/activity/activity.reducer';

import { IKnowledgeDomain } from 'app/shared/model/knowledge-domain.model';
import { getEntities as getKnowledgeDomains } from 'app/entities/knowledge-domain/knowledge-domain.reducer';
import { IPerimeter } from 'app/shared/model/perimeter.model';
import { getEntities as getPerimeters } from 'app/entities/perimeter/perimeter.reducer';
import { ISite } from 'app/shared/model/site.model';
import { getEntities as getSites } from 'app/entities/site/site.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { GenderEnum } from 'app/shared/model/enumerations/gender-enum.model';
import { getEntity, updateEntity, createEntity, reset } from './employee.reducer';

export const EmployeeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const employees = useAppSelector(state => state.employee.entities);
  const jobTitles = useAppSelector(state => state.jobTitle.entities);
  const levels = useAppSelector(state => state.level.entities);
  const knowledgeDomains = useAppSelector(state => state.knowledgeDomain.entities);
  const perimeters = useAppSelector(state => state.perimeter.entities);
  const activities = useAppSelector(state => state.activity.entities);
  const sites = useAppSelector(state => state.site.entities);
  const employeeEntity = useAppSelector(state => state.employee.entity);
  const loading = useAppSelector(state => state.employee.loading);
  const updating = useAppSelector(state => state.employee.updating);
  const updateSuccess = useAppSelector(state => state.employee.updateSuccess);
  const genderEnumValues = Object.keys(GenderEnum);

  const handleClose = () => {
    navigate('/employee' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getEmployees({}));
    dispatch(getJobTitles({}));
    dispatch(getLevels({}));
    dispatch(getKnowledgeDomains({}));
    dispatch(getPerimeters({}));
    dispatch(getSites({}));
    dispatch(getActivities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...employeeEntity,
      ...values,
      knowledgeDomains: mapIdList(values.knowledgeDomains),
      perimeters: mapIdList(values.perimeters),
      user: users.find(it => it.id.toString() === values.user.toString()),
      superiorEmployee: employees.find(it => it.id.toString() === values.superiorEmployee.toString()),
      jobTitle: jobTitles.find(it => it.id.toString() === values.jobTitle.toString()),
      level: levels.find(it => it.id.toString() === values.level.toString()),
      site: sites.find(it => it.id.toString() === values.site.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          genderEmployee: 'Male',
          ...employeeEntity,
          user: employeeEntity?.user?.id,
          superiorEmployee: employeeEntity?.superiorEmployee?.id,
          jobTitle: employeeEntity?.jobTitle?.id,
          level: employeeEntity?.level?.id,
          knowledgeDomains: employeeEntity?.knowledgeDomains?.map(e => e.id.toString()),
          perimeters: employeeEntity?.perimeters?.map(e => e.id.toString()),
          site: employeeEntity?.site?.id,
        };

  const [levelName, setLevelName] = useState(0);
  const [activityState, setActivityState] = useState(0);

  const onLevelChange = event => {
    setLevelName(event.target.value);
  };

  const onActivityChange = event => {
    setActivityState(event.target.value);
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="indicatifyApp.employee.home.createOrEditLabel" data-cy="EmployeeCreateUpdateHeading">
            <Translate contentKey="indicatifyApp.employee.home.createOrEditLabel">Create or edit a Employee</Translate>
          </h2>
        </Col>
      </Row>

      <div className="accordion-body">
        <div className="accordion" id="accordionExample">
          <div className="accordion-item">
            <h2 className="accordion-header" id="headingOne">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseOne"
                aria-expanded="false"
                aria-controls="collapseOne"
              >
                Accordion Item #1
              </button>
            </h2>
            <div id="collapseOne" className="accordion-collapse collapse" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
              <div className="accordion-body">
                <strong>This is the first item's accordion body.</strong> It is shown by default, until the collapse plugin adds the
                appropriate classes that we use to style each element. These classes control the overall appearance, as well as the showing
                and hiding via CSS transitions. You can modify any of this with custom CSS or overriding our default variables. It's also
                worth noting that just about any HTML can go within the <code>.accordion-body</code>, though the transition does limit
                overflow.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header" id="headingTwo">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseTwo"
                aria-expanded="false"
                aria-controls="collapseTwo"
              >
                Accordion Item #2
              </button>
            </h2>
            <div id="collapseTwo" className="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
              <div className="accordion-body">
                <strong>This is the second item's accordion body.</strong> It is hidden by default, until the collapse plugin adds the
                appropriate classes that we use to style each element. These classes control the overall appearance, as well as the showing
                and hiding via CSS transitions. You can modify any of this with custom CSS or overriding our default variables. It's also
                worth noting that just about any HTML can go within the <code>.accordion-body</code>, though the transition does limit
                overflow.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header" id="headingThree">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseThree"
                aria-expanded="false"
                aria-controls="collapseThree"
              >
                Accordion Item #3
              </button>
            </h2>
            <div
              id="collapseThree"
              className="accordion-collapse collapse"
              aria-labelledby="headingThree"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                <strong>This is the third item's accordion body.</strong> It is hidden by default, until the collapse plugin adds the
                appropriate classes that we use to style each element. These classes control the overall appearance, as well as the showing
                and hiding via CSS transitions. You can modify any of this with custom CSS or overriding our default variables. It's also
                worth noting that just about any HTML can go within the <code>.accordion-body</code>, though the transition does limit
                overflow.
              </div>
            </div>
          </div>
        </div>
      </div>

      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="employee-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}

              {/* Start Identification */}

              <br />
              <br />

              <ValidatedField
                id="employee-level"
                name="level"
                data-cy="level"
                label={translate('indicatifyApp.employee.level')}
                type="select"
                onChange={onLevelChange}
                required
              >
                <option value="" key="0" />
                {levels
                  ? levels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.valueLevel}
                      </option>
                    ))
                  : null}
              </ValidatedField>

              {levels ? levels.map(otherEntity => (otherEntity.id == levelName ? <h3> {otherEntity.nameLevel} </h3> : null)) : null}

              {levelName != 0 ? (
                <div>
                  <ValidatedField
                    id="employee-site"
                    name="site"
                    data-cy="site"
                    label={translate('indicatifyApp.employee.site')}
                    type="select"
                    required
                  >
                    <option value="" key="0" />
                    {sites
                      ? sites.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  {levelName != 4 ? (
                    <ValidatedField
                      id="employee-superiorEmployee"
                      name="superiorEmployee"
                      data-cy="superiorEmployee"
                      label={translate('indicatifyApp.employee.superiorEmployee')}
                      type="select"
                    >
                      <option value="" key="0" />
                      {employees
                        ? employees.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.id}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                  ) : null}
                  {/* End Identification */}
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  {/* Start Infos */}
                  <ValidatedField
                    label={translate('indicatifyApp.employee.firstnameEmployee')}
                    id="employee-firstnameEmployee"
                    name="firstnameEmployee"
                    data-cy="firstnameEmployee"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.lastnameEmployee')}
                    id="employee-lastnameEmployee"
                    name="lastnameEmployee"
                    data-cy="lastnameEmployee"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.matriculationNumberEmployee')}
                    id="employee-matriculationNumberEmployee"
                    name="matriculationNumberEmployee"
                    data-cy="matriculationNumberEmployee"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.dateOfBirthEmployee')}
                    id="employee-dateOfBirthEmployee"
                    name="dateOfBirthEmployee"
                    data-cy="dateOfBirthEmployee"
                    type="date"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.emailEmployee')}
                    id="employee-emailEmployee"
                    name="emailEmployee"
                    data-cy="emailEmployee"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.phoneEmployee')}
                    id="employee-phoneEmployee"
                    name="phoneEmployee"
                    data-cy="phoneEmployee"
                    type="text"
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.hireDateEmployee')}
                    id="employee-hireDateEmployee"
                    name="hireDateEmployee"
                    data-cy="hireDateEmployee"
                    type="date"
                  />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.genderEmployee')}
                    id="employee-genderEmployee"
                    name="genderEmployee"
                    data-cy="genderEmployee"
                    type="select"
                  >
                    {genderEnumValues.map(genderEnum => (
                      <option value={genderEnum} key={genderEnum}>
                        {translate('indicatifyApp.GenderEnum.' + genderEnum)}
                      </option>
                    ))}
                  </ValidatedField>
                  <ValidatedField
                    label={translate('indicatifyApp.employee.descEmployee')}
                    id="employee-descEmployee"
                    name="descEmployee"
                    data-cy="descEmployee"
                    type="text"
                  />
                  {/* End infos */}
                  {/* Start Activite */}
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  {levelName != 4 ? (
                    <div>
                      <ValidatedField
                        label="Activity"
                        id="employee-activity"
                        data-cy="activite"
                        type="select"
                        onChange={onActivityChange}
                        name="activities"
                      >
                        <option value="" key="0" />
                        {activities
                          ? activities.map(otherEntity => (
                              <option value={otherEntity.id} key={otherEntity.id}>
                                {otherEntity.nameActivity}
                              </option>
                            ))
                          : null}
                      </ValidatedField>

                      <ValidatedField
                        label={translate('indicatifyApp.employee.perimeter')}
                        id="employee-perimeter"
                        data-cy="perimeter"
                        type="select"
                        multiple
                        name="perimeters"
                      >
                        <option value="" key="0" />
                        {perimeters
                          ? perimeters.map(otherEntity =>
                              otherEntity.activity.id == activityState ? (
                                <option value={otherEntity.id} key={otherEntity.id}>
                                  {otherEntity.namePerimeter}
                                </option>
                              ) : null
                            )
                          : null}
                      </ValidatedField>
                    </div>
                  ) : (
                    <ValidatedField
                      label={translate('indicatifyApp.employee.perimeter')}
                      id="employee-perimeter"
                      data-cy="perimeter"
                      type="select"
                      multiple
                      name="perimeters"
                    >
                      <option value="" key="0" />
                      {perimeters
                        ? perimeters.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id} selected>
                              {otherEntity.namePerimeter}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                  )}
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  {/* <ValidatedField
                id="employee-user"
                name="user"
                data-cy="user"
                label={translate('indicatifyApp.employee.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText> */}
                  <ValidatedField
                    id="employee-jobTitle"
                    name="jobTitle"
                    data-cy="jobTitle"
                    label={translate('indicatifyApp.employee.jobTitle')}
                    type="select"
                    required
                  >
                    <option value="" key="0" />
                    {jobTitles
                      ? jobTitles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nameJobTitle}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <FormText>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </FormText>
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <ValidatedField
                    label={translate('indicatifyApp.employee.knowledgeDomain')}
                    id="employee-knowledgeDomain"
                    data-cy="knowledgeDomain"
                    type="select"
                    multiple
                    name="knowledgeDomains"
                  >
                    <option value="" key="0" />
                    {knowledgeDomains
                      ? knowledgeDomains.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nameKnowledgeDomain}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <br />
                  <br />
                  <br />
                  <Button
                    tag={Link}
                    id="cancel-save"
                    className="btn btn-success"
                    data-cy="entityCreateCancelButton"
                    to="/employee"
                    replace
                    color="info"
                  >
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </span>
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp;
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </Button>
                </div>
              ) : null}
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EmployeeUpdate;
