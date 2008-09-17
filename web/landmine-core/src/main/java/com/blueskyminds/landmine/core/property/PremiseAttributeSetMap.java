package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * Maps an AttributeSet to a premise
 *
 * Date Started: 29/10/2007
 * <p/>
 * History:
 * 
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class PremiseAttributeSetMap extends AbstractEntity {

    private Premise premise;
    private PremiseAttributeSet attributeSet;

    public PremiseAttributeSetMap(Premise premise, PremiseAttributeSet attributeSet) {
        this.premise = premise;
        this.attributeSet = attributeSet;
    }

    public PremiseAttributeSetMap() {
    }

    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="AttributeSetId")
    public PremiseAttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(PremiseAttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }
}
