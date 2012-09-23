package com.conx.logistics.kernel.workspace.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.SortBy;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.Container.Filter;

public class NamedQueryLocalEntityProvider<T> extends LocalEntityProvider<T> {

	private String namedQuery;
	
	private Map<String,Object> parameterMap = new HashMap<String,Object>();

	public NamedQueryLocalEntityProvider(Class<T> entityClass) {
		super(entityClass);
	}
	
	public NamedQueryLocalEntityProvider(Class<T> entityClass, String namedQuery) {
		this(entityClass);
		this.namedQuery = namedQuery;
	}
	
	public NamedQueryLocalEntityProvider(Class<T> entityClass, String namedQuery, EntityManager em) {
		super(entityClass,em);
		this.namedQuery = namedQuery;
	}	
	

	   /**
     * Creates a filtered, optionally sorted, query.
     * 
     * @param fieldsToSelect
     *            the fields to select (must not be null).
     * @param entityAlias
     *            the alias of the entity (must not be null).
     * @param filter
     *            the filter to apply, or null if no filters should be applied.
     * @param sortBy
     *            the fields to sort by (must include at least one field), or
     *            null if the result should not be sorted at all.
     * @param swapSortOrder
     *            true to swap the sort order, false to use the sort order
     *            specified in <code>sortBy</code>. Only applies if
     *            <code>sortBy</code> is not null.
     * @param propertyIdPreprocessor
     *            the property ID preprocessor to pass to
     *            {@link Filter#toQLString(com.vaadin.addon.jpacontainer.Filter.PropertyIdPreprocessor)  }
     *            , or null to use a default preprocessor (should be sufficient
     *            in most cases).
     * @return the query (never null).
     */
	@Override
    protected TypedQuery<Object> createFilteredQuery(
            List<String> fieldsToSelect, Filter filter, List<SortBy> sortBy,
            boolean swapSortOrder) {
        assert fieldsToSelect != null : "fieldsToSelect must not be null";
        assert sortBy == null || !sortBy.isEmpty() : "sortBy must be either null or non-empty";
        
        TypedQuery query = (TypedQuery)doGetEntityManager().createNamedQuery(this.namedQuery);  
        for (String param : parameterMap.keySet())
        {
        	query.setParameter(param, parameterMap.get(param));
        }
        return query;
    }	
	
	@Override
    protected int doGetEntityCount(Filter filter) {
        TypedQuery<Long> query = (TypedQuery)doGetEntityManager().createNamedQuery("Count"+this.namedQuery);  
        for (String param : parameterMap.keySet())
        {
        	query.setParameter(param, parameterMap.get(param));
        }
        int count = query.getSingleResult().intValue();
        return count;
    }	
	
	
	@Override
	protected T doGetEntity(Object entityId) {
		
	        assert entityId != null : "entityId must not be null";
	        //T entity = doGetEntityManager().find(
	        //        getEntityClassMetadata().getMappedClass(), entityId);
	        return detachEntity((T)entityId);
	}
	
	@Override
	protected boolean doContainsEntity(Object entityId, Filter filter) {
		assert entityId != null : "entityId must not be null";
		return entityId != null;
	}
    
    
    public void addParameter(String name, Object value) {
    	parameterMap.put(name, value);
    }
}
