package com.conx.logistics.app.whse.dao.services;

import com.conx.logistics.mdm.domain.application.Application;

public interface IWarehouseApplicationDAOService {
	/**
	 * Warehouse App
	 */
	// == App
	public static final String WAREHOUSE_APP_NAME = "Warehouse";
	public static final String WAREHOUSE_APP_CODE = "WHSE";

	// ==== Receiving Featureset
	public static final String WAREHOUSE_APP_RECEIVING_NAME = "Receiving";
	public static final String WAREHOUSE_APP_RECEIVING_CODE = "RCVNG";

	// ==== Receiving ASN Featureset
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NAME = "ASN's";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_CODE = "ASN";

	// ========= Receiving ASN :: Search ASN Feature
	public static final String WAREHOUSE_APP_RECEIVING_ASN_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_SEARCH_CODE = "SEARCH_ASN";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_SEARCH_CAPTION = "Search ASN's";

	// ========= Receiving ASN :: New ASN Feature
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_CODE = "NEW_ASN";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_CAPTION = "New ASN";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_ICON_URL = "toolstrip/img/task.png";

	// ==== Receiving Receive Featureset
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NAME = "Receives";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_CODE = "RCVS";

	// ========= Receiving RCV :: Search Receives Feature
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_CODE = "SEARCH_RCV";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_COMPONENT = "searchReceives";

	// ========= Receiving RCV :: New Receive Feature
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NEW_CODE = "NEW_RCV";

	// ==== Receiving Arrival Featureset
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NAME = "Arrivals";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_CODE = "ARVLS";

	// ========= Receiving ARVL :: Search Arrivals Feature
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_CODE = "SEARCH_ARVL";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_CAPTION = "Search Arrivals";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_COMPONENT = "searchArrivals";

	// ========= Receiving ARVL :: New Arrival Feature
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_CODE = "NEW_ARVL";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_CAPTION = "New Arrival";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_ICON_URL = "toolstrip/img/task.png";
	
	// ==== IM Featureset
	public static final String WAREHOUSE_APP_IM_NAME = "Inventory Management";
	public static final String WAREHOUSE_APP_IM_CODE = "IM";

	// ==== IM ASN Featureset
	public static final String WAREHOUSE_APP_IM_STOCKITEM_NAME = "StockItems";
	public static final String WAREHOUSE_APP_IM_STOCKITEM_CODE = "SI";

	// ========= IM STOCKITEM :: Search STOCKITEM Feature
	public static final String WAREHOUSE_APP_IM_STOCKITEM_SEARCH_NAME = "Search StockItems";
	public static final String WAREHOUSE_APP_IM_STOCKITEM_SEARCH_CODE = "SEARCH_SI";
	public static final String WAREHOUSE_APP_IM_STOCKITEM_SEARCH_COMPONENT = "searchStockItems";


	public Application provideApplicationMetadata();
}
