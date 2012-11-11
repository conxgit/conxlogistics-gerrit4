package com.conx.logistics.mdm.dao.services.impl.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.mdm.dao.services.product.IDimUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IUnitConversionService;
import com.conx.logistics.mdm.dao.services.product.IWeightUnitDAOService;
import com.conx.logistics.mdm.domain.constants.DimUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.WeightUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.product.DimUnit;
import com.conx.logistics.mdm.domain.product.WeightUnit;

@Transactional
@Repository
public class UnitConversionImpl implements IUnitConversionService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	public static Map<WeightUnit, Map<WeightUnit, Double>> weightLUT = new HashMap<WeightUnit, Map<WeightUnit, Double>>();
	public static Map<DimUnit, Map<DimUnit, Double>> dimLUT = new HashMap<DimUnit, Map<DimUnit, Double>>();	
    /**
     * Spring will inject a managed JPA {@link EntityManager} into this field.
     */
    @PersistenceContext
    private EntityManager em;	
    
    @Autowired
    private IWeightUnitDAOService weightUnitDao;
    
    @Autowired
    IDimUnitDAOService dimUnitDao;   
    
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public void init() {
		WeightUnit lbs = weightUnitDao.provide(WeightUnitCustomCONSTANTS.TYPE_LB, WeightUnitCustomCONSTANTS.TYPE_LB_DESCRIPTION);
		WeightUnit kg = weightUnitDao.provide(WeightUnitCustomCONSTANTS.TYPE_KG, WeightUnitCustomCONSTANTS.TYPE_KG_DESCRIPTION);
		
		DimUnit in = dimUnitDao.provide(DimUnitCustomCONSTANTS.TYPE_IN, DimUnitCustomCONSTANTS.TYPE_IN_DESCRIPTION);
		DimUnit ft = dimUnitDao.provide(DimUnitCustomCONSTANTS.TYPE_FT, DimUnitCustomCONSTANTS.TYPE_FT_DESCRIPTION);
		DimUnit m = dimUnitDao.provide(DimUnitCustomCONSTANTS.TYPE_M, DimUnitCustomCONSTANTS.TYPE_M_DESCRIPTION);
		DimUnit cf = dimUnitDao.provide(DimUnitCustomCONSTANTS.TYPE_CF, DimUnitCustomCONSTANTS.TYPE_CF_DESCRIPTION);
//		DimUnit cm = dimUnitDao.provide(DimUnitCustomCONSTANTS.TYPE_CM, DimUnitCustomCONSTANTS.TYPE_CM_DESCRIPTION);
		
		/**
		 * WEIGHT LUTs
		 */
		Map<WeightUnit, Double> weightFactorMap = null;

		// -- LB
		
		weightFactorMap = new HashMap<WeightUnit, Double>();
		weightFactorMap.put(lbs, 1.0);
		weightFactorMap.put(kg, 0.45359237);
		weightLUT.put(lbs, weightFactorMap);

		// -- KG
		weightFactorMap = new HashMap<WeightUnit, Double>();
		weightFactorMap.put(lbs, 2.2046226218);
		weightFactorMap.put(kg, 1.0);
		weightLUT.put(kg, weightFactorMap);

		/**
		 * DIM LUTs
		 */
		Map<DimUnit, Double> dimFactorMap = null;

		// -- IN
		dimFactorMap = new HashMap<DimUnit, Double>();
		dimFactorMap.put(in, 1.0);
		dimFactorMap.put(ft, 0.083333333333);
		dimFactorMap.put(m, 0.0254);
		dimLUT.put(in, dimFactorMap);

		// -- FT
		dimFactorMap = new HashMap<DimUnit, Double>();
		dimFactorMap.put(ft, 1.0);
		dimFactorMap.put(in, 12.0);
		dimFactorMap.put(m, 0.3048);
		dimLUT.put(ft, dimFactorMap);

		// -- CF
		dimFactorMap = new HashMap<DimUnit, Double>();
		dimFactorMap.put(cf, 1.0);
		dimFactorMap.put(in, 1728.0);
		dimFactorMap.put(m, 0.028316846592);
		dimLUT.put(cf, dimFactorMap);

		// -- CM
		dimFactorMap = new HashMap<DimUnit, Double>();
		dimFactorMap.put(m, 1.0);
		dimFactorMap.put(in, 61023.744095);
		dimFactorMap.put(cf, 35.314666721);
		dimLUT.put(m, dimFactorMap);

		// -- CM3
//		dimFactorMap = new HashMap<DimUnit, Double>();
//		dimFactorMap.put(DimUnit.CM3, 1.0);
//		dimFactorMap.put(m3, 0.000001);
//		dimFactorMap.put(DimUnit.CF, 0.000035314666721);
//		dimFactorMap.put(in3, 0.061023744095);
//		dimLUT.put(DimUnit.CM3, dimFactorMap);

		// -- CI3
//		dimFactorMap = new HashMap<DimUnit, Double>();
//		dimFactorMap.put(in3, 1.0);
//		dimFactorMap.put(m3, 0.000016387064);
//		dimFactorMap.put(DimUnit.CF, 0.0005787037037);
//		dimFactorMap.put(DimUnit.CM3, 16.387064);
//		dimLUT.put(in3, dimFactorMap);

	}	

	@Override
	public Double convertWeight(Double amount, WeightUnit fromUnit, WeightUnit toUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getWeightConversionFactor(WeightUnit fromUnit, WeightUnit toUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double convertDimension(Double amount, DimUnit fromUnit, DimUnit toUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDimConversionFactor(DimUnit fromUnit, DimUnit toUnit) {
		// TODO Auto-generated method stub
		return null;
	}

}
