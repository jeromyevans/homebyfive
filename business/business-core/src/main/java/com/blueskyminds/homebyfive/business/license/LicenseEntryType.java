package com.blueskyminds.homebyfive.business.license;

import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.HashSet;

/**
 * Abstract class for LicenseEntry types
 *
 * A LicenseEntryType always has one or more contra types - that is, opposite types.  The conta-types are
 * tested when creating transactions with multiple entries to ensure the transaction balances.
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class LicenseEntryType {

    /** The enumeration for this type */
    private LicenseEntryTypes type;

    /** The list of enumerations of the opposite type */
    private Collection<LicenseEntryTypes> contraTypes;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new type of LicenseEntry
     * @param type
     * @param contraType - the opposite of this type
     */
    public LicenseEntryType(LicenseEntryTypes type, LicenseEntryTypes contraType) {
        this.type = type;
        contraTypes = new HashSet<LicenseEntryTypes>();
        contraTypes.add(contraType);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Tests whether this LicenseEntryType is the contra of the specified type
     *
     * @param type
     * @return true if this is the contra type of the specified type
     */
    public boolean isContra(LicenseEntryType type) {
        return contraTypes.contains(type.getType());
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Define another conta-type for this license entry
     * @param contraType
     */
    public void addContraType(LicenseEntryTypes contraType) {
        if (!contraTypes.contains(contraType)) {
            contraTypes.add(contraType);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Provides the enumeration type underlying this LicenseEntryType
     * @return the enumeration value
     */
    @Enumerated
    public LicenseEntryTypes getType() {
        return type;
    }

    // ------------------------------------------------------------------------------------------------------
}
