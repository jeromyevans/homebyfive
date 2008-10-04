package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.country.RegionFactory;

import javax.persistence.EntityManager;

/**
 * Creates some sample data for testing regions
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestRegionTools {
    public static final String AUSTRALIA = "Australia";
    public static final String NEUTRAL_BAY = "Neutral Bay";
    public static final String NSW = "NSW";
    private static final String POSTCODE_2089 = "2089";
    public static final String BULL_CREEK = "Bull Creek";
    public static final String LEEMING = "Leeming";
    public static final String WILLETTON = "Willetton";
    public static final String POSTCODE_6149 = "6149";
    public static final String POSTCODE_6155 = "6155";

//    public static RegionOLD initialiseAusRegions() {
//        RegionOLD australia = new RegionOLD(AUSTRALIA, RegionTypes.Country);
//
//        RegionOLD wa = new RegionOLD("WA", RegionTypes.State);
//        RegionOLD bullCreek = new RegionOLD("Bull Creek", RegionTypes.Suburb);
//        RegionOLD leeming = new RegionOLD("Leeming", RegionTypes.Suburb);
//        RegionOLD willetton = new RegionOLD("Willetton", RegionTypes.Suburb);
//        wa.addChildRegion(bullCreek);
//        wa.addChildRegion(leeming);
//        wa.addChildRegion(willetton);
//
//        RegionOLD nsw = new RegionOLD(NSW, RegionTypes.State);
//        RegionOLD neutralBay = new RegionOLD(NEUTRAL_BAY, RegionTypes.Suburb);
//        RegionOLD kirribilli = new RegionOLD("Kirribilli", RegionTypes.Suburb);
//        nsw.addChildRegion(neutralBay);
//        nsw.addChildRegion(kirribilli);
//
//        australia.addChildRegion(wa);
//        australia.addChildRegion(nsw);
//
//        RegionOLD pc6149 = new RegionOLD("6149", RegionTypes.PostCode);
//        RegionOLD pc6155 = new RegionOLD("6155", RegionTypes.PostCode);
//        RegionOLD pc2089 = new RegionOLD(POSTCODE_2089, RegionTypes.PostCode);
//        RegionOLD pc2060 = new RegionOLD("2060", RegionTypes.PostCode);
//
//        wa.addChildRegion(pc6149);
//        wa.addChildRegion(pc6155);
//        nsw.addChildRegion(pc2089);
//        nsw.addChildRegion(pc2060);
//
//        pc6149.addChildRegion(bullCreek);
//        pc6149.addChildRegion(leeming);   // bull creek and leeming in same postcode
//        pc6155.addChildRegion(willetton);
//        pc2089.addChildRegion(neutralBay);
//        pc2060.addChildRegion(kirribilli);
//
//        RegionOLD northSydney = new RegionOLD("North Sydney", RegionTypes.Council);
//        RegionOLD melville = new RegionOLD("Melville", RegionTypes.Council);
//        northSydney.addChildRegion(neutralBay);
//        northSydney.addChildRegion(kirribilli);
//        melville.addChildRegion(bullCreek);
//        wa.addChildRegion(melville);
//        nsw.addChildRegion(northSydney);
//
//        return australia;
//    }

    public static Region initialiseAusRegionsX(EntityManager em) {
        Region australia = new RegionFactory().createCountry(AUSTRALIA, "AU", "AUS", "AUD'");

        Region wa = new RegionFactory().createState("Western Australia", "WA");
        Region bullCreek = new RegionFactory().createSuburb(BULL_CREEK);
        Region leeming = new RegionFactory().createSuburb(LEEMING);
        Region willetton = new RegionFactory().createSuburb(WILLETTON);
        wa.addChildRegion(bullCreek);
        wa.addChildRegion(leeming);
        wa.addChildRegion(willetton);

        Region nsw = new RegionFactory().createState("New South Wales", "NSW");
        Region neutralBay = new RegionFactory().createSuburb(NEUTRAL_BAY);
        Region kirribilli = new RegionFactory().createSuburb("Kirribilli");
        nsw.addChildRegion(neutralBay);
        nsw.addChildRegion(kirribilli);

        australia.addChildRegion(wa);
        australia.addChildRegion(nsw);

        Region pc6149 = new RegionFactory().createPostCode(POSTCODE_6149);
        Region pc6155 = new RegionFactory().createPostCode(POSTCODE_6155);
        Region pc2089 = new RegionFactory().createPostCode(POSTCODE_2089);
        Region pc2060 = new RegionFactory().createPostCode("2060");

        wa.addChildRegion(pc6149);
        wa.addChildRegion(pc6155);
        nsw.addChildRegion(pc2089);
        nsw.addChildRegion(pc2060);

        pc6149.addChildRegion(bullCreek);
        pc6149.addChildRegion(leeming);   // bull creek and leeming in same postcode
        pc6155.addChildRegion(willetton);
        pc2089.addChildRegion(neutralBay);
        pc2060.addChildRegion(kirribilli);
//
//        Region northSydney = new Region("North Sydney", RegionTypes.Council);
//        Region melville = new Region("Melville", RegionTypes.Council);
//        northSydney.addChildRegion(neutralBay);
//        northSydney.addChildRegion(kirribilli);
//        melville.addChildRegion(bullCreek);
//        wa.addChildRegion(melville);
//        nsw.addChildRegion(northSydney);

        em.persist(australia);
        em.persist(wa);
        em.persist(nsw);
        em.persist(pc6149);
        em.persist(pc6155);
        em.persist(pc2089);
        em.persist(pc2060);
        em.persist(bullCreek);
        em.persist(leeming);
        em.persist(willetton);
        em.persist(neutralBay);
        em.persist(kirribilli);
        em.flush();

        return australia;
    }

}
