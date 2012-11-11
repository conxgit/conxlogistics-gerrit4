package com.conx.logistics.mdm.dao.services.product;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.mdm.domain.constants.DimUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.WeightUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.product.DimUnit;
import com.conx.logistics.mdm.domain.product.WeightUnit;

public interface IUnitConversionService {
	/**
	 * WEIGHT
	 * 
	 * @param amount
	 * @param fromUnit
	 * @param toUnit
	 * @return
	 */
	public Double convertWeight(Double amount, WeightUnit fromUnit, WeightUnit toUnit);

	public Double getWeightConversionFactor(WeightUnit fromUnit, WeightUnit toUnit);
	
	/**
	 * DimUnit
	 * 
	 * @param amount
	 * @param fromUnit
	 * @param toUnit
	 * @return
	 */
	public Double convertDimension(Double amount, DimUnit fromUnit, DimUnit toUnit);

	public Double getDimConversionFactor(DimUnit fromUnit, DimUnit toUnit);
}
