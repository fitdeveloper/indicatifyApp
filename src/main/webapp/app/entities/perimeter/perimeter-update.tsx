import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivity } from 'app/shared/model/activity.model';
import { getEntities as getActivities } from 'app/entities/activity/activity.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IPerimeter } from 'app/shared/model/perimeter.model';
import { getEntity, updateEntity, createEntity, reset } from './perimeter.reducer';

export const PerimeterUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activities = useAppSelector(state => state.activity.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const perimeterEntity = useAppSelector(state => state.perimeter.entity);
  const loading = useAppSelector(state => state.perimeter.loading);
  const updating = useAppSelector(state => state.perimeter.updating);
  const updateSuccess = useAppSelector(state => state.perimeter.updateSuccess);

  const handleClose = () => {
    navigate('/perimeter' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActivities({}));
    dispatch(getEmployees({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...perimeterEntity,
      ...values,
      activity: activities.find(it => it.id.toString() === values.activity.toString()),
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
          ...perimeterEntity,
          activity: perimeterEntity?.activity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="indicatifyApp.perimeter.home.createOrEditLabel" data-cy="PerimeterCreateUpdateHeading">
            <Translate contentKey="indicatifyApp.perimeter.home.createOrEditLabel">Create or edit a Perimeter</Translate>
          </h2>
        </Col>
      </Row>
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
                  id="perimeter-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('indicatifyApp.perimeter.namePerimeter')}
                id="perimeter-namePerimeter"
                name="namePerimeter"
                data-cy="namePerimeter"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('indicatifyApp.perimeter.descPerimeter')}
                id="perimeter-descPerimeter"
                name="descPerimeter"
                data-cy="descPerimeter"
                type="text"
              />
              <ValidatedField
                id="perimeter-activity"
                name="activity"
                data-cy="activity"
                label={translate('indicatifyApp.perimeter.activity')}
                type="select"
                required
              >
                <option value="" key="0" />
                {activities
                  ? activities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/perimeter" replace color="info">
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
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PerimeterUpdate;
