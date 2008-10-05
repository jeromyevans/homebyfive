package com.blueskyminds.enterprise.address;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.index.RegionIndex;
import com.blueskyminds.enterprise.region.index.SuburbBean;

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
    public AddressBean(RegionIndex region) {
        if (region != null) {
            this.basePath = region.getPath();
            if (RegionTypes.Country.equals(region.getType())) {
                this.country = ((SuburbBean) region).getCountryName();
                countryConst = true;
            }
            if (RegionTypes.State.equals(region.getType())) {
                this.state = ((SuburbBean) region).getStateName();
                this.country = ((SuburbBean) region).getCountryName();
                countryConst = true;
                stateConst = true;
            }
            if (RegionTypes.Suburb.equals(region.getType())) {
                this.suburb = region.getName();
                this.postCode = ((SuburbBean) region).getPostalCodeName();
                this.state = ((SuburbBean) region).getStateName();
                this.country = ((SuburbBean) region).getCountryName();
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
    public Address toAddress(RegionIndex regionBean) {
        Address address = null;
        Suburb suburb = null;
        if (regionBean != null) {
            if (RegionTypes.Suburb.equals(regionBean.getType())) {
                suburb = (Suburb) regionBean.getRegion();

                address = super.augmentWithKnown(suburb);
            }
        }

        if (address == null) {
            address = new PlainTextAddress(format(true, false, false));
        }

        return address;
    }
    
}
