package com.blueskyminds.homebyfive.business.contact;

import java.util.Set;

/**
 * Identifies an entity that has PointOfContact's
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public interface Contactable {

    /** Get all the points of contact of the specified type */
    Set<PointOfContact> getPointsOfContactOfType(final POCType type);

    /**
     * Determine if this entity has at least one point of contact of the specified type
     *
     * This can be useful to check whether the entity has an email address
     *
     * @param type
     * @return true if the entity has the PointOfContact
     */
    boolean hasPointOfContactOfType(final POCType type);
}
