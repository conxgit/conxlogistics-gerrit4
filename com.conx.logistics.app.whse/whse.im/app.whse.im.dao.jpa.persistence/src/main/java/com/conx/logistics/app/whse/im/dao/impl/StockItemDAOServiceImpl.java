package com.conx.logistics.app.whse.im.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.domain.location.Location;
import com.conx.logistics.app.whse.im.dao.services.IStockItemDAOService;
import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;
import com.conx.logistics.app.whse.im.domain.types.STOCKITEMSTATUS;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalReceiptDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveLineDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLineStockItemSet;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALRECEIPTLINESTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALRECEIPTSTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALSTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVELINESTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVESTATUS;
import com.conx.logistics.common.utils.Validator;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.dao.services.ICommercialRecordDAOService;
import com.conx.logistics.mdm.dao.services.ICommercialValueDAOService;
import com.conx.logistics.mdm.domain.commercialrecord.CommercialRecord;
import com.conx.logistics.mdm.domain.commercialrecord.CommercialValue;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.conx.logistics.mdm.domain.metamodel.EntityType;

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
public class StockItemDAOServiceImpl implements IStockItemDAOService {
	static Logger log = LoggerFactory.getLogger(StockItemDAOServiceImpl.class.getName());

	/**
	 * Spring will inject a managed JPA {@link EntityManager} into this field.
	 */
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private IArrivalReceiptDAOService arrivalReceiptDAOService;

	@Autowired
	private IArrivalDAOService arrivalDAOService;

	@Autowired
	private IRemoteDocumentRepository documentRepositoryService;

	@Autowired
	private IEntityTypeDAOService entityTypeDAOService;

	@Autowired
	private ICommercialRecordDAOService commercialRecordDAOService;

	@Autowired
	private ICommercialValueDAOService commercialValueDAOService;

	@Autowired
	private IReceiveLineDAOService receiveLineDAOService;

	@Autowired
	private IReceiveDAOService receiveDAOService;

	private ReceiveLineStockItemSet getStockItemSetByReceiveLine(ReceiveLine receiveLine) throws Exception {
		TypedQuery<ReceiveLineStockItemSet> q = em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLineStockItemSet o WHERE o.receiveLine = :receiveLine",
				ReceiveLineStockItemSet.class);
		q.setParameter("receiveLine", receiveLine);
		ReceiveLineStockItemSet itemSet = null;
		try {
			itemSet = q.getSingleResult();
			return itemSet;
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new Exception("There was more than one ReceiveLineStockItemSet for this ReceiveLine");
		}
	}

	private ReceiveLineStockItemSet provideStockItemSet(ReceiveLine receiveLine, ArrivalReceipt parentArrivalReceipt, ArrivalReceiptLine parentArrivalReceiptLine) throws Exception {
		ReceiveLineStockItemSet itemSet = getStockItemSetByReceiveLine(receiveLine);
		if (itemSet == null) {
			itemSet = new ReceiveLineStockItemSet();
			itemSet.setReceiveLine(receiveLine);
			itemSet.setArrivalReceipt(parentArrivalReceipt);
			itemSet.setArrival(parentArrivalReceipt.getParentArrival());
			itemSet.setArrivalReceiptLine(parentArrivalReceiptLine);
			itemSet = em.merge(itemSet);
		}
		return itemSet;
	}

	@Override
	public StockItem addOneOfGroup(StockItem newRecord, Long receiveLinePK, Long arrivalReceiptPK, Long arrivalReceiptLinePK) throws Exception {
		ReceiveLine parentReceiveLine = em.getReference(ReceiveLine.class, receiveLinePK);
		assert Validator.isNotNull(parentReceiveLine);
		ArrivalReceipt parentArrivalReceipt = em.getReference(ArrivalReceipt.class, arrivalReceiptPK);
		assert Validator.isNotNull(parentArrivalReceipt);
		ArrivalReceiptLine parentArrivalReceiptLine = em.getReference(ArrivalReceiptLine.class, arrivalReceiptLinePK);
		assert Validator.isNotNull(parentArrivalReceiptLine);
		
		ReceiveLineStockItemSet itemSet = provideStockItemSet(parentReceiveLine, parentArrivalReceipt, parentArrivalReceiptLine);
		newRecord = processRegularStockItem(newRecord, itemSet);
		return newRecord;
	}

	/*
	 * public StockItem addOneOfGroup(StockItem newRecord) { if
	 * (Validator.isNotNull(newRecord.getReceiveLine())) { try { newRecord =
	 * processRegularStockItem(newRecord); } catch (Exception e) { StringWriter
	 * sw = new StringWriter(); e.printStackTrace(new PrintWriter(sw)); String
	 * stacktrace = sw.toString(); log.warn(stacktrace); } }
	 * 
	 * return newRecord; }
	 */

	// public StockItem addManyWithOneLabel(StockItem newRecord) {
	// // --Naming
	// // Process arrival item
	// if (Validator.isNotNull(newRecord.getReceiveLine()))// This item belongs
	// // to a rcv
	// {
	// try {
	// ReceiveLine rcvLine = newRecord.getReceiveLine();
	//
	// newRecord = (StockItem) em.merge(newRecord);
	//
	// newRecord = processRegularStockItem(newRecord);
	//
	// // -- Create Portal folder assoc. with this Arrvl
	// /**
	// * Create a folder dedicated to this item
	// */
	// try {
	// /*
	// * HttpPrincipal lepUser =
	// * LiferayHTTPRemotingUtil.getHttpPrincipal(); DLFolder
	// * itemFolder = DLFolderServiceHttp.addFolder(lepUser,
	// * LPEPropsValues.GROUP_ID,
	// * LPEPropsValues._DOCLIB_ARRIVALS_FOLDERID,//Parent Folder
	// * Long.toString(newRecord.getId()),
	// * Long.toString(newRecord.getId()), new ServiceContext());
	// * newRecord.setDlFolderId(itemFolder.getFolderId());
	// */
	// } catch (Exception e) {
	// StringWriter sw = new StringWriter();
	// e.printStackTrace(new PrintWriter(sw));
	// String stacktrace = sw.toString();
	// log.warn(stacktrace);
	// } catch (Error e) {
	// StringWriter sw = new StringWriter();
	// e.printStackTrace(new PrintWriter(sw));
	// String stacktrace = sw.toString();
	// log.error(e.getMessage(), stacktrace);
	// }
	// } catch (Exception e) {
	// StringWriter sw = new StringWriter();
	// e.printStackTrace(new PrintWriter(sw));
	// String stacktrace = sw.toString();
	// log.warn(stacktrace);
	// }
	// }
	//
	// return newRecord;
	// }

	//
	// public Double getExpectedProdTotalWeightInLbs(ReceiveLine receiveLine)
	// {
	// if (receiveLine.getProduct().getWeight() != null)
	// {
	// Double ttl = UnitConversionUtil.convertWeight(getProduct().getWeight(),
	// WEIGHTUNIT.valueOf(getProduct().getWeightUnit().getCode()),
	// WEIGHTUNIT.LB);
	// if (getExpectedInnerPackCount() != null)
	// ttl = ttl*getExpectedInnerPackCount();
	// return ttl;
	// }
	// else
	// return 0.0;
	// }
	//
	// public Double WHReceiveLine.getExpectedProdTotalVolumeInCFs()
	// {
	// if (getProduct().getVolume() != null)
	// {
	// Double ttl =
	// UnitConversionUtil.convertDimension(getProduct().getVolume(),
	// DIMUNIT.valueOf(getProduct().getVolUnit().getCode()), DIMUNIT.CF);
	// if (getExpectedInnerPackCount() != null)
	// ttl = ttl*getExpectedInnerPackCount();
	// return ttl;
	// }
	// else
	// return 0.0;
	// }
	//
	// public Double WHReceiveLine.getExpectedProdTotalWeight()
	// {
	// if (getProduct().getWeight() != null)
	// {
	// Double ttl = getProduct().getWeight();
	// if (getExpectedInnerPackCount() != null)
	// ttl = ttl*getExpectedInnerPackCount();
	// return ttl;
	// }
	// else
	// return 0.0;
	// }
	//
	// public Double WHReceiveLine.getExpectedProdTotalVolume()
	// {
	// if (getProduct().getVolume() != null)
	// {
	// Double ttl = getProduct().getVolume();
	// if (getExpectedInnerPackCount() != null)
	// ttl = ttl*getExpectedInnerPackCount();
	// return ttl;
	// }
	// else
	// return 0.0;
	// }
	//
	// /**
	// * I've got 5 things, create label for 1 of 5, 2 of 5 ... 5 of 5
	// * @param newRecord
	// * @return
	// */
	// public List<StockItem> addManyAsGroup(StockItem newRecord) {
	// List<StockItem> newStockItems = new ArrayList<StockItem>();
	//
	// // --Naming
	// // Process arrival item
	// if (Validator.isNotNull(newRecord.getReceiveLine()))// This item belongs
	// // to a rcv
	// {
	// try {
	// ReceiveLine rcvLine = newRecord.getReceiveLine();
	// int rcvLineStockCount = rcvLine.getExpectedInnerPackCount();
	// int rcvLineArrvdStockCount = Validator.isNotNull(rcvLine
	// .getArrivedInnerPackCount()) ? rcvLine
	// .getArrivedInnerPackCount() : 0;
	// int reqdStockCount = rcvLineArrvdStockCount
	// - rcvLineArrvdStockCount;
	//
	// StockItem newGrpStockItem = null;
	// Double guessedWeight = rcvLine.getExpectedProdTotalWeightInLbs() /
	// rcvLineStockCount;
	// Double guessedVol = rcvLine.getExpectedProdTotalVolumeInCFs()
	// / rcvLineStockCount;
	// for (int i = rcvLineArrvdStockCount + 1; i <= rcvLineStockCount; i++) {
	// newGrpStockItem = new StockItem();
	// newGrpsetStockCount(1);
	// newGrpsetReceiveLine(rcvLine);
	// newGrpsetArrival(newRecord.getArrival());
	// newGrpsetArrivalReceipt(newRecord.getArrivalReceipt());
	// newGrpsetRcvLineGroupIndex(i);
	// newGrpsetRcvLineGroupSize(rcvLineStockCount);
	// newGrpsetWeight(guessedWeight);
	// newGrpsetVolume(guessedVol);
	// newGrpsetLocation(LocationData.provideTBD());
	//
	// newGrpStockItem = (StockItem) newGrpmerge();
	//
	// newGrpStockItem = processRegularStockItem(newGrpStockItem);
	//
	// newStockItems.add(newGrpStockItem);
	// }
	//
	// } catch (Exception e) {
	// StringWriter sw = new StringWriter();
	// e.printStackTrace(new PrintWriter(sw));
	// String stacktrace = sw.toString();
	// log.warn(stacktrace);
	// }
	// }
	//
	// return newStockItems;
	// }
	//
	private StockItem processRegularStockItem(StockItem stockItem, ReceiveLineStockItemSet itemSet) throws Exception {
		assert (itemSet.getReceiveLine() != null);
		assert (itemSet.getArrival() != null);

		log.debug("Processing Regular Stock Item[" + stockItem + "]...");

		// Assign name
		ReceiveLine parentRcvLine = itemSet.getReceiveLine();
		if (parentRcvLine.getRemainingInnerPackCount() == null) {
			if (parentRcvLine.getExpectedInnerPackCount() == null) {
				parentRcvLine.setExpectedInnerPackCount(0);
				// No Receive Line should exist with an expected inner pack
				// count of 0
				parentRcvLine.setStatus(RECEIVELINESTATUS.ONHOLD);
				parentRcvLine.setRemainingInnerPackCount(0);
			} else {
				parentRcvLine.setRemainingInnerPackCount(parentRcvLine.getExpectedInnerPackCount());
			}
			parentRcvLine = em.merge(parentRcvLine);
		}
		// -- Product
		stockItem.setProduct(parentRcvLine.getProduct());
		// -- Location
		if (Validator.isNull(stockItem.getLocation())) {
			Location tbd = null;// LocationData.provideTBD();
			stockItem.setLocation(tbd);
		}
		Arrival parentArrival = itemSet.getArrival();
		String parentRcvLineName = parentRcvLine.getName();

		String arrvlItemName = null;
		if (Validator.isNotNull(itemSet.getGroupSize()) && itemSet.getGroupSize() > 0) {
			String formatIndex = String.format("%02d", stockItem.getGroupIndex());
			String formatSize = String.format("%02d", itemSet.getGroupSize());
			arrvlItemName = parentRcvLineName + "L" + parentRcvLine.getLineNumber() + formatIndex + "_" + formatSize;
		} else {
			arrvlItemName = parentRcvLineName + "L" + parentRcvLine.getLineNumber() + "_" + (parentRcvLine.getArrivedInnerPackCount() == null ? "0" : parentRcvLine.getArrivedInnerPackCount());
		}
		
		StockItem existingStockItem = getByCode(arrvlItemName);
		if (existingStockItem == null) {
			stockItem.setName(arrvlItemName);
			stockItem.setCode(arrvlItemName);
		} else {
			stockItem = existingStockItem;
		}
		
		// -- Update ArrivalReceipt[Line]
		if (Validator.isNull(itemSet.getArrivalReceiptLine())) {
			ArrivalReceipt ar = itemSet.getArrivalReceipt();
			if (Validator.isNull(ar)) {
				ar = new ArrivalReceipt();
				ar.setParentArrival(itemSet.getArrival());
				ar.setShipper(itemSet.getArrival().getActualShipper());
				ar.setStatus(ARRIVALRECEIPTSTATUS.ARRIVING);
				ar = arrivalReceiptDAOService.add(parentArrival.getId(), ar);
			}
			ArrivalReceiptLine rcptLine = new ArrivalReceiptLine();
			rcptLine.setParentArrivalReceipt(ar);
			rcptLine.setReceiveLine(itemSet.getReceiveLine());
			rcptLine.setStatus(ARRIVALRECEIPTLINESTATUS.ARRIVED);
			rcptLine = arrivalReceiptDAOService.addArrivalReceiptLine(ar.getId(), rcptLine);
			itemSet.setArrivalReceiptLine(rcptLine);
			itemSet.setArrivalReceipt(ar);
			ar.getRcptLines().add(rcptLine);
			arrivalReceiptDAOService.update(ar);
		}
		/**
		 * Commercial Record
		 */
		CommercialRecord cr = new CommercialRecord();
		EntityType et = entityTypeDAOService.provide(StockItem.class);
		cr.setOwnerEntityTypeId(et.getId());
		cr.setOwnerEntityId(stockItem.getId());
		cr = commercialRecordDAOService.add(cr);

		CommercialValue cv = new CommercialValue();
		cv.setValue(parentRcvLine.getProduct().getCommercialRecord().getCommercialValue().getValue());
		cv.setCurrency(parentRcvLine.getProduct().getCommercialRecord().getCommercialValue().getCurrency());
		cv.setName("Value of " + stockItem.getName());

		cv.setParentCommercialRecord(cr);
		cv = commercialValueDAOService.add(cv);
		cr.setCommercialValue(cv);

		stockItem.setCommercialRecord(cr);
		stockItem.setStatus(STOCKITEMSTATUS.RECEIVED);
		stockItem.setUsedStockCount(0);
		stockItem.setActive(true);
		stockItem.setDateCreated(new Date());
		stockItem.setDateLastUpdated(new Date());
		stockItem.setExpectedVolumeUnit(parentRcvLine.getProduct().getVolUnit());
		stockItem.setVolumeUnit(parentRcvLine.getProduct().getVolUnit());
		stockItem.setExpectedVolume(parentRcvLine.getProduct().getVolume());
		stockItem.setExpectedWeightUnit(parentRcvLine.getProduct().getWeightUnit());
		stockItem.setWeightUnit(parentRcvLine.getProduct().getWeightUnit());
		stockItem.setExpectedLength(parentRcvLine.getProduct().getLen());
		stockItem.setExpectedLengthUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setLengthUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setExpectedWidth(parentRcvLine.getProduct().getWidth());
		stockItem.setExpectedWidthUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setWidthUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setExpectedHeight(parentRcvLine.getProduct().getHeight());
		stockItem.setExpectedHeightUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setHeightUnit(parentRcvLine.getProduct().getDimUnit());
		stockItem.setExpectedInnerPackUnit(parentRcvLine.getProduct().getInnerPackUnit());
		stockItem.setInnerPackUnit(parentRcvLine.getProduct().getInnerPackUnit());
		stockItem.setExpectedInnerPackCount(parentRcvLine.getProduct().getInnerPackCount());
		stockItem.setInnerPackCount(parentRcvLine.getProduct().getInnerPackCount());
		stockItem.setExpectedOuterPackUnit(parentRcvLine.getProduct().getOuterPackUnit());
		stockItem.setOuterPackUnit(parentRcvLine.getProduct().getOuterPackUnit());
		stockItem.setExpectedOuterPackCount(parentRcvLine.getProduct().getOuterPackCount());
		stockItem.setOuterPackCount(parentRcvLine.getProduct().getOuterPackCount());

		Folder fldr = documentRepositoryService.provideFolderForEntity(et, stockItem.getId());
		stockItem.setDocFolder(fldr);

		stockItem = (StockItem) update(stockItem);
		log.debug("Created Stock Item[" + stockItem.getName() + "]...");

		itemSet.getStockItems().add(stockItem);
		itemSet = em.merge(itemSet);

		// Update parent receive line
		updateParentRcvLine(stockItem, null, itemSet, false);
		em.flush();
		
		return stockItem;
	}

	public void updateParentRcvLine(StockItem arrvlItemNew, StockItem arrvlItemOld, ReceiveLineStockItemSet itemSet, boolean delete) throws Exception {
		ReceiveLine rcvLine_ = null;
		rcvLine_ = itemSet.getReceiveLine();

		Integer expectedReceiveLineInnerPackCount = rcvLine_.getExpectedInnerPackCount();
		Integer arrivedReceiveLineInnerPackCount = rcvLine_.getArrivedInnerPackCount();
		Integer stockItemPackCount = arrvlItemNew.getInnerPackCount();

		if (expectedReceiveLineInnerPackCount == null) {
			expectedReceiveLineInnerPackCount = 0;
			rcvLine_.setExpectedInnerPackCount(expectedReceiveLineInnerPackCount);
			rcvLine_ = em.merge(rcvLine_);
			throw new Exception("Cannot attach a Stock Item to a Receive Line with an expected inner pack count of 0 (or null).");
		} else if (expectedReceiveLineInnerPackCount == 0) {
			throw new Exception("Cannot attach a Stock Item to a Receive Line with an expected inner pack count of 0 (or null).");
		}
		if (arrivedReceiveLineInnerPackCount == null) {
			arrivedReceiveLineInnerPackCount = 0;
		}
		if (stockItemPackCount == null) {
			stockItemPackCount = 1;
			arrvlItemNew.setInnerPackCount(stockItemPackCount);
			arrvlItemNew = em.merge(arrvlItemNew);
		} else if (stockItemPackCount == 0) {
			throw new Exception("Stock Item Inner Pack count is zero - this should be impossible");
		}

		arrivedReceiveLineInnerPackCount += stockItemPackCount;
		rcvLine_.setArrivedInnerPackCount(arrivedReceiveLineInnerPackCount);
		int remainingInnerPackCount = expectedReceiveLineInnerPackCount - arrivedReceiveLineInnerPackCount;
		rcvLine_.setRemainingInnerPackCount(remainingInnerPackCount);
		if (remainingInnerPackCount == 0) {
			rcvLine_.setStatus(RECEIVELINESTATUS.ARRIVED);
		} else {
			rcvLine_.setStatus(RECEIVELINESTATUS.ARRIVING);
		}
		rcvLine_ = receiveLineDAOService.update(rcvLine_);

		// -- Update owners
		updateOwnerReceive(rcvLine_);
		updateOwnerArrival(itemSet.getArrival());
	}

	private void updateOwnerArrival(Arrival arrival) {
		Receive ownerRcv = arrival.getReceive();
		if (ownerRcv.getStatus() == RECEIVESTATUS.ARRIVED) {
			arrival.setArrvlStatus(ARRIVALSTATUS.ARRIVED);
			arrival = arrivalDAOService.update(arrival);
		}
	}

	private void updateOwnerReceive(ReceiveLine rcvLine_) {
		int ttlLinesArrived = receiveLineDAOService.findReceiveLinesByStatusAndReceive(RECEIVELINESTATUS.ARRIVED, rcvLine_.getParentReceive()).getResultList().size();
		int ttlLines = rcvLine_.getParentReceive().getRcvLines().size();
		log.debug("Receive[" + rcvLine_.getName() + "] ttlLinesArrived:" + ttlLinesArrived);
		log.debug("Receive[" + rcvLine_.getName() + "] ttlLines:" + ttlLines);
		Receive ownerRcv = rcvLine_.getParentReceive();
		if (ttlLinesArrived > 0) {
			if (ttlLinesArrived == ttlLines) {
				ownerRcv.setStatus(RECEIVESTATUS.ARRIVED);
			} else {
				ownerRcv.setStatus(RECEIVESTATUS.ARRIVING);
			}
			ownerRcv = (Receive) receiveDAOService.update(ownerRcv);
		}
	}

	@Override
	public StockItem update(StockItem record) throws Exception {
		// -- Check if Unit Conversion is in effect
		/*
		 * ProductUnitConversion uc = record.getReceivedStockUnitConvesion();
		 * String siStockUnit = uc.getParentPackUnit().getCode(); String
		 * siExpectedStockUnit = record.getReceiveLine().getProduct()
		 * .getCode(); if (!Validator.equals(siStockUnit, siExpectedStockUnit))
		 * { Double convFactor = uc.getQuantityInParentPack(); Integer
		 * stockCount = record.getStockCount(); record.setStockCount(stockCount
		 * * convFactor.intValue()); }
		 */
		// Product.updateProductAvailability(record.getProduct());

		record.setDateLastUpdated(new Date());
		return (StockItem) em.merge(record);
	}

	@Override
	public List<StockItem> getAll() {
		return null;
	}

	@Override
	public void delete(StockItem record) {
		em.remove(record);
	}

	@Override
	public StockItem get(long id) {
		return em.getReference(StockItem.class, id);
	}

	@Override
	public StockItem getByCode(String string) {
		try {
			Query q = em.createQuery("select o from com.conx.logistics.app.whse.im.domain.stockitem.StockItem o WHERE o.code = :code");
			q.setParameter("code", string);
			return (StockItem) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
