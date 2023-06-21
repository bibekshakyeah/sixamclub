import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content.reducer';

export const ContentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentEntity = useAppSelector(state => state.content.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentDetailsHeading">
          <Translate contentKey="sixamclubApp.content.detail.title">Content</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="sixamclubApp.content.title">Title</Translate>
            </span>
          </dt>
          <dd>{contentEntity.title}</dd>
          <dt>
            <span id="sourceUrl">
              <Translate contentKey="sixamclubApp.content.sourceUrl">Source Url</Translate>
            </span>
          </dt>
          <dd>{contentEntity.sourceUrl}</dd>
          <dt>
            <span id="videoUrl">
              <Translate contentKey="sixamclubApp.content.videoUrl">Video Url</Translate>
            </span>
          </dt>
          <dd>{contentEntity.videoUrl}</dd>
          <dt>
            <span id="contentText">
              <Translate contentKey="sixamclubApp.content.contentText">Content Text</Translate>
            </span>
          </dt>
          <dd>{contentEntity.contentText}</dd>
          <dt>
            <span id="publishedDate">
              <Translate contentKey="sixamclubApp.content.publishedDate">Published Date</Translate>
            </span>
          </dt>
          <dd>
            {contentEntity.publishedDate ? <TextFormat value={contentEntity.publishedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sixamclubApp.content.contentType">Content Type</Translate>
          </dt>
          <dd>{contentEntity.contentType ? contentEntity.contentType.id : ''}</dd>
          <dt>
            <Translate contentKey="sixamclubApp.content.categories">Categories</Translate>
          </dt>
          <dd>
            {contentEntity.categories
              ? contentEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {contentEntity.categories && i === contentEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content/${contentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentDetail;
