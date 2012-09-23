package com.conx.logistics.app.whse.dao.services;

import com.conx.logistics.mdm.domain.application.Application;

public interface IWarehouseApplicationDAOService {
	/**
	 * Warehouse App
	 */
	//== App
	public static final String WAREHOUSE_APP_NAME = "Warehouse";
	public static final String WAREHOUSE_APP_CODE = "WHSE";
	
	//==== Receiving Featureset
	public static final String WAREHOUSE_APP_RECEIVING_NAME = "Receiving";
	public static final String WAREHOUSE_APP_RECEIVING_CODE = "RCVNG";	
	
	//==== Receiving ASN Featureset
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NAME = "ASN's";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_CODE = "ASN";		
	
	//========= Receiving ASN :: Search Feature
	public static final String WAREHOUSE_APP_RECEIVING_ASN_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_SEARCH_CODE = "SEARCH";	
	
	
	//========= Receiving ASN :: New ASN Feature
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_ASN_NEW_CODE = "NEW";	
	
	//==== Receiving ASN Featureset
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NAME = "Receives";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_CODE = "RCV";
	
	//========= Receiving RCV :: Search Feature
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_CODE = "SEARCH";	
	public static final String WAREHOUSE_APP_RECEIVING_RCV_SEARCH_COMPONENT = "searchReceives";
	
	//========= Receiving RCV :: New ASN Feature
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_RCV_NEW_CODE = "NEW";	
	
	//========= Receiving ARVL :: Search Feature
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_NAME = "Search";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_CODE = "SEARCH";	
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_SEARCH_COMPONENT = "searchArrivals";
	
	//========= Receiving ARVL :: New ASN Feature
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_NAME = "New";
	public static final String WAREHOUSE_APP_RECEIVING_ARVL_NEW_CODE = "NEW";		

	public Application provideApplicationMetadata();	
}
