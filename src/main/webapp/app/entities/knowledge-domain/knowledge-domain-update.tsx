import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IKnowledgeDomain } from 'app/shared/model/knowledge-domain.model';
import { getEntity, updateEntity, createEntity, reset } from './knowledge-domain.reducer';

export const KnowledgeDomainUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const knowledgeDomainEntity = useAppSelector(state => state.knowledgeDomain.entity);
  const loading = useAppSelector(state => state.knowledgeDomain.loading);
  const updating = useAppSelector(state => state.knowledgeDomain.updating);
  const updateSuccess = useAppSelector(state => state.knowledgeDomain.updateSuccess);

  const handleClose = () => {
    navigate('/knowledge-domain' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEmployees({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...knowledgeDomainEntity,
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
          ...knowledgeDomainEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="indicatifyApp.knowledgeDomain.home.createOrEditLabel" data-cy="KnowledgeDomainCreateUpdateHeading">
            <Translate contentKey="indicatifyApp.knowledgeDomain.home.createOrEditLabel">Create or edit a KnowledgeDomain</Translate>
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
                  id="knowledge-domain-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('indicatifyApp.knowledgeDomain.nameKnowledgeDomain')}
                id="knowledge-domain-nameKnowledgeDomain"
                name="nameKnowledgeDomain"
                data-cy="nameKnowledgeDomain"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('indicatifyApp.knowledgeDomain.descKnowledgeDomain')}
                id="knowledge-domain-descKnowledgeDomain"
                name="descKnowledgeDomain"
                data-cy="descKnowledgeDomain"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/knowledge-domain" replace color="info">
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

export default KnowledgeDomainUpdate;
