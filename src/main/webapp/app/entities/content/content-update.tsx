import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentType } from 'app/shared/model/content-type.model';
import { getEntities as getContentTypes } from 'app/entities/content-type/content-type.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { IContent } from 'app/shared/model/content.model';
import { getEntity, updateEntity, createEntity, reset } from './content.reducer';

export const ContentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentTypes = useAppSelector(state => state.contentType.entities);
  const categories = useAppSelector(state => state.category.entities);
  const contentEntity = useAppSelector(state => state.content.entity);
  const loading = useAppSelector(state => state.content.loading);
  const updating = useAppSelector(state => state.content.updating);
  const updateSuccess = useAppSelector(state => state.content.updateSuccess);

  const handleClose = () => {
    navigate('/content' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContentTypes({}));
    dispatch(getCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.publishedDate = convertDateTimeToServer(values.publishedDate);

    const entity = {
      ...contentEntity,
      ...values,
      categories: mapIdList(values.categories),
      contentType: contentTypes.find(it => it.id.toString() === values.contentType.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          publishedDate: displayDefaultDateTime(),
        }
      : {
          ...contentEntity,
          publishedDate: convertDateTimeFromServer(contentEntity.publishedDate),
          contentType: contentEntity?.contentType?.id,
          categories: contentEntity?.categories?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sixamclubApp.content.home.createOrEditLabel" data-cy="ContentCreateUpdateHeading">
            <Translate contentKey="sixamclubApp.content.home.createOrEditLabel">Create or edit a Content</Translate>
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
                  id="content-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sixamclubApp.content.title')}
                id="content-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sixamclubApp.content.sourceUrl')}
                id="content-sourceUrl"
                name="sourceUrl"
                data-cy="sourceUrl"
                type="text"
              />
              <ValidatedField
                label={translate('sixamclubApp.content.videoUrl')}
                id="content-videoUrl"
                name="videoUrl"
                data-cy="videoUrl"
                type="text"
              />
              <ValidatedField
                label={translate('sixamclubApp.content.contentText')}
                id="content-contentText"
                name="contentText"
                data-cy="contentText"
                type="text"
              />
              <ValidatedField
                label={translate('sixamclubApp.content.publishedDate')}
                id="content-publishedDate"
                name="publishedDate"
                data-cy="publishedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="content-contentType"
                name="contentType"
                data-cy="contentType"
                label={translate('sixamclubApp.content.contentType')}
                type="select"
              >
                <option value="" key="0" />
                {contentTypes
                  ? contentTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('sixamclubApp.content.categories')}
                id="content-categories"
                data-cy="categories"
                type="select"
                multiple
                name="categories"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content" replace color="info">
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

export default ContentUpdate;
