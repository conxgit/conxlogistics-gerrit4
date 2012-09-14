package com.conx.logistics.kernel.ui.factory.services;

import java.util.Map;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;

public interface IEntityEditorFactory {
	/**
	 * 
	 * Factory constants
	 * 
	 */
	//-- Params
	public final static String FACTORY_PARAM_MVP_MAIN_APP = "MAIN_APP";
	public final static String FACTORY_PARAM_MVP_ENTITY_MANAGER_FACTORY = "ENTITY_MANAGER_FACTORY";
	public final static String FACTORY_PARAM_MVP_ENTITY_MANAGER = "ENTITY_MANAGER";
	public final static String FACTORY_PARAM_MVP_EXTRA_PARAMS_MAP = "EXTRA_PARAMS_MAP";
	public final static String FACTORY_PARAM_MVP_PRESENTER_FACTORY = "PRESENTER_FACTORY";
	public final static String FACTORY_PARAM_MVP_EVENTBUS_MANAGER = "EVENTBUS_MANAGER";
	public final static String FACTORY_PARAM_MVP_COMPONENT_MODEL = "COMPONENT_MODEL";
	public final static String FACTORY_PARAM_MVP_LOCALE = "LOCALE";
	public final static String FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER = "CURRENT_MLENTITY_EDITOR_PRESENTER";
	public final static String FACTORY_PARAM_MVP_CURRENT_APP_PRESENTER = "CURRENT_APP_PRESENTER";
	public final static String FACTORY_PARAM_MVP_EDITOR_CONTAINER = "MVP_EDITOR_CONTAINER";
	public final static String FACTORY_PARAM_MVP_PARENT_EDITOR = "MVP_PARENT_EDITOR";
	public final static String FACTORY_PARAM_MVP_ITEM_DATASOURCE = "MVP_ITEM_DATASOURCE";
	public final static String FACTORY_PARAM_MVP_LINE_EDITOR_SECTION_PRESENTER = "MVP_LINE_EDITOR_SECTION_PRESENTER";
	
	public final static String FACTORY_PARAM_MVP_ENTITYMANAGERPERREQUESTHELPER = "ENTITYMANAGERPERREQUESTHELPER";
	
	//-- Services
	public final static String FACTORY_PARAM_IDOCLIB_REPO_SERVICE = "IDOCLIB_REPO_SERVICE";
	public final static String FACTORY_PARAM_IFOLDER_SERVICE = "IFOLDER_SERVICE";
	public final static String FACTORY_PARAM_IENTITY_METADATA_SERVICE = "IENTITY_METADATA_SERVICE";
	public final static String FACTORY_PARAM_MAIN_APP = "MAIN_APP";
	
	public Object create(AbstractConXComponent conXComponent, Map<String,Object> params);
}
