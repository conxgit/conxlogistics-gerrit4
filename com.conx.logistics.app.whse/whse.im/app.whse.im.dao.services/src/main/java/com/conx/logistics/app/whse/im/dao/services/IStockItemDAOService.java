package com.conx.logistics.app.whse.im.dao.services;

import java.util.List;

import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;


public interface IStockItemDAOService {
	public List<StockItem> getAll();

	public void delete(StockItem record);

	public StockItem update(StockItem record) throws Exception;

	public StockItem get(long id);

	public StockItem getByCode(String string);
	
	public StockItem addDynamicStockItem(StockItem newRecord, Long arrivalReceiptPK, Long arrivalReceiptLinePK) throws Exception;
	
	public StockItem addOneOfGroup(StockItem newRecord, Long receiveLinePK, Long arrivalReceiptPK, Long arrivalReceiptLinePK) throws Exception;

	/**
	 * Gets the label url for a stock item given the reporting url.
	 * 
	 * @param stockItem
	 * @param reportingUrl url of the reporting web service
	 * @return the url of the stock item label
	 */
	public String getStockItemLabelUrl(StockItem stockItem, String reportingUrl);
}
