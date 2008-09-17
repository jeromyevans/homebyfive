package com.blueskyminds.landmine.core.property.assets;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.*;
import java.util.Date;

/**
 * Apply the installation or removal of an Asset to a premise
 *
 * Date Started: 2/05/2008
 */
@Entity
@Table(name="PremiseAssetMap")
public class PremiseAssetMap extends AbstractDomainObject {

    private Premise premise;
    private PremiseAsset asset;
    private PremiseAssetMapType type;
    private Date dateApplied;
    private Integer quantity;

    public PremiseAssetMap(Premise premise, PremiseAsset asset, PremiseAssetMapType type, Date dateApplied, Integer quantity) {
        this.premise = premise;
        this.asset = asset;
        this.dateApplied = dateApplied;
        this.quantity = quantity;
        this.type = type;
    }

    public PremiseAssetMap() {
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateInstalled")
    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    @ManyToOne
    @JoinColumn(name="AssetId")
    public PremiseAsset getAsset() {
        return asset;
    }

    public void setAsset(PremiseAsset asset) {
        this.asset = asset;
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @Basic    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Enumerated
    @Column(name="Type")
    public PremiseAssetMapType getType() {
        return type;
    }

    public void setType(PremiseAssetMapType type) {
        this.type = type;
    }
}
