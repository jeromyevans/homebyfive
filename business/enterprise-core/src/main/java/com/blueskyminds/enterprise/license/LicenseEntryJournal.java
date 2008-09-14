package com.blueskyminds.enterprise.license;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Tracks the LicenseEntries as they commit changes to relationships
 *
 * The journal is implemented as a singleton with process-wide access
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class LicenseEntryJournal {

    /**
     * All of the entries in this journal.  Access to this list must be synchronized
     */
    private List<LicenseEntry> entries;

    /**
     * A singleton instance with process-wide access
     */
    private static LicenseEntryJournal soleInstance = new LicenseEntryJournal();

    // ------------------------------------------------------------------------------------------------------

    /** Create a new journal */
    public LicenseEntryJournal() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the journal with default attributes
     */
    private void init() {
        entries = Collections.synchronizedList(new LinkedList<LicenseEntry>());
    }

    // ------------------------------------------------------------------------------------------------------

    /** Submits an entry to the journal in the sole instance */
    private synchronized void submitEntry(LicenseEntry entry) {
        entries.add(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Commits the specified entry to the journal */
    public static void addEntry(LicenseEntry entry) {
        soleInstance.submitEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Provides a list of all the entries for the specified account
     *
     * @param account
     * @return list of entries for the account
     */
    public List<LicenseEntry> getEntriesForAccount(LicenseAccount account) {

        List<LicenseEntry> list = new LinkedList<LicenseEntry>();

        // find entries matching the account
        for (LicenseEntry entry : entries) {
            if (entry.getAccount().equals(account)) {
                list.add(entry);
            }
        }

        return list;
    }

    // ------------------------------------------------------------------------------------------------------
}
