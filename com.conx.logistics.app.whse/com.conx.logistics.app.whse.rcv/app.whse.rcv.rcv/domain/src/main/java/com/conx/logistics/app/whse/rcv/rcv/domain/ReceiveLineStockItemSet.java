package com.conx.logistics.app.whse.rcv.rcv.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;
import com.conx.logistics.mdm.domain.MultitenantBaseEntity;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="whreceivelinestockitemset")
public class ReceiveLineStockItemSet extends MultitenantBaseEntity {
	private static final long serialVersionUID = 1L;

	private int matchedCount;
	
	private int groupSize;

    @OneToOne(targetEntity = ReceiveLine.class, fetch = FetchType.EAGER)
    @JoinColumn
    private ReceiveLine receiveLine;

    @ManyToOne(targetEntity = Arrival.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Arrival arrival;
    
    @ManyToOne(targetEntity = ArrivalReceipt.class, fetch = FetchType.LAZY)
    @JoinColumn
    private ArrivalReceipt arrivalReceipt;    
    
    @ManyToOne(targetEntity = ArrivalReceiptLine.class, fetch = FetchType.LAZY)
    @JoinColumn
    private ArrivalReceiptLine arrivalReceiptLine;   

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<StockItem> stockItems = new java.util.HashSet<StockItem>();
    
	public int getMatchedCount() {
		return matchedCount;
	}

	public void setMatchedCount(int matchedCount) {
		this.matchedCount = matchedCount;
	}

	public ReceiveLine getReceiveLine() {
		return receiveLine;
	}

	public void setReceiveLine(ReceiveLine receiveLine) {
		this.receiveLine = receiveLine;
	}

	public Set<StockItem> getStockItems() {
		return stockItems;
	}

	public void setStockItems(Set<StockItem> stockItems) {
		this.stockItems = stockItems;
	}

	public Arrival getArrival() {
		return arrival;
	}

	public void setArrival(Arrival arrival) {
		this.arrival = arrival;
	}

	public ArrivalReceipt getArrivalReceipt() {
		return arrivalReceipt;
	}

	public void setArrivalReceipt(ArrivalReceipt arrivalReceipt) {
		this.arrivalReceipt = arrivalReceipt;
	}

	public ArrivalReceiptLine getArrivalReceiptLine() {
		return arrivalReceiptLine;
	}

	public void setArrivalReceiptLine(ArrivalReceiptLine arrivalReceiptLine) {
		this.arrivalReceiptLine = arrivalReceiptLine;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
}