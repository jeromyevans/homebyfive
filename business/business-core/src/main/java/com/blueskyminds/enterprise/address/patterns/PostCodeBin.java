package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.Bin;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.NamedStringComparator;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.Country;

import java.util.Collection;

/**
 *  Matches words to postcodes
 *
 * Uses the postcodes loaded from persistence for the given country.
 * Patterns are matched exactly.
 * The metadata for each pattern is used to identify the name of the corresponding PostCode instance.
 *
 * Date Started: 18/06/2006
 *
 * History:
 * 
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PostCodeBin extends Bin {

    public PostCodeBin(Country country, AddressDAO addressDAO) throws PatternMatcherInitialisationException {
        Collection<PostalCode> postCodes = addressDAO.listPostCodesInCountry(country);

        for (PostalCode postCode : postCodes) {
            addPattern(postCode, new NamedStringComparator(new IgnoreCaseComparator()), true, -1, postCode);
             
//            for (String alias : postCode.getNames()) {
//                addPattern(alias.toLowerCase(), true, -1, postCode);
//            }
            // create the pattern using the postcode as metadata
            //addPattern(postCode.getPostCode().toLowerCase(), postCode.getPostCode(), true, -1, postCode);
        }

    }
}