package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;

/**
 * Simple bean for creating an address.  Specializes the MultifieldAddress to include a path and flags to
 * indicate which sections of the address are known (eg. to control whether they can be modified)
 *
 * Date Started: 22/05/2008
 */
public class AddressBean extends MultifieldAddress {

    private String basePath;
    private boolean suburbConst;
    private boolean postCodeConst;
    private boolean stateConst;
    private boolean countryConst;

    public AddressBean() {
    }

    /**
     * Create an AddressBean pre-populated with the attributes of a RegionBean
     * @param region
     */
    public AddressBean(Region region) {
        if (region != null) {
            this.basePath = region.getPath();
            if (RegionTypes.Country.equals(region.getType())) {
                this.country = ((Country) region).getName();
                countryConst = true;
            }
            if (RegionTypes.State.equals(region.getType())) {
                this.state = ((State) region).getName();
//                this.country = ((State) region).getCountry().getName();
                countryConst = true;
                stateConst = true;
            }
            if (RegionTypes.Suburb.equals(region.getType())) {
                this.suburb = region.getName();
//                this.postCode = ((Suburb) region).getPostCodeName();
//                this.state = ((Suburb) region).getStateName();
//                this.country = ((Suburb) region).getCountryName();
                countryConst = true;
                stateConst = true;
                postCodeConst = true;
                suburbConst = true;
            }
        }
    }
    
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isSuburbConst() {
        return suburbConst;
    }

    public boolean isPostCodeConst() {
        return postCodeConst;
    }

    public boolean isStateConst() {
        return stateConst;
    }

    public boolean isCountryConst() {
        return countryConst;
    }

    /**
     * Convert this bean to a non-persistent Address of the appropriate type, using the supplied RegionBean as the
     * source of predetermined fields
     *
     * @param regionBean
     * @return
     */
    public Address toAddress(Region regionBean) {
        Address address = null;
        Suburb suburb = null;
        if (regionBean != null) {
            if (RegionTypes.Suburb.equals(regionBean.getType())) {
                suburb = (Suburb) regionBean;

                address = super.augmentWithKnown(suburb);
            }
        }

        if (address == null) {
            address = new PlainTextAddress(format(true, false, false));
        }

        return address;
    }
    
}
