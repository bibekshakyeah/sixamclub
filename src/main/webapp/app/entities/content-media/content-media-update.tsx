import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContent } from 'app/shared/model/content.model';
import { getEntities as getContents } from 'app/entities/content/content.reducer';
import { IContentMedia } from 'app/shared/model/content-media.model';
import { getEntity, updateEntity, createEntity, reset } from './content-media.reducer';

export const ContentMediaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contents = useAppSelector(state => state.content.entities);
  const contentMediaEntity = useAppSelector(state => state.contentMedia.entity);
  const loading = useAppSelector(state => state.contentMedia.loading);
  const updating = useAppSelector(state => state.contentMedia.updating);
  const updateSuccess = useAppSelector(state => state.contentMedia.updateSuccess);

  const handleClose = () => {
    navigate('/content-media' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContents({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...contentMediaEntity,
      ...values,
      content: contents.find(it => it.id.toString() === values.content.toString()),
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
          ...contentMediaEntity,
          content: contentMediaEntity?.content?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sixamclubApp.contentMedia.home.createOrEditLabel" data-cy="ContentMediaCreateUpdateHeading">
            <Translate contentKey="sixamclubApp.contentMedia.home.createOrEditLabel">Create or edit a ContentMedia</Translate>
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
                  id="content-media-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sixamclubApp.contentMedia.path')}
                id="content-media-path"
                name="path"
                data-cy="path"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sixamclubApp.contentMedia.type')}
                id="content-media-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="content-media-content"
                name="content"
                data-cy="content"
                label={translate('sixamclubApp.contentMedia.content')}
                type="select"
              >
                <option value="" key="0" />
                {contents
                  ? contents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content-media" replace color="info">
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

export default ContentMediaUpdate;
