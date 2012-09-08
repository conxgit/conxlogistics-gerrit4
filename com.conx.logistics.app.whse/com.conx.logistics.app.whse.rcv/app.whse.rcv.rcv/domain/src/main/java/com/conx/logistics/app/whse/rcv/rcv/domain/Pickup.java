package com.conx.logistics.app.whse.rcv.rcv.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.conx.logistics.app.whse.domain.docktype.DockType;
import com.conx.logistics.app.whse.rcv.asn.shared.type.DROPMODE;
import com.conx.logistics.mdm.domain.MultitenantBaseEntity;
import com.conx.logistics.mdm.domain.geolocation.Address;
import com.conx.logistics.mdm.domain.organization.Organization;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="whrcvpickup")
public class Pickup extends MultitenantBaseEntity implements Serializable {
    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Organization actualCfs;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Address actualCfsAddress;
    
    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Organization actualLocalTrans;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Address actualLocalTransAddress;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Organization actualPickUpFrom;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Address actualPickUpFromAddress;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date actualPickup;

    
    @ManyToOne(targetEntity = DockType.class, cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn
    private DockType actualDockType;

    private String actualShippersRef;
    
    private String actualDriverId;
    
    private String actualVehicleId;
    
    private String actualBolNumber;
    
    private String actualSealNumber;

    @Enumerated(EnumType.STRING)
    private DROPMODE actualDropMode;


	public Organization getLocalTrans() {
		return actualLocalTrans;
	}

	public void setLocalTrans(Organization localTrans) {
		this.actualLocalTrans = localTrans;
	}

	public Address getLocalTransAddress() {
		return actualLocalTransAddress;
	}

	public void setLocalTransAddress(Address localTransAddress) {
		this.actualLocalTransAddress = localTransAddress;
	}

	public Organization getPickUpFrom() {
		return actualPickUpFrom;
	}

	public void setPickUpFrom(Organization pickUpFrom) {
		this.actualPickUpFrom = pickUpFrom;
	}

	public Address getPickUpFromAddress() {
		return actualPickUpFromAddress;
	}

	public void setPickUpFromAddress(Address pickUpFromAddress) {
		this.actualPickUpFromAddress = pickUpFromAddress;
	}

	public Date getActualPickup() {
		return actualPickup;
	}

	public void setActualPickup(Date actualPickup) {
		this.actualPickup = actualPickup;
	}

	public String getShippersRef() {
		return actualShippersRef;
	}

	public void setShippersRef(String shippersRef) {
		this.actualShippersRef = shippersRef;
	}

	public DROPMODE getDropMode() {
		return actualDropMode;
	}

	public void setDropMode(DROPMODE dropMode) {
		this.actualDropMode = dropMode;
	}

	public String getDriverId() {
		return actualDriverId;
	}

	public void setDriverId(String driverId) {
		this.actualDriverId = driverId;
	}

	public String getVehicleId() {
		return actualVehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.actualVehicleId = vehicleId;
	}

	public String getBolNumber() {
		return actualBolNumber;
	}

	public void setBolNumber(String bolNumber) {
		this.actualBolNumber = bolNumber;
	}

	public String getSealNumber() {
		return actualSealNumber;
	}

	public void setSealNumber(String sealNumber) {
		this.actualSealNumber = sealNumber;
	}

	public DockType getDockType() {
		return actualDockType;
	}

	public void setDockType(DockType dockType) {
		this.actualDockType = dockType;
	}

	public Organization getActualCfs() {
		return actualCfs;
	}

	public void setActualCfs(Organization actualCfs) {
		this.actualCfs = actualCfs;
	}

	public Address getActualCfsAddress() {
		return actualCfsAddress;
	}

	public void setActualCfsAddress(Address actualCfsAddress) {
		this.actualCfsAddress = actualCfsAddress;
	}

	public Organization getActualLocalTrans() {
		return actualLocalTrans;
	}

	public void setActualLocalTrans(Organization actualLocalTrans) {
		this.actualLocalTrans = actualLocalTrans;
	}

	public Address getActualLocalTransAddress() {
		return actualLocalTransAddress;
	}

	public void setActualLocalTransAddress(Address actualLocalTransAddress) {
		this.actualLocalTransAddress = actualLocalTransAddress;
	}

	public Organization getActualPickUpFrom() {
		return actualPickUpFrom;
	}

	public void setActualPickUpFrom(Organization actualPickUpFrom) {
		this.actualPickUpFrom = actualPickUpFrom;
	}

	public Address getActualPickUpFromAddress() {
		return actualPickUpFromAddress;
	}

	public void setActualPickUpFromAddress(Address actualPickUpFromAddress) {
		this.actualPickUpFromAddress = actualPickUpFromAddress;
	}

	public DockType getActualDockType() {
		return actualDockType;
	}

	public void setActualDockType(DockType actualDockType) {
		this.actualDockType = actualDockType;
	}

	public String getActualShippersRef() {
		return actualShippersRef;
	}

	public void setActualShippersRef(String actualShippersRef) {
		this.actualShippersRef = actualShippersRef;
	}

	public String getActualDriverId() {
		return actualDriverId;
	}

	public void setActualDriverId(String actualDriverId) {
		this.actualDriverId = actualDriverId;
	}

	public String getActualVehicleId() {
		return actualVehicleId;
	}

	public void setActualVehicleId(String actualVehicleId) {
		this.actualVehicleId = actualVehicleId;
	}

	public String getActualBolNumber() {
		return actualBolNumber;
	}

	public void setActualBolNumber(String actualBolNumber) {
		this.actualBolNumber = actualBolNumber;
	}

	public String getActualSealNumber() {
		return actualSealNumber;
	}

	public void setActualSealNumber(String actualSealNumber) {
		this.actualSealNumber = actualSealNumber;
	}

	public DROPMODE getActualDropMode() {
		return actualDropMode;
	}

	public void setActualDropMode(DROPMODE actualDropMode) {
		this.actualDropMode = actualDropMode;
	}
}
