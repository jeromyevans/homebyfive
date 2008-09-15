package com.blueskyminds.enterprise.accountability;

import com.blueskyminds.enterprise.party.Party;

/**
 *
 * Accountability between two Parties
 *
 * One party is the commissioner (commissioning party)
 * The other party is responsible (responsible party)
 * The accountability always has a type
 *
 * Example:
 * A person works for a company.  In accountability, the company is the comissioner and the person is responsible
 * to the company.  The accountability type is employment.
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class Accountability {

    private AccountabilityType type;

    private Party commissioner;

    private Party responsible;

    // ------------------------------------------------------------------------------------------------------

    public Accountability(Party commissioner, Party responsible, AccountabilityType type) {
        this.commissioner = commissioner;
        this.responsible = responsible;
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

}
