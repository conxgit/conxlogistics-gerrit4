package com.conx.logistics.app.whse.dao.services.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conx.logistics.app.whse.dao.services.ILocationDAOService;
import com.conx.logistics.app.whse.domain.location.Location;

public class LocationDAOImpl implements ILocationDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Location get(long id) {
		return em.getReference(Location.class, id);
	}

	@Override
	public List<Location> getAll() {
		return em.createQuery("select o from com.conx.logistics.app.whse.domain.location.Location o record by o.id", Location.class).getResultList();
	}

	@Override
	public Location getByCode(String code) {
		Location org = null;

		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Location> query = builder.createQuery(Location.class);
			Root<Location> rootEntity = query.from(Location.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("code"), p));

			TypedQuery<Location> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p, code);

			org = typedQuery.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}

		return org;
	}

	@Override
	public Location add(Location record) {
		String name = record.getRow() + "-" + record.getColumn() + "-" + record.getLevel();
		record.setCode(name);
		record.setName(name);
		record = em.merge(record);
		return record;
	}

	@Override
	public void delete(Location record) {
		em.remove(record);
	}

	@Override
	public Location update(Location record) {
		return em.merge(record);
	}

	@Override
	public Location provide(int row, int column, String level) {
		Location location = null;

		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Location> query = builder.createQuery(Location.class);
			Root<Location> rootEntity = query.from(Location.class);
			
			ParameterExpression<Integer> rowParam = builder.parameter(Integer.class);
			ParameterExpression<Integer> columnParam = builder.parameter(Integer.class);
			ParameterExpression<String> levelParam = builder.parameter(String.class);
			
			query.select(rootEntity).where(builder.and(builder.equal(rootEntity.get("row"), rowParam), builder.equal(rootEntity.get("column"), columnParam), builder.equal(rootEntity.get("level"), levelParam)));

			TypedQuery<Location> typedQuery = em.createQuery(query);
			typedQuery.setParameter(rowParam, row);
			typedQuery.setParameter(columnParam, column);
			typedQuery.setParameter(levelParam, level);

			location = typedQuery.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			return null;
		}
		
		if (location == null) {
			location = new Location();
			location.setRow(row);
			location.setColumn(column);
			location.setLevel(level);
			location = add(location);
		}

		return location;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
