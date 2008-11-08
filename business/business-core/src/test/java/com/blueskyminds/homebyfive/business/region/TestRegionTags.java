package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.tag.Tag;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestRegionTags extends RegionTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        Country aus = countryService.lookup("/au");
        Tag english =  tagService.lookupOrCreateTag("English");
        aus.addTag(english);

        countryService.update(aus.getPath(), aus);
    }

    public void testListTags() {
        TableModel countryTags = countryService.listTags("/au");
        TableModel stateTags = stateService.listTags("/au/nsw");
        TableModel suburbTags = suburbService.listTags("/au/nsw/neutral+bay");
        TableModel postalCodeTags = postalCodeService.listTags("/au/nsw/2089");

        assertEquals(1, countryTags.getRows().size());
    }

    public void testAssignCountryTag() throws Exception {
        countryService.assignTag("/au", "English");

        em.flush();
        assertEquals(1, countryService.listTags("/au").getRows().size());

        countryService.assignTag("/au", "Continent");

        em.flush();

        assertEquals(2, countryService.listTags("/au").getRows().size());
        assertEquals(2, tagService.listTags().size());        
        em.refresh(countryService.lookup("/au"));
        countryService.removeTag("/au", "English");

        em.flush();

        assertEquals(1, countryService.listTags("/au").getRows().size());
        assertEquals(2, tagService.listTags().size());
    }

    public void testAssignStateTag() throws Exception {
        String path = "/au/nsw";
        stateService.assignTag(path, "English");

        em.flush();
        assertEquals(1, stateService.listTags(path).getRows().size());

        stateService.assignTag(path, "cool");

        em.flush();

        assertEquals(2, stateService.listTags(path).getRows().size());
        assertEquals(2, tagService.listTags().size());
        em.refresh(stateService.lookup(path));
        stateService.removeTag(path, "English");

        em.flush();

        assertEquals(1, stateService.listTags(path).getRows().size());
        assertEquals(2, tagService.listTags().size());
    }

    public void testAssignPostalCodeTag() throws Exception {
       String path = "/au/nsw/2089";
       postalCodeService.assignTag(path, "English");

       em.flush();
       assertEquals(1, postalCodeService.listTags(path).getRows().size());

       postalCodeService.assignTag(path, "cool");

       em.flush();

       assertEquals(2, postalCodeService.listTags(path).getRows().size());
       assertEquals(2, tagService.listTags().size());
       em.refresh(postalCodeService.lookup(path));
       postalCodeService.removeTag(path, "English");

       em.flush();

       assertEquals(1, postalCodeService.listTags(path).getRows().size());
       assertEquals(2, tagService.listTags().size());
   }


    public void testAssignSuburbTag() throws Exception {
        String path = "/au/nsw/neutral+bay";
        suburbService.assignTag(path, "English");

        em.flush();
        assertEquals(1, suburbService.listTags(path).getRows().size());

        suburbService.assignTag(path, "cool");

        em.flush();

        assertEquals(2, suburbService.listTags(path).getRows().size());
        assertEquals(2, tagService.listTags().size());
        em.refresh(suburbService.lookup(path));
        suburbService.removeTag(path, "English");

        em.flush();

        assertEquals(1, suburbService.listTags(path).getRows().size());
        assertEquals(2, tagService.listTags().size());
    }


    public void testListByTag() throws Exception {
        assertEquals(1, countryService.listByTag("English").size());
    }

}
