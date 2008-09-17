package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.enterprise.party.Individual;

/**
 * Date Started: 23/04/2008
 */
public class PropertyAdvertisementSummaryFactory {

    public static PropertyAdvertisementCampaignSummary createCampaign(PropertyAdvertisementCampaign campaign) {
        if (campaign != null) {
            PropertyAdvertisementCampaignSummary summary = new PropertyAdvertisementCampaignSummary(campaign.getId(), campaign.getType());
            for (PropertyAdvertisement advertisement : campaign.getPropertyAdvertisements()) {
                summary.addAdvertisement(createAdvertisement(advertisement));
            }
            return summary;
        } else {
            return null;
        }

    }
    public static PropertyAdvertisementSummary createAdvertisement(PropertyAdvertisement propertyAdvertisement) {
        PropertyAdvertisementSummary summary = new PropertyAdvertisementSummary(propertyAdvertisement.getId(), propertyAdvertisement.getType());

        Organisation agency = propertyAdvertisement.getAgency();
        if (agency != null) {
            PropertyAgencySummary agencySummary = PropertyAgencySummary.create(agency.getName()).
                    withAddress(agency.getPrimaryBusinessAddress()).
                    withEmail(agency.getPrimaryBusinessEmail()).
                    withFax(agency.getPrimaryBusinessFax()).
                    withPhone(agency.getPrimaryBusinessPhone()).
                    withWebsite(agency.getPrimaryBusinessWebsite());

            for (Individual individual : propertyAdvertisement.getContacts()) {
                PropertyAgencyContactSummary contact = new PropertyAgencyContactSummary(individual.getId(),
                        individual.getFullName(), individual.getPrimaryBusinessPhone(), individual.getPrimaryBusinessEmail());
                agencySummary.addContact(contact);
            }

            summary.setAgency(agencySummary);
        }

        summary.setDateListed(propertyAdvertisement.getDateListed());
        summary.setDescription(propertyAdvertisement.getDescription());
        summary.setAskingPrice(propertyAdvertisement.getPrice());

        return summary;
    }
}
