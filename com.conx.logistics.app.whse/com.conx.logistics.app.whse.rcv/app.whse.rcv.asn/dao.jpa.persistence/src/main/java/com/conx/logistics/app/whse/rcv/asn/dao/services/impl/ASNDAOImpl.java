package com.conx.logistics.app.whse.rcv.asn.dao.services.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNDropOff;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNLine;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNPickup;
import com.conx.logistics.common.utils.Validator;
import com.conx.logistics.mdm.dao.services.IEntityMetadataDAOService;
import com.conx.logistics.mdm.domain.metadata.DefaultEntityMetadata;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumberType;

/**
 * Implementation of {@link ASN} that uses JPA for persistence.
 * <p />
 * <p/>
 * This class is marked as {@link Transactional}. The Spring configuration for
 * this module, enables AspectJ weaving for adding transaction demarcation to
 * classes annotated with <code>@Transactional</code>.
 */
@Transactional
@Repository
public class ASNDAOImpl implements IASNDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * Spring will inject a managed JPA {@link EntityManager} into this field.
	 */
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private IEntityMetadataDAOService entityMetadataDAOService;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public ASN get(long id) {
		return em.find(ASN.class, id);
	}

	@Override
	public List<ASN> getAll() {
		return em.createQuery("select o from com.conx.logistics.app.whse.rcv.asn.domain.ASN o record by o.id", ASN.class).getResultList();
	}

	@Override
	public ASN add(ASN record) {
		if (record.getId() == null) {
			record = em.merge(record);
		}
		assignCode(record);
		record = em.merge(record);
		return record;
	}

	@Override
	public void delete(ASN record) {
		em.remove(record);
	}

	@Override
	public ASN update(ASN record) {
		return em.merge(record);
	}

	private void assignCode(ASN newRecord) {
		String format = String.format("%%0%dd", 6);
		String paddedId = String.format(format, newRecord.getId());
		String code = "ASN" + paddedId;
		newRecord.setName(code);
		newRecord.setCode(code);
	}
	
	private void assignCode(ASNLine newRecord, ASN parentASN) {
		String format = String.format("%%0%dd", 3);
		String paddedId = String.format(format, newRecord.getId());
		String code = parentASN.getCode() + "-ALN" + paddedId;
		newRecord.setName(code);
		newRecord.setCode(code);
	}

	@Override
	public ASN addLines(Long asnId, Set<ASNLine> lines) throws Exception {
		ASN asn = null;
		try {
			asn = em.getReference(ASN.class, asnId);
			// Product prod = null;
			for (ASNLine line : lines) {
				// line.setParentASN(asn);

				// prod = em.merge(line.getProduct());
				// line.setProduct(prod);

				line = (ASNLine) em.merge(line);
				asn.getAsnLines().add(line);
			}

			asn = update(asn);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return asn;
	}

	@Override
	public ASN addRefNums(Long asnId, Set<ReferenceNumber> numbers) throws Exception {
		ASN asn = null;
		try {
			asn = em.getReference(ASN.class, asnId);
			DefaultEntityMetadata emd = entityMetadataDAOService.provide(ASN.class);
			ReferenceNumberType rnt = null;
			for (ReferenceNumber number : numbers) {
				number.setEntityMetadata(emd);
				number.setCode(asn.getCode() + "-" + number.getType().getCode() + "-" + number.getValue());
				number.setEntityPK(asnId);

				// rnt = em.merge(number.getType());
				number.setType(rnt);

				// number = em.merge(number);
				asn.getRefNumbers().add(number);
			}
			asn = update(asn);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return asn;
	}

	@Override
	public ASN addLocalTrans(Long asnId, ASNPickup pickUp, ASNDropOff dropOff) throws Exception {
		ASN asn = null;

		try {
			asn = em.getReference(ASN.class, asnId);

			if (Validator.isNotNull(pickUp)) {
				pickUp = (ASNPickup) em.merge(pickUp);
				asn.setPickup(pickUp);
			}

			if (Validator.isNotNull(dropOff)) {
				dropOff = (ASNDropOff) em.merge(dropOff);
				asn.setDropOff(dropOff);
			}

			asn.setPickup(pickUp);
			asn.setDropOff(dropOff);

			asn = update(asn);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return asn;
	}

	@Override
	public ASN getByCode(String code) {
		ASN asn = null;

		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<ASN> query = builder.createQuery(ASN.class);
			Root<ASN> rootEntity = query.from(ASN.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("code"), p));

			TypedQuery<ASN> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p, code);

			asn = typedQuery.getSingleResult();
			/*
			 * TypedQuery<Organization> q = em.createQuery(
			 * "select o from com.conx.logistics.mdm.domain.organization.Organization o WHERE o.code = :code"
			 * ,Organization.class); q.setParameter("code", code);
			 * 
			 * org = q.getSingleResult();
			 */
		} catch (NoResultException e) {
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}

		return asn;
	}

	@Override
	public ASN addLines(ASN asn, Set<ASNLine> lines) throws Exception {
		try {
			// Product prod = null;
			for (ASNLine line : lines) {
				// line.setParentASN(asn);

				// prod = em.merge(line.getProduct());
				// line.setProduct(prod);

				line = (ASNLine) em.merge(line);
				asn.getAsnLines().add(line);
			}

			asn = update(asn);
			return asn;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}
	}

	@Override
	public ASN addRefNums(ASN asn, Set<ReferenceNumber> numbers) throws Exception {
		try {
			DefaultEntityMetadata emd = entityMetadataDAOService.provide(ASN.class);
			ReferenceNumberType rnt = null;
			for (ReferenceNumber number : numbers) {
				number.setEntityMetadata(emd);
				number.setCode(asn.getCode() + "-" + number.getType().getCode() + "-" + number.getValue());
				number.setEntityPK(asn.getId());

				// rnt = em.merge(number.getType());
				number.setType(rnt);

				// number = em.merge(number);
				asn.getRefNumbers().add(number);
			}
			asn = update(asn);
			return asn;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}
	}

	@Override
	public ASN addLocalTrans(ASN asn, ASNPickup pickUp, ASNDropOff dropOff) throws Exception {
		try {
			if (Validator.isNotNull(pickUp)) {
				pickUp = (ASNPickup) em.merge(pickUp);
				asn.setPickup(pickUp);
			}

			if (Validator.isNotNull(dropOff)) {
				dropOff = (ASNDropOff) em.merge(dropOff);
				asn.setDropOff(dropOff);
			}

			asn.setPickup(pickUp);
			asn.setDropOff(dropOff);

			asn = update(asn);
			return asn;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}
	}

	@Override
	public ASNLine addLine(ASNLine line, Long asnId) throws Exception {
		line = em.merge(line);
		ASN asn = get(asnId);
		assert (asn == null) : "The asn did not exist";
		assignCode(line, asn);
		line.setParentASN(asn);
		line = em.merge(line);
		asn.getAsnLines().add(line);
		asn = em.merge(asn);
		return line;
	}

	@Override
	public ASNLine update(ASNLine record) {
		return em.merge(record);
	}
}
