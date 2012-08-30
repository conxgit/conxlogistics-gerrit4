package com.conx.logistics.app.whse.dao.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.dao.services.IWarehouseApplicationDAOService;
import com.conx.logistics.common.utils.Validator;
import com.conx.logistics.kernel.system.dao.services.application.IApplicationDAOService;
import com.conx.logistics.kernel.system.dao.services.application.IFeatureDAOService;
import com.conx.logistics.kernel.system.dao.services.application.IFeatureSetDAOService;
import com.conx.logistics.mdm.domain.application.Application;
import com.conx.logistics.mdm.domain.application.Feature;

@Transactional
@Repository
public class WahouseApplicationDAOImpl implements IWarehouseApplicationDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());	
    /**
     * Spring will inject a managed JPA {@link EntityManager} into this field.
     */
	@PersistenceContext
    private EntityManager em;	
	
    @Autowired
    private IFeatureDAOService featureDaoService;
    
    @Autowired
    private IFeatureSetDAOService featureSetDaoService;    	
    
    @Autowired
    private IApplicationDAOService applicationDaoService;  
    
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Application provideApplicationMetadata() {
		Application cpApp = applicationDaoService.findApplicationByCode(IWarehouseApplicationDAOService.WAREHOUSE_APP_CODE);
		if (Validator.isNull(cpApp))
		{
			/**
			 *
			 * Warehouse App
			 * 
			 **/
			cpApp = new Application(IWarehouseApplicationDAOService.WAREHOUSE_APP_CODE);
			cpApp.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_NAME);
			cpApp.setThemeIconPath("");
			
			cpApp = applicationDaoService.addApplication(cpApp);
			
			/**
			 * Receiving Featureset
			 */
			Feature mfs = new Feature(cpApp,null,IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_CODE);
			mfs.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_NAME);
			mfs = featureDaoService.addFeature(mfs);
			cpApp.getFeatures().add(mfs);
			
			
			//-- ASN
			Feature smfs = new Feature(cpApp,mfs,IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_CODE);
			smfs.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_NAME);
			smfs.setFeatureSet(true);
			smfs = featureDaoService.addFeature(smfs);
			mfs.getChildFeatures().add(smfs);
			mfs = featureDaoService.updateFeature(mfs);		
			
			Feature searchFt = new Feature(cpApp,smfs, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_SEARCH_CODE);
			searchFt.setName(WAREHOUSE_APP_RECEIVING_ASN_SEARCH_NAME);
			searchFt = featureDaoService.addFeature(searchFt);
			smfs.getChildFeatures().add(searchFt);
			
			Feature ft = new Feature(cpApp,smfs, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_NEW_CODE);
			ft.setName(WAREHOUSE_APP_RECEIVING_ASN_NEW_NAME);
			ft.setTaskFeature(true);
			ft.setOnCompletionFeature(searchFt);
			ft.setCode("whse.rcv.asn.CreateNewASNByOrgV1.0");
			ft.setExternalCode("KERNEL.PAGEFLOW.STARTTASK");
			ft.setName("New");
			ft = featureDaoService.addFeature(ft);
			smfs.getChildFeatures().add(ft);	
			
			smfs = featureDaoService.updateFeature(smfs);
			
			
			//-- Receive
			smfs = new Feature(cpApp,mfs,IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_CODE);
			smfs.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_NAME);
			smfs.setFeatureSet(true);
			smfs = featureDaoService.addFeature(smfs);
			mfs.getChildFeatures().add(smfs);
			mfs = featureDaoService.updateFeature(mfs);		
			
			searchFt = new Feature(cpApp,smfs, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_SEARCH_CODE);
			searchFt.setName(WAREHOUSE_APP_RECEIVING_RCV_SEARCH_NAME);
			searchFt.setComponentModelCode(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_SEARCH_COMPONENT);
			searchFt = featureDaoService.addFeature(searchFt);
			smfs.getChildFeatures().add(searchFt);
			
			ft = new Feature(cpApp,smfs, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_NEW_CODE);
			ft.setName(WAREHOUSE_APP_RECEIVING_RCV_NEW_NAME);
			ft.setTaskFeature(true);
			ft.setOnCompletionFeature(searchFt);
			ft.setCode("whse.rcv.asn.CreateNewRCVByOrgV1.0");
			ft.setExternalCode("KERNEL.PAGEFLOW.STARTTASK");
			ft.setName("New");
			ft = featureDaoService.addFeature(ft);
			smfs.getChildFeatures().add(ft);				

			cpApp = applicationDaoService.updateApplication(cpApp);					
			try {
				//em.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cpApp;
	}
}
