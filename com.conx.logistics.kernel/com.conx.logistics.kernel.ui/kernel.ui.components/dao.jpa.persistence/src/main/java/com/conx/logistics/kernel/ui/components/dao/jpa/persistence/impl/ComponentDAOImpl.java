package com.conx.logistics.kernel.ui.components.dao.jpa.persistence.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.metamodel.dao.services.IBasicTypeDAOService;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.metamodel.domain.BasicType;
import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;



/**
 * Implementation of {@link AbstractConXComponent} that uses JPA for persistence.<p />
 * <p/>
 * This class is marked as {@link Transactional}. The Spring configuration for this module, enables AspectJ weaving for
 * adding transaction demarcation to classes annotated with <code>@Transactional</code>.
 */
@Transactional
@Repository
public class ComponentDAOImpl implements IComponentDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	
	private transient Map<String,AbstractConXComponent> cache = new HashMap<String, AbstractConXComponent>(); 
	
    /**
     * Spring will inject a managed JPA {@link EntityManager} into this field.
     */
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private IEntityTypeDAOService entityTypeDao;
    
    @Autowired
    private IBasicTypeDAOService basicTypeDao;
    

	public void setEm(EntityManager em) {
		this.em = em;
	}


	@Override
	public AbstractConXComponent get(long id) {
		return em.getReference(AbstractConXComponent.class, id);
	}    

	@Override
	public List<AbstractConXComponent> getAll() {
		return em.createQuery("select o from com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent o record by o.id",AbstractConXComponent.class).getResultList();
	}

	@Override
	public AbstractConXComponent add(AbstractConXComponent record) {
		record = em.merge(record);
		
		return record;
	}

	@Override
	public AbstractConXComponent getByCode(String code) {
		AbstractConXComponent comp = null;

		try
		{
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<AbstractConXComponent> query = builder.createQuery(AbstractConXComponent.class);
			Root<AbstractConXComponent> rootEntity = query.from(AbstractConXComponent.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("code"), p));

			TypedQuery<AbstractConXComponent> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p, code);
			
			comp = typedQuery.getSingleResult();
		}
		catch(NoResultException e){}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}
		catch(Error e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}		
		
		return comp;		
	}



	@Override
	public void delete(AbstractConXComponent record) {
		em.remove(record);
	}

	@Override
	public AbstractConXComponent update(AbstractConXComponent record) {
		return em.merge(record);
	}


	@Override
	public MasterDetailComponent getMasterDetailByDataSource(DataSource ds) {
		MasterDetailComponent mdc = null;
		
		try
		{
		TypedQuery<MasterDetailComponent> q = em.createQuery("select DISTINCT o from com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent o WHERE o.typeId = :typeId AND o.dataSource = :dataSource",MasterDetailComponent.class);
		q.setParameter("typeId","masterdetailcomponent");
		q.setParameter("dataSource",ds);
		mdc = q.getSingleResult();
		}
		catch(javax.persistence.NoResultException e)
		{}
		
		return mdc;		
	}
}
