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
@Table(name="whrcvdropoff")
public class DropOff extends MultitenantBaseEntity implements Serializable {

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Organization actualCfs;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Address actualCfsAddress;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Organization actualDropOffAt;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn
    private Address actualDropOffAtAddress;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date actualDropOff;

    private String actualShippersRef;

    @Enumerated(EnumType.STRING)
    private DROPMODE actualDropMode;
    
    @ManyToOne(targetEntity = DockType.class, cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn
    private DockType actualDockType;

	public Organization getCfs() {
		return actualCfs;
	}

	public void setCfs(Organization cfs) {
		this.actualCfs = cfs;
	}

	public Address getCfsAddress() {
		return actualCfsAddress;
	}

	public void setCfsAddress(Address cfsAddress) {
		this.actualCfsAddress = cfsAddress;
	}

	public Organization getDropOffAt() {
		return actualDropOffAt;
	}

	public void setDropOffAt(Organization dropOffAt) {
		this.actualDropOffAt = dropOffAt;
	}

	public Address getDropOffAtAddress() {
		return actualDropOffAtAddress;
	}

	public void setDropOffAtAddress(Address dropOffAtAddress) {
		this.actualDropOffAtAddress = dropOffAtAddress;
	}


	public Date getActualDropOff() {
		return actualDropOff;
	}

	public void setActualDropOff(Date actualDropOff) {
		this.actualDropOff = actualDropOff;
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

	public Organization getActualDropOffAt() {
		return actualDropOffAt;
	}

	public void setActualDropOffAt(Organization actualDropOffAt) {
		this.actualDropOffAt = actualDropOffAt;
	}

	public Address getActualDropOffAtAddress() {
		return actualDropOffAtAddress;
	}

	public void setActualDropOffAtAddress(Address actualDropOffAtAddress) {
		this.actualDropOffAtAddress = actualDropOffAtAddress;
	}

	public String getActualShippersRef() {
		return actualShippersRef;
	}

	public void setActualShippersRef(String actualShippersRef) {
		this.actualShippersRef = actualShippersRef;
	}

	public DROPMODE getActualDropMode() {
		return actualDropMode;
	}

	public void setActualDropMode(DROPMODE actualDropMode) {
		this.actualDropMode = actualDropMode;
	}

	public DockType getActualDockType() {
		return actualDockType;
	}

	public void setActualDockType(DockType actualDockType) {
		this.actualDockType = actualDockType;
	}
}
