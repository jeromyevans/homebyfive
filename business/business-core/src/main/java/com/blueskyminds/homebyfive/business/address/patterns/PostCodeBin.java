package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.Bin;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.NamedStringComparator;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;

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

    public PostCodeBin(Country country, PostalCodeService postalCodeService) throws PatternMatcherInitialisationException {
        Collection<PostalCode> postCodes = postalCodeService.list(country);

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