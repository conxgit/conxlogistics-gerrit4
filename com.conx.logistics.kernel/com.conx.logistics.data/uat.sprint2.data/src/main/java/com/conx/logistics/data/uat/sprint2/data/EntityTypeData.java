package com.conx.logistics.data.uat.sprint2.data;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;

public class EntityTypeData {
	public final static com.conx.logistics.mdm.domain.metamodel.EntityType provide(IEntityTypeDAOService entityTypeDAOService, EntityManager em, Class entityjavaClass) throws Exception {
		Metamodel mm = em.getMetamodel();
		EntityType me = mm.entity(entityjavaClass);

		com.conx.logistics.mdm.domain.metamodel.EntityType et = entityTypeDAOService.provide(me);
		return et;
	}
}
