import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOceanModel } from 'app/shared/model/ocean-model.model';
import { getEntities as getOceanModels } from 'app/entities/ocean-model/ocean-model.reducer';
import { IOceanFacet } from 'app/shared/model/ocean-facet.model';
import { getEntity, updateEntity, createEntity, reset } from './ocean-facet.reducer';

export const OceanFacetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const oceanModels = useAppSelector(state => state.oceanModel.entities);
  const oceanFacetEntity = useAppSelector(state => state.oceanFacet.entity);
  const loading = useAppSelector(state => state.oceanFacet.loading);
  const updating = useAppSelector(state => state.oceanFacet.updating);
  const updateSuccess = useAppSelector(state => state.oceanFacet.updateSuccess);

  const handleClose = () => {
    navigate('/ocean-facet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOceanModels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...oceanFacetEntity,
      ...values,
      oceanModel: oceanModels.find(it => it.id.toString() === values.oceanModel.toString()),
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
          ...oceanFacetEntity,
          oceanModel: oceanFacetEntity?.oceanModel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sixamclubApp.oceanFacet.home.createOrEditLabel" data-cy="OceanFacetCreateUpdateHeading">
            <Translate contentKey="sixamclubApp.oceanFacet.home.createOrEditLabel">Create or edit a OceanFacet</Translate>
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
                  id="ocean-facet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sixamclubApp.oceanFacet.name')}
                id="ocean-facet-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sixamclubApp.oceanFacet.value')}
                id="ocean-facet-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="ocean-facet-oceanModel"
                name="oceanModel"
                data-cy="oceanModel"
                label={translate('sixamclubApp.oceanFacet.oceanModel')}
                type="select"
              >
                <option value="" key="0" />
                {oceanModels
                  ? oceanModels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ocean-facet" replace color="info">
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

export default OceanFacetUpdate;
