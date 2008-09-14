package com.blueskyminds.enterprise.license;

/**
 * A special-case License indicating that the license is not available
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class NullLicense {//extends License {

    /** The license that was not actualy available */
    private License referencedLicense;

    // ------------------------------------------------------------------------------------------------------

    /** Create a special-case 'not-available' license, referencing the license that was not available. */
    public NullLicense(License referencedLicense) {
        //super(referencedLicense.getType());
        this.referencedLicense = referencedLicense;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the license that's wrapped by this NullLicense
     * @return underlying License
     */
    public License getReferencedLicense() {
        return referencedLicense;
    }
}
