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
		if (Validator.isNull(cpApp)) {
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
			Feature warehouseFeatureSet = new Feature(cpApp, null, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_CODE);
			warehouseFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_NAME);
			warehouseFeatureSet = featureDaoService.addFeature(warehouseFeatureSet);
			cpApp.getFeatures().add(warehouseFeatureSet);

			/*
			 * ASNs
			 */
			// ASN Feature Set
			Feature asnFeatureSet = new Feature(cpApp, warehouseFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_CODE);
			asnFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_NAME);
			asnFeatureSet.setFeatureSet(true);
			asnFeatureSet = featureDaoService.addFeature(asnFeatureSet);
			warehouseFeatureSet.getChildFeatures().add(asnFeatureSet);
			warehouseFeatureSet = featureDaoService.updateFeature(warehouseFeatureSet);

			// ASN Search Feature
			Feature searchAsnFeature = new Feature(cpApp, asnFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_SEARCH_CODE);
			searchAsnFeature.setName(WAREHOUSE_APP_RECEIVING_ASN_SEARCH_NAME);
			searchAsnFeature = featureDaoService.addFeature(searchAsnFeature);
			asnFeatureSet.getChildFeatures().add(searchAsnFeature);

			// ASN New Feature
			Feature newAsnFeature = new Feature(cpApp, asnFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ASN_NEW_CODE);
			newAsnFeature.setName(WAREHOUSE_APP_RECEIVING_ASN_NEW_NAME);
			newAsnFeature.setTaskFeature(true);
			newAsnFeature.setOnCompletionFeature(searchAsnFeature);
			newAsnFeature.setCode("whse.rcv.asn.CreateNewASNByOrgV1.0");
			newAsnFeature.setExternalCode("KERNEL.PAGEFLOW.STARTTASK");
			newAsnFeature = featureDaoService.addFeature(newAsnFeature);
			asnFeatureSet.getChildFeatures().add(newAsnFeature);

			// Update ASN Feature Set
			asnFeatureSet = featureDaoService.updateFeature(asnFeatureSet);

			/*
			 * Receives
			 */
			// Receive Feature Set
			Feature receiveFeatureSet = new Feature(cpApp, warehouseFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_CODE);
			receiveFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_NAME);
			receiveFeatureSet.setFeatureSet(true);
			receiveFeatureSet = featureDaoService.addFeature(receiveFeatureSet);
			warehouseFeatureSet.getChildFeatures().add(receiveFeatureSet);

			// Receive Search Feature
			Feature searchReceiveFeature = new Feature(cpApp, receiveFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_SEARCH_CODE);
			searchReceiveFeature.setName(WAREHOUSE_APP_RECEIVING_RCV_SEARCH_NAME);
			searchReceiveFeature.setCaption("Receives");
			searchReceiveFeature.setIconUrl("breadcrumb/img/conx-bread-crumb-grid-highlighted.png");
			searchReceiveFeature.setComponentModelCode(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_SEARCH_COMPONENT);
			searchReceiveFeature = featureDaoService.addFeature(searchReceiveFeature);
			receiveFeatureSet.getChildFeatures().add(searchReceiveFeature);

			// Receive New Feature
			/*Feature newReceiveFeature = new Feature(cpApp, smfs, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_NEW_CODE);
			newReceiveFeature.setName(WAREHOUSE_APP_RECEIVING_RCV_NEW_NAME);
			newReceiveFeature.setTaskFeature(true);
			newReceiveFeature.setOnCompletionFeature(searchReceiveFeature);
			newReceiveFeature.setCode("whse.rcv.asn.CreateNewRCVByOrgV1.0");
			newReceiveFeature.setExternalCode("KERNEL.PAGEFLOW.STARTTASK");
			newReceiveFeature = featureDaoService.addFeature(newReceiveFeature);
			receiveFeatureSet.getChildFeatures().add(newReceiveFeature);*/
			
			// Update Receive Feature Set
			receiveFeatureSet = featureDaoService.updateFeature(receiveFeatureSet);

			/*
			 * Arrivals
			 */
			// Arrival Feature Set
			Feature arrivalFeatureSet = new Feature(cpApp, warehouseFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_CODE);
			arrivalFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_NAME);
			arrivalFeatureSet.setFeatureSet(true);
			arrivalFeatureSet = featureDaoService.addFeature(arrivalFeatureSet);
			warehouseFeatureSet.getChildFeatures().add(arrivalFeatureSet);
			
			
			// Arrival Search Feature
			Feature searchArrivalFeature = new Feature(cpApp, arrivalFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_CODE);
			searchArrivalFeature.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_RCV_SEARCH_NAME);
			searchArrivalFeature.setCaption(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_NAME);
			searchArrivalFeature.setIconUrl("breadcrumb/img/conx-bread-crumb-grid-highlighted.png");
			searchArrivalFeature.setComponentModelCode(IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_COMPONENT);
			searchArrivalFeature = featureDaoService.addFeature(searchArrivalFeature);
			arrivalFeatureSet.getChildFeatures().add(searchArrivalFeature);

			// Arrival New Feature
			Feature newArrivalFeature = new Feature(cpApp, arrivalFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_RECEIVING_ARVL_NEW_CODE);
			newArrivalFeature.setName(WAREHOUSE_APP_RECEIVING_ARVL_NEW_NAME);
			newArrivalFeature.setTaskFeature(true);
			newArrivalFeature.setOnCompletionFeature(searchArrivalFeature);
			newArrivalFeature.setCode("whse.rcv.arrivalproc.ProcessCarrierArrivalV1.0");
			newArrivalFeature.setExternalCode("KERNEL.PAGEFLOW.STARTTASK");
			newArrivalFeature = featureDaoService.addFeature(newArrivalFeature);
			arrivalFeatureSet.getChildFeatures().add(newArrivalFeature);
			
			// Update Arrival Feature Set
			arrivalFeatureSet = featureDaoService.updateFeature(arrivalFeatureSet);
			
			

			/**
			 * IM Featureset
			 */
			Feature imFeatureSet = new Feature(cpApp, null, IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_CODE);
			imFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_NAME);
			imFeatureSet = featureDaoService.addFeature(imFeatureSet);
			cpApp.getFeatures().add(imFeatureSet);

			/*
			 * StockItems
			 */
			// StockItem Feature Set
			Feature siFeatureSet = new Feature(cpApp, imFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_CODE);
			siFeatureSet.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_NAME);
			siFeatureSet.setFeatureSet(true);
			siFeatureSet = featureDaoService.addFeature(siFeatureSet);
			imFeatureSet.getChildFeatures().add(siFeatureSet);
			imFeatureSet = featureDaoService.updateFeature(imFeatureSet);
			
			// StockItem Search Feature
			Feature searchSIFeature = new Feature(cpApp, siFeatureSet, IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_SEARCH_CODE);
			searchSIFeature.setName(IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_SEARCH_NAME);
			searchSIFeature.setCaption(IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_SEARCH_NAME);
			searchSIFeature.setIconUrl("breadcrumb/img/conx-bread-crumb-grid-highlighted.png");
			searchSIFeature.setComponentModelCode(IWarehouseApplicationDAOService.WAREHOUSE_APP_IM_STOCKITEM_SEARCH_COMPONENT);
			searchSIFeature = featureDaoService.addFeature(searchSIFeature);
			siFeatureSet.getChildFeatures().add(searchSIFeature);


			// Update StockItem Feature Set
			siFeatureSet = featureDaoService.updateFeature(siFeatureSet);
			

			// Update Warehouse Feature Set
			warehouseFeatureSet = featureDaoService.updateFeature(warehouseFeatureSet);
			cpApp = applicationDaoService.updateApplication(cpApp);
		}

		return cpApp;
	}
}
