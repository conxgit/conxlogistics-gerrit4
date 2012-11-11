package com.conx.logistics.app.whse.im.dao.services;

import java.util.List;

import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;


public interface IStockItemDAOService {
	public List<StockItem> getAll();

	public void delete(StockItem record);

	public StockItem update(StockItem record) throws Exception;

	public StockItem get(long id);

	public StockItem getByCode(String string);
	
	public StockItem addOneOfGroup(StockItem newRecord, Long receiveLinePK, Long arrivalReceiptPK, Long arrivalReceiptLinePK) throws Exception;
}
