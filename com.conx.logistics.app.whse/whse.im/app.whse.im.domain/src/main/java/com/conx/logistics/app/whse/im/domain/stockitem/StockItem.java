package com.conx.logistics.app.whse.im.domain.stockitem;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.conx.logistics.app.whse.domain.location.Location;
import com.conx.logistics.app.whse.im.domain.types.STOCKITEMCREATIONTYPE;
import com.conx.logistics.app.whse.im.domain.types.STOCKITEMSTATUS;
import com.conx.logistics.mdm.domain.MultitenantBaseEntity;
import com.conx.logistics.mdm.domain.commercialrecord.CommercialRecord;
import com.conx.logistics.mdm.domain.organization.Organization;
import com.conx.logistics.mdm.domain.product.DimUnit;
import com.conx.logistics.mdm.domain.product.PackUnit;
import com.conx.logistics.mdm.domain.product.Product;
import com.conx.logistics.mdm.domain.product.ProductUnitConversion;
import com.conx.logistics.mdm.domain.product.WeightUnit;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "whstockitem")
public class StockItem extends MultitenantBaseEntity {
	private int groupIndex;
	
	private int groupSize = 1;
	
	@ManyToOne(targetEntity = Location.class, fetch = FetchType.LAZY)
	@JoinColumn
	private Location location;

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinColumn
	private Product product;
	
	@ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
	@JoinColumn
	private Organization shipper;
	
	@ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
	@JoinColumn
	private Organization consignee;

	@OneToOne(targetEntity = CommercialRecord.class, fetch = FetchType.LAZY)
	@JoinColumn
	private CommercialRecord commercialRecord;

	@OneToOne(targetEntity = PackagedStockItem.class, fetch = FetchType.LAZY)
	@JoinColumn
	private PackagedStockItem packagedStockItem;

	@Enumerated(EnumType.STRING)
	private STOCKITEMSTATUS status;

	@Enumerated(EnumType.STRING)
	private STOCKITEMCREATIONTYPE stockCreationType;

	@OneToOne(targetEntity = ProductUnitConversion.class, fetch = FetchType.LAZY)
	@JoinColumn
	private ProductUnitConversion receivedStockUnitConvesion;

	private Integer innerPackCount;
	
	private Integer usedInnerPackCount;
	
	@ManyToOne(targetEntity = PackUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private PackUnit innerPackUnit;

	private Integer expectedInnerPackCount;
	
	@ManyToOne(targetEntity = PackUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private PackUnit expectedInnerPackUnit;

	private Integer outerPackCount;
	
	@ManyToOne(targetEntity = PackUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private PackUnit outerPackUnit;

	private Integer expectedOuterPackCount;
	
	@ManyToOne(targetEntity = PackUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private PackUnit expectedOuterPackUnit;
	
	private Double weight;
	
	@ManyToOne(targetEntity = WeightUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private WeightUnit weightUnit;

	private Double expectedWeight;
	
	@ManyToOne(targetEntity = WeightUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private WeightUnit expectedWeightUnit;

	private Double length;

	private Double expectedLength;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit lengthUnit;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit expectedLengthUnit;

	private Double width;

	private Double expectedWidth;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit widthUnit;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit expectedWidthUnit;

	private Double height;

	private Double expectedHeight;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit heightUnit;

	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit expectedHeightUnit;

	private Double volume;
	
	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit volumeUnit;

	private Double expectedVolume;
	
	@ManyToOne(targetEntity = DimUnit.class, fetch = FetchType.LAZY)
	@JoinColumn
	private DimUnit expectedVolumeUnit;

	private String sku;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	public CommercialRecord getCommercialRecord() {
		return commercialRecord;
	}

	public void setCommercialRecord(CommercialRecord commercialRecord) {
		this.commercialRecord = commercialRecord;
	}

	public PackagedStockItem getPackagedStockItem() {
		return packagedStockItem;
	}

	public void setPackagedStockItem(PackagedStockItem packagedStockItem) {
		this.packagedStockItem = packagedStockItem;
	}

	public STOCKITEMSTATUS getStatus() {
		return status;
	}

	public void setStatus(STOCKITEMSTATUS status) {
		this.status = status;
	}

	public STOCKITEMCREATIONTYPE getStockCreationType() {
		return stockCreationType;
	}

	public void setStockCreationType(STOCKITEMCREATIONTYPE stockCreationType) {
		this.stockCreationType = stockCreationType;
	}

	public ProductUnitConversion getReceivedStockUnitConvesion() {
		return receivedStockUnitConvesion;
	}

	public void setReceivedStockUnitConvesion(ProductUnitConversion receivedStockUnitConvesion) {
		this.receivedStockUnitConvesion = receivedStockUnitConvesion;
	}

	public Integer getUsedStockCount() {
		return usedInnerPackCount;
	}

	public void setUsedStockCount(Integer usedStockCount) {
		this.usedInnerPackCount = usedStockCount;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public WeightUnit getExpectedWeightUnit() {
		return expectedWeightUnit;
	}

	public void setExpectedWeightUnit(WeightUnit expectedWeightUnit) {
		this.expectedWeightUnit = expectedWeightUnit;
	}

	public Double getExpectedWeight() {
		return expectedWeight;
	}

	public void setExpectedWeight(Double expectedWeight) {
		this.expectedWeight = expectedWeight;
	}

	public Double getExpectedLength() {
		return expectedLength;
	}

	public void setExpectedLength(Double expectedLength) {
		this.expectedLength = expectedLength;
	}

	public Double getExpectedWidth() {
		return expectedWidth;
	}

	public void setExpectedWidth(Double expectedWidth) {
		this.expectedWidth = expectedWidth;
	}

	public Double getExpectedHeight() {
		return expectedHeight;
	}

	public void setExpectedHeight(Double expectedHeight) {
		this.expectedHeight = expectedHeight;
	}

	public PackUnit getInnerPackUnit() {
		return innerPackUnit;
	}

	public void setInnerPackUnit(PackUnit innerPackUnit) {
		this.innerPackUnit = innerPackUnit;
	}

	public PackUnit getOuterPackUnit() {
		return outerPackUnit;
	}

	public void setOuterPackUnit(PackUnit outerPackUnit) {
		this.outerPackUnit = outerPackUnit;
	}

	public PackUnit getExpectedInnerPackUnit() {
		return expectedInnerPackUnit;
	}

	public void setExpectedInnerPackUnit(PackUnit expectedInnerPackUnit) {
		this.expectedInnerPackUnit = expectedInnerPackUnit;
	}

	public PackUnit getExpectedOuterPackUnit() {
		return expectedOuterPackUnit;
	}

	public void setExpectedOuterPackUnit(PackUnit expectedOuterPackUnit) {
		this.expectedOuterPackUnit = expectedOuterPackUnit;
	}

	public Integer getInnerPackCount() {
		return innerPackCount;
	}

	public void setInnerPackCount(Integer innerPackCount) {
		this.innerPackCount = innerPackCount;
	}

	public Integer getExpectedInnerPackCount() {
		return expectedInnerPackCount;
	}

	public void setExpectedInnerPackCount(Integer expectedInnerPackCount) {
		this.expectedInnerPackCount = expectedInnerPackCount;
	}

	public Integer getOuterPackCount() {
		return outerPackCount;
	}

	public void setOuterPackCount(Integer outerPackCount) {
		this.outerPackCount = outerPackCount;
	}

	public Integer getExpectedOuterPackCount() {
		return expectedOuterPackCount;
	}

	public void setExpectedOuterPackCount(Integer expectedOuterPackCount) {
		this.expectedOuterPackCount = expectedOuterPackCount;
	}

	public DimUnit getLengthUnit() {
		return lengthUnit;
	}

	public void setLengthUnit(DimUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public DimUnit getExpectedLengthUnit() {
		return expectedLengthUnit;
	}

	public void setExpectedLengthUnit(DimUnit expectedLengthUnit) {
		this.expectedLengthUnit = expectedLengthUnit;
	}

	public DimUnit getWidthUnit() {
		return widthUnit;
	}

	public void setWidthUnit(DimUnit widthUnit) {
		this.widthUnit = widthUnit;
	}

	public DimUnit getExpectedWidthUnit() {
		return expectedWidthUnit;
	}

	public void setExpectedWidthUnit(DimUnit expectedWidthUnit) {
		this.expectedWidthUnit = expectedWidthUnit;
	}

	public DimUnit getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(DimUnit heightUnit) {
		this.heightUnit = heightUnit;
	}

	public DimUnit getExpectedHeightUnit() {
		return expectedHeightUnit;
	}

	public void setExpectedHeightUnit(DimUnit expectedHeightUnit) {
		this.expectedHeightUnit = expectedHeightUnit;
	}

	public Integer getUsedInnerPackCount() {
		return usedInnerPackCount;
	}

	public void setUsedInnerPackCount(Integer usedInnerPackCount) {
		this.usedInnerPackCount = usedInnerPackCount;
	}

	public DimUnit getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(DimUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public Double getExpectedVolume() {
		return expectedVolume;
	}

	public void setExpectedVolume(Double expectedVolume) {
		this.expectedVolume = expectedVolume;
	}

	public DimUnit getExpectedVolumeUnit() {
		return expectedVolumeUnit;
	}

	public void setExpectedVolumeUnit(DimUnit expectedVolumeUnit) {
		this.expectedVolumeUnit = expectedVolumeUnit;
	}

	public int getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	public Organization getShipper() {
		return shipper;
	}

	public void setShipper(Organization shipper) {
		this.shipper = shipper;
	}

	public Organization getConsignee() {
		return consignee;
	}

	public void setConsignee(Organization consignee) {
		this.consignee = consignee;
	}
}
