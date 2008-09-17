package com.blueskyminds.landmine.core.property.advertisement.service;

import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementBean;
import com.blueskyminds.landmine.core.property.advertisement.ejb.AdvertisementServiceException;

/**
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public interface AdvertisementImportService {

    /**
     * Creates a Property Advertisement to associate with a Premise and Parties:
     *
     * @param bean
     */
    Long importAdvertisementBean(PropertyAdvertisementBean bean) throws AdvertisementServiceException;

    /**
     * Update an existing property advertisement
     *
     * @param advertisementId
     * @param newBean
     * @return
     * @throws AdvertisementServiceException
     */
    Long updateAdvertisementBean(Long advertisementId, PropertyAdvertisementBean newBean) throws AdvertisementServiceException;
}
