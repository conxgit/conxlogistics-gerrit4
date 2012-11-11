package com.conx.logistics.app.whse.rcv.rcv.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.conx.logistics.app.tms.domain.types.TRANSMODE;
import com.conx.logistics.app.whse.domain.warehouse.Warehouse;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALSTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALTYPE;
import com.conx.logistics.mdm.domain.MultitenantBaseEntity;
import com.conx.logistics.mdm.domain.organization.Organization;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="wharrival")
public class Arrival extends MultitenantBaseEntity {
	@ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Warehouse warehouse;
	
    @OneToOne(targetEntity = Pickup.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Pickup actualPickUp;
    
    @OneToOne(targetEntity = DropOff.class, fetch = FetchType.LAZY)
    @JoinColumn
    private DropOff actualDropOff;    
    
    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Organization actualLocalTrans;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Organization actualShipper;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Organization actualShippedFrom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReceiveLineStockItemSet> rlStockItems = new java.util.HashSet<ReceiveLineStockItemSet>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ArrivalReceipt> receipts = new java.util.HashSet<ArrivalReceipt>();
    
    @OneToOne(targetEntity = Receive.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Receive receive;

    private String actualBolNumber;

    private String actualVehicleNumber;

    private String actualDriverName;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date actualArrivalTime;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date weekEndingDate;

    private String weekEnding;

    @Enumerated(EnumType.STRING)
    private ARRIVALTYPE arvlType;
    
    @Enumerated(EnumType.STRING)
    private TRANSMODE mode;

    @Enumerated(EnumType.STRING)
    private ARRIVALSTATUS arrvlStatus;

	public Pickup getActualPickUp() {
		return actualPickUp;
	}

	public void setActualPickUp(Pickup actualPickUp) {
		this.actualPickUp = actualPickUp;
	}

	public DropOff getActualDropOff() {
		return actualDropOff;
	}

	public void setActualDropOff(DropOff actualDropOff) {
		this.actualDropOff = actualDropOff;
	}

	public Organization getActualLocalTrans() {
		return actualLocalTrans;
	}

	public void setActualLocalTrans(Organization actualLocalTrans) {
		this.actualLocalTrans = actualLocalTrans;
	}

	public Organization getActualShipper() {
		return actualShipper;
	}

	public void setActualShipper(Organization actualShipper) {
		this.actualShipper = actualShipper;
	}

	public Organization getActualShippedFrom() {
		return actualShippedFrom;
	}

	public void setActualShippedFrom(Organization actualShippedFrom) {
		this.actualShippedFrom = actualShippedFrom;
	}

	public Set<ReceiveLineStockItemSet> getRlStockItems() {
		return rlStockItems;
	}

	public void setRlStockItems(Set<ReceiveLineStockItemSet> rlStockItems) {
		this.rlStockItems = rlStockItems;
	}

	public Set<ArrivalReceipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(Set<ArrivalReceipt> receipts) {
		this.receipts = receipts;
	}

	public Receive getReceive() {
		return receive;
	}

	public void setReceive(Receive receive) {
		this.receive = receive;
	}

	public String getActualBolNumber() {
		return actualBolNumber;
	}

	public void setActualBolNumber(String actualBolNumber) {
		this.actualBolNumber = actualBolNumber;
	}

	public String getActualVehicleNumber() {
		return actualVehicleNumber;
	}

	public void setActualVehicleNumber(String actualVehicleNumber) {
		this.actualVehicleNumber = actualVehicleNumber;
	}

	public String getActualDriverName() {
		return actualDriverName;
	}

	public void setActualDriverName(String actualDriverName) {
		this.actualDriverName = actualDriverName;
	}

	public Date getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(Date actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

	public Date getWeekEndingDate() {
		return weekEndingDate;
	}

	public void setWeekEndingDate(Date weekEndingDate) {
		this.weekEndingDate = weekEndingDate;
	}

	public String getWeekEnding() {
		return weekEnding;
	}

	public void setWeekEnding(String weekEnding) {
		this.weekEnding = weekEnding;
	}

	public ARRIVALTYPE getArvlType() {
		return arvlType;
	}

	public void setArvlType(ARRIVALTYPE arvlType) {
		this.arvlType = arvlType;
	}

	public TRANSMODE getMode() {
		return mode;
	}

	public void setMode(TRANSMODE mode) {
		this.mode = mode;
	}

	public ARRIVALSTATUS getArrvlStatus() {
		return arrvlStatus;
	}

	public void setArrvlStatus(ARRIVALSTATUS arrvlStatus) {
		this.arrvlStatus = arrvlStatus;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
}