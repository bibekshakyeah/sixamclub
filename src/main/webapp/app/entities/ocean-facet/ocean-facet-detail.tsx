import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ocean-facet.reducer';

export const OceanFacetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const oceanFacetEntity = useAppSelector(state => state.oceanFacet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="oceanFacetDetailsHeading">
          <Translate contentKey="sixamclubApp.oceanFacet.detail.title">OceanFacet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{oceanFacetEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="sixamclubApp.oceanFacet.name">Name</Translate>
            </span>
          </dt>
          <dd>{oceanFacetEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="sixamclubApp.oceanFacet.value">Value</Translate>
            </span>
          </dt>
          <dd>{oceanFacetEntity.value}</dd>
          <dt>
            <Translate contentKey="sixamclubApp.oceanFacet.oceanModel">Ocean Model</Translate>
          </dt>
          <dd>{oceanFacetEntity.oceanModel ? oceanFacetEntity.oceanModel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ocean-facet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ocean-facet/${oceanFacetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OceanFacetDetail;
