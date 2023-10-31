import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILevel } from 'app/shared/model/level.model';
import { LevelEnum } from 'app/shared/model/enumerations/level-enum.model';
import { getEntity, updateEntity, createEntity, reset } from './level.reducer';

export const LevelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const levelEntity = useAppSelector(state => state.level.entity);
  const loading = useAppSelector(state => state.level.loading);
  const updating = useAppSelector(state => state.level.updating);
  const updateSuccess = useAppSelector(state => state.level.updateSuccess);
  const levelEnumValues = Object.keys(LevelEnum);

  const handleClose = () => {
    navigate('/level' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...levelEntity,
      ...values,
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
          valueLevel: 'N0',
          ...levelEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="indicatifyApp.level.home.createOrEditLabel" data-cy="LevelCreateUpdateHeading">
            <Translate contentKey="indicatifyApp.level.home.createOrEditLabel">Create or edit a Level</Translate>
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
                  id="level-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('indicatifyApp.level.nameLevel')}
                id="level-nameLevel"
                name="nameLevel"
                data-cy="nameLevel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('indicatifyApp.level.valueLevel')}
                id="level-valueLevel"
                name="valueLevel"
                data-cy="valueLevel"
                type="select"
              >
                {levelEnumValues.map(levelEnum => (
                  <option value={levelEnum} key={levelEnum}>
                    {translate('indicatifyApp.LevelEnum.' + levelEnum)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('indicatifyApp.level.descLevel')}
                id="level-descLevel"
                name="descLevel"
                data-cy="descLevel"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/level" replace color="info">
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

export default LevelUpdate;
