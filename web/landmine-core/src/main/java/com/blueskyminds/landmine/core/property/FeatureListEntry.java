package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;

import java.util.Date;

/**
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class FeatureListEntry<T extends DomainObject> extends AbstractDomainObject {

    private FeatureList featureList;

    private T underlying;

    private Date dateApplied;

    // ------------------------------------------------------------------------------------------------------

    public FeatureListEntry(FeatureList featureList, T underlying, Date dateApplied) {
        this.featureList = featureList;
        this.underlying = underlying;
        this.dateApplied = dateApplied;
    }

    protected FeatureListEntry() {
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Initialise the FeatureListEntry with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public T getUnderlying() {
        return underlying;
    }

    public void setUnderlying(T underlying) {
        this.underlying = underlying;
    }

    public FeatureList getFeatureList() {
        return featureList;
    }

    public void setFeatureList(FeatureList featureList) {
        this.featureList = featureList;
    }
}
