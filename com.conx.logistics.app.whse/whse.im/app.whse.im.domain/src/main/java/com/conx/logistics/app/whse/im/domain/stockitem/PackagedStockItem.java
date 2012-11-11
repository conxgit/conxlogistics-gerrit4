package com.conx.logistics.app.whse.im.domain.stockitem;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.conx.logistics.mdm.domain.MultitenantBaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="whpackagedstockitem")
public class PackagedStockItem extends MultitenantBaseEntity {

/*    @ManyToOne(targetEntity = WHPackage.class, fetch = FetchType.LAZY)
    @JoinColumn
    private WHPackage parentPackage;

    @ManyToOne(targetEntity = WHOrderLine.class, fetch = FetchType.LAZY)
    @JoinColumn
    private WHOrderLine orderLine;

    @ManyToOne(targetEntity = WHPick.class, fetch = FetchType.LAZY)
    @JoinColumn
    private WHPick pickedWith;*/

    @ManyToOne(targetEntity = StockItem.class, fetch = FetchType.LAZY)
    @JoinColumn
    private StockItem stockItem;

    private int packagedCount;
}
