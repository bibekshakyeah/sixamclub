import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-media.reducer';

export const ContentMediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentMediaEntity = useAppSelector(state => state.contentMedia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentMediaDetailsHeading">
          <Translate contentKey="sixamclubApp.contentMedia.detail.title">ContentMedia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentMediaEntity.id}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="sixamclubApp.contentMedia.path">Path</Translate>
            </span>
          </dt>
          <dd>{contentMediaEntity.path}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="sixamclubApp.contentMedia.type">Type</Translate>
            </span>
          </dt>
          <dd>{contentMediaEntity.type}</dd>
          <dt>
            <Translate contentKey="sixamclubApp.contentMedia.content">Content</Translate>
          </dt>
          <dd>{contentMediaEntity.content ? contentMediaEntity.content.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/content-media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-media/${contentMediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentMediaDetail;
